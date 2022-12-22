package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_INTERNAL_DESTINATION;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.SOURCE_STR;
import static ca.homedepot.preference.writer.GSFileWriterOutbound.createFileOnGCS;

@Slf4j
@Component
public class InternalOutboundFileWriter implements ItemWriter<InternalOutboundProcessorDto>, ItemStream
{
	@Value("${folders.internal.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.internalCa}")
	protected String caFileFormat;
	@Value("${outbound.files.internalMover}")
	protected String moverFileFormat;
	@Value("${outbound.files.internalGarden}")
	protected String gardenFileFormat;


	protected String sourceId;
	@Autowired
	private FileService fileService;

	private Format formatter = new SimpleDateFormat("yyyyMMdd");

	private StringBuilder caFile = new StringBuilder();


	/**
	 * Method used to generate a plain text file
	 * 
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends InternalOutboundProcessorDto> items) throws Exception
	{
		sourceId = items.get(0).getCanPtcSourceId().replace(",", "");
		for (InternalOutboundProcessorDto internal : items)
		{
			caFile.append(internal.getEmailAddr()).append(internal.getCanPtcEffectiveDate()).append(internal.getCanPtcSourceId())
					.append(internal.getEmailStatus()).append(internal.getCanPtcFlag()).append(internal.getLanguagePreference())
					.append(internal.getEarlyOptInDate()).append(internal.getCndCompliantFlag()).append(internal.getHdCaFlag())
					.append(internal.getHdCaGardenClubFlag()).append(internal.getHdCaNewMoverFlag())
					.append(internal.getHdCaNewMoverEffDate()).append(internal.getHdCaProFlag()).append(internal.getPhonePtcFlag())
					.append(internal.getFirstName()).append(internal.getLastName()).append(internal.getPostalCode())
					.append(internal.getProvince()).append(internal.getCity()).append(internal.getPhoneNumber())
					.append(internal.getBussinessName()).append(internal.getIndustryCode()).append(internal.getMoveDate())
					.append(internal.getDwellingType());
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException
	{
		caFile.append(PreferenceBatchConstants.INTERNAL_CA_HEADERS);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Chunk Executed");
	}

	@Override
	public void close() throws ItemStreamException
	{
		String cafileName = caFileFormat.replace("YYYYMMDD", formatter.format(new Date()));
		String movefileName = moverFileFormat.replace("YYYYMMDD", formatter.format(new Date()));
		String gardenfileName = gardenFileFormat.replace("YYYYMMDD", formatter.format(new Date()));
		createFileOnGCS(CloudStorageUtils.generatePath(repositorySource + folderSource, cafileName), JOB_NAME_INTERNAL_DESTINATION,
				caFile.toString().getBytes());
		createFileOnGCS(CloudStorageUtils.generatePath(repositorySource + folderSource, movefileName),
				JOB_NAME_INTERNAL_DESTINATION, caFile.toString().getBytes());
		createFileOnGCS(CloudStorageUtils.generatePath(repositorySource + folderSource, gardenfileName),
				JOB_NAME_INTERNAL_DESTINATION, caFile.toString().getBytes());
		caFile = new StringBuilder();
		setFileRecord(cafileName);
		setFileRecord(movefileName);
		setFileRecord(gardenfileName);
	}

	/**
	 * This method registry in file table the generated file
	 *
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_INTERNAL_DESTINATION,
				JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insertOldId(file);
	}

}

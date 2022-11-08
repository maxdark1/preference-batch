package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_INTERNAL_DESTINATION;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.STATUS_STR;

@Slf4j
@Component
public class InternalOutboundFileWriter implements ItemWriter<InternalOutboundProcessorDto>
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
	private FileOutputStream writer;


	private String sourceId;
	@Autowired
	private FileService fileService;

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
		StringBuilder fileBuilder = new StringBuilder();

		for (InternalOutboundProcessorDto internal : items)
		{
			fileBuilder.append(internal.getEmailAddr()).append(internal.getCanPtcEffectiveDate()).append(internal.getCanPtcSourceId()).append(internal.getEmailStatus()).append(internal.getCanPtcFlag()).append(internal.getLanguagePreference()).append(internal.getEarlyOptInDate()).append(internal.getCndCompliantFlag()).append(internal.getHdCaFlag()).append(internal.getHdCaGardenClubFlag()).append(internal.getHdCaNewMoverFlag()).append(internal.getHdCaNewMoverEffDate()).append(internal.getHdCaProFlag()).append(internal.getPhonePtcFlag()).append(internal.getFirstName()).append(internal.getLastName()).append(internal.getPostalCode()).append(internal.getProvince()).append(internal.getCity()).append(internal.getPhoneNumber()).append(internal.getBussinessName()).append(internal.getIndustryCode()).append(internal.getMoveDate()).append(internal.getDwellingType());

		}
		String file = fileBuilder.toString();
		generateFile(file, caFileFormat);
		generateFile(file, moverFileFormat);
		generateFile(file, gardenFileFormat);

	}

	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void generateFile(String file, String filePath) throws IOException
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String fileName = filePath.replace("YYYYMMDD", formatter.format(new Date()));

		writer = new FileOutputStream(repositorySource + folderSource + fileName, true);
		byte[] toFile = file.getBytes();
		writer.write(toFile);
		writer.flush();
		writer.close();
		setFileRecord(fileName);
	}


	/**
	 * This method registry in file table the generated file
	 * 
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_INTERNAL_DESTINATION);
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

}

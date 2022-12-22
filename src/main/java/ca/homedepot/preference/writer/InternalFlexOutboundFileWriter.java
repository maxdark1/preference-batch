package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.InternalFlexOutboundProcessorDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.StorageException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_FLEX_INTERNAL_DESTINATION;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.STATUS_STR;

@Slf4j
@Component
public class InternalFlexOutboundFileWriter implements ItemStreamWriter<InternalFlexOutboundProcessorDTO>
{
	public static final String PIPE = "|";
	public static final String COMMA = ",";
	public static final String CR = "\n";
	public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
	private final Format formatter = new SimpleDateFormat(YYYYMMDD_HHMMSS);
	public static final String YYYYMMDD_T_HHMISS = "YYYYMMDDTHHMISS";
	public static final String SPACE = " ";
	public static final String TEE = "T";

	@Value("${folders.flexAttributes.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.flexAttributes}")
	protected String flexAttributesFileFormat;

	private String sourceId;
	@Autowired
	private FileService fileService;

	private StringBuilder recordBuilder = new StringBuilder();

	@Override
	public void write(List<? extends InternalFlexOutboundProcessorDTO> items) throws Exception
	{
		sourceId = items.get(0).getSourceId().replace(COMMA, "");

		for (InternalFlexOutboundProcessorDTO item : items)
		{
			recordBuilder.append(item.getFileId()).append(PIPE).append(item.getSequenceNbr()).append(PIPE)
					.append(item.getEmailAddr()).append(PIPE).append(item.getHdHhId()).append(PIPE).append(item.getHdIndId())
					.append(PIPE).append(item.getCustomerNbr()).append(PIPE).append(item.getStoreNbr()).append(PIPE)
					.append(item.getOrgName()).append(PIPE).append(item.getCompanyCd()).append(PIPE).append(item.getCustTypeCd())
					.append(PIPE).append(item.getSourceId()).append(PIPE).append(item.getEffectiveDate()).append(PIPE)
					.append(item.getLastUpdateDate()).append(PIPE).append(item.getIndustryCode()).append(PIPE)
					.append(item.getCompanyName()).append(PIPE).append(item.getContactFirstName()).append(PIPE)
					.append(item.getContactLastName()).append(PIPE).append(item.getContactRole()).append(COMMA).append(CR);

		}



	}

	@SneakyThrows
	private void generateFile(String record, String fileNameFormat) throws StorageException
	{

		String stamp = formatter.format(Calendar.getInstance().getTime());
		String fileName = fileNameFormat.replace(YYYYMMDD_T_HHMISS, stamp.replace(SPACE, TEE));
		GSFileWriterOutbound.createFileOnGCS(CloudStorageUtils.generatePath(repositorySource, folderSource, fileName),
				JOB_NAME_FLEX_INTERNAL_DESTINATION, record.getBytes());
		setFileRecord(fileName);
	}

	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_FLEX_INTERNAL_DESTINATION,
				JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}


	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info(" Internal Flex Outbound Writer Started. ");
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info(" Internal Flex Outbound Writer. Chunk Executed");
	}

	@Override
	public void close() throws ItemStreamException
	{
		String lineRow = recordBuilder.toString();
		generateFile(lineRow, flexAttributesFileFormat);
		recordBuilder = new StringBuilder();
	}
}

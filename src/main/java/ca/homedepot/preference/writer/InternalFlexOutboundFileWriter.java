package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.InternalFlexOutboundProcessorDTO;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_FLEX_INTERNAL_DESTINATION;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.STATUS_STR;

@Slf4j
@Component
public class InternalFlexOutboundFileWriter implements ItemWriter<InternalFlexOutboundProcessorDTO>
{
	public static final String PIPE = "|";
	public static final String COMMA = ",";
	@Value("${folders.flexAttributes.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.flexAttributes}")
	protected String flexAttributesFileFormat;

	private FileOutputStream writer;


	private String sourceId;
	@Autowired
	private FileService fileService;


	@Override
	public void write(List<? extends InternalFlexOutboundProcessorDTO> items) throws Exception
	{
		sourceId = items.get(0).getSourceId().replace(COMMA, "");
		StringBuilder fileBuilder = new StringBuilder();

		for (InternalFlexOutboundProcessorDTO item : items)
		{
			fileBuilder.append(item.getFileId()).append(PIPE).append(item.getSequenceNbr()).append(PIPE).append(item.getEmailAddr())
					.append(PIPE).append(item.getHdHhId()).append(PIPE).append(item.getHdIndId()).append(PIPE)
					.append(item.getCustomerNbr()).append(PIPE).append(item.getStoreNbr()).append(PIPE).append(item.getOrgName())
					.append(PIPE).append(item.getCompanyCd()).append(PIPE).append(item.getCustTypeCd()).append(PIPE)
					.append(item.getSourceId()).append(PIPE).append(item.getEffectiveDate()).append(PIPE)
					.append(item.getLastUpdateDate()).append(PIPE).append(item.getIndustryCode()).append(PIPE)
					.append(item.getCompanyName()).append(PIPE).append(item.getContactFirstName()).append(PIPE)
					.append(item.getContactLastName()).append(PIPE).append(item.getContactRole()).append(COMMA);

		}
		String file = fileBuilder.toString();
		generateFile(file, flexAttributesFileFormat);


	}

	private void generateFile(String record, String filePath) throws IOException
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
		String stamp = formatter.format(Calendar.getInstance().getTime());
		String fileName = filePath.replace("YYYYMMDDTHHMISS", stamp.replace(" ", "T"));

		writer = new FileOutputStream(repositorySource + folderSource + fileName, true);
		byte[] toFile = record.getBytes();
		writer.write(toFile);
		writer.flush();
		writer.close();
		setFileRecord(fileName);
	}

	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_FLEX_INTERNAL_DESTINATION);
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

}

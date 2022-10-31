package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Data
public class CitiSupressionFileWriter extends FlatFileItemWriter<CitiSuppresionOutboundDTO>
{

	@Value("${folders.citi.path}")
	private String repositorySource;
	@Value("${folders.outbound}")
	private String folderSource;
	@Value("${outbound.citi.mastersuppresion}")
	private String fileNameFormat;

	@Autowired
	private FileService fileService;

	private String jobName;

	private String fileName;

	public CitiSupressionFileWriter()
	{
		setLineAggregator(getLineAgreggator());
		setHeaderCallback(getHeaderCallBack());
		setShouldDeleteIfExists(true);
		setSaveState(false);
	}

	public void setResource()
	{
		Format formatter = new SimpleDateFormat("YYYYMMDD");
		this.fileName = this.fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));

		Resource resource = new FileSystemResource(repositorySource + folderSource + fileName);
		if (resource.exists())
		{
			// removes if exists
			try
			{
				Files.delete(new File(repositorySource + folderSource + fileName).toPath());
			}
			catch (IOException e)
			{
				log.info(" File for citi suppresion will be created. ");
			}
		}
		super.setResource(resource);
	}


	@Override
	public void write(List<? extends CitiSuppresionOutboundDTO> items) throws Exception
	{
		saveFileRecord();
		super.write(items);
	}

	public FlatFileHeaderCallback getHeaderCallBack()
	{
		return writer -> writer.write(
				"'FIRST_NAME','MIDDLE_INITIAL','LAST_NAME','ADDR_LINE_1','ADDR_LINE_2','CITY','STATE_CD','POSTAL_CD','EMAIL_ADDR','PHONE','SMS_MOBILE_PHONE','BUSINESS_NAME',DM_OPT_OUT,EMAIL_OPT_OUT,PHONE_OPT_OUT,SMS_OPT_OUT");
	}

	public DelimitedLineAggregator<CitiSuppresionOutboundDTO> getLineAgreggator()
	{
		BeanWrapperFieldExtractor<CitiSuppresionOutboundDTO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		beanWrapperFieldExtractor.setNames(new String[]
		{ "FirstName", "MiddleInitial", "LastName", "AddrLine1", "AddrLine2", "City", "StateCd", "PostalCd", "EmailAddr", "Phone",
				"SmsMobilePhone", "BusinessName", "DmOptOut", "EmailOptOut", "PhoneOptOut", "SmsOptOut" });

		DelimitedLineAggregator<CitiSuppresionOutboundDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setDelimiter(",");
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		return delimitedLineAggregator;
	}


	public void saveFileRecord()
	{

		BigDecimal jobId = fileService.getJobId(jobName);
		BigDecimal sourceId = MasterProcessor.getSourceID("SOURCE", "citi_bank").getMasterId();
		Master fileStatus = MasterProcessor.getSourceID("STATUS", "VALID");

		FileDTO file = new FileDTO(null, fileName, jobId, sourceId, fileStatus.getValueVal(), fileStatus.getMasterId(), new Date(),
				new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);

	}
}
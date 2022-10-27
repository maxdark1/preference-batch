package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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

	private static final String JOB_NAME = "sendCitiSuppresionToCiti";

	private String fileName;

	public CitiSupressionFileWriter()
	{
		setLineAggregator(getLineAgreggator());
		setHeaderCallback(getHeaderCallBack());
	}

	@PostConstruct
	public void setResourcePostConstruct()
	{
		Format formatter = new SimpleDateFormat("YYYYMMDD");
		fileName = fileNameFormat.replaceAll("YYYYMMDD", formatter.format(new Date()));

		setResource(new FileSystemResource(repositorySource + folderSource + fileName));
	}

	@Override
	public String doWrite(List<? extends CitiSuppresionOutboundDTO> items)
	{

		saveFileRecord(fileName);
		return super.doWrite(items);
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


	public void saveFileRecord(String file_name)
	{

		BigDecimal jobId = fileService.getJobId(JOB_NAME);
		BigDecimal sourceId = MasterProcessor.getSourceID("SOURCE", "citi_bank").getMaster_id();
		fileService.insert(file_name, "VALID", sourceId, new Date(), jobId, new Date(), "BATCH", BigDecimal.valueOf(19),
				new Date());

	}
}

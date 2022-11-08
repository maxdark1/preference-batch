package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
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
public class SalesforceExtractFileWriter extends FlatFileItemWriter<SalesforceExtractOutboundDTO>
{

	@Value("${folders.salesforce.path}")
	private String repositorySource;
	@Value("${folders.outbound}")
	private String folderSource;
	@Value("${outbound.salesforce.extract}")
	private String fileNameFormat;

	@Autowired
	private FileService fileService;

	private String jobName;

	private String fileName;

	public SalesforceExtractFileWriter()
	{
		setLineAggregator(getLineAggregator());
		setHeaderCallback(getHeaderCallback());
	}

	public void setResource()
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd");
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
			{//TODO what needs to be done in case of exception
				log.info(" File for citi supresion will be created. ");
			}
		}
		super.setResource(resource);
	}

	@Override
	public void write(List<? extends SalesforceExtractOutboundDTO> items) throws Exception
	{
		saveFileRecord();
		super.write(items);
	}

	public FlatFileHeaderCallback getHeaderCallback()
	{
		return writer -> writer.write(
				"EMAIL_ADDRESS   ||   AS_OF_DATE   ||   SOURCE_ID   ||   EMAIL_STATUS   ||   EMAIL_PTC   ||   LANGUAGE_PREFERENCE   ||   EARLIEST_OPT_IN_DATE   ||   HD_CANADA_EMAIL_COMPLIANT_FLAG   ||   HD_CANADA_FLAG   ||   GARDEN_CLUB_FLAG   ||   NEW_MOVER_FLAG   ||   PRO_FLAG   "
						+ "||   PHONE_PTC_FLAG   ||   FIRST_NAME   ||   LAST_NAME   ||   POSTAL_CODE   ||   PROVINCE   ||   CITY   ||   PHONE_NUMBER   ||   BUSINESS_NAME   ||   BUSINESS_TYPE   ||   MOVE_DATE   ||   DWELLING_TYPE");
	}

	public DelimitedLineAggregator<SalesforceExtractOutboundDTO> getLineAggregator()
	{
		BeanWrapperFieldExtractor<SalesforceExtractOutboundDTO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		//TODO can be a constant array, instead making everytime new
		beanWrapperFieldExtractor.setNames(new String[]
		{ "EmailAddress", "AsOfDate", "SourceId", "EmailStatus", "EmailPtc", "LanguagePreference", "EarliestOptInDate",
				"HdCanadaEmailCompliantFlag", "HdCanadaFlag", "GardenClubFlag", "NewMoverFlag", "ProFlag", "PhonePtcFlag",
				"FirstName", "LastName", "PostalCode", "Province", "City", "PhoneNumber", "BusinessName", "BusinessType", "MoveDate",
				"DwellingType" });

		DelimitedLineAggregator<SalesforceExtractOutboundDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setDelimiter("      ");
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		return delimitedLineAggregator;
	}

	public void saveFileRecord()
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		BigDecimal sourceId = MasterProcessor.getSourceID("SOURCE_ID", "citisup").getMasterId();
		Master fileStatus = MasterProcessor.getSourceID("STATUS", "VALID");
		//TODO duplicate string literals
		FileDTO file = new FileDTO(null, fileName, jobId, sourceId, fileStatus.getValueVal(), fileStatus.getMasterId(), new Date(),
				new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

}

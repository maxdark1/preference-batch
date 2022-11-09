package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

import static ca.homedepot.preference.constants.PreferenceBatchConstants.SALESFORCE_EXTRACT_HEADERS;
import static ca.homedepot.preference.constants.SchedulerConfigConstants.SALESFORCE_EXTRACT_NAMES;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Slf4j
@Component
@Getter
@Setter
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
		return writer -> writer.write(SALESFORCE_EXTRACT_HEADERS);
	}

	public DelimitedLineAggregator<SalesforceExtractOutboundDTO> getLineAggregator()
	{
		BeanWrapperFieldExtractor<SalesforceExtractOutboundDTO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		beanWrapperFieldExtractor.setNames(SALESFORCE_EXTRACT_NAMES);

		DelimitedLineAggregator<SalesforceExtractOutboundDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setDelimiter("      ");
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		return delimitedLineAggregator;
	}

	public void saveFileRecord()
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		BigDecimal sourceId = MasterProcessor.getSourceID(SOURCE_ID_STR, CITI_SUP).getMasterId();
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, VALID);

		FileDTO file = new FileDTO(null, fileName, jobId, sourceId, fileStatus.getValueVal(), fileStatus.getMasterId(), new Date(),
				new Date(), INSERTEDBY, new Date(), null, null);

		fileService.insert(file);
	}

}

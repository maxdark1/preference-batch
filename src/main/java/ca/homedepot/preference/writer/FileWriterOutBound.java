package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Slf4j
@Getter
@Setter
public class FileWriterOutBound<T> extends FlatFileItemWriter<T>
{
	private final Format formatter = new SimpleDateFormat("yyyyMMdd");
	private String repositorySource;

	private String folderSource;

	private String fileNameFormat;

	private FileService fileService;

	private String jobName;

	private String source;

	private String fileName;

	private String header = "";

	private String[] names;

	private String delimiter = ",";

	public FileWriterOutBound()
	{
		setAppendAllowed(true);
	}

	public void setHeader(String header)
	{
		this.header = header;
		setHeaderCallback(getHeaderCallBack());
	}

	public void setNames(String[] names)
	{
		this.names = names;
		setLineAggregator(getLineAgreggator());
	}

	public void setDelimiter(String delimiter)
	{
		this.delimiter = delimiter;
	}

	public void setFilename()
	{
		this.fileName = this.fileNameFormat.replace(YYYYMMDD_FILE, formatter.format(new Date()));
	}

	public void setResource()
	{
		setFilename();

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
				log.error(" File {} will be created ", fileName);
			}
		}
		super.setResource(resource);
	}

	@Override
	public void write(List<? extends T> items) throws Exception
	{
		saveFileRecord();
		super.write(items);
	}

	public FlatFileHeaderCallback getHeaderCallBack()
	{
		return writer -> writer.write(header);
	}

	public DelimitedLineAggregator<T> getLineAgreggator()
	{
		BeanWrapperFieldExtractor<T> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		beanWrapperFieldExtractor.setNames(names);

		DelimitedLineAggregator<T> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setDelimiter(this.delimiter);
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		return delimitedLineAggregator;
	}


	public void saveFileRecord()
	{
		BigDecimal jobId = fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId());
		String sourceStr = source.equals(CITI_SUP) ? SOURCE_ID_STR : SOURCE_STR;
		BigDecimal sourceId = MasterProcessor.getSourceID(sourceStr, source).getMasterId();
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, VALID);

		FileDTO file = new FileDTO(null, fileName, jobId, sourceId, fileStatus.getValueVal(), fileStatus.getMasterId(), new Date(),
				new Date(), INSERTEDBY, new Date(), null, null);

		fileService.insert(file);

	}
}
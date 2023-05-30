package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ca.homedepot.preference.config.StorageApplicationGCS.getBucketName;
import static ca.homedepot.preference.config.StorageApplicationGCS.storage;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Slf4j
@Component
public class PreferenceOutboundItemWriter<T> implements ItemWriter<T>, ItemStream
{
	@Value("${folders.salesforce.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.salesforce.extract}")
	protected String fileNameFormat;

	public String sourceFile;

	protected String sourceId;
	@Autowired
	private FileService fileService;
	private final Format formatter = new SimpleDateFormat("yyyyMMdd");

	private int quantityRecords = 0;
	StringBuilder fileBuilder = new StringBuilder();

	private List<Counters> counters;

	private JobListener jobListener;

	private String jobName;

	public String source;

	public String header = "";

	private Counters counter = new Counters(0, 0, 0);

	public void setCounters(List<Counters> counters)
	{
		this.counters = counters;
	}

	public Storage storage;
	public BlobId blobId;

	public WritableByteChannel writer;

	public String fileName;

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public String getFileName()
	{
		return fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
	}

	/**
	 * Method used to generate a plain text file
	 *
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */

	@Override
	public void write(List<? extends T> items) throws Exception
	{
		if (!items.isEmpty())
		{
			for (T item : items)
			{
				fileBuilder.append(item.toString()).append("\n");
				quantityRecords++;
				writer.write(ByteBuffer.wrap(fileBuilder.toString().getBytes()));
				fileBuilder = new StringBuilder();
			}
		}
		else
		{
			log.info("Nothing to Write in {} Outbound File", sourceFile);
		}
	}


	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 *
	 * @param file
	 * @throws IOException
	 */


	/**
	 * This method registry in file table the generated file
	 *
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, VALID);
		String sourceStr = source.equals(CITI_SUP) ? SOURCE_ID_STR : SOURCE_STR;
		BigDecimal sourceId = MasterProcessor.getSourceID(sourceStr, source).getMasterId();
		log.error(" PREFERENCE-BATCH: SOURCE_TYPE master_id: {}", sourceId);
		BigDecimal sourceIdBD = sourceId == null || sourceId.equals(BigDecimal.valueOf(-400L))
				? MasterProcessor.getSourceID("SOURCE_ID", "database").getOldID()
				: sourceId;
		log.error(" PREFERENCE-BATCH: SOURCE_TYPE to insert: {}", sourceIdBD);
		jobListener.setFiles(fileName);
		FileDTO file = new FileDTO(null, fileName, jobId, sourceIdBD, fileStatus.getValueVal(), fileStatus.getMasterId(),
				new Date(), new Date(), INSERTEDBY, new Date(), null, null);

		fileService.insert(file);
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Daily Compliant Writer Start");
		if (!header.equals(""))
			fileBuilder.append(header).append("\n");
		this.storage = StorageOptions.getDefaultInstance().getService();
		fileName = CloudStorageUtils.generatePath(repositorySource, UUID.randomUUID().toString());
		blobId = BlobId.of(getBucketName(), fileName);
		BlobInfo file = BlobInfo.newBuilder(blobId).build();
		writer = storage().create(file).writer();
		log.error("PREFERENCE-BATCH: Temporary File Created: " + fileName);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Chunck Executed");
	}

	@Override
	public void close() throws ItemStreamException
	{
		String newFileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
		try
		{
			writer.close();
			log.info("Writter Closed");
			String fileDestination = repositorySource + newFileName;
			BlobId rename = BlobId.of(getBucketName(), fileDestination);
			storage().copy(Storage.CopyRequest.newBuilder().setSource(blobId).setTarget(rename).build());
			storage().get(blobId).delete();
			log.error("PREFERENCE-BATCH: File Renamed to: " + fileDestination);
			log.info(" {} items were written on file {}", quantityRecords, newFileName);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);

		}

		log.error(" PREFERENCE-BATCH: file: {} going to be write on DataBase", newFileName);
		setFileRecord(newFileName);
		counter.quantityRecords = quantityRecords;
		counter.fileName = newFileName;
		counter.date = new Date().toString();
		counters.add(counter);
		quantityRecords = 0;
	}

}

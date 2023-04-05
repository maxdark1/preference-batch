package ca.homedepot.preference.writer;

import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.StorageApplicationGCS.*;

@Getter
@Setter
@Slf4j

public class GSFileWriterOutbound<T> extends FileWriterOutBound<T>
{


	private File tempFile;
	private OutputStream os;
	private Boolean isFirstStep = true;

	private StringBuilder stringBuilder;

	private int quantityRecords = 0;
	StringBuilder fileBuilder = new StringBuilder();

	private List<Counters> counters;

	private Counters counter = new Counters(0, 0, 0);

	public void setCounters(List<Counters> counters)
	{
		this.counters = counters;
	}

	/**
	 * Set the filename and delete if exists in the current bucket
	 */
	@Override
	public void setResource()
	{
		setFilename();
		deleteObject(getFolderSource(), getFileName());
		stringBuilder = new StringBuilder();
		tempFile = FileUtil.createTempFile(getFileName());
		super.setResource(new FileSystemResource(tempFile));
	}

	@Override
	public void write(List<? extends T> items) throws Exception
	{
		try {
			super.write(items);
			if (quantityRecords == 0) {
				String headers = getHeader().isBlank() ? "" : getHeader() + "\n";
				stringBuilder.append(headers);
			}
			String line = super.doWrite(items);
			stringBuilder.append(line);
			quantityRecords += items.size();
		} catch (Exception ex){
			log.error("GSFILE ERROR - " + ex.getMessage());
			throw ex;
		}

	}

	/**
	 * After de writer is close, it generate the file on the Google Cloud Storage
	 */
	@Override
	public void close()
	{
		super.close();
		String value = stringBuilder.toString();
		log.info("PREFERENCE-BATCH-INFO Saving content to GCP Bucket with the size of - " + value.length());
		/* We placed one position more to the length to include the return character */
		int contentLength = getHeader().length() + 1;
		if (value.length() > contentLength)
		{
			byte[] content = value.getBytes();
			createFileOnGCS(CloudStorageUtils.generatePath(getFolderSource(), getFileName()), getJobName(), content);
			counter.quantityRecords = quantityRecords;
			counter.fileName = getFileName();
			counter.date = new Date().toString();
			counters.add(counter);
			quantityRecords = 0;
		}
		super.saveFileRecord();
	}

	/**
	 * Method to create file on the Google cloud Storage
	 * 
	 * @param filename
	 * @param content
	 */
	public static void createFileOnGCS(String filename, String jobName, byte[] content)
	{
		try
		{
			BlobId blobId = BlobId.of(getBucketName(), filename);
			BlobInfo file = BlobInfo.newBuilder(blobId).build();
			storage().create(file, content);

		}
		catch (StorageException e)
		{
			log.error(" PREFERENCE BATCH ERROR - Failure on job {} to publish the file {} on bucket", jobName, filename);
		}
	}
}

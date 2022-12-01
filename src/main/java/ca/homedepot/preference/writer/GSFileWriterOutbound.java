package ca.homedepot.preference.writer;

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
import java.util.List;

import static ca.homedepot.preference.config.StorageApplicationGCS.*;

@Getter
@Setter
@Slf4j
public class GSFileWriterOutbound<T> extends FileWriterOutBound<T>
{


	private File tempFile;
	private OutputStream os;

	private StringBuilder stringBuilder;

	/**
	 * Set the filename and delete if exists in the current bucket
	 */
	@Override
	public void setResource()
	{
		String headers = getHeader().isBlank() ? "" : getHeader() + "\n";
 		stringBuilder = new StringBuilder(headers);
		setFilename();
		deleteObject(getFolderSource(), getFileName());
		tempFile = FileUtil.createTempFile(getFileName());

		super.setResource(new FileSystemResource(tempFile));
	}

	@Override
	public void write(List<? extends T> items) throws Exception
	{
		super.write(items);

		String line = super.doWrite(items);
		if (!stringBuilder.toString().contains(line))
			stringBuilder.append(line);

	}

	/**
	 * After de writer is close, it generate the file on the Google Cloud Storage
	 */
	@Override
	public void close()
	{
		super.close();

		if (!stringBuilder.toString().equalsIgnoreCase(getHeader() + "\n"))
		{
			byte[] content = stringBuilder.toString().getBytes();
			createFileOnGCS(CloudStorageUtils.generatePath(getFolderSource(), getFileName()), content);
		}
	}

	/**
	 * Method to create file on the Google cloud Storage
	 * 
	 * @param filename
	 * @param content
	 */
	public static void createFileOnGCS(String filename, byte[] content) {
		try {
			BlobId blobId = BlobId.of(getBucketName(), filename);
			BlobInfo file = BlobInfo.newBuilder(blobId).build();
			storage().create(file, content);

		}catch (StorageException e){
			log.error(" PREFERENCE BATCH ERROR - Failed to publish the file {} on the bucket.", filename);
		}
	}
}

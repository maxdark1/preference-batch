package ca.homedepot.preference.tasklet;


import ca.homedepot.preference.exception.PreferenceCenterCustomException;
import ca.homedepot.preference.util.FileUtil;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * The type Upload tasklet.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UploadTasklet implements Tasklet
{

	/**
	 * The Bucket.
	 */
	@Value("${analytic.file.bucket}")
	String bucket;

	/**
	 * The File path.
	 */
	@Value("${analytic.file.path}")
	String filePath;

	/**
	 * The Storage.
	 */
	private final Storage storage;

	/**
	 * Execute repeat status.
	 *
	 * @param stepContribution
	 *           the step contribution
	 * @param chunkContext
	 *           the chunk context
	 * @return the repeat status
	 * @throws Exception
	 *            the exception
	 */
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
	{
		try
		{
			File csvFile = new File(FileUtil.getEmailanalyticsFile());
			String emailAnalyticsFileStoragePath = filePath + FileUtil.getEmailanalyticsFile();

			log.info("Email analytics File path : {}", emailAnalyticsFileStoragePath);

			uploadFile(csvFile, emailAnalyticsFileStoragePath);
			csvFile.deleteOnExit();

			csvFile = new File(FileUtil.getRegistrationFile());
			String registrationFileStoragePath = filePath + FileUtil.getRegistrationFile();

			log.info("Email analytics File path : {}", registrationFileStoragePath);

			uploadFile(csvFile, registrationFileStoragePath);
			csvFile.deleteOnExit();
		}
		catch (Exception e)
		{
			log.error("unable to upload files into bucket {}", bucket);
			throw new PreferenceCenterCustomException(e);
		}

		return RepeatStatus.FINISHED;
	}

	/**
	 * Upload file.
	 *
	 * @param csvFile
	 *           the csv file
	 * @param fileName
	 *           the file name
	 * @throws IOException
	 *            the io exception
	 */
	private void uploadFile(File csvFile, String fileName)
	{
		final BlobId blobId = BlobId.of(bucket, fileName);
		final BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/csv").build();
		try (FileInputStream fileInputStream = new FileInputStream(csvFile))
		{
			final byte[] data = fileInputStream.readAllBytes();
			storage.create(blobInfo, data);
		}
		catch (IOException ioexception)
		{
			log.error("issue in uploading file {}", ioexception.getMessage());
			throw new PreferenceCenterCustomException(ioexception);
		}
		log.info("Successfully uploaded {} file into bucket {}", fileName, bucket);
	}

}

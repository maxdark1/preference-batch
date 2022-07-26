package ca.homedepot.preference.tasklet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import ca.homedepot.preference.util.FileUtil;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * The type Upload tasklet test.
 */
@RunWith(MockitoJUnitRunner.class)
public class UploadTaskletTest
{
	/**
	 * The Storage.
	 */
	@Mock
	Storage storage;

	/**
	 * The Upload tasklet.
	 */
	@InjectMocks
	UploadTasklet uploadTasklet;

	/**
	 * The Step contribution.
	 */
	StepContribution stepContribution;

	/**
	 * The Chunk context.
	 */
	ChunkContext chunkContext;

	/**
	 * Sets up.
	 *
	 * @throws IOException
	 *            the io exception
	 */
	@Before
	public void setUp() throws IOException
	{
		MockitoAnnotations.initMocks(this.getClass());

		ReflectionTestUtils.setField(uploadTasklet, "bucket", "thdca-datasources");
		ReflectionTestUtils.setField(uploadTasklet, "filePath", "subscription/analytics/");

		stepContribution = Mockito.mock(StepContribution.class);
		chunkContext = Mockito.mock(ChunkContext.class);

		File testFile = new File("testfile.csv");
		if (!testFile.exists())
		{
			testFile.createNewFile();
		}
		FileUtil.setEmailanalyticsFile("testfile.csv");
		FileUtil.setRegistrationFile("testfile.csv");
	}

	/**
	 * Test execute.
	 *
	 * @throws Exception
	 *            the exception
	 */
	@Test
	public void testExecute() throws Exception
	{
		uploadTasklet.execute(stepContribution, chunkContext);
		verify(storage, atLeast(2)).create(any(BlobInfo.class), any(byte[].class));
	}

	@Test(expected = Exception.class)
	public void testExecuteWithNoFile() throws Exception
	{
		FileUtil.setRegistrationFile("Nofile.csv");
		uploadTasklet.execute(stepContribution, chunkContext);
	}

}

package ca.homedepot.preference.listener;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class InvalidFileListenerTest
{

	@Mock
	Storage storage;
	@Mock
	CloudStorageUtils cloudStorageUtils;

	@Mock
	Page<Blob> blobPage;
	@InjectMocks
	@Spy
	private InvalidFileListener invalidFileListener;



	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		StorageApplicationGCS.setStorage(storage);
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
	}

	@Test
	void beforeStepNotFiles()
	{
		StepExecution stepExecution = new StepExecution("readInboundCSVFileStep", new JobExecution(10L));
		invalidFileListener = new InvalidFileListener();
		invalidFileListener.beforeStep(stepExecution);
	}

	@Test
	void beforeStepWithFiles()
	{
		List<Resource> files = new ArrayList<>();
		Resource tmpFile = new FileSystemResource("test.txt");
		List<String> paths = new ArrayList<>(List.of("test.txt"));
		files.add(tmpFile);
		Map<String, List<Resource>> map = new HashMap<>();
		map.put("INVALID", files);
		StepExecution stepExecution = new StepExecution("readInboundCSVFileStep", new JobExecution(10L));
		invalidFileListener = new InvalidFileListener();
		invalidFileListener.setDirectory("Directory");
		invalidFileListener.setProcess("Project");
		try (MockedStatic<StorageApplicationGCS> storageApplicationGCSMockedStatic = Mockito
				.mockStatic(StorageApplicationGCS.class))
		{
			Mockito.when(
							storage.list("BucketName", Storage.BlobListOption.prefix("path/"), Storage.BlobListOption.currentDirectory()))
					.thenReturn(blobPage);
			Mockito.when(cloudStorageUtils.listObjectInBucket("Directory")).thenReturn(paths);
			storageApplicationGCSMockedStatic.when(() -> StorageApplicationGCS.getsGCPResourceMap("Directory", "Project"))
					.thenReturn(map);
			InvalidFileListener spy = Mockito.spy(invalidFileListener);
			Mockito.doReturn(map).when(spy).getResources("Directory", "Project");
			invalidFileListener.beforeStep(stepExecution);
		}

	}

	@Test
	void afterStep()
	{
		StepExecution stepExecution = new StepExecution("readInboundCSVFileStep", new JobExecution(10L));
		invalidFileListener = new InvalidFileListener();
		invalidFileListener.afterStep(stepExecution);
	}

	@Test
	void checkExecution()
	{
		invalidFileListener = new InvalidFileListener();
		assertFalse(invalidFileListener.checkExecution("nothing"));
		assertTrue(invalidFileListener.checkExecution("readInboundCSVFileStep"));
		assertTrue(invalidFileListener.checkExecution("readSFMCOptOutsStep1"));
		assertTrue(invalidFileListener.checkExecution("readInboundCSVFileCRMStep"));
	}



	@Test
	void getResources()
	{
		Map<String, List<Resource>> expected = new HashMap<>();

		expected.put("VALID", new ArrayList<Resource>());
		expected.put("INVALID", new ArrayList<Resource>());
		List<String> paths = new ArrayList<>();

		try (MockedStatic<StorageApplicationGCS> storageApplicationGCSMockedStatic = Mockito
				.mockStatic(StorageApplicationGCS.class))
		{
			Mockito.when(
					storage.list("BucketName", Storage.BlobListOption.prefix("path/"), Storage.BlobListOption.currentDirectory()))
					.thenReturn(blobPage);
			Mockito.when(cloudStorageUtils.listObjectInBucket("Directory")).thenReturn(paths);
			storageApplicationGCSMockedStatic.when(() -> StorageApplicationGCS.getsGCPResourceMap("Directory", "Project"))
					.thenReturn(expected);
		}



		//Mockito.when(invalidFileListener.getResources("Directory", "Project")).thenReturn(expected);

		Map<String, List<Resource>> map = invalidFileListener.getResources("Directory", "Project");
		assertEquals(expected, map);
	}

	@Test
	void WriteFile()
	{
		FileService fileMock = Mockito.mock(FileService.class);
		MockitoAnnotations.openMocks(this);
		invalidFileListener.setFileService(fileMock);
		String jobName = "jobName";
		invalidFileListener.setJobName("jobName");
		when(fileMock.getJobId(anyString(), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(10));

		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "SOURCE", "hybris", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "VALID", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "INVALID", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));

		MasterProcessor.setMasterList(masterList);
		invalidFileListener.setSource("hybris");
		invalidFileListener.writeFile("File", false);
		invalidFileListener.writeFile("File", true);
		assertEquals(invalidFileListener.getSource(), "hybris");
	}

}
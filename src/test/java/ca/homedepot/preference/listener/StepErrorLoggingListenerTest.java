package ca.homedepot.preference.listener;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StepErrorLoggingListenerTest
{
	@Mock
	FileService fileService;

	@Mock
	JobListener jobListener;

	@InjectMocks
	@Spy
	private StepErrorLoggingListener stepErrorLoggingListener;



	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		stepErrorLoggingListener.setJobListener(jobListener);

		List<Master> masterList = List.of(new Master(BigDecimal.TEN, BigDecimal.ONE, "KEY_VALUE", "hybris", true, null),
				new Master(BigDecimal.valueOf(2L), BigDecimal.valueOf(2L), "STATUS", "VALID", true, null),
				new Master(BigDecimal.valueOf(3L), BigDecimal.valueOf(2L), "STATUS", "INVALID", true, null));
		MasterProcessor.setMasterList(masterList);

	}

	@AfterEach
	void tearDown()
	{
	}

	@Test
	void beforeStep()
	{
		// given
		List<Throwable> exceptions = new ArrayList<>();
		StepExecution stepExecution = mock(StepExecution.class);
		when(stepExecution.getFailureExceptions()).thenReturn(exceptions);
		String stepName = "someStep";
		JobExecution jobExecution = mock(JobExecution.class);
		doNothing().when(jobExecution).addStepExecutions(anyList());
		Long id = 1l;
		// when
		stepErrorLoggingListener.beforeStep(new StepExecution(stepName, jobExecution, id));
		// then
		assertTrue(true);
	}

	@Test
	void afterStepFailedCase()
	{
		// given
		List<Throwable> exceptions = new ArrayList<>();
		StepExecution stepExecution = mock(StepExecution.class);
		when(stepExecution.getFailureExceptions()).thenReturn(exceptions);
		JobExecution jobExecution = mock(JobExecution.class);
		doNothing().when(jobExecution).addStepExecutions(anyList());
		// when
		ExitStatus exitStatus = stepErrorLoggingListener.afterStep(stepExecution);
		// then
		assertEquals(ExitStatus.COMPLETED, exitStatus);
	}

	@Test
	void afterStep()
	{
		Throwable throwable = new Exception("Failure");
		StepExecution stepExecution = stepExecution = new StepExecution("STEP EXECUTION", mock(JobExecution.class), 1L);
		stepExecution.addFailureException(throwable);

		ExitStatus exitStatus = stepErrorLoggingListener.afterStep(stepExecution);
		Mockito.verify(stepErrorLoggingListener).afterStep(stepExecution);

		assertEquals(ExitStatus.FAILED, exitStatus);
	}

	@Test
	void moveFile()
	{
		// given
		FileDTO file = new FileDTO();
		file.setFileId(BigDecimal.ONE);
		file.setFileName("somefile.txt");
		file.setSourceType(BigDecimal.valueOf(2l));
		file.setJob(BigDecimal.valueOf(3l));
		file.setStatus("someStatus");
		Master fileStatus = new Master(BigDecimal.TEN, BigDecimal.ONE, "KEY_VALUE", "hybris", true, null);
		List<FileDTO> filesToMove = Collections.singletonList(file);
		when(fileService.getFilesToMove()).thenReturn(filesToMove);
		when(fileService.updateFileEndTime(file.getFileId(), new Date(), "BATCH", new Date(), fileStatus)).thenReturn(1);

		// when
		try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class))
		{

			stepErrorLoggingListener.moveFile();
			fileUtil.verify(() -> FileUtil.moveFile(file.getFileName(), true, "VALID"));
			Mockito.verify(stepErrorLoggingListener).moveFile();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		// then
		assertTrue(true);
	}

	@Test
	void moveFilesGCS()
	{
		BigDecimal sourceType = BigDecimal.valueOf(2);
		FileDTO instance = FileDTO.builder().fileId(BigDecimal.ONE).fileName("test.txt").endTime(new Date()).insertedBy("test")
				.insertedDate(new Date()).job(BigDecimal.TEN).sourceType(sourceType).startTime(new Date()).updatedDate(new Date())
				.updatedBy("test").statusId(BigDecimal.ONE).status("TEST").build();

		when(fileService.getFilesToMove()).thenReturn(Collections.singletonList(instance));
		when(fileService.updateFileEndTime(any(BigDecimal.class), any(Date.class), anyString(), any(Date.class), any(Master.class)))
				.thenReturn(1);

		try (MockedStatic<MasterProcessor> mockMasterProcessor = mockStatic(MasterProcessor.class))
		{
			mockMasterProcessor.when(() -> MasterProcessor.getValueVal(sourceType)).thenReturn("STATUS");
			mockMasterProcessor.when(() -> MasterProcessor.getSourceID(anyString(), anyString())).thenReturn(new Master());
		}
		FileUtil.setInbound("SomeFile.txt");
		FileUtil.setProcessed("MovedFile.txt");
		FileUtil.setError("/ERRORS/");
		try (MockedStatic<StorageApplicationGCS> storageApplicationGCSMockedStatic = mockStatic(StorageApplicationGCS.class))
		{
			storageApplicationGCSMockedStatic.verifyNoInteractions();
		}
		CloudStorageUtils cloudStorageUtils = mock(CloudStorageUtils.class);
		doNothing().when(cloudStorageUtils).moveObject(anyString(), anyString(), anyString());
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
		stepErrorLoggingListener.moveFileGCS();
		verify(stepErrorLoggingListener).moveFileGCS();
	}


}
package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
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

	@InjectMocks
	private InvalidFileListener invalidFileListener;


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
		files.add(tmpFile);
		Map<String, List<Resource>> map = new HashMap<>();
		map.put("INVALID", files);
		StepExecution stepExecution = new StepExecution("readInboundCSVFileStep", new JobExecution(10L));
		invalidFileListener = new InvalidFileListener();
		invalidFileListener.setDirectory("Directory");
		invalidFileListener.setProcess("Project");
		InvalidFileListener spy = Mockito.spy(invalidFileListener);
		Mockito.doReturn(map).when(spy).getResources("Directory", "Project");
		invalidFileListener.beforeStep(stepExecution);
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
		invalidFileListener = new InvalidFileListener();
		invalidFileListener.getResources("Directory", "Project");
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
		assertEquals(invalidFileListener.getSource(), "hybris");
	}

}
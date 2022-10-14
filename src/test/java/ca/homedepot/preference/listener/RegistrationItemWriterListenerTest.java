package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

class RegistrationItemWriterListenerTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	RegistrationItemWriterListener registrationItemWriterListener;



	@BeforeEach
	void setup()
	{

		fileService = Mockito.mock(FileServiceImpl.class);
		registrationItemWriterListener = new RegistrationItemWriterListener();
		registrationItemWriterListener.setFileService(fileService);
		registrationItemWriterListener.setJobName("JOB_NAME");
		registrationItemWriterListener.setFileID(BigDecimal.ONE);

	}

	@Test
	void beforeWrite()
	{
	}

	@Test
	void writeFile()
	{
		String fileName = "TEST_FILE";
		String jobName = "JOB_NAME";
		String insertedBy = "insertedBy";
		Master fileStatus = new Master(BigDecimal.ZERO, BigDecimal.ZERO, "STATUS", "VALID", true);

		MasterProcessor.setMasterList(List.of(fileStatus));
		Integer records = 1;
		BigDecimal fileId = BigDecimal.valueOf(123L);
		BigDecimal jobId = BigDecimal.ONE;

		Mockito.when(fileService.getJobId(eq(jobName))).thenReturn(jobId);
		Mockito.when(fileService.getFile(eq(fileName), eq(BigDecimal.ONE))).thenReturn(fileId);

		BigDecimal currentFileId = registrationItemWriterListener.getFromTableFileID(fileName);
		assertEquals(fileId, currentFileId);
	}

	@Test
	void afterWrite()
	{
	}


	@Test
	void getFileService()
	{
		assertNotNull(registrationItemWriterListener.getFileService());
	}

	@Test
	void getJobName()
	{
		assertEquals("JOB_NAME", registrationItemWriterListener.getJobName());
	}

	@Test
	void getFileID()
	{
		assertEquals(BigDecimal.ONE, registrationItemWriterListener.getFileID());
	}

}
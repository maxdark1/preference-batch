package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationItemReaderListenerTest
{

	@Mock
	FileService fileService;

	String fileName;
	String jobName;

	BigDecimal fileID;
	Master master;

	@InjectMocks
	RegistrationItemReaderListener registrationItemReaderListener;

	@BeforeEach
	void beforeRead()
	{
		fileService = Mockito.mock(FileService.class);
		registrationItemReaderListener = new RegistrationItemReaderListener(fileService);
		fileName = "TEST";
		jobName = "TEST_JOB";
		fileID = new BigDecimal("12345");
		master = new Master(BigDecimal.ONE, BigDecimal.ONE, "TEST", "TEST", true, BigDecimal.TEN);

		registrationItemReaderListener.setFileName(fileName);
		registrationItemReaderListener.setJobName(jobName);
		registrationItemReaderListener.setFileID(fileID);
		registrationItemReaderListener.setMaster(master);
	}

	@Test
	void getFileService()
	{
		assertNotNull(registrationItemReaderListener.getFileService());
	}

	@Test
	void getFileName()
	{
		assertEquals(fileName, registrationItemReaderListener.getFileName());
	}

	@Test
	void getJobName()
	{
		assertEquals(jobName, registrationItemReaderListener.getJobName());
	}

	@Test
	void getFileID()
	{
		assertEquals(fileID, registrationItemReaderListener.getFileID());
	}

	@Test
	void getMaster()
	{
		assertEquals(master, registrationItemReaderListener.getMaster());
	}
}
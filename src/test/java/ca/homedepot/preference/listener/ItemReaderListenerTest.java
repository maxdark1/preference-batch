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

class ItemReaderListenerTest
{

	@Mock
	FileService fileService;

	String fileName;
	String jobName;

	BigDecimal fileID;
	Master master;

	@InjectMocks
	ItemReaderListener itemReaderListener;

	@BeforeEach
	void beforeRead()
	{
		fileService = Mockito.mock(FileService.class);
		itemReaderListener = new ItemReaderListener(fileService);
		fileName = "TEST";
		jobName = "TEST_JOB";
		fileID = new BigDecimal("12345");
		master = new Master(BigDecimal.ONE, BigDecimal.ONE, "TEST", "TEST", true);

		itemReaderListener.setFileName(fileName);
		itemReaderListener.setJobName(jobName);
		itemReaderListener.setFileID(fileID);
		itemReaderListener.setMaster(master);
	}

	@Test
	void getFileService()
	{
		assertNotNull(itemReaderListener.getFileService());
	}

	@Test
	void getFileName()
	{
		assertEquals(fileName, itemReaderListener.getFileName());
	}

	@Test
	void getJobName()
	{
		assertEquals(jobName, itemReaderListener.getJobName());
	}

	@Test
	void getFileID()
	{
		assertEquals(fileID, itemReaderListener.getFileID());
	}

	@Test
	void getMaster()
	{
		assertEquals(master, itemReaderListener.getMaster());
	}
}
package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.BatchStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class RegistrationItemWriterListenerTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	RegistrationItemWriterListener registrationItemWriterListener;


	List<FileInboundStgTable> items;


	@BeforeEach
	void setup()
	{

		MockitoAnnotations.openMocks(this);

		List<Master> masterList = new ArrayList<>();


		Master fileStatus = new Master(BigDecimal.ZERO, BigDecimal.ZERO, "STATUS", "VALID", true, null);


		masterList.add(new Master(new BigDecimal("15"), new BigDecimal("5"), "JOB_STATUS", "STARTED", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("17"), new BigDecimal("5"), "JOB_STATUS", "COMPLETED", true, null));
		masterList.add(new Master(new BigDecimal("18"), new BigDecimal("5"), "JOB_STATUS", "ERROR", true, null));
		masterList.add(fileStatus);
		MasterProcessor.setMasterList(masterList);

		registrationItemWriterListener.setFileService(fileService);
		registrationItemWriterListener.setJobName("JOB_NAME");
		registrationItemWriterListener.setFileID(BigDecimal.ONE);

		items = new ArrayList<>();
		FileInboundStgTable stgTable1 = FileInboundStgTable.builder().build();
		stgTable1.setFileName("TEST_FILE");
		stgTable1.setStatus("NS");
		items.add(stgTable1);

	}

	@Test
	void beforeWrite()
	{
		BigDecimal fileId = BigDecimal.valueOf(123L);
		String fileName = "TEST_FILE";
		Mockito.when(registrationItemWriterListener.getFromTableFileID(fileName)).thenReturn(fileId);

		registrationItemWriterListener.beforeWrite(items);
		Mockito.verify(registrationItemWriterListener).beforeWrite(items);
		assertEquals(fileId, items.get(0).getFileId());
	}

	@Test
	void getFromTableFileID()
	{
		String fileName = "TEST_FILE";

		BigDecimal fileId = BigDecimal.valueOf(123L);
		BigDecimal jobId = BigDecimal.ONE;

		Mockito.when(fileService.getJobId(anyString(), any(BigDecimal.class))).thenReturn(jobId);
		Mockito.when(fileService.getFile(fileName, BigDecimal.ONE)).thenReturn(fileId);

		BigDecimal currentFileId = registrationItemWriterListener.getFromTableFileID(fileName);
		assertEquals(fileId, currentFileId);
	}

	@Test
	void afterWrite()
	{
		int records = 1;
		BigDecimal fileId = BigDecimal.valueOf(123L);

		Mockito.when(fileService.updateInboundStgTableStatus(fileId, "IP", "NS")).thenReturn(records);
		registrationItemWriterListener.afterWrite(items);
		Mockito.verify(registrationItemWriterListener).afterWrite(items);
	}

	@Test
	void onWriteError()
	{
		Exception exception = new Exception();
		registrationItemWriterListener.onWriteError(exception, items);
		Mockito.verify(registrationItemWriterListener).onWriteError(exception, items);
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
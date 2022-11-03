package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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

		MockitoAnnotations.initMocks(this);
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
		assertEquals(fileId, items.get(0).getFile_id());
	}

	@Test
	void getFromTableFileID()
	{
		String fileName = "TEST_FILE";
		String jobName = "JOB_NAME";
		Master fileStatus = new Master(BigDecimal.ZERO, BigDecimal.ZERO, "STATUS", "VALID", true, null);

		MasterProcessor.setMasterList(List.of(fileStatus));

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
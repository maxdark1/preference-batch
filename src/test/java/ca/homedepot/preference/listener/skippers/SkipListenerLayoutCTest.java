package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.BatchStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkipListenerLayoutCTest
{

	@Mock
	FileServiceImpl fileService;
	@InjectMocks
	@Spy
	SkipListenerLayoutC skipListenerLayoutC;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		skipListenerLayoutC.setJobName("JOB_NAME");

		MasterProcessor.setMasterList(
				List.of(new Master(BigDecimal.ONE, BigDecimal.ONE, "EMAIL_STATUS", "Valid Email Addresses", true, BigDecimal.ONE),
						new Master(BigDecimal.TEN, BigDecimal.ONE, "EMAIL_STATUS", "Invalid Email Addresses", true, BigDecimal.TEN),
						new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null)));

	}

	@Test
	void onSkipInRead()
	{
		Throwable t = Mockito.mock(Throwable.class);
		skipListenerLayoutC.onSkipInRead(t);
		Mockito.verify(skipListenerLayoutC).onSkipInRead(t);
	}

	@Test
	void onSkipInReadCase2()
	{
		Throwable t = new IOException("Exception");
		IOException ioException = assertThrows(IOException.class, () -> {
			skipListenerLayoutC.onSkipInRead(t);
		});
		assertTrue(ioException.getMessage().contains("PREFERENCE BATCH ERROR"));
	}

	@Test
	void onSkipInWrite()
	{
		Throwable t = Mockito.mock(Throwable.class);
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);

		skipListenerLayoutC.onSkipInWrite(fileInboundStgTable, t);
		Mockito.verify(skipListenerLayoutC).onSkipInWrite(fileInboundStgTable, t);
	}

	@Test
	void onSkipInProcess()
	{
		BigDecimal jobId = BigDecimal.ONE, fileId = BigDecimal.ONE;
		String fileName = "TEST";
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
		InboundRegistration item = new InboundRegistration();
		item.setFileName(fileName);
		item.setLanguagePreference("F");
		item.setAsOfDate("01-22-2022 22:22:22");
		String jobName = "jobName";
		skipListenerLayoutC.setJobName("jobName");
		Throwable t = new Exception("message");

		Mockito.when(fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId())).thenReturn(jobId);
		Mockito.when(fileService.getFile(fileName, jobId)).thenReturn(fileId);
		Mockito.when(fileService.insertInboundStgError(fileInboundStgTable)).thenReturn(1);

		skipListenerLayoutC.onSkipInProcess(item, t);
		Mockito.verify(skipListenerLayoutC).onSkipInProcess(item, t);
	}

	@Test
	void getDateToInsert() throws ParseException
	{
		String correctDate = "01-01-2022 22:22:22", incorrectDate = "00/00/00 00:00:00";
		Throwable t = new Exception("month");
		Date date = skipListenerLayoutC.getDateToInsert(correctDate, t);
		t = new Exception("month date format");

		Date dateIncorrect = skipListenerLayoutC.getDateToInsert(incorrectDate, t);
		assertNotNull(date);
		assertNull(dateIncorrect);
	}

	@Test
	void getJobName()
	{
		assertEquals("JOB_NAME", skipListenerLayoutC.getJobName());
	}
}
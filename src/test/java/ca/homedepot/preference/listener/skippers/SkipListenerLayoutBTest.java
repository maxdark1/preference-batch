package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.BatchStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkipListenerLayoutBTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	SkipListenerLayoutB skipListenerLayoutB;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		skipListenerLayoutB.setJobName("JOB_NAME");


		MasterProcessor.setMasterList(
				List.of(new Master(BigDecimal.ONE, BigDecimal.ONE, "EMAIL_STATUS", "Valid Email Addresses", true, BigDecimal.ONE),
						new Master(BigDecimal.TEN, BigDecimal.ONE, "EMAIL_STATUS", "Invalid Email Addresses", true, BigDecimal.TEN),
						new Master(new BigDecimal("24"), BigDecimal.TEN, "EMAIL_STATUS", "Hard Bounces", true, new BigDecimal("50")),
						new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null)));
	}

	@Test
	void onSkipInRead()
	{
		Throwable t = Mockito.mock(Throwable.class);
		skipListenerLayoutB.onSkipInRead(t);
		Mockito.verify(skipListenerLayoutB).onSkipInRead(t);

	}

	@Test
	void onSkipInWrite()
	{
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
		Throwable t = Mockito.mock(Throwable.class);
		skipListenerLayoutB.onSkipInWrite(fileInboundStgTable, t);
		Mockito.verify(skipListenerLayoutB).onSkipInWrite(fileInboundStgTable, t);
	}

	@Test
	void onSkipInProcess()
	{

		BigDecimal jobId = BigDecimal.ONE, fileId = BigDecimal.ONE;
		String fileName = "TEST";
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
		EmailOptOuts item = new EmailOptOuts();
		item.setDateUnsubscribed("2/2/2022 7 :22");
		item.setFileName(fileName);
		item.setStatus("held");
		String jobName = "jobName";
		skipListenerLayoutB.setJobName(jobName);
		Throwable t = new Exception("message");

		Mockito.when(fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId())).thenReturn(jobId);
		Mockito.when(fileService.getFile(fileName, jobId)).thenReturn(fileId);
		Mockito.when(fileService.insertInboundStgError(fileInboundStgTable)).thenReturn(1);

		skipListenerLayoutB.onSkipInProcess(item, t);
		Mockito.verify(skipListenerLayoutB).onSkipInProcess(item, t);
	}

	@Test
	void getDateToInsertTest()
	{
		Throwable t = new Exception("month");
		String date = "2-2-2022 2 :2";
		assertNull(skipListenerLayoutB.getDateToInsert(date, t));
	}

	@Test
	void getJobName()
	{
		assertEquals("JOB_NAME", skipListenerLayoutB.getJobName());
	}
}
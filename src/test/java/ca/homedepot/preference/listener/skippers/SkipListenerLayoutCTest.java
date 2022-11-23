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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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
		MockitoAnnotations.initMocks(this);
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
	void getJobName()
	{
		assertEquals("JOB_NAME", skipListenerLayoutC.getJobName());
	}
}
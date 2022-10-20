package ca.homedepot.preference.listener.skipers;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class SkipListenerLayoutBTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	SkipListenerLayoutB skipListenerLayoutB;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.initMocks(this);
		skipListenerLayoutB.setJobName("JOB_NAME");
	}

	@Test
	void onSkipInRead()
	{
		Throwable t = Mockito.mock(Throwable.class);
		skipListenerLayoutB.onSkipInRead(t);
	}

	@Test
	void onSkipInWrite()
	{
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
		Throwable t = Mockito.mock(Throwable.class);
		skipListenerLayoutB.onSkipInWrite(fileInboundStgTable, t);
	}

	@Test
	void onSkipInProcess()
	{

		BigDecimal jobId = BigDecimal.ONE, fileId = BigDecimal.ONE;
		String fileName = "TEST";
		FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
		EmailOptOuts item = new EmailOptOuts();
		item.setFileName(fileName);
		item.setStatus("held");
		Throwable t = Mockito.mock(Throwable.class);

		Mockito.when(fileService.getJobId(anyString())).thenReturn(jobId);
		Mockito.when(fileService.getFile(eq(fileName), eq(jobId))).thenReturn(fileId);
		Mockito.when(fileService.insertInboundStgError(eq(fileInboundStgTable))).thenReturn(1);

		skipListenerLayoutB.onSkipInProcess(item, t);
	}

	@Test
	void getJobName()
	{
		assertEquals("JOB_NAME", skipListenerLayoutB.getJobName());
	}
}
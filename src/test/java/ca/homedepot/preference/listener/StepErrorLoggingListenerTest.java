package ca.homedepot.preference.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StepErrorLoggingListenerTest
{

	private StepErrorLoggingListener stepErrorLoggingListener;

	@BeforeEach
	void setUp()
	{
		stepErrorLoggingListener = new StepErrorLoggingListener();
	}

	@AfterEach
	void tearDown()
	{
	}

	@Test
	void beforeStep()
	{
		// given
		List<Throwable> exceptions = new ArrayList<>();
		StepExecution stepExecution = mock(StepExecution.class);
		when(stepExecution.getFailureExceptions()).thenReturn(exceptions);
		String stepName = "someStep";
		JobExecution jobExecution = mock(JobExecution.class);
		doNothing().when(jobExecution).addStepExecutions(anyList());
		Long id = 1l;
		// when
		stepErrorLoggingListener.beforeStep(new StepExecution(stepName, jobExecution, id));
		// then
		assertTrue(true);
	}


	@Test
	void afterStep()
	{
	}

	@Test
	void moveFile()
	{
	}
}
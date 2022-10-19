package ca.homedepot.preference.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		/**
		 * List<Throwable> exceptions = stepExecution.getFailureExceptions();
		 *
		 * if (exceptions.isEmpty()) { moveFile(); return ExitStatus.COMPLETED; } log.info(" The step: {} has {} erros. ",
		 * stepExecution.getStepName(), exceptions.size()); exceptions.forEach(ex -> log.info(" Exception has ocurred: " +
		 * ex.getMessage()));
		 *
		 *
		 *
		 * return ExitStatus.FAILED;
		 */
		List<Throwable> exceptions = new ArrayList<>();
		StepExecution stepExecution = mock(StepExecution.class);
		when(stepExecution.getFailureExceptions()).thenReturn(exceptions);
		String stepName = null;
		JobExecution jobExecution = null;
		Long id = null;
		stepErrorLoggingListener.beforeStep(new StepExecution(stepName, jobExecution, id));
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
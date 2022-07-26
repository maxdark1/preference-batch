package ca.homedepot.preference.listener;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * The type Job listener test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class JobListenerTest
{
	/**
	 * The Exit.
	 */
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	/**
	 * The Application context.
	 */
	@Mock
	ApplicationContext applicationContext;

	/**
	 * The Job listener.
	 */
	@InjectMocks
	JobListener jobListener;

	/**
	 * The Job execution.
	 */
	JobExecution jobExecution;

	/**
	 * Sets up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this.getClass());
		jobExecution = Mockito.mock(JobExecution.class);
		jobListener.beforeJob(jobExecution);
	}

	/**
	 * Test after job.
	 */
	@Test
	public void testAfterJob()
	{
		when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
		exit.expectSystemExitWithStatus(0);
		jobListener.afterJob(jobExecution);
		verify(jobExecution).getStatus();
	}

	/**
	 * Test after job with batch not completed.
	 */
	@Test
	public void testAfterJob_with_batch_not_completed()
	{
		when(jobExecution.getStatus()).thenReturn(BatchStatus.ABANDONED);
		exit.expectSystemExitWithStatus(-1);
		jobListener.afterJob(jobExecution);
		verify(jobExecution).getStatus();
	}


}
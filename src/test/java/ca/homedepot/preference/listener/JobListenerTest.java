package ca.homedepot.preference.listener;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.ApplicationContext;

import ca.homedepot.preference.service.impl.PreferenceServiceImpl;


/**
 * The type Job listener test.
 */
//@SpringBootTest
public class JobListenerTest
{
	/**
	 * The Exit.
	 */
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	/*
	 * Preference Service to save the Job
	 */
	@Mock
	PreferenceServiceImpl preferenceService;
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

	JobInstance jobInstance;

	/**
	 * Sets up.
	 */
	@Before
	public void setUp()
	{
		jobListener = new JobListener();
		MockitoAnnotations.initMocks(this.getClass());
		preferenceService = Mockito.mock(PreferenceServiceImpl.class);

		jobListener.setPreferenceService(preferenceService);
		JobParameters jobParameters = new JobParameters();
		jobExecution = new JobExecution(jobInstance, jobParameters);
		jobInstance = new JobInstance(1L, "TEST_JOB");
		jobExecution.setStartTime(new Date());
		jobExecution.setStatus(BatchStatus.STARTED);

		jobExecution.setJobInstance(jobInstance);
		jobListener.beforeJob(jobExecution);




	}

//	@Test
//	public void testStatus()
//	{
//		BatchStatus batchStatusCompleted = BatchStatus.COMPLETED;
//		BatchStatus batchStatusStarted = BatchStatus.STARTED;
//		BatchStatus batchStatusFailed = BatchStatus.FAILED;
//		BatchStatus batchStatusStopped = BatchStatus.STOPPED;
//		BatchStatus batchStatusUnknown = BatchStatus.UNKNOWN;
//
//		Assertions.assertEquals("G", jobListener.status(batchStatusStarted));
//		Assertions.assertEquals("C", jobListener.status(batchStatusCompleted));
//		Assertions.assertEquals("S", jobListener.status(batchStatusStopped));
//		Assertions.assertEquals("F", jobListener.status(batchStatusFailed));
//		Assertions.assertEquals("U", jobListener.status(batchStatusUnknown));
//	}

}
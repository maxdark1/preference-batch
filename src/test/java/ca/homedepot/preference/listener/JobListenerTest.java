package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.repositories.entities.impl.PreferenceServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.Assertions;
import org.mockito.*;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
	@Spy
	JobListener jobListener;

	/**
	 * The Job execution.
	 */
	JobExecution jobExecution;

	JobInstance jobInstance;

	List<Master> masterList;

	/**
	 * Sets up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.openMocks(this);
		masterList = new ArrayList<>();
		masterList.add(new Master(new BigDecimal("15"), new BigDecimal("5"), "JOB_STATUS", "STARTED", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("17"), new BigDecimal("5"), "JOB_STATUS", "COMPLETED", true, null));
		masterList.add(new Master(new BigDecimal("18"), new BigDecimal("5"), "JOB_STATUS", "ERROR", true, null));

		MasterProcessor.setMasterList(masterList);




		JobParameters jobParameters = new JobParameters();
		jobExecution = new JobExecution(jobInstance, jobParameters);
		jobInstance = new JobInstance(1L, "TEST_JOB");
		jobExecution.setStartTime(new Date());
		jobExecution.setStatus(BatchStatus.STARTED);

		jobExecution.setJobInstance(jobInstance);
		jobListener.beforeJob(jobExecution);
	}

	@Test
	public void testStatus()
	{
		BatchStatus batchStatusCompleted = BatchStatus.COMPLETED;
		BatchStatus batchStatusStarted = BatchStatus.STARTED;
		BatchStatus batchStatusFailed = BatchStatus.FAILED;
		BatchStatus batchStatusStarting = BatchStatus.STARTING;


		Assertions.assertEquals(masterList.get(0), jobListener.status(batchStatusStarting));
		Assertions.assertEquals(masterList.get(1), jobListener.status(batchStatusStarted));
		Assertions.assertEquals(masterList.get(2), jobListener.status(batchStatusCompleted));
		Assertions.assertEquals(masterList.get(3), jobListener.status(batchStatusFailed));
	}

	@Test
	public void testAfterJob()
	{
		jobExecution.setEndTime(new Date());
		jobExecution.setStatus(BatchStatus.COMPLETED);

		jobListener.afterJob(jobExecution);
		Mockito.verify(jobListener).afterJob(jobExecution);
	}

}
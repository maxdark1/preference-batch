package ca.homedepot.preference.listener;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * The type Job listener.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JobListener implements JobExecutionListener
{
	/**
	 * The Application context.
	 */
	private final ApplicationContext applicationContext;

	/**
	 * Before job.
	 *
	 * @param jobExecution
	 *           the job execution
	 */
	@Override
	public void beforeJob(JobExecution jobExecution)
	{
		log.debug("Batch Started.");
	}

	/**
	 * After job.
	 *
	 * @param jobExecution
	 *           the job execution
	 */
	@Override
	public void afterJob(JobExecution jobExecution)
	{
		int exit;
		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
		{
			log.debug("Batch Executed Successfully!");
			exit = SpringApplication.exit(applicationContext, () -> 0);
		}
		else
		{
			List exceptions = jobExecution.getAllFailureExceptions();
			exceptions.stream().forEach(e -> log.error("Job Failed With Error {}", e));
			exit = SpringApplication.exit(applicationContext, () -> -1);
		}
		System.exit(exit);
	}

}

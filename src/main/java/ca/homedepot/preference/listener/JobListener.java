package ca.homedepot.preference.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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


	private PreferenceService preferenceService;

	private List<JobEntity> jobEntityList;


	private DataSource dataSource;

	public void setPreferenceService(PreferenceService preferenceService)
	{
		jobEntityList = new ArrayList<>();
		this.preferenceService = preferenceService;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public JobEntity getJob(String jobName)
	{
		System.out.println("\n " + Arrays.toString(jobEntityList.toArray()) + "\n");
		return jobEntityList.stream().filter(job -> job.getJobName().contains(jobName)).findFirst().get();
	}

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
		ca.homedepot.preference.dto.Job job = new ca.homedepot.preference.dto.Job();
		job.setJob_name(jobExecution.getJobInstance().getJobName());
		job.setStatus(status(jobExecution.getStatus())); // Cambiarle

		job.setStart_time(jobExecution.getStartTime());
		job.setInserted_by("BATCH");
		job.setInserted_date(new Date());

		preferenceService.insert(job.getJob_name(), job.getStatus(), job.getStart_time(), job.getInserted_by(),
				job.getInserted_date());

	}

	public String status(BatchStatus batchStatus)
	{

		switch (batchStatus)
		{
			case STARTED:
				return "G";
			case COMPLETED:
				return "C";
			case FAILED:
				return "F";
			case STOPPED:
				return "S";
			default:
				return "U";
		}
	}

	public JdbcBatchItemWriter<Job> jobWriter()
	{

		JdbcBatchItemWriter<Job> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql(SqlQueriesConstants.SQL_INSERT_HDPC_JOB);

		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Job>());

		return writer;
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

		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
			log.info(" Job {} ends with completes status: ", jobExecution.getJobInstance().getJobName());

		Job job = new Job();
		job.setJob_name(jobExecution.getJobInstance().getJobName());
		job.setStatus(status(jobExecution.getStatus())); //Cambiar
		job.setUpdated_date(new Date());
		job.setStart_time(jobExecution.getStartTime());

		int insert = preferenceService.updateJob(job);

		log.info("  {} Job(s) updated", insert);

	}

}

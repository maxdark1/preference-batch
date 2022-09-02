package ca.homedepot.preference.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ca.homedepot.preference.config.SchedulerConfig;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


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

	public void setPreferenceService(PreferenceService preferenceService) {
		jobEntityList = new ArrayList<>();
		this.preferenceService = preferenceService;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JobEntity getJob(String jobName){
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
		job.setStatus(jobExecution.getStatus().isRunning());
		job.setStart_time(jobExecution.getStartTime());
		job.setInserted_by("BATCH");
		job.setInserted_date(new Date());

		preferenceService.insert(job.getJob_name(), job.getStatus(), job.getStart_time(), job.getInserted_by(), job.getInserted_date());

		//jobEntityList.add(preferenceService.saveJob(job));
	}

	public JdbcBatchItemWriter<Job> jobWriter(){

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
		JobEntity jobEntity = new JobEntity();
		jobEntity.setJobName(jobExecution.getJobConfigurationName());
		jobEntity.setStatus(jobExecution.getStatus().isRunning());
		jobEntity.setStartTime(jobExecution.getStartTime());
		jobEntity.setInsertedBy("BATCH");
		jobEntityList.remove(jobEntity);
		System.exit(exit);
	}

}

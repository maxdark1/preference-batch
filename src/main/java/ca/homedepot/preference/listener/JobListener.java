package ca.homedepot.preference.listener;

import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;


/**
 * The type Job listener.
 */
@Slf4j
@Component
public class JobListener implements JobExecutionListener
{
	private PreferenceService preferenceService;

	@Autowired
	public void setPreferenceService(PreferenceService preferenceService)
	{
		this.preferenceService = preferenceService;
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

		int insert = preferenceService.updateJob(job, "G");

		log.info("  {} Job(s) updated", insert);

	}

}

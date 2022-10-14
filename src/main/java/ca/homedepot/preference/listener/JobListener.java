package ca.homedepot.preference.listener;

import java.util.Date;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
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
	/**
	 * Preference service
	 */
	private PreferenceService preferenceService;

	/**
	 * Set preference service
	 * 
	 * @param preferenceService
	 */
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

		Master master = status(jobExecution.getStatus());
		job.setStatus(master.getValue_val());
		job.setStatus_id(master.getMaster_id());
		job.setStart_time(jobExecution.getStartTime());
		job.setInserted_by("BATCH");
		job.setInserted_date(new Date());

		preferenceService.insert(job.getJob_name(), job.getStatus(), job.getStatus_id(), job.getStart_time(), job.getInserted_by(),
				job.getInserted_date());

	}

	/**
	 * status
	 *
	 * @param batchStatus
	 *           the job status
	 * @return Master
	 *
	 */
	public Master status(BatchStatus batchStatus)
	{

		switch (batchStatus)
		{
			case STARTING:
				return MasterProcessor.getSourceId("JOB_STATUS", "STARTED");
			case STARTED:
				return MasterProcessor.getSourceId("JOB_STATUS", "IN PROGRESS");
			case COMPLETED:
				return MasterProcessor.getSourceId("JOB_STATUS", "COMPLETED");
			default:
				return MasterProcessor.getSourceId("JOB_STATUS", "ERROR");
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
			log.info(" Job {} ends with completes status ", jobExecution.getJobInstance().getJobName());

		Job job = new Job();
		job.setJob_name(jobExecution.getJobInstance().getJobName());

		Master master = status(jobExecution.getStatus());
		job.setStatus(master.getValue_val());
		job.setStatus_id(master.getMaster_id());

		job.setUpdated_date(new Date());
		job.setStart_time(jobExecution.getStartTime());
		job.setEnd_time(jobExecution.getEndTime());
		job.setUpdated_by("BATCH JobListener");


		int insert = preferenceService.updateJob(job, "IN PROGRESS");

		log.info("  {} Job(s) updated", insert);

	}

}

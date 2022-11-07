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

import static ca.homedepot.preference.constants.SourceDelimitersConstants.JOB_STATUS;


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

		/**
		 * Before anything else, It purges the staging table
		 */
		purgeStagingTableRecordsWithSuccessedStatus();

		/**
		 * Gets Job's information
		 */
		Job job = new Job();
		job.setJob_name(jobExecution.getJobInstance().getJobName());

		Master master = status(jobExecution.getStatus());
		job.setStatus(master.getValueVal());
		job.setStatus_id(master.getMasterId());
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
				return MasterProcessor.getSourceID(JOB_STATUS, "STARTED");
			case STARTED:
				return MasterProcessor.getSourceID(JOB_STATUS, "IN PROGRESS");
			case COMPLETED:
				return MasterProcessor.getSourceID(JOB_STATUS, "COMPLETED");
			default:
				return MasterProcessor.getSourceID(JOB_STATUS, "ERROR");
		}
	}

	/**
	 * Purge staging table records with success status
	 */
	public void purgeStagingTableRecordsWithSuccessedStatus()
	{
		int purgeRecords = preferenceService.purgeStagingTableSuccessRecords();
		log.info(" {} records has been purged from Staging table.", purgeRecords);
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

		/**
		 * Gets the current value for the job that is ending
		 */
		Job job = new Job();
		job.setJob_name(jobExecution.getJobInstance().getJobName());

		Master master = status(jobExecution.getStatus());
		job.setStatus(master.getValueVal());
		job.setStatus_id(master.getMasterId());

		job.setUpdated_date(new Date());
		job.setStart_time(jobExecution.getStartTime());
		job.setEnd_time(jobExecution.getEndTime());
		//TODO read from a contant file to make sure all jobs are using same values
		job.setUpdated_by("BATCH JobListener");


		/**
		 * Updates the job record with the end_time and status
		 */
		//TODO status should be read from master table or create a Enum to make sure all jobs are consistent
		int updatedRecords = preferenceService.updateJob(job, "IN PROGRESS");

		log.info("  {} Job(s) updated", updatedRecords);

	}

}

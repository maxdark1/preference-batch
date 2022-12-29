package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INSERTEDBY;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.JOB_STATUS;
import static ca.homedepot.preference.dto.enums.JobStatusEnum.*;


/**
 * The type Job listener.
 */
@Slf4j
@Setter
@Component
public class JobListener implements JobExecutionListener
{
	/**
	 * Preference service
	 */
	private PreferenceService preferenceService;

	private String files = "";

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
		job.setJobName(jobExecution.getJobInstance().getJobName());

		Master master = status(jobExecution.getStatus());
		job.setStatus(master.getValueVal());
		job.setStatusId(master.getMasterId());
		job.setStartTime(jobExecution.getStartTime());
		job.setInsertedBy("BATCH");
		job.setInsertedDate(new Date());

		preferenceService.insert(job.getJobName(), job.getStatusId(), job.getStartTime(), job.getInsertedBy(),
				job.getInsertedDate());

	}

	/**
	 * status
	 *
	 * @param batchStatus
	 *           the job status
	 * @return Master
	 *
	 */
	public static Master status(BatchStatus batchStatus)
	{

		switch (batchStatus)
		{
			case STARTING:
				return MasterProcessor.getSourceID(JOB_STATUS, STARTED.getStatus());
			case STARTED:
				return MasterProcessor.getSourceID(JOB_STATUS, IN_PROGRESS.getStatus());
			case COMPLETED:
				return MasterProcessor.getSourceID(JOB_STATUS, COMPLETED.getStatus());
			default:
				return MasterProcessor.getSourceID(JOB_STATUS, ERROR.getStatus());
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
		String jobName = jobExecution.getJobInstance().getJobName();
		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
		{
			String process = "inbound";
			//Outbound process
			if (jobName.toUpperCase().contains("SEND"))
				process = "outbound";
			log.error(" JOB COMPLETED SUCCESSFULLY- The {} file(s) [ {} ] has been successfully processed ", process, files);
			log.info(" Job {} ends with completed status ", jobName);
		}
		/**
		 * Gets the current value for the job that is ending
		 */
		Job job = new Job();
		job.setJobName(jobName);

		Master master = status(jobExecution.getStatus());
		Master status = status(BatchStatus.STARTED);
		job.setStatus(master.getValueVal());
		job.setStatusId(master.getMasterId());

		job.setUpdatedDate(new Date());
		job.setStartTime(jobExecution.getStartTime());
		job.setEndTime(jobExecution.getEndTime());
		job.setUpdatedBy(INSERTEDBY);


		/**
		 * Updates the job record with the end_time and status
		 */
		int updatedRecords = preferenceService.updateJob(job, status.getMasterId());

		log.info("  {} Job(s) updated", updatedRecords);

	}

}

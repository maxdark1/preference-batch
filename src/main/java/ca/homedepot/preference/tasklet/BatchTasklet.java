package ca.homedepot.preference.tasklet;


import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


/**
 * The type Batch tasklet.
 */
@Slf4j
@RequiredArgsConstructor
@Component

public class BatchTasklet implements Tasklet
{
	/**
	 * The back inn stock service.
	 */
	private final PreferenceService preferenceService;


	public PreferenceService getPreferenceService()
	{
		return preferenceService;
	}

	/**
	 * Execute repeat status.
	 *
	 * @param stepContribution
	 *           the step contribution
	 * @param chunkContext
	 *           the chunk context
	 * @return the repeat status
	 * @throws Exception
	 *            the exception
	 */
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
	{
		log.debug("Tasklet is executing");
		try
		{
			purgeStagingTableRecordsWithSuccessedStatus();
		}
		catch (Exception e)
		{
			log.error("Error while Executing Tasklet :", e);
		}

		return RepeatStatus.FINISHED;
	}

	/**
	 * Purge staging table records with success status
	 */
	public void purgeStagingTableRecordsWithSuccessedStatus()
	{
		int purgeRecords = preferenceService.purgeStagingTableSuccessRecords();
		log.info(" {} records has been purged.", purgeRecords);
	}

}

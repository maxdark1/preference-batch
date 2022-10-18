package ca.homedepot.preference.tasklet;


import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.PreferenceItemList;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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
	private final PreferenceService backinStockService;

	/**
	 * The Email message publisher.
	 */
	//private final EmailMessagePublisher emailMessagePublisher;


	/**
	 * The Purge daysfor analytics.
	 */
	@Value("200")
	Integer purgeDaysforAnalytics;
	/**
	 * The purge days for Inventory status
	 */
	@Value("100")
	Integer purgeDaysforInventoryStatus;

	/**
	 * The Template id en.
	 */
	@Value("ca")
	String templateIdEn;

	/**
	 * The Template id fr.
	 */
	@Value("cd")
	String templateIdFr;

	/**
	 * The Subject en.
	 */
	@Value("hello")
	String subjectEn;

	/**
	 * The Subject fr.
	 */
	@Value("df")
	String subjectFr;

	/**
	 * The Environment.
	 */
	@Value("dev")
	String environment;

	public PreferenceService getBackinStockService()
	{
		return backinStockService;
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
			//getPreferences();
		}
		catch (Exception e)
		{
			log.error("Error while Executing Tasklet :", e);
		}

		return RepeatStatus.FINISHED;
	}


	/**
	 * Send email.
	 *
	 * @param jobEntity
	 *           the notification subscription entity
	 */
	//	private void sendEmail(JobEntity jobEntity)
	//	{
	//
	//
	//		EmailAddressDTO emailAddressDTO = new EmailAddressDTO();
	//		//emailAddressDTO.setEmail(jobEntity.getEmailId());
	//		//emailAddressDTO.setName(jobEntity.getEmailId());
	//
	//		EmailParametersDTO emailParametersDTO = new EmailParametersDTO();
	//		//emailParametersDTO.setSubject(FRENCH.equalsIgnoreCase(jobEntity.getLangcode()) ? subjectFr : subjectEn);
	//		emailParametersDTO.setEnvironment(environment);
	//
	//		//emailMessagePublisher.publishEmailMessageToTopic(null);
	//
	//		saveEmailRequest(jobEntity);
	//	}

	/**
	 * Save email request.
	 *
	 * @param jobEntity
	 *           the notification subscription entity
	 */
	private void saveEmailRequest(JobEntity jobEntity)
	{
		//EmailEntity emailEntity = new EmailEntity();
		//emailEntity.setEmailId(jobEntity.getEmailId());
		//emailEntity.setRegId(jobEntity.getRegId());
		//emailEntity.setArticleId(jobEntity.getArticleId());
		//emailEntity.setEmailType(ContactTypeEnum.EMAIL.toString());
		//emailEntity.setInventory(PreferenceBatchConstants.SHELF_LIFE_EXPIRY_INVENTORY);
		//emailService.saveEmailEntity(emailEntity);
	}

}

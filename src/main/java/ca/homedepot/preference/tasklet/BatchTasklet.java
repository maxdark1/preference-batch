package ca.homedepot.preference.tasklet;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.data.ContactTypeEnum;
import ca.homedepot.preference.dto.EmailAddressDTO;
import ca.homedepot.preference.dto.EmailMessagePublisher;
import ca.homedepot.preference.dto.EmailParametersDTO;
import ca.homedepot.preference.repositories.entities.EmailEntity;
import ca.homedepot.preference.repositories.entities.NotificationSubscriptionEntity;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ca.homedepot.preference.constants.PreferenceBatchConstants.FRENCH;


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
	private final EmailMessagePublisher emailMessagePublisher;

	/**
	 * The Purge days.
	 */
	@Value("${shelflife.bisn.purge.days}")
	Integer purgeDays;

	/**
	 * The Purge daysfor analytics.
	 */
	@Value("${analytics.bisn.purge.days}")
	Integer purgeDaysforAnalytics;
	/**
	 * The purge days for Inventory status
	 */
	@Value("${inventory.bisn.purge.days}")
	Integer purgeDaysforInventoryStatus;

	/**
	 * The Template id en.
	 */
	@Value("${email.en.template}")
	String templateIdEn;

	/**
	 * The Template id fr.
	 */
	@Value("${email.fr.template}")
	String templateIdFr;

	/**
	 * The Subject en.
	 */
	@Value("${email.en.subject}")
	String subjectEn;

	/**
	 * The Subject fr.
	 */
	@Value("${email.fr.subject}")
	String subjectFr;

	/**
	 * The Environment.
	 */
	@Value("${email.domain.environment}")
	String environment;


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
			purgeOldRecords();
			purgeOldRecordsfromRegistration();
			purgeOldRecordsfromEmail();
			purgeOldRecordsfromInventory();
		}
		catch (Exception e)
		{
			log.error("Error while Executing Tasklet :", e);
		}

		return RepeatStatus.FINISHED;
	}

	private void purgeOldRecordsfromInventory()
	{
		log.debug("Started Deleting old records from Inventory status message");
		Date deleteDate = Date
				.from(LocalDateTime.now().minusDays(purgeDaysforInventoryStatus).atZone(ZoneId.systemDefault()).toInstant());
		Integer count = backinStockService.purgeOldRecordsfromInventory(deleteDate);
		log.info("Total number of records purged from inventory_status_message:{}", count);
	}

	/**
	 * Purge old records.
	 */
	public void purgeOldRecords()
	{
		log.debug("Started Deleting old records from notification subscription");
		Date deleteDate = Date.from(LocalDateTime.now().minusDays(purgeDays).atZone(ZoneId.systemDefault()).toInstant());
		List<NotificationSubscriptionEntity> notificationSubscriptionEntites = backinStockService
				.getAllNotificationsCreatedBefore(deleteDate);
		notificationSubscriptionEntites.stream().filter(Objects::nonNull).forEach(notification -> sendEmail(notification));
		backinStockService.purgeOldRecords(notificationSubscriptionEntites);
		log.info("Total " + notificationSubscriptionEntites.size() + " Old Records deleted");
	}

	/**
	 * Purge old recordsfrom registration.
	 */
	public void purgeOldRecordsfromRegistration()
	{
		log.debug("Started Deleting old records from notification registartion");
		Date deleteDate = Date
				.from(LocalDateTime.now().minusDays(purgeDaysforAnalytics).atZone(ZoneId.systemDefault()).toInstant());
		Integer count = backinStockService.purgeOldRecordsfromRegistration(deleteDate);
		log.info("Total " + count + " Old Records deleted");
	}

	/**
	 * Purge old recordsfrom email.
	 */
	public void purgeOldRecordsfromEmail()
	{
		log.debug("Started Deleting old records from notification email");
		Date deleteDate = Date
				.from(LocalDateTime.now().minusDays(purgeDaysforAnalytics).atZone(ZoneId.systemDefault()).toInstant());
		Integer count = backinStockService.purgeOldRecordsfromEmail(deleteDate);
		log.info("Total " + count + " Old Records deleted");
	}

	/**
	 * Send email.
	 *
	 * @param notificationSubscriptionEntity
	 *           the notification subscription entity
	 */
	private void sendEmail(NotificationSubscriptionEntity notificationSubscriptionEntity)
	{


		EmailAddressDTO emailAddressDTO = new EmailAddressDTO();
		emailAddressDTO.setEmail(notificationSubscriptionEntity.getEmailId());
		emailAddressDTO.setName(notificationSubscriptionEntity.getEmailId());

		EmailParametersDTO emailParametersDTO = new EmailParametersDTO();
		emailParametersDTO
				.setSubject(FRENCH.equalsIgnoreCase(notificationSubscriptionEntity.getLangcode()) ? subjectFr : subjectEn);
		emailParametersDTO.setEnvironment(environment);

		//emailMessagePublisher.publishEmailMessageToTopic(null);

		saveEmailRequest(notificationSubscriptionEntity);
	}

	/**
	 * Save email request.
	 *
	 * @param notificationSubscriptionEntity
	 *           the notification subscription entity
	 */
	private void saveEmailRequest(NotificationSubscriptionEntity notificationSubscriptionEntity)
	{
		EmailEntity emailEntity = new EmailEntity();
		emailEntity.setEmailId(notificationSubscriptionEntity.getEmailId());
		emailEntity.setRegId(notificationSubscriptionEntity.getRegId());
		emailEntity.setArticleId(notificationSubscriptionEntity.getArticleId());
		emailEntity.setEmailType(ContactTypeEnum.EMAIL.toString());
		emailEntity.setInventory(PreferenceBatchConstants.SHELF_LIFE_EXPIRY_INVENTORY);
		//emailService.saveEmailEntity(emailEntity);
	}

}

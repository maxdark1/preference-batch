package ca.homedepot.preference.tasklet;

import ca.homedepot.preference.dto.EmailMessagePublisher;
import ca.homedepot.preference.repositories.entities.NotificationSubscriptionEntity;
import ca.homedepot.preference.service.PreferenceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * The type Batch tasklet test.
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchTaskletTest
{
	/**
	 * The Batch tasklet.
	 */
	@InjectMocks
	BatchTasklet batchTasklet;

	/**
	 * The preference service.
	 */
	@Mock
	PreferenceService preferenceService;

	/**
	 * The Email message publisher.
	 */
	@Mock
	EmailMessagePublisher emailMessagePublisher;

	StepContribution stepContribution;

	/**
	 * The Chunk context.
	 */
	ChunkContext chunkContext;

	/**
	 * The Notification subscription entity.
	 */
	NotificationSubscriptionEntity notificationSubscriptionEntity;




	/**
	 * Sets up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this.getClass());

		stepContribution = Mockito.mock(StepContribution.class);
		chunkContext = Mockito.mock(ChunkContext.class);

		notificationSubscriptionEntity = new NotificationSubscriptionEntity();
		notificationSubscriptionEntity.setRegId("876bd496-4712-4499-a236-e86be5a6ed2c");
		notificationSubscriptionEntity.setNotificationType("EMAIL");
		notificationSubscriptionEntity.setArticleId("1000834262");
		notificationSubscriptionEntity.setEmailId("naruto_uzumaki9@homedepot.com");
		notificationSubscriptionEntity.setLangcode("fr");
		notificationSubscriptionEntity.setSubscriptionType("EMAIL");
		notificationSubscriptionEntity.setUom("02521474-1a85-44d2-8c57-12e456a53608");
		notificationSubscriptionEntity.setCreatedOn(new Date());
		notificationSubscriptionEntity.setPhoneNo("+1-613-555-0121");




		ReflectionTestUtils.setField(batchTasklet, "purgeDays", 1);
		ReflectionTestUtils.setField(batchTasklet, "purgeDaysforAnalytics", 1);
		ReflectionTestUtils.setField(batchTasklet, "purgeDaysforInventoryStatus", 1);
		ReflectionTestUtils.setField(batchTasklet, "templateIdEn", "492");
		ReflectionTestUtils.setField(batchTasklet, "templateIdFr", "492");
		ReflectionTestUtils.setField(batchTasklet, "subjectEn", "Test Subject");
		ReflectionTestUtils.setField(batchTasklet, "subjectFr", "Test Subject");
		ReflectionTestUtils.setField(batchTasklet, "environment", "https://api.qa-gcp.homedepot.ca/");
	}

	/**
	 * Test execute.
	 */
	@Test
	public void testExecute()
	{
		when(preferenceService.getAllNotificationsCreatedBefore(any(Date.class)))
				.thenReturn(Arrays.asList(notificationSubscriptionEntity));


		when(preferenceService.purgeOldRecordsfromInventory(any())).thenReturn(4);

		batchTasklet.execute(stepContribution, chunkContext);


		//		verify(emailMessagePublisher).publishEmailMessageToTopic(any(EmailDTO.class));
	}

	/**
	 * Test execute with english.
	 */
	@Test
	public void testExecuteWithEnglish()
	{
		notificationSubscriptionEntity.setLangcode("en");
		when(preferenceService.getAllNotificationsCreatedBefore(any(Date.class)))
				.thenReturn(Arrays.asList(notificationSubscriptionEntity));


		when(preferenceService.purgeOldRecordsfromInventory(any())).thenReturn(4);

		batchTasklet.execute(stepContribution, chunkContext);


		//verify(emailMessagePublisher).publishEmailMessageToTopic(any(EmailDTO.class));
	}

	/**
	 * Test execute with exception.
	 */
	@Test
	public void testExecuteWithException()
	{
		when(preferenceService.getAllNotificationsCreatedBefore(any(Date.class)))
				.thenReturn(Arrays.asList(notificationSubscriptionEntity));


		batchTasklet.execute(stepContribution, chunkContext);

		verify(preferenceService).getAllNotificationsCreatedBefore(any(Date.class));
	}

}

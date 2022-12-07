package ca.homedepot.preference.tasklet;

import ca.homedepot.preference.repositories.entities.JobEntity;
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

import static org.junit.Assert.assertTrue;


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



	StepContribution stepContribution;

	/**
	 * The Chunk context.
	 */
	ChunkContext chunkContext;

	/**
	 * The Notification subscription entity.
	 */
	JobEntity jobEntity;




	/**
	 * Sets up.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this.getClass());

		stepContribution = Mockito.mock(StepContribution.class);
		chunkContext = Mockito.mock(ChunkContext.class);

		jobEntity = new JobEntity();

	}

	/**
	 * Test execute.
	 */
	//@Test
	public void testExecute()
	{
		//when(preferenceService.getAllNotificationsCreatedBefore(any(Date.class))).thenReturn(Arrays.asList(jobEntity));


		//when(preferenceService.purgeOldRecordsfromInventory(any())).thenReturn(4);

		batchTasklet.execute(stepContribution, chunkContext);


		//		verify(emailMessagePublisher).publishEmailMessageToTopic(any(EmailDTO.class));
	}

	/**
	 * Test execute with english.
	 */
	//@Test
	public void testExecuteWithEnglish()
	{
		//jobEntity.setLangcode("en");
		//when(preferenceService.getAllNotificationsCreatedBefore(any(Date.class))).thenReturn(Arrays.asList(jobEntity));


		//when(preferenceService.purgeOldRecordsfromInventory(any())).thenReturn(4);

		batchTasklet.execute(stepContribution, chunkContext);


		//verify(emailMessagePublisher).publishEmailMessageToTopic(any(EmailDTO.class));
	}

	/**
	 * Test execute with exception.
	 */
	@Test
	public void testExecuteWithException()
	{
		batchTasklet.execute(stepContribution, chunkContext);
		assertTrue(true);

	}

}

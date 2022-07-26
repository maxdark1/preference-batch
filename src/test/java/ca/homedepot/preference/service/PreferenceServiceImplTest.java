package ca.homedepot.preference.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.homedepot.preference.repositories.PreferenceCenterRepository;
import ca.homedepot.preference.repositories.entities.NotificationSubscriptionEntity;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;

import java.util.Arrays;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PreferenceServiceImplTest
{

	/**
	 * The BackinStockRepository repository.
	 */
	@Mock
	PreferenceCenterRepository preferenceCentreRepository;

	/**
	 * The Preference service.
	 */
	@InjectMocks
	PreferenceServiceImpl preferenceServiceImpl;

	/**
	 * The NotificationSubscription entity.
	 */
	NotificationSubscriptionEntity backinStockEntity;

	/**
	 * setUp Method.
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this.getClass());
		backinStockEntity = new NotificationSubscriptionEntity();
		backinStockEntity.setEmailId("test@gmail.com");
		backinStockEntity.setRegId("123");
		backinStockEntity.setLangcode("en");
		backinStockEntity.setNotificationType("SUB");
		backinStockEntity.setPhoneNo("9878987678");
		backinStockEntity.setArticleId("654");
		backinStockEntity.setSubscriptionType("SUB");
	}

	/**
	 * Test purge old records.
	 */
	@Test
	public void testPurgeOldRecords()
	{
		when(preferenceCentreRepository.deleteAllByCreatedOnLessThan(ArgumentMatchers.<Date> any())).thenReturn(4);
		preferenceServiceImpl.purgeOldRecords(Arrays.asList(backinStockEntity));
		verify(preferenceCentreRepository).deleteAll(anyList());
	}

	/**
	 * Test purge old records with no records.
	 */
	@Test
	public void testPurgeOldRecords_With_No_records()
	{
		when(preferenceCentreRepository.deleteAllByCreatedOnLessThan(any(Date.class))).thenReturn(0);
		preferenceServiceImpl.purgeOldRecords(Arrays.asList(backinStockEntity));
		verify(preferenceCentreRepository).deleteAll(anyList());
	}

	@Test
	public void testGetAllNotificationsCreatedBefore()
	{
		when(preferenceCentreRepository.findAllByCreatedOnLessThan(any(Date.class))).thenReturn(Arrays.asList(backinStockEntity));
		assertEquals(Arrays.asList(backinStockEntity), preferenceServiceImpl.getAllNotificationsCreatedBefore(new Date()));
	}

}

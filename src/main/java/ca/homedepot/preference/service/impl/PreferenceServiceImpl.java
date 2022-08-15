package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.repositories.entities.JobEntity;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.repositories.JobRepository;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Preference service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	/**
	 * The notification subscription repository.
	 */
	private final JobRepository jobRepository;


	/**
	 * Purge old records from notification subscription.
	 *
	 * @param notificationSubscriptionEntities
	 *           the notification subscription entities
	 */
	@Override
	public void purgeOldRecords(List<JobEntity> notificationSubscriptionEntities)
	{
		jobRepository.deleteAll(notificationSubscriptionEntities);
	}

	/**
	 * Gets all notifications created before given date.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the all notifications created before given date
	 */
	@Override
	public List<JobEntity> getAllNotificationsCreatedBefore(Date createdDate)
	{
		return jobRepository.findAllByStartTimeLessThan(createdDate);
	}

	/**
	 * Purge old records integer.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromRegistration(Date createdDate)
	{

		return 1;// registrationRepository.deleteAllByCreatedOnLessThan(createdDate);
	}

	/**
	 * Purge old records integer.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromEmail(Date createdDate)
	{

		return 1;//emailRepository.deleteAllByCreatedOnLessThan(createdDate);
	}

	/**
	 * Purge old records from inventory status.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromInventory(Date createdDate)
	{
		return 1;//inventoryStatusMessagesRepository.deleteAllByCreatedTsLessThan(createdDate);
	}

}

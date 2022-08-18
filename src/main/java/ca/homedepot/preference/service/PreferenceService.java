package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.PreferenceItemList;
import ca.homedepot.preference.repositories.entities.JobEntity;

import java.util.Date;
import java.util.List;


/**
 * The interface Preference service.
 */

public interface PreferenceService
{

	/**
	 * Purge old records from notification subscription.
	 *
	 * @param notificationSubscriptionEntities
	 *           the notification subscription entities
	 */
	void purgeOldRecords(List<JobEntity> notificationSubscriptionEntities);

	/**
	 * Gets all notifications created before given date.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the all notifications created before given date
	 */
	List<JobEntity> getAllNotificationsCreatedBefore(Date createdDate);

	/**
	 * Purge old records from notification registration.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */
	Integer purgeOldRecordsfromRegistration(Date createdDate);

	/**
	 * Purge old records from notification email.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	Integer purgeOldRecordsfromEmail(Date createdDate);

	/**
	 * Purge old records from inventory status.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	Integer purgeOldRecordsfromInventory(Date createdDate);

	PreferenceItemList getPreferences(String id);
}

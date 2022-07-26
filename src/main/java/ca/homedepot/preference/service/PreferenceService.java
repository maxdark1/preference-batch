package ca.homedepot.preference.service;

import ca.homedepot.preference.repositories.entities.NotificationSubscriptionEntity;
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
	void purgeOldRecords(List<NotificationSubscriptionEntity> notificationSubscriptionEntities);

	/**
	 * Gets all notifications created before given date.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the all notifications created before given date
	 */
	List<NotificationSubscriptionEntity> getAllNotificationsCreatedBefore(Date createdDate);

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

}

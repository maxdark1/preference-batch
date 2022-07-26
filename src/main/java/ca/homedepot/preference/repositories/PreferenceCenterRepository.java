package ca.homedepot.preference.repositories;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.homedepot.preference.repositories.entities.NotificationSubscriptionEntity;

/**
 * The interface Preference Center repository.
 */
@Repository
@Transactional
public interface PreferenceCenterRepository extends JpaRepository<NotificationSubscriptionEntity, String>
{

	/**
	 * Find all by created on less than list.
	 *
	 * @param createdOn
	 *           the created on
	 * @return the list
	 */
	List<NotificationSubscriptionEntity> findAllByCreatedOnLessThan(Date createdOn);

	/**
	 * Delete all by created on less than integer.
	 *
	 * @param createdOn
	 *           the created on
	 * @return the integer
	 */
	Integer deleteAllByCreatedOnLessThan(Date createdOn);
}
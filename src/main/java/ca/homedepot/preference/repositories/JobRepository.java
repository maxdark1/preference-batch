package ca.homedepot.preference.repositories;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.homedepot.preference.repositories.entities.JobEntity;

/**
 * The interface Preference Center repository.
 */
@Repository
@Transactional
public interface JobRepository extends JpaRepository<JobEntity, String>
{

	/**
	 * Find all by created on less than list.
	 *
	 * @param startTime
	 *           the created on
	 * @return the list
	 */
	List<JobEntity> findAllByStartTimeLessThan(Date startTime);

	/**
	 * Delete all by created on less than integer.
	 *
	 * @param startTime
	 *           the created on
	 * @return the integer
	 */
	Integer deleteAllByStartTimeLessThan(Date startTime);
}
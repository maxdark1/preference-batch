package ca.homedepot.preference.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.repositories.entities.JobEntity;

/**
 * The interface Preference Center repository.
 */
@Repository
public interface JobRepo extends JpaRepository<JobEntity, String>
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


	@Query(value = SqlQueriesConstants.SQL_INSERT_HDPC_JOB, nativeQuery = true)
	void insert(String job_name, Boolean status, Date start_time, String inserted_by, Date inserted_date);
}
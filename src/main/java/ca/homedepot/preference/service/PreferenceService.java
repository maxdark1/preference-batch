package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.dto.PreferenceItemList;
import ca.homedepot.preference.repositories.entities.JobEntity;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


/**
 * The interface Preference service.
 */

public interface PreferenceService
{

	PreferenceItemList getPreferences(String id);

	void saveJob(Job job);

	int insert(String job_name, Boolean status, Date start_time, String inserted_by, Date inserted_date);

}

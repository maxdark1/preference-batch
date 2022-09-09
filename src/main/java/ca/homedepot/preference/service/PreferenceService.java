package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.*;

import java.util.Date;
import java.util.List;


/**
 * The interface Preference service.
 */

public interface PreferenceService
{

	PreferenceItemList getPreferences(String id);

	RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items);

	int insert(String job_name, String status, Date start_time, String inserted_by, Date inserted_date);

	List<Master> getMasterInfo();

	int updateJob(Job job);
}

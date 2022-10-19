package ca.homedepot.preference.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.dto.*;


/**
 * The interface Preference service.
 */

public interface PreferenceService
{

	RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items);

	RegistrationResponse preferencesSFMCEmailOptOutsLayoutB(List<? extends RegistrationRequest> items);

	int insert(String job_name, String status, BigDecimal status_id, Date start_time, String inserted_by, Date inserted_date);

	List<Master> getMasterInfo();

	int updateJob(Job job, String status);
}

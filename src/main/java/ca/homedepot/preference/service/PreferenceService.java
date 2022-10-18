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

	/**
	 * Gets preferences
	 * 
	 * @param id
	 * @return preference Item list
	 */
	PreferenceItemList getPreferences(String id);

	/**
	 * Sends to API LayoutC endpoint Registration information
	 * 
	 * @param items
	 * @return registration response
	 */
	RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items);

	/**
	 * Sends to API Unsubscribed LAyoutB information
	 * 
	 * @param items
	 * @return Registration Response with status
	 */
	RegistrationResponse preferencesSFMCEmailOptOutsLayoutB(List<? extends RegistrationRequest> items);

	/**
	 * Inserts on persistence job information
	 * 
	 * @param job_name
	 * @param status
	 * @param status_id
	 * @param start_time
	 * @param inserted_by
	 * @param inserted_date
	 * @return inserted records
	 */
	int insert(String job_name, String status, BigDecimal status_id, Date start_time, String inserted_by, Date inserted_date);

	/**
	 * Gets master's information
	 * 
	 * @return Master information
	 */
	List<Master> getMasterInfo();

	/**
	 * Update job's status
	 * 
	 * @param job
	 * @param status
	 * @return updated records
	 */
	int updateJob(Job job, String status);

	/**
	 * Purge records with Sucess status
	 * @return
	 */
	int purgeStagingTableSuccessRecords();
}

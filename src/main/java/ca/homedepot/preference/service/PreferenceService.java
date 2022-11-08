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
	 * @param jobName
	 * @param status
	 * @param statusId
	 * @param startTime
	 * @param insertedBy
	 * @param insertedDate
	 * @return inserted records
	 */
	int insert(String jobName, String status, BigDecimal statusId, Date startTime, String insertedBy, Date insertedDate);

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
	 *
	 * @return
	 */
	int purgeStagingTableSuccessRecords();
}

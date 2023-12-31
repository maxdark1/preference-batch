package ca.homedepot.preference.repositories.entities.impl;

import ca.homedepot.preference.config.feign.PreferenceRegistrationClient;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.service.PreferenceService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.SUCCESS;

/**
 * The type Preference service.
 */
@Service
@Slf4j

public class PreferenceServiceImpl implements PreferenceService
{


	/**
	 * The JDBC template
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * The preference registration client
	 */
	private PreferenceRegistrationClient preferenceRegistrationClient;


	/**
	 * Sets JdbcTemplate
	 *
	 * @param jdbcTemplate
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	public void setPreferenceRegistrationFeignClient(PreferenceRegistrationClient preferenceRegistrationClient)
	{
		this.preferenceRegistrationClient = preferenceRegistrationClient;
	}

	public Response isServiceAvailable()
	{
		return preferenceRegistrationClient.getServiceStatus();
	}

	/**
	 * Send request to service for subscribe/unsubscribe from marketing programs
	 *
	 * @param items
	 *
	 */
	public RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items)
	{
		log.debug(" {} item(s) has been sent through Request Registration {} ", items.size(), new Gson().toJson(items));
		log.info(" {} item(s) has been sent through Request Registration.", items.size());
		return preferenceRegistrationClient.registration(items);
	}

	/**
	 * Send request to service for SFMC unsubscribe
	 *
	 * @param items
	 *
	 */
	@Override
	public RegistrationResponse preferencesSFMCEmailOptOutsLayoutB(List<? extends RegistrationRequest> items)
	{

		log.debug(" {} item(s) has been sent through Request Registration LayoutB {} ", items.size(), new Gson().toJson(items));
		log.info(" {}  item(s) has been sent through Request Registration Layout B.", items.size());
		return preferenceRegistrationClient.registrationLayoutB(items);
	}

	@Override
	public RegistrationResponse sendEmail(EmailNotificationRequest email)
	{
		log.error("PREFERENCE-BATCH-LOG: Calling Notification Email Endpoint");
		RegistrationResponse response;
		try
		{
			response = preferenceRegistrationClient.sendEmail(email);
		}
		catch (Exception ex)
		{
			log.error("PREFERENCE-BATCH-ERROR: Error Sending Email to Endpoint" + ex.getMessage());
			response = null;
		}
		return response;
	}


	/**
	 * Inserts on persistence job information
	 *
	 * @param jobName
	 * @param statusId
	 * @param startTime
	 * @param insertedBy
	 * @param insertedDate
	 * @return inserted records
	 */
	@Override
	public int insert(String jobName, BigDecimal statusId, Date startTime, String insertedBy, Date insertedDate)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, jobName, statusId, startTime, insertedBy, insertedDate);
	}

	/**
	 * Gets master's information
	 *
	 * @return Master information
	 */
	@Override
	public List<Master> getMasterInfo()
	{
		return jdbcTemplate.query(SqlQueriesConstants.SQL_SELECT_MASTER_ID,
				(rs, rowNum) -> new Master(rs.getBigDecimal("master_id"), rs.getBigDecimal("key_id"), rs.getString("key_value"),
						rs.getString("value_val"), rs.getBoolean("active"), rs.getBigDecimal("old_id")));
	}

	/**
	 * Update job's status
	 *
	 * @param job
	 * @param status
	 * @return updated records
	 */
	@Override
	public int updateJob(Job job, BigDecimal status)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STATUS_JOB, job.getStatusId(), job.getUpdatedDate(),
				job.getUpdatedBy(), job.getEndTime(), job.getStartTime(), job.getJobName(), status);
	}

	/**
	 * Purge the staging table with all the success records
	 *
	 * @return
	 */
	@Override
	public int purgeStagingTableSuccessRecords()
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_PURGE_SUCCESS_STG_TABLE, SUCCESS);
	}

	@Override
	public int saveNotificationEvent(String filaName, String Quantity, String loaded, String failed, String sourceName,
			String type, String insertedBy)
	{
		jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_NOTIFICATION, filaName, Quantity, loaded, failed, sourceName, type,
				insertedBy);
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_GET_NOTIFICATION_ID, Integer.class);
	}


}

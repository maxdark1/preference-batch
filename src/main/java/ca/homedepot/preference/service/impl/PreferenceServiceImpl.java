package ca.homedepot.preference.service.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.config.feign.PreferenceRegistrationClient;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


/**
 * The type Preference service.
 */
@Service
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	/**
	 * The base url
	 */
	@Value("${service.preference.baseurl}")
	public String baseUrl;

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


	/**
	 * Send request to service for subscribe/unsubscribe from marketing programs
	 *
	 * @param items
	 *
	 */
	public RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items)
	{

		log.info(" {} item(s) has been sent through Request Registration {} ", items.size(), new Gson().toJson(items));


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

		log.info(" {} item(s) has been sent through Request Registration LayoutB {} ", items.size(), new Gson().toJson(items));

		return preferenceRegistrationClient.registrationLayoutB(items);
	}


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

	@Override
	public int insert(String job_name, String status, BigDecimal status_id, Date start_time, String inserted_by,
			Date inserted_date)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, job_name, status, status_id, start_time, inserted_by,
				inserted_date);
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
	public int updateJob(Job job, String status)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STAUTS_JOB, job.getStatus_id(), job.getUpdated_date(),
				job.getUpdated_by(), job.getStatus(), job.getEnd_time(), job.getStart_time(), job.getJob_name(), status);
	}

	/**
	 * Purge
	 * 
	 * @return
	 */
	@Override
	public int purgeStagingTableSuccessRecords()
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_PURGE_SUCCESS_STG_TABLE);
	}


}

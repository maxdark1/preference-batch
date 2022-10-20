package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.config.feign.PreferenceRegistrationClient;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The type Preference service.
 */
@Service
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	@Value("${service.preference.baseurl}")
	public String baseUrl;
	private JdbcTemplate jdbcTemplate;
	private WebClient webClient;

	private PreferenceRegistrationClient preferenceRegistrationClient;

	/**
	 * Sent JdbcTemplate
	 *
	 * @param jdbcTemplate
	 *
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setWebClient(WebClient webClient)
	{
		this.webClient = webClient;
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

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

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

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_SFMC_EXTACT_TARGET_EMAIL;

		log.info(" {} item(s) has been sent through Request Registration LayoutB {} ", items.size(), new Gson().toJson(items));

		return preferenceRegistrationClient.registrationLayoutB(items);
	}


	/**
	 * Save Job Information on persistence
	 *
	 * @param job_name,
	 *           status, start_time, inserted_by, inserted_date
	 */
	@Override
	public int insert(String job_name, String status, BigDecimal status_id, Date start_time, String inserted_by,
			Date inserted_date)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, job_name, status, status_id, start_time, inserted_by,
				inserted_date);
	}

	/**
	 * Gets Master's table information from persistence
	 *
	 */
	@Override
	public List<Master> getMasterInfo()
	{
		return jdbcTemplate.query(SqlQueriesConstants.SQL_SELECT_MASTER_ID,
				(rs, rowNum) -> new Master(rs.getBigDecimal("master_id"), rs.getBigDecimal("key_id"), rs.getString("key_value"),
						rs.getString("value_val"), rs.getBoolean("active")));
	}

	/**
	 * Update Job status on persistence
	 *
	 * @param job,
	 *           status
	 *
	 */
	@Override
	public int updateJob(Job job, String status)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STAUTS_JOB, job.getStatus_id(), job.getUpdated_date(),
				job.getUpdated_by(), job.getStatus(), job.getEnd_time(), job.getStart_time(), job.getJob_name(), status);
	}


}

package ca.homedepot.preference.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.config.feign.PreferenceRegistrationClient;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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

import javax.sql.DataSource;


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
	 * Initialization of WebClient
	 */
	@Autowired
	public void setUpWebClient()
	{
		this.webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
				.baseUrl(baseUrl).build();
	}


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

	//	public PreferenceItemList getPreferences(String id)
	//	{
	//		String path = baseUrl + "{id}/preferences";
	//
	//		PreferenceItemList response = webClient.get().uri(uriBuilder -> uriBuilder.path(path).build(id))
	//				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(PreferenceItemList.class)
	//				.doOnError(e -> log.error(e.getMessage())).block();
	//
	//		log.info("Response {} ", response);
	//		return response;
	//
	//	}

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

	@Override
	public void preferenceOutbound(PreferenceOutboundDto item, DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND,
				item.getEmail(),
				item.getEffective_date(),
				item.getSource_id(),
				item.getEmail_status(),
				item.getEmail_permission(),
				item.getLanguage_pref(),
				item.getEarly_opt_in_date(),
				item.getCnd_compliant_flag(),
				item.getEmail_pref_hd_ca(),
				item.getEmail_pref_garden_club(),
				item.getEmail_pref_pro(),
				item.getPostal_code(),
				item.getCustomer_nbr(),
				item.getPhone_ptc_flag(),
				item.getDncl_suppresion(),
				item.getPhone_number(),
				item.getFirst_name(),
				item.getLast_name(),
				item.getBusiness_name(),
				item.getIndustry_code(),
				item.getCity(),
				item.getProvince(),
				item.getHd_ca_pro_src_id());
	}
}

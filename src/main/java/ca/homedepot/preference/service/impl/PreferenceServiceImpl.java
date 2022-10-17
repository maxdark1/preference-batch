package ca.homedepot.preference.service.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
import reactor.core.publisher.Mono;
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
	 * The WebClient
	 */
	private WebClient webClient;

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
	 * Sets JdbcTemplate
	 * 
	 * @param jdbcTemplate
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Sets WebClient
	 * 
	 * @param webClient
	 */
	public void setWebClient(WebClient webClient)
	{
		this.webClient = webClient;
	}

	/**
	 * Gets preferences
	 * 
	 * @param id
	 * @return preference Item list
	 */
	public PreferenceItemList getPreferences(String id)
	{
		String path = baseUrl + "{id}/preferences";

		PreferenceItemList response = webClient.get().uri(uriBuilder -> uriBuilder.path(path).build(id))
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(PreferenceItemList.class)
				.doOnError(e -> log.error(e.getMessage())).block();

		log.info("Response {} ", response);
		return response;

	}

	/**
	 * Sends to API LayoutC endpoint Registration information
	 * 
	 * @param items
	 * @return registration response
	 */
	public RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items)
	{

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

		log.info(" {} item(s) has been sent through Request Registration {} ", items.size(), new Gson().toJson(items));

		return webClient.post().uri(path).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(items).retrieve().bodyToMono(RegistrationResponse.class).doOnError(e -> log.error(e.getMessage())).block();
	}

	/**
	 * Sends to API Unsubscribed LAyoutB information
	 * 
	 * @param items
	 * @return Registration Response with status
	 */
	@Override
	public RegistrationResponse preferencesSFMCEmailOptOutsLayoutB(List<? extends RegistrationRequest> items)
	{

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_SFMC_EXTACT_TARGET_EMAIL;

		log.info(" {} item(s) has been sent through Request Registration LayoutB {} ", items.size(), new Gson().toJson(items));

		return webClient.post().uri(path).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(items).retrieve().bodyToMono(RegistrationResponse.class).doOnError(e -> log.error(e.getMessage())).block();
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
						rs.getString("value_val"), rs.getBoolean("active")));
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


}

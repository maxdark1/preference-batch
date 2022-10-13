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

	@Value("${service.preference.baseurl}")
	public String baseUrl;
	private JdbcTemplate jdbcTemplate;
	private WebClient webClient;

	@Autowired
	public void setUpWebClient()
	{
		this.webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
				.baseUrl(baseUrl).build();
	}



	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setWebClient(WebClient webClient)
	{
		this.webClient = webClient;
	}


	public PreferenceItemList getPreferences(String id)
	{
		String path = baseUrl + "{id}/preferences";

		PreferenceItemList response = webClient.get().uri(uriBuilder -> uriBuilder.path(path).build(id)).accept(MediaType.APPLICATION_JSON)
				.retrieve().bodyToMono(PreferenceItemList.class).doOnError(e-> log.error(e.getMessage())).block();

		log.info("Response {} ", response);
		return response;

	}

	/**
	 * Send request to service for subscribe/unsubscribe from marketing programs
	 *
	 * @param List<? extends RegistrationRequest> items
	 *
	 */
	public RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items)
	{

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

		log.info(" {} item(s) has been sent throw Request Registration {} ", items.size(), new Gson().toJson(items));

		return webClient.post().uri(path).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).bodyValue(items)
				.retrieve().bodyToMono(RegistrationResponse.class).doOnError(e-> log.error(e.getMessage())).block();
	}

	/**
	 * Send request to service for SFMC unsubscribe
	 *
	 * @param List<? extends RegistrationRequest> items
	 *
	 */

	@Override
	public RegistrationResponse preferencesSFMCEmailOptOutsLayoutB(List<? extends RegistrationRequest> items)
	{

		String path = PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_SFMC_EXTACT_TARGET_EMAIL;

		log.info(" {} item(s) has been sent throw Request Registration LayoutB {} ", items.size(), new Gson().toJson(items));

		return webClient.post().uri(path).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(items).retrieve().bodyToMono(RegistrationResponse.class).doOnError(e-> log.error(e.getMessage())).block();
	}


	/**
	 * Save Job Information on persistence
	 *
	 * @param job_name,
	 *           status, start_time, inserted_by, inserted_date
	 */

	@Override
	public int insert(String job_name, String status,BigDecimal status_id, Date start_time, String inserted_by, Date inserted_date)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, job_name, status, status_id, start_time, inserted_by,
				inserted_date);
	}

	/**
	 * Gets Master's table information from persistence
	 *
	 * @param No params
	 *           The List resulting will be on MasterProcessor list as static
	 */
	@Override
	public List<Master> getMasterInfo()
	{
		return jdbcTemplate.query(SqlQueriesConstants.SQL_SELECT_MASTER_ID, (rs, rowNum) -> new Master(rs.getBigDecimal("master_id"), rs.getBigDecimal("key_id"),rs.getString("key_value"), rs.getString("value_val"),
						rs.getBoolean("active")));
	}

	/**
	 * Update Job status on persistence
	 *
	 * @param job, status
	 *
	 */
	@Override
	public int updateJob(Job job, String status)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STAUTS_JOB, job.getStatus_id(), job.getUpdated_date(), job.getUpdated_by(),job.getStatus(), job.getEnd_time(),
				job.getStart_time(), job.getJob_name(), status);
	}


}

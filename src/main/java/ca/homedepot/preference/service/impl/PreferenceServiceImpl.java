package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.repositories.entities.JobEntity;

import java.net.URI;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.repositories.JobRepo;
import ca.homedepot.preference.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * The type Preference service.
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	private JdbcTemplate jdbcTemplate;

	/**
	 * The notification subscription repository.
	 */
	private final JobRepo jobRepo;

	private WebClient webClient;

	@Value("${service.preference.baseurl}")
	public String baseUrl;

	@Autowired
	public PreferenceServiceImpl(JobRepo jobRepo)
	{
		this.jobRepo = jobRepo;
		this.webClient = WebClient.builder().baseUrl(baseUrl).build();


	}
	@Autowired
	public  void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setWebClient(WebClient webClient)
	{
		this.webClient = webClient;
	}


	public PreferenceItemList getPreferences(String id)
	{
		String path = baseUrl + "{id}/preferences";

		PreferenceItemList response = webClient.get().uri(uriBuilder -> {
			URI uri = uriBuilder.path(path).build(id);
			return uri;
		}).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(PreferenceItemList.class).block();

		log.info("Response {} ", response);
		return response;

	}

	public RegistrationResponse preferencesRegistration(List<? extends RegistrationRequest> items)
	{

		String path = baseUrl + PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

		return webClient.post().uri(uriBuilder -> {
					URI uri = uriBuilder.path(path).build();
					return uri;
				})
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(items), new ParameterizedTypeReference<List<RegistrationRequest>>() {
				})
				.retrieve().bodyToMono(RegistrationResponse.class).block();
	}


	/**
	 * Save Job Information on persistence
	 *
	 * @param job_name, status, start_time, inserted_by, inserted_date
	 *           the job notification entity
	 */

	@Override
	public int insert(String job_name, String status, Date start_time, String inserted_by, Date inserted_date) {
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, job_name, status, start_time, inserted_by, inserted_date);
	}

	@Override
	public List<Master> getMasterInfo() {
		return jdbcTemplate.query(SqlQueriesConstants.SQL_SELECT_MASTER_ID,
				(rs,rowNum)-> new Master(rs.getBigDecimal("master_id"),
						rs.getString("key_val"),
						rs.getString("value_val"),
						rs.getString("active"),
						rs.getString("inserted_by"),
						rs.getDate("inserted_date"),
						rs.getString("updated_by"),
						rs.getDate("updated_date")
				));
	}

	@Override
	public int updateJob(Job job) {
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STAUTS_JOB, job.getStatus(), job.getUpdated_date(),job.getStart_time(),  job.getJob_name());
	}


}

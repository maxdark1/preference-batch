package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.dto.PreferenceItemList;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.repositories.entities.JobEntity;

import java.net.URI;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.repositories.JobRepo;
import ca.homedepot.preference.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;


/**
 * The type Preference service.
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	private JdbcTemplate jdbcTemplate;


	@Autowired
	public  void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

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

	public Response preferencesRegistration(RegistrationRequest registration)
	{

		String path = baseUrl + PreferenceBatchConstants.PREFERENCE_CENTER_REGISTRATION_URL;

		return webClient.post().uri(uriBuilder -> {
			URI uri = uriBuilder.path(path).build();
			return uri;
		}).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(registration, RegistrationRequest.class)
				.retrieve().bodyToMono(Response.class).block();
	}

	/**
	 * Save Job Information on persistence
	 *
	 * @param job
	 *           the job notification entity
	 */

	public void saveJob(Job job){
		JobEntity jobEntity = new JobEntity();
		jobEntity.setJobName(job.getJob_name());
		jobEntity.setStatus(job.getStatus());
		jobEntity.setStartTime(job.getStart_time());
		jobEntity.setInsertedBy(job.getInserted_by());
		jobEntity.setInserted_date(job.getInserted_date());
		jobRepo.insert(job.getJob_name(), job.getStatus(), job.getStart_time(), job.getInserted_by(), job.getInserted_date());
	}

	@Override
	public int insert(String job_name, Boolean status, Date start_time, String inserted_by, Date inserted_date) {
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_JOB, job_name, status, start_time, inserted_by, inserted_date);
	}
}

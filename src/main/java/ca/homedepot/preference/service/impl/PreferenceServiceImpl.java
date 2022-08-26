package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.PreferenceItemList;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.repositories.entities.JobEntity;

import java.net.URI;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.repositories.JobRepo;
import ca.homedepot.preference.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * The type Preference service.
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl implements PreferenceService
{

	/**
	 * The notification subscription repository.
	 */
	private final JobRepo jobRepo;

	private WebClient webClient;

	@Value("${service.preference.baseurl}")
	public String baseUrl;

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
	 * Purge old records from notification subscription.
	 *
	 * @param notificationSubscriptionEntities
	 *           the notification subscription entities
	 */
	@Override
	public void purgeOldRecords(List<JobEntity> notificationSubscriptionEntities)
	{
		jobRepo.deleteAll(notificationSubscriptionEntities);
	}

	/**
	 * Gets all notifications created before given date.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the all notifications created before given date
	 */
	@Override
	public List<JobEntity> getAllNotificationsCreatedBefore(Date createdDate)
	{
		return jobRepo.findAllByStartTimeLessThan(createdDate);
	}

	/**
	 * Purge old records integer.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromRegistration(Date createdDate)
	{

		return 1;// registrationRepository.deleteAllByCreatedOnLessThan(createdDate);
	}

	/**
	 * Purge old records integer.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromEmail(Date createdDate)
	{

		return 1;//emailRepository.deleteAllByCreatedOnLessThan(createdDate);
	}

	/**
	 * Purge old records from inventory status.
	 *
	 * @param createdDate
	 *           the created date
	 * @return the integer
	 */

	@Override
	public Integer purgeOldRecordsfromInventory(Date createdDate)
	{
		return 1;//inventoryStatusMessagesRepository.deleteAllByCreatedTsLessThan(createdDate);
	}

}

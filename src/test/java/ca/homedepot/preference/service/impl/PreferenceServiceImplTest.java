package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.config.feign.PreferenceRegistrationClient;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.repositories.entities.impl.PreferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

class PreferenceServiceImplTest
{

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestBodyUriSpec requestBodyUriSpec;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.UriSpec uriSpec;

	@Mock
	private WebClient.RequestBodySpec requestBodySpec;
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private List<RegistrationRequest> itemsRequest;

	@Mock
	private PreferenceRegistrationClient preferenceRegistrationClient;

	@InjectMocks
	private PreferenceServiceImpl preferenceServiceImpl;

	private List<RegistrationRequest> items;




	@BeforeEach
	public void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testIsServiceAvailable()
	{
		Response response = Mockito.mock(Response.class);
		Mockito.when(preferenceRegistrationClient.getServiceStatus()).thenReturn(response);
		preferenceRegistrationClient.getServiceStatus();

		Response responseActual = preferenceServiceImpl.isServiceAvailable();
		assertEquals(response, responseActual);

	}

	@Test
	void testPreferencesRegistration()
	{

		String id = "13";
		String path = "test/" + "{id}/preferences";
		RegistrationResponse registrationResponse = Mockito.mock(RegistrationResponse.class);
		registrationResponse.setRegistration(List.of(new Response("12345", "status", "details")));
		itemsRequest = new ArrayList<>();
		itemsRequest.add(new RegistrationRequest());

		Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.bodyValue(itemsRequest)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(RegistrationResponse.class)).thenReturn(Mono.just(registrationResponse));
		Mockito.when(preferenceRegistrationClient.registration(any(List.class))).thenReturn(registrationResponse);

		RegistrationResponse registrationResponseCurrent = preferenceServiceImpl.preferencesRegistration(itemsRequest);
		assertEquals(registrationResponse, registrationResponseCurrent);
	}

	@Test
	void preferencesSFMCEmailOptOutsLayoutB()
	{

		String id = "13";
		String path = "test/" + "{id}/preferences";
		RegistrationResponse registrationResponse = Mockito.mock(RegistrationResponse.class);
		registrationResponse.setRegistration(List.of(new Response("12345", "status", "details")));
		itemsRequest = new ArrayList<>();
		itemsRequest.add(new RegistrationRequest());

		Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.bodyValue(itemsRequest)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(RegistrationResponse.class)).thenReturn(Mono.just(registrationResponse));

		Mockito.when(preferenceRegistrationClient.registrationLayoutB(any(List.class))).thenReturn(registrationResponse);
		RegistrationResponse registrationResponseCurrent = preferenceServiceImpl.preferencesSFMCEmailOptOutsLayoutB(itemsRequest);
		assertEquals(registrationResponse, registrationResponseCurrent);
	}

	@Test
	void insert()
	{
		String job_name = "testJob", inserted_by = "BATCH";
		BigDecimal status_id = BigDecimal.ONE;
		Date start_time = new Date(), inserted_date = new Date();
		int value = 1;
		Mockito.when(
				jdbcTemplate.update(anyString(), eq(job_name), eq(status_id), eq(start_time), eq(inserted_by), eq(inserted_date)))
				.thenReturn(value);

		int resultValue = preferenceServiceImpl.insert(job_name, status_id, start_time, inserted_by, inserted_date);
		assertEquals(value, resultValue);
	}


	@Test
	void getMasterInfo()
	{
		List<Master> listMaster = new ArrayList<>();

		Master master = new Master();
		listMaster.add(master);
		RowMapper rowMapper = (rs, rowNum) -> new Master(rs.getBigDecimal("master_id"), rs.getBigDecimal("key_id"),
				rs.getString("key_value"), rs.getString("value_val"), rs.getBoolean("active"), rs.getBigDecimal("old_id"));
		Mockito.when(jdbcTemplate.query(anyString(), eq(rowMapper))).thenReturn(listMaster);
		Mockito.when(preferenceServiceImpl.getMasterInfo()).thenReturn(listMaster);

		List<Master> currentListMaster = preferenceServiceImpl.getMasterInfo();

		assertEquals(1, currentListMaster.size());
	}


	@Test
	void updateJob()
	{
		Job job = new Job();
		BigDecimal status = BigDecimal.ONE;
		int rowsAffected = 1;

		Mockito.when(jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STATUS_JOB, job.getStatusId(), job.getUpdatedDate(),
				job.getUpdatedBy(), job.getEndTime(), job.getStartTime(), job.getJobName(), status)).thenReturn(rowsAffected);

		int currentRowsAffected = preferenceServiceImpl.updateJob(job, status);
		assertEquals(rowsAffected, currentRowsAffected);
	}


}
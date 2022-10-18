package ca.homedepot.preference.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import ca.homedepot.preference.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

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

	@InjectMocks
	private PreferenceServiceImpl preferenceServiceImpl;

	private List<RegistrationRequest> items;




	@BeforeEach
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//		preferenceServiceImpl = new PreferenceServiceImpl();
		preferenceServiceImpl.baseUrl = "test/";
		// Mocking webClient obj
		//		webClient = Mockito.mock(WebClient.class);
		//		requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
		//		requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
		//		requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
		//		responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
		//
		//		jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		//
		//		preferenceServiceImpl.setJdbcTemplate(jdbcTemplate);
		//		preferenceServiceImpl.setWebClient(webClient);
		//
		//
		//		items = new ArrayList<>();
		//		RegistrationRequest registration = new RegistrationRequest();
		//		items.add(registration);

	}


	@Test
	void getPreferencesTests()
	{

		String id = "13";
		String path = "test/" + "{id}/preferences";
		PreferenceItemList preferenceItemList = new PreferenceItemList();

		Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
		Mockito.when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(PreferenceItemList.class)).thenReturn(Mono.just(preferenceItemList));


		//		PreferenceItemList preferenceItemListCurrent = preferenceServiceImpl.getPreferences(id);
		//		assertEquals(preferenceItemList, preferenceItemListCurrent);
	}

	@Test
	void preferencesRegistrationtest()
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


		RegistrationResponse registrationResponseCurrent = preferenceServiceImpl.preferencesSFMCEmailOptOutsLayoutB(itemsRequest);
		assertEquals(registrationResponse, registrationResponseCurrent);
	}

	@Test
	void insert()
	{
		String job_name = "testJob", inserted_by = "BATCH";
		String status = "1";
		BigDecimal status_id = BigDecimal.ONE;
		Date start_time = new Date(), inserted_date = new Date();
		int value = 1;
		Mockito.when(jdbcTemplate.update(anyString(), eq(job_name), eq(status), eq(status_id), eq(start_time), eq(inserted_by),
				eq(inserted_date))).thenReturn(value);

		int resultValue = preferenceServiceImpl.insert(job_name, status, status_id, start_time, inserted_by, inserted_date);
		assertEquals(value, resultValue);
	}


	@Test
	void getMasterInfo()
	{
		List<Master> listMaster = new ArrayList<>();

		Master master = new Master();
		listMaster.add(master);
		RowMapper rowMapper = (rs, rowNum) -> new Master(rs.getBigDecimal("master_id"), rs.getBigDecimal("key_id"),
				rs.getString("key_value"), rs.getString("value_val"), rs.getBoolean("active"));
		Mockito.when(jdbcTemplate.query(anyString(), eq(rowMapper))).thenReturn(listMaster);
		Mockito.when(preferenceServiceImpl.getMasterInfo()).thenReturn(listMaster);

		List<Master> currentListMaster = preferenceServiceImpl.getMasterInfo();

		assertEquals(1, currentListMaster.size());
	}


	@Test
	void updateJob()
	{
		Job job = new Job();
		String status = "C";
		int rowsAffected = 1;

		Mockito
				.when(jdbcTemplate.update(anyString(), eq(job.getStatus_id()), eq(job.getUpdated_date()), eq(job.getUpdated_by()),
						eq(job.getStatus()), eq(job.getEnd_time()), eq(job.getStart_time()), eq(job.getJob_name()), eq(status)))
				.thenReturn(rowsAffected);

		int currentRowsAffected = preferenceServiceImpl.updateJob(job, status);
		assertEquals(rowsAffected, currentRowsAffected);
	}

	@Test
	void setupWebClient()
	{
		preferenceServiceImpl.setUpWebClient();
	}
}
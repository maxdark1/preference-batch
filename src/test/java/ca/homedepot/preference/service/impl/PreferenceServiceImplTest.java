package ca.homedepot.preference.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.reactive.function.client.WebClient;

import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.RegistrationRequest;

class PreferenceServiceImplTest
{

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestBodyUriSpec requestBodyUriSpec;

	@Mock
	private WebClient.RequestBodySpec requestBodySpec;
	@Mock
	private WebClient.RequestHeadersSpec requestHeadersSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private PreferenceServiceImpl preferenceServiceImpl;

	private List<RegistrationRequest> items;


	@BeforeEach
	public void setUp()
	{
		preferenceServiceImpl = new PreferenceServiceImpl();
		preferenceServiceImpl.baseUrl = "test/";
		// Mocking webClient obj
		webClient = Mockito.mock(WebClient.class);
		requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
		requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
		requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
		responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

		jdbcTemplate = Mockito.mock(JdbcTemplate.class);

		preferenceServiceImpl.setJdbcTemplate(jdbcTemplate);
		preferenceServiceImpl.setWebClient(webClient);


		items = new ArrayList<>();
		RegistrationRequest registration = new RegistrationRequest();
		items.add(registration);

	}

	// TODO Make corrections on WebClient UnitTesting

	@Test
	void preferencesRegistration()
	{
		//		RegistrationResponse registration = new RegistrationResponse(List.of(new Response(1, "Published", "Done")));
		//		Mono<RegistrationResponse> response = Mono.just(registration);
		//
		//
		//		Mockito.lenient().when(webClient.post()).thenReturn(requestBodyUriSpec);
		//		Mockito.lenient().when(requestBodyUriSpec.uri(any(Function.class))).thenReturn(requestBodySpec);
		//		Mockito.lenient().when(requestBodySpec.accept(eq(MediaType.APPLICATION_JSON))).thenReturn(requestBodySpec);
		//		Mockito.lenient().when(requestBodySpec.contentType(eq(MediaType.APPLICATION_JSON))).thenReturn(requestBodySpec);
		//
		//		Mockito.lenient().when( requestBodySpec.body( any(Mono.class), eq(items.getClass()))).thenReturn(requestHeadersSpec);
		//		Mockito.lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		//		Mockito.lenient().when(responseSpec.bodyToMono(eq(RegistrationResponse.class))).thenReturn(response);
		//
		//		RegistrationResponse resultResponse = preferenceServiceImpl.preferencesRegistration(items);
		//		assertEquals(resultResponse.getRegistration().get(0).getDetails(), registration.getRegistration().get(0).getDetails());

	}

	@Test
	void insert()
	{
		String job_name = "testJob", inserted_by = "BATCH";
		String status = "1";
		BigDecimal status_id = BigDecimal.ONE;
		Date start_time = new Date(), inserted_date = new Date();
		int value = 1;
		Mockito.when(jdbcTemplate.update(anyString(), eq(job_name), eq(status), eq(status_id),eq(start_time), eq(inserted_by), eq(inserted_date)))
				.thenReturn(value);

		int resultValue = preferenceServiceImpl.insert(job_name, status, status_id, start_time, inserted_by, inserted_date);
		assertEquals(value, resultValue);
	}


	@Test
	void getMasterInfo()
	{
		List<Master> listMaster = new ArrayList<>();

		Master master = new Master();
		listMaster.add(master);

		Mockito.when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(listMaster);

		List<Master> currentListMaster = preferenceServiceImpl.getMasterInfo();

		assertEquals(1, currentListMaster.size());
	}


	@Test
	void updateJob()
	{
		Job job = new Job();
		String status = "C";
		int rowsAffected = 1;

		Mockito.when(jdbcTemplate.update(anyString(), eq(job.getStatus_id()), eq(job.getUpdated_date()), eq(job.getUpdated_by()), eq(job.getStatus()), eq(job.getEnd_time()),
				eq(job.getStart_time()), eq(job.getJob_name()), eq(status))).thenReturn(rowsAffected);

		int currentRowsAffected = preferenceServiceImpl.updateJob(job, status);
		assertEquals(rowsAffected, currentRowsAffected);
	}
}
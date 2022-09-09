package ca.homedepot.preference.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import reactor.core.publisher.Mono;

class PreferenceServiceImplTest
{

	@Mock
	private PreferenceServiceImpl preferenceServiceImpl;

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

	private List<RegistrationRequest> registrationRequests;


	@BeforeEach
	public void setUp()
	{
		preferenceServiceImpl = Mockito.mock(PreferenceServiceImpl.class);
		// Mocking webClient obj
		webClient = Mockito.mock(WebClient.class);
		requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
		requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
		requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
		responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

		jdbcTemplate = Mockito.mock(JdbcTemplate.class);

		preferenceServiceImpl.setJdbcTemplate(jdbcTemplate);
		preferenceServiceImpl.setWebClient(webClient);


		registrationRequests = new ArrayList<>();
		RegistrationRequest registration = new RegistrationRequest();
		registrationRequests.add(registration);

	}

	@Test
	void preferencesRegistration()
	{
		RegistrationResponse registration = new RegistrationResponse(List.of(new Response(1, "Published", "Done")));
		Mono<RegistrationResponse> response = Mono.just(registration);

		Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
		Mockito.when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
		Mockito.when(
				requestBodySpec.body(Mono.just(registrationRequests), new ParameterizedTypeReference<List<RegistrationRequest>>()
				{})).thenReturn(requestHeadersSpec);
		Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		Mockito.when(responseSpec.bodyToMono(RegistrationResponse.class)).thenReturn(response);

		Mockito.when(preferenceServiceImpl.preferencesRegistration(registrationRequests)).thenReturn(registration);

		RegistrationResponse resultResponse = preferenceServiceImpl.preferencesRegistration(registrationRequests);
		assertEquals(resultResponse.getRegistration().get(0).getDetails(), registration.getRegistration().get(0).getDetails());

	}

	@Test
	void insert()
	{
		String job_name = "testJob", inserted_by = "BATCH";
		String status = "1";
		Date start_time = new Date(), inserted_date = new Date();
		int value = 1;
		Mockito.when(jdbcTemplate.update(anyString(), eq(job_name), eq(status), eq(start_time), eq(inserted_by), eq(inserted_date)))
				.thenReturn(value);
		Mockito.when(preferenceServiceImpl.insert(job_name, status, start_time, inserted_by, inserted_date)).thenReturn(value);


		int resultValue = preferenceServiceImpl.insert(job_name, status, start_time, inserted_by, inserted_date);
		assertEquals(value, resultValue);
	}
}
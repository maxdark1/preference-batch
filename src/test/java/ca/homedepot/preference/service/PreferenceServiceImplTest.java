package ca.homedepot.preference.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//import ca.homedepot.preference.dto.PreferenceItem;
//import ca.homedepot.preference.dto.PreferenceItemList;
//import ca.homedepot.preference.repositories.JobRepository;
//import ca.homedepot.preference.repositories.entities.JobEntity;
//import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.mockito.*;
//import org.mockito.junit.MockitoJUnit;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;

//@RunWith(SpringJUnit4ClassRunner.class)
//public class PreferenceServiceImplTest
//{

	/**
	 * The BackinStockRepository repository.
	 */
//	@Mock
//	JobRepository jobRepository;

	/**
	 * The Preference service.
	 */
	//@InjectMocks
//	@MockBean
//	PreferenceServiceImpl preferenceServiceImpl;
//
//    @Mock
//    WebClient webClient;
//
//    @Mock
//    WebClient.RequestHeadersUriSpec uriSpecMock;
//
//    @Mock
//    WebClient.ResponseSpec responseSpec;
//
//	@Mock
//	WebClient.RequestHeadersSpec requestHeadersSpec;
//
//	@Mock
//	Mono<List<PreferenceItem>> monoResponse;
//
//	/**
//	 * The NotificationSubscription entity.
//	 */
//	JobEntity backinStockEntity;
//
//	/**
//	 * setUp Method.
//	 */
//	@Before
//	public void setUp()
//	{
//		MockitoAnnotations.initMocks(this.getClass());
//		backinStockEntity = new JobEntity();
//		/*
//		 * backinStockEntity.setEmailId("test@gmail.com"); backinStockEntity.setRegId("123");
//		 * backinStockEntity.setLangcode("en"); backinStockEntity.setNotificationType("SUB");
//		 * backinStockEntity.setPhoneNo("9878987678"); backinStockEntity.setArticleId("654");
//		 * backinStockEntity.setSubscriptionType("SUB");
//		 */
//	}
//
//    @BeforeEach
//    public void beforeEach(){
//        this.webClient = Mockito.mock(WebClient.class);
//        this.uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
//        this.responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
//		this.requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
//		this.monoResponse = Mockito.mock( Mono.class );
//		jobRepository = Mockito.mock(JobRepository.class);
//		// Injecting mocks to service
//
//		preferenceServiceImpl = new PreferenceServiceImpl(jobRepository);
//		preferenceServiceImpl.setWebClient(this.webClient);
//    }


//    @Test
//    public void testGetPreferences(){
//		String email = "test@gmail.com";
//		// {"items":[{"id":"1234","type":"email","value":"Y"}]}
//		List<PreferenceItem> listResponse = List.of(new PreferenceItem("1234", "email", "Y"));
//		preferenceServiceImpl = new PreferenceServiceImpl(jobRepository);
//		preferenceServiceImpl.setWebClient(webClient);
//
//		WebClient.ResponseSpec headersSpec_btm_Mock = mock(WebClient.ResponseSpec.class);
//		WebClient.RequestHeadersSpec<?> headersSpec_retrieve_Mock = mock(WebClient.RequestHeadersSpec.class);
//		WebClient.RequestHeadersSpec<?> headersSpec_accept_Mock = mock(WebClient.RequestHeadersSpec.class);
//
//		when(headersSpec_btm_Mock.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(null);
//		Mockito.when(headersSpec_retrieve_Mock.retrieve()).thenReturn(headersSpec_btm_Mock);
//		Mockito.when(headersSpec_accept_Mock.accept(any())).thenReturn(headersSpec_retrieve_Mock);
//		Mockito.when(uriSpecMock.uri(anyString(), any())).thenReturn(headersSpec_accept_Mock);
//        Mockito.when(webClient.get()).thenReturn(uriSpecMock);
//        Mockito.lenient().when(uriSpecMock.uri(Mockito.any(Function.class)))
//                .thenReturn(requestHeadersSpec);
//		Mockito.lenient().when(requestHeadersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersSpec);
//		Mockito.lenient().when(requestHeadersSpec.header("x-thd-client-system", "IJS"))
//				.thenReturn(requestHeadersSpec);
//		Mockito.lenient().when(requestHeadersSpec.attributes(Mockito.any(Consumer.class))).thenReturn(requestHeadersSpec);
//
//        Mockito.lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//
//		lenient().when(responseSpec.bodyToMono(new ParameterizedTypeReference<List<PreferenceItem>>() {
//				}))
//				.thenReturn(Mono.just(listResponse));
//		List<PreferenceItem> responsePreferenceItem = preferenceServiceImpl.getPreferences(email);
//		Assertions.assertEquals("1234", responsePreferenceItem.get(0).getId());
//    }

//	/**
//	 * Test purge old records.
//	 */
//	@Test
//	public void testPurgeOldRecords()
//	{
//		when(jobRepository.deleteAllByCreatedOnLessThan(ArgumentMatchers.<Date> any())).thenReturn(4);
//		preferenceServiceImpl.purgeOldRecords(Arrays.asList(backinStockEntity));
//		verify(jobRepository).deleteAll(anyList());
//	}
//
//	/**
//	 * Test purge old records with no records.
//	 */
//	@Test
//	public void testPurgeOldRecords_With_No_records()
//	{
//		when(jobRepository.deleteAllByCreatedOnLessThan(any(Date.class))).thenReturn(0);
//		preferenceServiceImpl.purgeOldRecords(Arrays.asList(backinStockEntity));
//		verify(jobRepository).deleteAll(anyList());
//	}
//
//	@Test
//	public void testGetAllNotificationsCreatedBefore()
//	{
//		when(jobRepository.findAllByCreatedOnLessThan(any(Date.class))).thenReturn(Arrays.asList(backinStockEntity));
//		assertEquals(Arrays.asList(backinStockEntity), preferenceServiceImpl.getAllNotificationsCreatedBefore(new Date()));
//	}

//}

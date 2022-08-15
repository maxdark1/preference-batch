//package ca.homedepot.preference.dto;
//
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.atLeastOnce;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import ca.homedepot.preference.exception.PreferenceCenterCustomException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.cloud.pubsub.v1.Publisher;
//import com.google.pubsub.v1.ProjectTopicName;
//import com.google.pubsub.v1.PubsubMessage;
//import java.io.IOException;
//import java.util.Arrays;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.springframework.cloud.gcp.pubsub.core.PubSubException;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//
///**
// * The type Email message publisher test.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//public class EmailMessagePublisherTest
//{
//	/**
//	 * The Email message publisher.
//	 */
//	EmailMessagePublisher emailMessagePublisher;
//
//	/**
//	 * The Email parameter ws dto.
//	 */
//	EmailParametersDTO emailParametersDTO;
//
//	/**
//	 * The Publisher.
//	 */
//	@Mock
//	Publisher publisher;
//
//	/**
//	 * The Object mapper.
//	 */
//	@Mock
//	ObjectMapper objectMapper;
//
//	/**
//	 * The Email ws dto.
//	 */
//	EmailDTO emailDTO;
//
//	/**
//	 * The constant TOPIC.
//	 */
//	public static final String TOPIC = "topic";
//	/**
//	 * The constant TOPIC1.
//	 */
//	public static final String TOPIC1 = "test-topic";
//	/**
//	 * The constant MAPPER.
//	 */
//	public static final String MAPPER = "mapper";
//	/**
//	 * The constant PROJECT.
//	 */
//	public static final String PROJECT = "test Project";
//	/**
//	 * The constant TOPIC2.
//	 */
//	public static final String TOPIC2 = "test topic";
//	/**
//	 * The constant MSG.
//	 */
//	public static final String MSG = "test exception";
//
//	/**
//	 * Sets up.
//	 *
//	 * @throws IOException
//	 *            the io exception
//	 */
//	@Before
//	public void setUp() throws IOException
//	{
//		emailMessagePublisher = spy(EmailMessagePublisher.class);
//		emailParametersDTO = new EmailParametersDTO();
//		emailDTO = new EmailDTO();
//		emailDTO.setParameters(emailParametersDTO);
//		EmailAddressDTO emailAddressDTO = new EmailAddressDTO();
//		emailAddressDTO.setEmail("naruto_uzumaki9@leaf.com");
//		emailDTO.setTo(Arrays.asList(emailAddressDTO));
//		ReflectionTestUtils.setField(emailMessagePublisher, TOPIC, TOPIC1);
//		ReflectionTestUtils.setField(emailMessagePublisher, MAPPER, new ObjectMapper());
//	}
//
//	/**
//	 * Test publish email message to topic.
//	 *
//	 * @throws IOException
//	 *            the io exception
//	 */
//	//@Test(expected = PreferenceCenterCustomException.class)
//	public void testPublishEmailMessageToTopic() throws IOException
//	{
//		/*
//		 * doReturn(ProjectTopicName.of(PROJECT, TOPIC2)).when(emailMessagePublisher).getProjectTopicName();
//		 * doReturn(publisher).when(emailMessagePublisher).getPublisher(any(ProjectTopicName.class));
//		 * emailMessagePublisher.publishEmailMessageToTopic(emailDTO); verify(publisher,
//		 * atLeastOnce()).publish(any(PubsubMessage.class));
//		 */
//	}
//
//	/**
//	 * Test publish email message to topic for exception.
//	 *
//	 * @throws IOException
//	 *            the io exception
//	 */
//	//@Test(expected = PreferenceCenterCustomException.class)
//	public void testPublishEmailMessageToTopic_for_exception() throws IOException
//	{
//		/*
//		 * doReturn(ProjectTopicName.of(PROJECT, TOPIC2)).when(emailMessagePublisher).getProjectTopicName();
//		 * doReturn(publisher).when(emailMessagePublisher).getPublisher(any(ProjectTopicName.class));
//		 * when(publisher.publish(any(PubsubMessage.class))).thenThrow(new PubSubException(MSG));
//		 * emailMessagePublisher.publishEmailMessageToTopic(emailDTO); verify(publisher,
//		 * atLeastOnce()).publish(any(PubsubMessage.class));
//		 */
//	}
//
//	/**
//	 * Test bean get project topic name.
//	 *
//	 * @throws IOException
//	 *            the io exception
//	 */
//	//@Test
//	public void testBeanGetProjectTopicName() throws IOException
//	{
//		/*
//		 * ProjectTopicName projectTopicName = emailMessagePublisher.getProjectTopicName();
//		 * Assert.assertNotNull(projectTopicName); Assert.assertEquals(projectTopicName.getProject(), "test-project");
//		 */
//	}
//
//	/**
//	 * Test publish email message to topic for exception in private.
//	 *
//	 * @throws IOException
//	 *            the io exception
//	 */
//	//@Test(expected = PreferenceCenterCustomException.class)
//	public void testPublishEmailMessageToTopic_for_exception_in_private() throws IOException
//	{
//		/*
//		 * ReflectionTestUtils.setField(emailMessagePublisher, MAPPER, objectMapper); doReturn(ProjectTopicName.of(PROJECT,
//		 * TOPIC2)).when(emailMessagePublisher).getProjectTopicName();
//		 * doReturn(publisher).when(emailMessagePublisher).getPublisher(any(ProjectTopicName.class));
//		 * when(objectMapper.writeValueAsString(emailDTO)).thenThrow(JsonProcessingException.class);
//		 * emailMessagePublisher.publishEmailMessageToTopic(emailDTO); verify(publisher,
//		 * never()).publish(any(PubsubMessage.class)); ReflectionTestUtils.setField(emailMessagePublisher, MAPPER, new
//		 * ObjectMapper());
//		 */
//	}
//}

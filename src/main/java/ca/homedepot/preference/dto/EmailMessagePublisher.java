//package ca.homedepot.preference.dto;
//
//import ca.homedepot.preference.exception.PreferenceCenterCustomException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.cloud.ServiceOptions;
//import com.google.cloud.pubsub.v1.Publisher;
//import com.google.protobuf.ByteString;
//import com.google.pubsub.v1.ProjectTopicName;
//import com.google.pubsub.v1.PubsubMessage;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import javax.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//
//
///**
// * Publish the email messages to the communication pub sub in GCP
// */
//@Component
//@Slf4j
//public class EmailMessagePublisher
//{
//	String MESSAGE_ATTRIBUTE_KEY = "eventType";
//	String MESSAGE_ATTRIBUTE_VALUE = "CC_HTML_EMAIL";
//
//
//	/**
//	 * The Topic.
//	 */
//	@Value("${email.pubsub-topic}")
//	private String topic;
//
//	/**
//	 * The Mapper.
//	 */
//	@Autowired
//	ObjectMapper mapper;
//
//
//	/**
//	 * Publish the list of email Messages to the PubSub topic.
//	 *
//	 * @param emailDTO
//	 */
//	public void publishEmailMessageToTopic(EmailDTO emailDTO)
//	{
//		/*
//		 * Publisher publisher = null; try { publisher = getPublisher(getProjectTopicName()); ByteString data =
//		 * encodePayload(getEmailMessageToPublish(emailDTO)); PubsubMessage emailMessage =
//		 * PubsubMessage.newBuilder().setData(data) .putAttributes(MESSAGE_ATTRIBUTE_KEY, MESSAGE_ATTRIBUTE_VALUE).build();
//		 * publisher.publish(emailMessage).get(); } catch (Exception e) {
//		 * log.error("Exception {} while publishing email message to PubSub for Removing Notification {}", e,
//		 * emailDTO.getCustomerId()); throw new PreferenceCenterCustomException(
//		 * "Exception while publishing email message to PubSub for Removing Notification " + emailDTO.getCustomerId()); }
//		 */
//
//	}
//
//	/**
//	 * Convert DTO to JSON string
//	 *
//	 * @param emailDTO
//	 * @return
//	 */
//	private String getEmailMessageToPublish(EmailDTO emailDTO)
//	{
//
//		String emailMessage = null;
//		try
//		{
//			return mapper.writeValueAsString(emailDTO);
//		}
//		catch (JsonProcessingException e)
//		{
//			log.error("Exception {} while converting emailDTO to JSON string for Removing Notification {} and email{}", e,
//					emailDTO.getCustomerId(), emailDTO.getTo().get(0).getEmail());
//		}
//		return emailMessage;
//	}
//
//	/**
//	 * Creates a Publisher Bean
//	 *
//	 * @param topicName
//	 * @return
//	 * @throws java.io.IOException
//	 */
//	/*
//	 * @Bean public Publisher getPublisher(ProjectTopicName topicName) throws IOException { return
//	 * Publisher.newBuilder(topicName).build(); }
//	 */
//
//
//	/**
//	 * Create a ProjectTopicName bean
//	 *
//	 * @return
//	 */
//	//	@PostConstruct
//	//	@Bean
//	//	public ProjectTopicName getProjectTopicName()
//	//	{
//	//		return ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), topic);
//	//	}
//
//
//	/**
//	 * Encode JSON string data to base64
//	 *
//	 * @param payload
//	 * @return
//	 */
//	private ByteString encodePayload(final String payload)
//	{
//		final String encodeData = Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
//		return ByteString.copyFromUtf8(encodeData);
//	}
//
//}

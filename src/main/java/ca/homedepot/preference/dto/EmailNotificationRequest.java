package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailNotificationRequest
{

	@JsonProperty("to")
	@Valid
	private List<EmailNotificationTo> to = null;

	@JsonProperty("emailSubject")
	private String emailSubject;

	@JsonProperty("fromEmail")
	private String fromEmail;

	@JsonProperty("eventId")
	private String eventId;

	@JsonProperty("eventName")
	private String eventName;

	@JsonProperty("sourceSystemId")
	private String sourceSystemId;

	@JsonProperty("localEventDefinitionId")
	private String localEventDefinitionId;

	@JsonProperty("langCd")
	private String langCd;

	@JsonProperty("MessageBody")
	private EmailNotificationMessageBody messageBody;

}

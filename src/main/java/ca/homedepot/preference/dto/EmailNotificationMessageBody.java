package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailNotificationMessageBody
{
	@JsonProperty("eventDate")
	private String eventDate;

	@JsonProperty("eventTime")
	private String eventTime;

	@JsonProperty("quantityInFile")
	private String quantityInFile;

	@JsonProperty("quantityLoaded")
	private String quantityLoaded;

	@JsonProperty("quantityFailed")
	private String quantityFailed;

	@JsonProperty("fileName")
	private String fileName;

	@JsonProperty("sourceName")
	private String sourceName;
}

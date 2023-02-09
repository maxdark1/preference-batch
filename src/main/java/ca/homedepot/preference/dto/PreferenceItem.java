package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class PreferenceItem
{
	/**
	 * The id
	 */
	@JsonProperty("id")
	private String id;
	/**
	 * The type
	 */
	@JsonProperty("type")
	private String type;

	/**
	 * The value
	 */
	@JsonProperty("value")
	private String value;
}

package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("registration")
@Generated
public class Response
{
	/**
	 * Sequence_nbr id
	 */
	private String id;
	/**
	 * The status
	 */
	private String status;
	/**
	 * The details
	 */
	private String details;

}

package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseValidation
{

	/**
	 * The status
	 */
	private String status;
	/**
	 * The details
	 */
	private String details;
}

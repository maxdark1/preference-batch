package ca.homedepot.preference.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse
{
	/**
	 * The list of Responses from service
	 */
	private List<Response> registration;
}

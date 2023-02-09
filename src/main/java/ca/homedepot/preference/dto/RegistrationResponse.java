package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

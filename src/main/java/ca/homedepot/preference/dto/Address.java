package ca.homedepot.preference.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Address
{
	/**
	 * The field src_address1 on request
	 */
	@JsonProperty("src_address1")
	private String srcAddress1;

	/**
	 * The field src_address2 on request
	 */
	@JsonProperty("src_address2")
	private String srcAddress2;

	/**
	 * The field src_city on request
	 */
	@JsonProperty("src_city")
	private String srcCity;

	/**
	 * The src_state on request
	 */
	@JsonProperty("src_state")
	private String srcState;

	/**
	 * The src_postal_code on request
	 */
	@JsonProperty("src_postal_code")
	private String srcPostalCode;
}

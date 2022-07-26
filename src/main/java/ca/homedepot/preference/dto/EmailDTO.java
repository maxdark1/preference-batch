package ca.homedepot.preference.dto;

import java.util.List;
import lombok.Data;


/**
 * EmailDTO
 */
@Data
public class EmailDTO
{
	private String requestId;

	private String templateId;

	private String customerId;

	private String langCd;

	private String retryFlag;

	private String scheduledOn;

	private List<EmailAddressDTO> to = null;

	private List<EmailAddressDTO> ccs = null;

	private List<EmailAddressDTO> bcs = null;

	private EmailParametersDTO parameters;

}


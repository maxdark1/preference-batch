package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationRequest
{
	/**
	 * The file id
	 */
	@JsonProperty("file_id")
	private BigDecimal fileId;

	/**
	 * The status
	 */
	@JsonProperty("status")
	private Boolean status;
	/**
	 * The sequence_nbr
	 */
	@JsonProperty("sequence_nbr")
	private String sequenceNbr;
	/**
	 * The source_id
	 */
	@JsonProperty("source_id")
	private Long sourceId;
	/**
	 * The language preference
	 */
	@JsonProperty("language_preference")
	private String languagePreference;
	/**
	 * The title name
	 */
	@JsonProperty("src_title_name")
	private String srcTitleName;
	/**
	 * The first name
	 */
	@JsonProperty("src_first_name")
	private String srcFirstName;
	/**
	 * The last name
	 */
	@JsonProperty("src_last_name")
	private String srcLastName;
	/**
	 * The email address
	 */
	@JsonProperty("src_email_address")
	private String srcEmailAddress;
	/**
	 * The email status
	 */
	@JsonProperty("email_status")
	private Integer emailStatus;
	/**
	 * The email address preference
	 */
	@JsonProperty("email_address_pref")
	private Integer emailAddressPref;
	/**
	 * The source date
	 */
	@JsonProperty("src_date")
	private String srcDate;
	/**
	 * The cell sms flag
	 */
	@JsonProperty("cell_sms_flag")
	private Integer cellSmsFlag;
	/**
	 * The phone number
	 */
	@JsonProperty("src_phone_number")
	private String srcPhoneNumber;
	/**
	 * The phone extension
	 */
	@JsonProperty("src_phone_extension")
	private String srcPhoneExtension;
	/**
	 * The phone pref
	 */
	@JsonProperty("phone_pref")
	private Integer phonePref;
	/**
	 * The fax number
	 */
	@JsonProperty("fax_number")
	private String faxNumber;
	/**
	 * The fax extension
	 */
	@JsonProperty("fax_extension")
	private String faxExtension;
	/**
	 * The address object
	 */
	@JsonProperty("src_address")
	@Valid
	private Address srcAddress;
	/**
	 * Mail address preference
	 */
	@JsonProperty("mail_address_pref")
	private Integer mailAddresspref;
	/**
	 * The email preference HD Canada
	 */
	@JsonProperty("email_pref_hd_ca")
	private Integer emailPrefHDCa;
	/**
	 * The email Preference for garden club
	 */
	@JsonProperty("email_pref_garden_club")
	private Integer emailPrefGardenClub;
	/**
	 * The email preference pro
	 */
	@JsonProperty("email_pref_pro")
	private Integer emailPrefPro;
	/**
	 * The email preference new mover
	 */
	@JsonProperty("email_pref_new_mover")
	private Integer emailPrefNewMover;
	/**
	 * The content_value pair
	 */
	@JsonProperty("content_value")
	private Map<String, String> contentValue;


}

package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationRequest
{
	@JsonProperty("file_id")
	private BigDecimal fileId;

	@JsonProperty("status")
	private Boolean status;

	@JsonProperty("sequence_nbr")
	private String sequenceNbr;

	@JsonProperty("source_id")
	private Long sourceId;

	@JsonProperty("language_preference")
	private String languagePreference;

	@JsonProperty("src_title_name")
	private String srcTitleName;

	@JsonProperty("src_first_name")
	private String srcFirstName;

	@JsonProperty("src_last_name")
	private String srcLastName;

	@JsonProperty("src_email_address")
	private String srcEmailAddress;

	@JsonProperty("email_status")
	private Integer emailStatus;

	@JsonProperty("email_address_pref")
	private Integer emailAddressPref;

	@JsonProperty("src_date")
	private String srcDate;

	@JsonProperty("cell_sms_flag")
	private Integer cellSmsFlag;

	@JsonProperty("src_phone_number")
	private String srcPhoneNumber;

	@JsonProperty("src_phone_extension")
	private String srcPhoneExtension;

	@JsonProperty("phone_pref")
	private Integer phonePref;

	@JsonProperty("fax_number")
	private String faxNumber;

	@JsonProperty("fax_extension")
	private String faxExtension;

	@JsonProperty("src_address")
	@Valid
	private Address srcAddress;

	@JsonProperty("mail_address_pref")
	private Integer mailAddresspref;

	@JsonProperty("email_pref_hd_ca")
	private Integer emailPrefHDCa;

	@JsonProperty("email_pref_garden_club")
	private Integer emailPrefGardenClub;

	@JsonProperty("email_pref_pro")
	private Integer emailPrefPro;

	@JsonProperty("email_pref_new_mover")
	private Integer emailPrefNewMover;

	@JsonProperty("content_value")
	private Map<String, String> contentValue;

	@JsonProperty("inserted_by")
	private String insertedBy;

	@JsonProperty("inserted_date")
	private String insertedDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("updated_date")
	private String updatedDate;

}

package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RegistrationRequest
{


	@JsonProperty("file_id")
	private long fileId;

	@JsonProperty("sequence_nbr")
	private long sequenceNbr;

	@JsonProperty("source_id")
	private int sourceId;

	@JsonProperty("phone_number")
	private String phoneNumber;

	@JsonProperty("src_first_name")
	private String firstName;

	@JsonProperty("src_middle_initial")
	private String middleInitial;

	@JsonProperty("src_last_name")
	private String lastName;

	@JsonProperty("src_address1")
	private String address1;

	@JsonProperty("src_address2")
	private String address2;

	@JsonProperty("src_city")
	private String city;

	@JsonProperty("src_state")
	private String state;

	@JsonProperty("src_zipcode")
	private String zipcode;

	@JsonProperty("src_zip4")
	private String zip4;

	@JsonProperty("src_postal_code")
	private String postalCode;

	@JsonProperty("src_system")
	private String srcSystem;

	@JsonProperty("credit_prin")
	private String creditPin;

	@JsonProperty("src_agent")
	private String agent;

	@JsonProperty("src_last_balance_amt")
	private String lastBalanceAmt;

	@JsonProperty("credit_acct_open_dt")
	private Date creditAcctOpenDt;

	@JsonProperty("src_last_trans_dt")
	private Date lastTransdt;

	@JsonProperty("credit_language_cd")
	private String creditLanguageCd;

	@JsonProperty("credit_store_origin")
	private String creditStoreOrigin;

	@JsonProperty("src_suppression_flag")
	private String suppresionFlag;

	@JsonProperty("src_email_address")
	private String emailAddress;

	@JsonProperty("src_title_name")
	private String titleName;

	@JsonProperty("phone_1_pref")
	private String phone1Pref;

	@JsonProperty("email_address_1_pref")
	private String emailAddress1Pref;

	@JsonProperty("mail_address_1_pref")
	private String mailAddress1Pref;

	@JsonProperty("src_date")
	private Date date;

	@JsonProperty("email_status")
	private int emailStatus;

	@JsonProperty("src_phone_extension")
	private String phoneExtension;

	@JsonProperty("email_pref_hd_ca")
	private String emailPrefHdCa;

	@JsonProperty("email_pref_garden_club")
	private String emailPrefGardenClub;

	private Content content;

	private Value value;
}

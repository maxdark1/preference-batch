package ca.homedepot.preference.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RegistrationRequest implements Serializable
{
	@JsonProperty("file_id")
	private String fileId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("sequence_nbr")
	private String sequenceNbr;

	@JsonProperty("source_id")
	private Long sourceId;

	@JsonProperty("src_phone_number")
	private String srcPhoneNumber;

	@JsonProperty("src_first_name")
	private String srcFirstName;

	@JsonProperty("src_last_name")
	private String srcLastName;

	@JsonProperty("src_address")
	@Valid
	private Address srcAddress;

	@JsonProperty("credit_language_cd")
	private String creditLanguageCd;

	@JsonProperty("src_email_address")
	private String srcEmailAddress;

	@JsonProperty("src_title_name")
	private String srcTitleName;

	@JsonProperty("phone_1_pref")
	private int phone1Pref;

	@JsonProperty("email_address_1_pref")
	private int emailAddress1Pref;

	@JsonProperty("mail_address_1_pref")
	private int mailAddress1pref;

	@JsonProperty("src_date")
	private String srcDate;


	@JsonProperty("credit_prin")
	private String creditPrin;

	@JsonProperty("src_agent")
	private String srcAgent;

	@JsonProperty("src_last_balance_amt")
	private String srcLastBalanceAmt;

	@JsonProperty("credit_acct_open_dt")
	private String creditAcctOpenDt;

	@JsonProperty("src_last_trans_dt")
	private String srcLastTransDt;

	@JsonProperty("src_phone_extension")
	private String srcPhoneExtension;



	@JsonProperty("credit_store_origin")
	private String creditStoreOrigin;

	@JsonProperty("src_suppresion_flag")
	private String srcSuppresionFlag;

	@JsonProperty("content_value")
	private Map<String, String> contentValue;

}

package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class RegistrationRequest
{
	@JsonProperty("file_id")
	private String fileId;

	@JsonProperty("status")
	private boolean status;

	@JsonProperty("sequence_nbr")
	private String sequenceNbr;

	@JsonProperty("source_id")
	private Long sourceId;

	@JsonProperty("src_phone_number")
	private String srcPhoneNumber;

	@JsonProperty("src_first_name")
	private String srcFirstName;

	@JsonProperty("src_middle_initial")
	private String srcMiddleInitial;

	@JsonProperty("src_last_name")
	private String srcLastName;

	@JsonProperty("src_address")
	private Address srcAddress;

	@JsonProperty("src_system")
	private String srcSystem;

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

	@JsonProperty("credit_language_cd")
	private String creditLanguageCd;

	@JsonProperty("credit_store_origin")
	private String creditStoreOrigin;

	@JsonProperty("src_suppresion_flag")
	private String srcSuppresionFlag;

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

	@JsonProperty("email_status")
	private int emailStatus;

	@JsonProperty("src_phone_extension")
	private String srcPhoneExtension;

	@JsonProperty("email_pref_hd_ca")
	private int emailPrefHdCa;

	@JsonProperty("email_pref_garden_club")
	private int emailPrefGardenClub;

	@JsonProperty("email_pref_pro")
	private int emailPrefPro;

	@JsonProperty("email_pref_new_mover")
	private int emailPrefNewMover;

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

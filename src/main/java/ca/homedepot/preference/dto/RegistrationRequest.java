package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class RegistrationRequest {

    @JsonProperty("file_id")
    private long fileId;

    @JsonProperty("sequence_nbr")
    private long sequenceNbr;

    @Length(max = 22)
    @JsonProperty("source_id")
    private int sourceId;

    /// EXCEL SHEET

    @NotNull
    @Length(min = 1, max = 2)
    @Pattern(regexp = "e|en|f|fr|E|EN|F|FR")
    @JsonProperty("language_pref")
    private String languagePref;


    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @JsonProperty("as_of_date")
    private Date asOfDate;


    @Length(min = 10,max = 10)
    @JsonProperty("phone_number")
    private String phoneNumber;

    // name properties
    @Length(max = 40)
    @JsonProperty("src_first_name")
    private String firstName;

    @JsonProperty("src_middle_initial")
    private String middleInitial;

    @Length(max = 60)
    @JsonProperty("src_last_name")
    private String lastName;

    @Length(max = 100)
    @JsonProperty("src_address1")
    private String address1;

    @Length(max = 60)
    @JsonProperty("src_address2")
    private String address2;

    @Length(max = 60)
    @JsonProperty("src_city")
    private String city;

    @Length(max = 60)
    @JsonProperty("src_state")
    private String state;


    @JsonProperty("src_zipcode")
    private String zipcode;

    @JsonProperty("src_zip4")
    private String zip4;

    @Length(max = 7)
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

    @Length(max = 60)
    @JsonProperty("src_title_name")
    private String titleName;

    @Pattern(regexp = "\\d")
    @Max(value = 1)
    @Min(value = -1)
    @JsonProperty("phone_1_pref")
    private String phone1Pref;

    @Pattern(regexp = "\\d")
    @Max(value = 1)
    @Min(value = -1)
    @Length(max = 2)
    @JsonProperty("email_address_1_pref")
    private String emailAddress1Pref;


    @Length(max = 2)
    @JsonProperty("mail_address_1_pref")
    private String mailAddress1Pref;

    @JsonProperty("src_date")
    private Date date;

    @JsonProperty("email_status")
    private int emailStatus;

    @Length(max = 6)
    @JsonProperty("src_phone_extension")
    private String phoneExtension;

    @Length(max = 2)
    @JsonProperty("email_pref_hd_ca")
    private String emailPrefHdCa;

    @Length(max = 2)
    @JsonProperty("email_pref_garden_club")
    private String emailPrefGardenClub;

    private Content content;

    private Value value;
    public RegistrationRequest(){}
}

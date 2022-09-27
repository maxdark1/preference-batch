package ca.homedepot.preference.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInboundStgTable
{
	BigDecimal file_id;
	String fileName;

	String status;

	String src_language_pref;
	Date updated_date;
	String src_email_address;
	String email_address_pref;
	String phone_pref;
	String src_phone_number;
	String src_phone_extension;
	String src_title_name;
	String src_first_name;
	String src_last_name;
	String src_address1;
	String src_address2;
	String src_city;
	String src_state;
	Date src_date;
	String src_postal_code;
	BigDecimal email_status;
	String mail_address_pref;
	String email_pref_hd_ca;
	String email_pref_garden_club;
	String email_pref_pro;
	String email_pref_new_mover;
	Long source_id;
	String cell_sms_flag;
	String customer_nbr;
	String org_name;
	String store_nbr;
	String cust_type_cd;
	String fax_number;
	String fax_extension;

	//----------------------
	// Content - Value pair
	//----------------------
	String content1;
	String value1;
	String content2;
	String value2;
	String content3;
	String value3;
	String content4;
	String value4;
	String content5;
	String value5;
	String content6;
	String value6;
	String content7;
	String value7;
	String content8;
	String value8;
	String content9;
	String value9;
	String content10;
	String value10;
	String content11;
	String value11;
	String content12;
	String value12;
	String content13;
	String value13;
	String content14;
	String value14;
	String content15;
	String value15;
	String content16;
	String value16;
	String content17;
	String value17;
	String content18;
	String value18;
	String content19;
	String value19;
	String content20;
	String value20;
	private String inserted_by;
	private Date inserted_date;
}

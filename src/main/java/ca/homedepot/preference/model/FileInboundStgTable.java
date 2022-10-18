package ca.homedepot.preference.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInboundStgTable
{
	/**
	 * Field file_id on persistence
	 *
	 */
	BigDecimal file_id;

	/**
	 * File Name Not a field on persistence Purpose: to search file's id
	 */
	String fileName;

	/*
	 * Status for record on staging table NS - Not Started IP - In Progress This last status come from Service response E -
	 * Error S - Success
	 */
	String status;

	/*
	 * Field src_language_pref on persistence
	 *
	 */
	String src_language_pref;

	/*
	 * Field src_email_address on persistence
	 *
	 */
	String src_email_address;


	/*
	 * Field email_address_pref on persistence
	 *
	 */
	String email_address_pref;

	/*
	 * Field phone_pref on persistence
	 *
	 */
	String phone_pref;

	/*
	 * Field src_phone_number on persistence
	 *
	 */
	String src_phone_number;

	/*
	 * Field src_phone_extension on persistence
	 *
	 */
	String src_phone_extension;

	/*
	 * Field src_title_name on persistence
	 *
	 */
	String src_title_name;

	/*
	 * Field src_first_name on persistence
	 *
	 */
	String src_first_name;

	/*
	 * Field src_last_name on persistence
	 *
	 */
	String src_last_name;

	/*
	 * Field src_address1 on persistence
	 *
	 */
	String src_address1;


	/*
	 * Field src_address2 on persistence
	 *
	 */
	String src_address2;

	/*
	 * Field src_city on persistence
	 *
	 */
	String src_city;

	/*
	 * Field src_state on persistence
	 *
	 */
	String src_state;

	/*
	 * Field src_date on persistence
	 *
	 */
	Date src_date;

	/*
	 * Field src_postal_code on persistence
	 *
	 */
	String src_postal_code;

	/*
	 * Field email_status on persistence
	 *
	 */
	BigDecimal email_status;

	/*
	 * Field mail_address_pref on persistence
	 *
	 */
	String mail_address_pref;

	/*
	 * Field email_pref_hd_ca on persistence
	 *
	 */
	String email_pref_hd_ca;

	/*
	 * Field email_pref_garden_club on persistence
	 *
	 */
	String email_pref_garden_club;

	/*
	 * Field email_pref_pro on persistence
	 *
	 */
	String email_pref_pro;

	/*
	 * Field email_pref_new_mover on persistence
	 *
	 */
	String email_pref_new_mover;

	/*
	 * Field source_id on persistence
	 *
	 */
	BigDecimal source_id;

	/*
	 * Field cell_sms_flag on persistence
	 *
	 */
	String cell_sms_flag;

	/*
	 * Field customer_nbr on persistence
	 *
	 */
	String customer_nbr;

	/*
	 * Field business_name on persistence
	 *
	 */
	String business_name;

	/*
	 * Field org_name on persistence
	 *
	 */
	String org_name;

	/*
	 * Field store_nbr on persistence
	 *
	 */
	String store_nbr;

	/*
	 * Field cust_type_cd on persistence
	 *
	 */
	String cust_type_cd;

	/*
	 * Field cust_type_cd on persistence
	 *
	 */
	String fax_number;

	/*
	 * Field fax_extension on persistence
	 *
	 */
	String fax_extension;

	//----------------------
	// Content - Value pair on persistence
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

	/*
	 * Field inserted_by on persistence
	 *
	 */
	private String inserted_by;

	/*
	 * Field inserted_date on persistence
	 *
	 */
	private Date inserted_date;
	Date updated_date;
}

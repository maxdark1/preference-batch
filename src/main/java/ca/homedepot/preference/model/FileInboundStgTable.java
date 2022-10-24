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

	/**
	 * The content Value pair
	 */

	/**
	 * Field comtent1 on persistence
	 */
	String content1;
	/**
	 * Field value1 on persistence
	 */
	String value1;
	/***
	 * Field content2 on persistence
	 */
	String content2;
	/**
	 * Field value2 on persistence
	 */
	String value2;
	/**
	 * Field content3 on persistence
	 */
	String content3;
	/**
	 * Field value3 on persistence
	 */
	String value3;
	/**
	 * Field content4 on persistence
	 */
	String content4;
	/**
	 * Field value4 on persistence
	 */
	String value4;
	/**
	 * Field content5 on persistence
	 */
	String content5;
	/**
	 * Field value5 on persistence
	 */
	String value5;
	/**
	 * Field content6 on persistence
	 */
	String content6;
	/**
	 * Field value6 on persistence
	 */
	String value6;
	/**
	 * Field content7 on persistence
	 */
	String content7;
	/**
	 * Field value7 on persistence
	 */
	String value7;
	/**
	 * Field content8 on persistence
	 */
	String content8;
	/**
	 * Field value8 on persistence
	 */
	String value8;
	/**
	 * Field content9 on persistence
	 */
	String content9;
	/**
	 * Field value9 on persistence
	 */
	String value9;
	/**
	 * Field content10 on persistence
	 */
	String content10;
	/**
	 * Field value10 on persistence
	 */
	String value10;
	/**
	 * Field content11 on persistence
	 */
	String content11;
	/**
	 * Field value11 on persistence
	 */
	String value11;
	/**
	 * Field content12 on persistence
	 */
	String content12;
	/**
	 * Field value12 on persistence
	 */
	String value12;
	/**
	 * Field content13 on persistence
	 */
	String content13;
	/**
	 * Field value13 on persistence
	 */
	String value13;
	/**
	 * Field content14 on persistence
	 */
	String content14;
	/**
	 * Field value14 on persistence
	 */
	String value14;
	/**
	 * Field content15 on persistence
	 */
	String content15;
	/**
	 * Field value15 on persistence
	 */
	String value15;
	/**
	 * Field content16 on persistence
	 */
	String content16;
	/**
	 * Field value16 on persistence
	 */
	String value16;
	/**
	 * Field content17 on persistence
	 */
	String content17;
	/**
	 * Field value17 on persistence
	 */
	String value17;
	/**
	 * Field content18 on persistence
	 */
	String content18;
	/**
	 * Field value18 on persistence
	 */
	String value18;
	/**
	 * Field content19 on persistence
	 */
	String content19;
	/**
	 * Field value19 on persistence
	 */
	String value19;
	/**
	 * Field content20 on persistence
	 */
	String content20;
	/**
	 * Field value20 on persistence
	 */
	String value20;

	/**
	 * Field inserted_by on persistence
	 */
	private String inserted_by;

	/**
	 * Field inserted_date on persistence
	 */
	private Date inserted_date;
	/**
	 * Field updated date
	 */
	Date updated_date;
}

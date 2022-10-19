package ca.homedepot.preference.constants;

public interface SqlQueriesConstants
{

	/*
	 * SELECT
	 */
	String SQL_SELECT_MASTER_ID = "SELECT master_id, mk.key_id, mk.key_value, value_val, active\n" + "\tFROM hdpc_master m\n"
			+ "\tINNER JOIN hdpc_master_key mk ON mk.key_id = m.key_id\n" + "\tWHERE active = true;";
	String SQL_SELECT_LAST_FILE = "SELECT file_id FROM hdpc_file ORDER BY file_id DESC LIMIT 1";
	String SQL_SELECT_LAST_FILE_INSERT = "SELECT file_id FROM hdpc_file WHERE file_name = ? AND job_id = ? LIMIT 1";
	String SQL_SELECT_LAST_JOB_W_NAME = "SELECT * FROM hdpc_job WHERE job_name = ? AND status = 'IN PROGRESS' ORDER BY job_id DESC LIMIT 1";


	/// ADD STATUS AS WELL IN HERE FIST (NS records)
	String SQL_GET_LAST_FILE_INSERTED_RECORDS_NOT_SFMC = "SELECT hfis.file_id, hfis.status, sequence_nbr, source_id, src_phone_number, src_first_name, src_middle_initial, src_last_name, src_address1, src_address2, src_city, src_state, src_zipcode, src_zip4, src_postal_code, src_system, credit_prin, src_agent, src_last_balance_amt, credit_acct_open_dt, src_last_trans_dt, src_language_pref, credit_store_origin, src_suppression_flag, src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_date, email_status, src_phone_extension, email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, cell_sms_flag, business_name, customer_nbr, org_name, store_nbr, cust_type_cd, content1, value1, content2, value2, content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9, content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15, content16, value16, content17, value17, content18, value18, content19, value19, content20, value20\n"
			+ "\tFROM public.hdpc_file_inbound_stg hfis\n" + "\tINNER JOIN public.hdpc_file ON hdpc_file.file_id = hfis.file_id\n"
			+ "\tINNER JOIN public.hdpc_job ON hdpc_job.job_id = hdpc_file.job_id\n" + "\tWHERE hdpc_job.job_name != ";

	String SQL_GET_LAST_FILE_INSERTED_RECORDS_SFMC = "SELECT hfis.file_id, hfis.status, sequence_nbr, source_id, src_phone_number, src_first_name, src_middle_initial, src_last_name, src_address1, src_address2, src_city, src_state, src_zipcode, src_zip4, src_postal_code, src_system, credit_prin, src_agent, src_last_balance_amt, credit_acct_open_dt, src_last_trans_dt, src_language_pref, credit_store_origin, src_suppression_flag, src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_date, email_status, src_phone_extension, email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, cell_sms_flag, business_name, customer_nbr, org_name, store_nbr, cust_type_cd, content1, value1, content2, value2, content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9, content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15, content16, value16, content17, value17, content18, value18, content19, value19, content20, value20\n"
			+ "\tFROM public.hdpc_file_inbound_stg hfis\n" + "\tINNER JOIN public.hdpc_file ON hdpc_file.file_id = hfis.file_id\n"
			+ "\tINNER JOIN public.hdpc_job ON hdpc_job.job_id = hdpc_file.job_id\n" + "\tWHERE hdpc_job.job_name = ";

	String SQL_CONDITION_IP = " AND hfis." + "status = 'IP';";

	String SQL_GET_FILES_TO_MOVE = "SELECT file_id, file_name, source_type, hdpc_master.value_val\n" + "\tFROM public.hdpc_file\n"
			+ "\tINNER JOIN hdpc_master ON hdpc_master.master_id = hdpc_file.source_type\n" + "\tWHERE end_time is null;";
	/*
	 * INSERTIONS
	 */
	String SQL_INSERT_HDPC_JOB = "INSERT INTO hdpc_job( job_name, status, status_id, start_time, inserted_by, inserted_date) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?) ";

	String SQL_INSERT_HDPC_FILE = "INSERT INTO hdpc_file (file_name, job_id, source_type, "
			+ "status,start_time, inserted_by, inserted_date, status_id, end_time) " + "VALUES (? , ? , ?, ?, ? , ?, ?, ?, ?); ";

	String SQL_INSERT_FILE_INBOUND_STG_ERROR = "INSERT INTO public.hdpc_file_inbound_stg_error(\n"
			+ "\tfile_id, status, source_id, src_phone_number, src_first_name, src_last_name, src_address1, "
			+ "src_address2, src_city, src_state, src_postal_code, src_language_pref, src_email_address, src_title_name, phone_pref, "
			+ "email_address_pref, mail_address_pref, src_date, email_status, src_phone_extension, email_pref_hd_ca, email_pref_garden_club, email_pref_pro,"
			+ " email_pref_new_mover, cell_sms_flag, business_name, customer_nbr, org_name, store_nbr, cust_type_cd, content1, "
			+ "value1, content2, value2, content3, value3, content4, value4, content5, "
			+ "value5, content6, value6, content7, value7, content8, value8, content9, "
			+ "value9, content10, value10, content11, value11, content12, value12, content13, "
			+ "value13, content14, value14, content15, value15, content16,value16, content17, "
			+ "value17, content18, value18, content19, value19, content20, value20, inserted_by, inserted_date)\n"
			+ "\tVALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	String SQL_INSERT_FILE_INBOUND_STG_REGISTRATION = "INSERT INTO hdpc_file_inbound_stg "
			+ "(file_id, status,src_date, source_id, src_phone_number, src_first_name,  src_last_name,"
			+ "src_address1, src_address2, src_city, src_state, src_postal_code, src_language_pref,"
			+ "src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_phone_extension,"
			+ "email_status, email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, content1, value1, content2, value2, "
			+ "content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9,"
			+ "content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15,"
			+ "content16, value16, content17, value17, content18, value18, content19, value19, content20, value20, updated_date, cell_sms_flag, "
			+ "customer_nbr, org_name, store_nbr, cust_type_cd, inserted_by, inserted_date) " + "VALUES"
			+ "(:file_id,:status,:src_date, :source_id, :src_phone_number, :src_first_name, :src_last_name,"
			+ ":src_address1, :src_address2, :src_city, :src_state, :src_postal_code, :src_language_pref,"
			+ ":src_email_address, :src_title_name, :phone_pref, :email_address_pref, :mail_address_pref, :src_phone_extension,"
			+ ":email_status, :email_pref_hd_ca, :email_pref_garden_club, :email_pref_pro, :email_pref_new_mover, :content1, :value1, :content2, :value2, "
			+ ":content3, :value3, :content4, :value4, :content5, :value5, :content6, :value6, :content7, :value7, :content8, :value8, :content9, :value9,"
			+ ":content10, :value10, :content11, :value11, :content12, :value12, :content13, :value13, :content14, :value14, :content15, :value15,"
			+ ":content16, :value16, :content17, :value17, :content18, :value18, :content19, :value19, :content20, :value20, :updated_date, :cell_sms_flag, "
			+ ":customer_nbr, :org_name, :store_nbr, :cust_type_cd, :inserted_by, :inserted_date);";

	/*
	 * UPDATE
	 */

	String SQL_UPDATE_STAUTS_JOB = "UPDATE hdpc_job\n"
			+ "\tSET  status_id=?, updated_date=?, updated_by=?,status = ?, end_time = ?\n"
			+ "\tWHERE start_time = ? AND job_name = ? AND status = ?;";

	String SQL_UPDATE_STAUTS_FILE = "UPDATE hdpc_file \n"
			+ "\tSET  status=?, status_id = ?,updated_date=?, endTime = ?, updated_by = ? \n"
			+ "\tWHERE file_name = ? AND status = ? AND job_id = ?;";

	String SQL_UPDATE_ENDTIME_FILE = "UPDATE hdpc_file SET end_time = ?, updated_date = ?, updated_by = ?, status = ?, status_id = ? "
			+ " WHERE file_id = ?";

	String SQL_UPDATE_STATUS_INBOUND = "UPDATE hdpc_file_inbound_stg SET status = ?, updated_date = ?, updated_by = ? WHERE status = ? and (sequence_nbr = ? or file_id = ?)";

	String SQL_GET_CRM_OUTBOUND = "SELECT \n" + "      email.email\temail,\n"
			+ "      cust_email.effective_date\teffective_date,\n" + "\t  file_inbound.source_id source_id,\n"
			+ "\t  email.status_id\temail_status,\n" + "\t  CASE \n" + "\t  \tWHEN cust_email.permission_val is null then 'U'  \n"
			+ "\t\tWHEN cust_email.permission_val = true then 'Y'\n"
			+ "\t\tWHEN cust_email.permission_val = false then 'N' end email_permission,\n"
			+ "\t  cust.language_pref language_preference,\n" + "\t  pref.opt_in_date early_opt_in_date,\n" + "\t  CASE\n"
			+ "\t  \tWHEN cust_email.permission_val = 'Y' AND email.status_id <> '00' then 'Y' else 'N' end cnd_compliant_flag,\n"
			+ "\t  CASE WHEN pref.preference_type = 6 then 'Y' else 'N' end email_pref_hd_ca,\n"
			+ "      CASE WHEN pref.preference_type = 7 then 'Y' else 'N' end email_pref_garden_club,\n"
			+ "      CASE WHEN pref.preference_type = 8 then 'Y' else 'N' end email_pref_pro,\n"
			+ "\t  addr.postal_code          src_postal_code,\n" + "\t  cust_extn.customer_nbr    customer_nbr,\n" + "\t  CASE \n"
			+ "\t  \tWHEN cust_phone.text_permission is null then 'U'  \n" + "\t\tWHEN cust_phone.text_permission = true then 'Y'\n"
			+ "\t\tWHEN cust_phone.text_permission = false then 'N' end phone_ptc_flag,\n" + "\t  CASE \n"
			+ "\t  \tWHEN cust_phone.call_permission is null then 'U'  \n" + "\t\tWHEN cust_phone.call_permission = true then 'Y'\n"
			+ "\t\tWHEN cust_phone.call_permission = false then 'N' end dncl_suppresion,\n"
			+ "\t  phone.phone_number phone_number,\n" + "\t  cust.first_name first_name,\n" + "\t  cust.last_name last_name,\n"
			+ "\t  cust_extn.business_name business_name,\n" + "\t  cust_extn.industry_code industry_code,\n"
			+ "\t  addr.city city,\n" + "\t  addr.province province,\n" + "\t  file_inbound.source_id hd_ca_pro_src_id\n"
			+ "    FROM hdpc_customer cust\n" + "    JOIN hdpc_file_customer file_cust\n"
			+ "        ON cust.customer_id = file_cust.customer_id\n" + "\tLEFT JOIN hdpc_file_inbound_stg file_inbound\n"
			+ "\t\tON file_cust.file_id = file_inbound.file_id\n" + "    LEFT JOIN hdpc_customer_email cust_email\n"
			+ "        ON cust.customer_id = cust_email.email_id\n" + "    LEFT JOIN hdpc_email email\n"
			+ "        ON cust_email.email_id = email.email_id\n" + "    LEFT JOIN hdpc_customer_phone cust_phone\n"
			+ "        ON cust.customer_id = cust_phone.customer_id\n" + "    LEFT JOIN hdpc_phone phone\n"
			+ "        ON cust_phone.phone_id = phone.phone_id\n" + "    LEFT JOIN hdpc_customer_extn cust_extn\n"
			+ "        ON cust.customer_id = cust_extn.customer_id\n" + "    LEFT JOIN hdpc_customer_address cust_addr\n"
			+ "        ON cust.customer_id = cust_addr.customer_id\n" + "    LEFT JOIN hdpc_address addr\n"
			+ "        ON cust_addr.address_id = addr.address_id\n" + "    LEFT JOIN hdpc_customer_preference pref\n"
			+ "        ON cust.customer_id = pref.customer_id\n" + "\tWHERE pref.preference_type = 8 --Preference Type for CRM\n"
			+ "\tGROUP BY email.email,\n" + "\tcust_email.effective_date,\n" + "\tfile_inbound.source_id,\n" + "\temail.status_id,\n"
			+ "\tcust_email.permission_val,\n" + "\tcust.language_pref,\n" + "\tpref.opt_in_date,\n" + "\tpref.preference_type,\n"
			+ "    addr.postal_code,\n" + "\tcust_extn.customer_nbr,\n" + "\tcust_phone.text_permission,\n"
			+ "\tcust_phone.call_permission,\n" + "\tphone.phone_number,\n" + "\tcust.first_name,\n" + "\tcust.last_name,\n"
			+ "\tcust_extn.business_name,\n" + "\tcust_extn.industry_code,\n" + "\taddr.city,\n" + "\taddr.province";

	String SQL_INSERT_STG_PREFERENCE_OUTBOUND = "INSERT INTO public.hdpc_out_daily_compliant(\n" +
			"\temail_addr, \n" +
			"\tcan_ptc_effective_date,\n" +
			"\tcan_ptc_source_id,\n" +
			"\temail_status, \n" +
			"\tcan_ptc_flag, \n" +
			"\tlanguage_preference,\n" +
			"\tearly_opt_in_date, \n" +
			"\tcnd_compliant_flag, \n" +
			"\thd_ca_flag, \n" +
			"\thd_ca_garden_club_flag,\n" +
			"\thd_ca_pro_flag, \n" +
			"\tpostal_cd, \n" +
			"\tcustomer_nbr,\n" +
			"\tphone_ptc_flag, \n" +
			"\tdncl_suppression_flag, \n" +
			"\tphone_number, \n" +
			"\tfirst_name, \n" +
			"\tlast_name, \n" +
			"\tbusiness_name, \n" +
			"\tindustry_code, city, \n" +
			"\tprovince, \n" +
			"\thd_ca_pro_src_id)\n" +
			"\tVALUES (\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?,\n" +
			"\t?\n" +
			"\t)";
}

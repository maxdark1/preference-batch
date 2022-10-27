package ca.homedepot.preference.constants;

/**
 * SQL Quesries that are use on JdbcTemplate
 */
public final class SqlQueriesConstants
{
	private SqlQueriesConstants()
	{

	}

	/**************************************
	 * SELECTS
	 ***************************************/
	/**
	 * SELECT to get master information from hdpc_master
	 */
	public static String SQL_SELECT_MASTER_ID = "SELECT master_id, mk.key_id, mk.key_value, value_val, active, mir.old_id FROM hdpc_master m\n"
			+ "INNER JOIN hdpc_master_key mk ON mk.key_id = m.key_id\n"
			+ "LEFT JOIN public.hdpc_master_id_rel mir ON m.master_id = mir.pcam_id \n" + "WHERE m.active = true;\n";

	/**
	 * SELECT to get last file id from hdpc_file
	 */
	public static String SQL_SELECT_LAST_FILE = "SELECT file_id FROM hdpc_file ORDER BY file_id DESC LIMIT 1";
	/**
	 * SELECT to get last file inserted for an specific file_name and job_id from hdpc_file
	 */
	public static String SQL_SELECT_LAST_FILE_INSERT = "SELECT file_id FROM hdpc_file WHERE file_name = ? AND job_id = ? LIMIT 1";

	/**
	 * SELECT to get the last job according to job_name and status IN PROGRESS from hdpc_jop
	 */
	public static String SQL_SELECT_LAST_JOB_W_NAME = "SELECT * FROM hdpc_job WHERE job_name = ? AND status = 'IN PROGRESS' ORDER BY job_id DESC LIMIT 1";


	/**
	 * SELECT to get hdpc_file_inbound_stg table information excluding SFMC requests
	 */
	public static String SQL_GET_LAST_FILE_INSERTED_RECORDS_NOT_SFMC = "SELECT hfis.file_id, hfis.status, sequence_nbr, source_id, src_phone_number, src_first_name, src_middle_initial, src_last_name, src_address1, src_address2, src_city, src_state, src_zipcode, src_zip4, src_postal_code, src_system, credit_prin, src_agent, src_last_balance_amt, credit_acct_open_dt, src_last_trans_dt, src_language_pref, credit_store_origin, src_suppression_flag, src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_date, email_status, src_phone_extension, email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, cell_sms_flag, business_name, customer_nbr, org_name, store_nbr, cust_type_cd, content1, value1, content2, value2, content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9, content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15, content16, value16, content17, value17, content18, value18, content19, value19, content20, value20\n"
			+ "\tFROM public.hdpc_file_inbound_stg hfis\n" + "\tINNER JOIN public.hdpc_file ON hdpc_file.file_id = hfis.file_id\n"
			+ "\tINNER JOIN public.hdpc_job ON hdpc_job.job_id = hdpc_file.job_id\n" + "\tWHERE hdpc_job.job_name != ";
	/**
	 * SELECT to get hdpc_file_inbound_stg table information just getting SFMC requests
	 */
	public static String SQL_GET_LAST_FILE_INSERTED_RECORDS_SFMC = "SELECT hfis.file_id, hfis.status, sequence_nbr, source_id, src_phone_number, src_first_name, src_middle_initial, src_last_name, src_address1, src_address2, src_city, src_state, src_zipcode, src_zip4, src_postal_code, src_system, credit_prin, src_agent, src_last_balance_amt, credit_acct_open_dt, src_last_trans_dt, src_language_pref, credit_store_origin, src_suppression_flag, src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_date, email_status, src_phone_extension, email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, cell_sms_flag, business_name, customer_nbr, org_name, store_nbr, cust_type_cd, content1, value1, content2, value2, content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9, content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15, content16, value16, content17, value17, content18, value18, content19, value19, content20, value20\n"
			+ "\tFROM public.hdpc_file_inbound_stg hfis\n" + "\tINNER JOIN public.hdpc_file ON hdpc_file.file_id = hfis.file_id\n"
			+ "\tINNER JOIN public.hdpc_job ON hdpc_job.job_id = hdpc_file.job_id\n" + "\tWHERE hdpc_job.job_name = ";

	/**
	 * Condition where status = 'IP'
	 */
	public static String SQL_CONDITION_IP = " AND hfis." + "status = 'IP';";

	/**
	 * Gets all files to move, Those that don't have "end_time"
	 */
	public static String SQL_GET_FILES_TO_MOVE = "SELECT file_id, file_name, source_type, hdpc_master.value_val\n"
			+ "\tFROM public.hdpc_file\n" + "\tINNER JOIN hdpc_master ON hdpc_master.master_id = hdpc_file.source_type\n"
			+ "\tWHERE end_time is null;";


	/**************************************
	 * INSERTIONS
	 ***************************************/

	/**
	 * Insert to hdpc_job table
	 */
	public static final String SQL_INSERT_HDPC_JOB = "INSERT INTO hdpc_job( job_name, status, status_id, start_time, inserted_by, inserted_date) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?) ";
	/**
	 * Insert to hdpc_file table
	 */
	public static final String SQL_INSERT_HDPC_FILE = "INSERT INTO hdpc_file (file_name, job_id, source_type, "
			+ "status,start_time, inserted_by, inserted_date, status_id, end_time) " + "VALUES (? , ? , ?, ?, ? , ?, ?, ?, ?); ";
	/**
	 * Insert to hdpc_file_inbound_stg_error table
	 */
	public static String SQL_INSERT_FILE_INBOUND_STG_ERROR = "INSERT INTO public.hdpc_file_inbound_stg_error(\n"
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

	/**
	 * Insert to hdpc_file_inbound_stg table
	 */
	public static String SQL_INSERT_FILE_INBOUND_STG_REGISTRATION = "INSERT INTO hdpc_file_inbound_stg "
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

	/**************************************
	 * UPDATE
	 ***************************************/

	/**
	 * Update of hdpc_job, to change it status when job ends
	 */
	public static String SQL_UPDATE_STAUTS_JOB = "UPDATE hdpc_job\n"
			+ "\tSET  status_id=?, updated_date=?, updated_by=?,status = ?, end_time = ?\n"
			+ "\tWHERE start_time = ? AND job_name = ? AND status = ?;";

	/**
	 * To update hdpc_file record when it ends to read the file
	 */
	public static String SQL_UPDATE_STAUTS_FILE = "UPDATE hdpc_file \n"
			+ "\tSET  status=?, status_id = ?,updated_date=?, endTime = ?, updated_by = ? \n"
			+ "\tWHERE file_name = ? AND status = ? AND job_id = ?;";

	/**
	 * To update hppd_file record when it ends to read the file
	 */
	public static String SQL_UPDATE_ENDTIME_FILE = "UPDATE hdpc_file SET end_time = ?, updated_date = ?, updated_by = ?, status = ?, status_id = ? "
			+ " WHERE file_id = ?";

	/**
	 * To update hdpc_file_inbound_stg once either step 1 or step 2 ends.
	 */
	public static String SQL_UPDATE_STATUS_INBOUND = "UPDATE hdpc_file_inbound_stg SET status = ?, updated_date = ?, updated_by = ? WHERE status = ? and (sequence_nbr = ? or file_id = ?)";

	/*******************************
	 * DELETE
	 ********************************/

	public static String SQL_PURGE_SUCCESS_STG_TABLE = "DELETE FROM public.hdpc_file_inbound_stg WHERE status = 'S';";

}

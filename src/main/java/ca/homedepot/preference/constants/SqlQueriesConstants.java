package ca.homedepot.preference.constants;

public interface SqlQueriesConstants
{

	/*
	 * SELECT
	 */
	String SQL_SELECT_MASTER_ID = "SELECT master_id, mk.key_id, mk.key_value, value_val, active\n" +
			"\tFROM pcam.hdpc_master m\n" +
			"\tINNER JOIN pcam.hdpc_master_key mk ON mk.key_id = m.key_id\n" +
			"\tWHERE active = true;";
	String SQL_SELECT_LAST_FILE = "SELECT file_id FROM pcam.hdpc_file ORDER BY file_id DESC LIMIT 1";
	String SQL_SELECT_LAST_FILE_INSERT = "SELECT file_id FROM pcam.hdpc_file WHERE file_name = ? AND job_id = ? LIMIT 1";
	String SQL_SELECT_LAST_JOB_W_NAME = "SELECT * FROM pcam.hdpc_job WHERE job_name = ? AND status = 'IN PROGRESS' ORDER BY job_id DESC LIMIT 1";


	String SQL_GET_LAST_FILE_INSERTED_RECORDS = "SELECT * FROM pcam.hdpc_file_inbound_stg WHERE file_id = ("
			+ SqlQueriesConstants.SQL_SELECT_LAST_FILE + "); ";
	/*
	 * INSERTIONS
	 */
	String SQL_INSERT_HDPC_JOB = "INSERT INTO pcam.hdpc_job( job_name, status, status_id,start_time, inserted_by, inserted_date) "
			+ "VALUES ( ?, ?,15, ?, ?, ?) ";

	String SQL_INSERT_HDPC_FILE = "INSERT INTO pcam.hdpc_file (file_name, job_id, source_type, "
			+ "status,start_time, inserted_by, inserted_date, status_id) " + "VALUES (? , ? , ?, ?, ? , ?, ?, ?); ";

	String SQL_INSERT_FILE_INBOUND_STG_REGISTRATION = "INSERT INTO pcam.hdpc_file_inbound_stg "
			+ "(file_id, status,src_date, source_id, src_phone_number, src_first_name,  src_last_name,"
			+ "src_address1, src_address2, src_city, src_state, src_postal_code, src_language_pref,"
			+ "src_email_address, src_title_name, phone_pref, email_address_pref, mail_address_pref, src_phone_extension,"
			+ "email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, content1, value1, content2, value2, "
			+ "content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9,"
			+ "content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15,"
			+ "content16, value16, content17, value17, content18, value18, content19, value19, content20, value20, updated_date, cell_sms_flag, "
			+ "customer_nbr, org_name, store_nbr, cust_type_cd, inserted_by, inserted_date) " + "VALUES"
			+ "(:file_id,:status,:src_date, :source_id, :src_phone_number, :src_first_name, :src_last_name,"
			+ ":src_address1, :src_address2, :src_city, :src_state, :src_postal_code, :src_language_pref,"
			+ ":src_email_address, :src_title_name, :phone_pref, :email_address_pref, :mail_address_pref, :src_phone_extension,"
			+ ":email_pref_hd_ca, :email_pref_garden_club, :email_pref_pro, :email_pref_new_mover, :content1, :value1, :content2, :value2, "
			+ ":content3, :value3, :content4, :value4, :content5, :value5, :content6, :value6, :content7, :value7, :content8, :value8, :content9, :value9,"
			+ ":content10, :value10, :content11, :value11, :content12, :value12, :content13, :value13, :content14, :value14, :content15, :value15,"
			+ ":content16, :value16, :content17, :value17, :content18, :value18, :content19, :value19, :content20, :value20, :updated_date, :cell_sms_flag, "
			+ ":customer_nbr, :org_name, :store_nbr, :cust_type_cd, :inserted_by, :inserted_date);";

	/*
	 * UPDATE
	 */

	String SQL_UPDATE_STAUTS_JOB = "UPDATE pcam.hdpc_job\n" + "\tSET  status=?, updated_date=?\n"
			+ "\tWHERE inserted_date = ? AND job_name = ? AND status = ?;";

	String SQL_UPDATE_STAUTS_FILE = "UPDATE pcam.hdpc_file \n" + "\tSET  status=?, updated_date=?\n"
			+ "\tWHERE file_name = ? AND status = ?;";

	String SQL_UPDATE_STATUS_INBOUND = "UPDATE pcam.hdpc_file_inbound_stg SET status = ?, updated_date = ?, updated_by = ? WHERE status = 'NS' and file_id = ?";
}

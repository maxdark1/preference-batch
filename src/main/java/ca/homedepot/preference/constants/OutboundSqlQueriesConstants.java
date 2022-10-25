package ca.homedepot.preference.constants;

public interface OutboundSqlQueriesConstants {
    String SQL_GET_CRM_OUTBOUND = "SELECT \n" +
            "      email.email\temail,\n" +
            "      cust_email.effective_date\teffective_date,\n" +
            "\t  email.source_type source_id,\n" +
            "\t  email.status_id\temail_status,\n" +
            "\t  CASE \n" +
            "\t  \tWHEN cust_email.permission_val is null then 'U'  \n" +
            "\t\tWHEN cust_email.permission_val = true then 'Y'\n" +
            "\t\tWHEN cust_email.permission_val = false then 'N' end email_permission,\n" +
            "\t  cust.language_pref language_preference,\n" +
            "\t  pref.opt_in_date early_opt_in_date,\n" +
            "\t  CASE\n" +
            "\t  \tWHEN cust_email.permission_val = true AND email.status_id <> '00' then 'Y' else 'N' end cnd_compliant_flag,\n" +
            "\t  CASE WHEN pref.preference_type = 6 then 'Y' else 'N' end email_pref_hd_ca,\n" +
            "      CASE WHEN pref.preference_type = 7 then 'Y' else 'N' end email_pref_garden_club,\n" +
            "      CASE WHEN pref.preference_type = 8 then 'Y' else 'N' end email_pref_pro,\n" +
            "\t  addr.postal_code          src_postal_code,\n" +
            "\t  cust_extn.customer_nbr    customer_nbr,\n" +
            "\t  CASE \n" +
            "\t  \tWHEN cust_phone.text_permission is null then 'U'  \n" +
            "\t\tWHEN cust_phone.text_permission = true then 'Y'\n" +
            "\t\tWHEN cust_phone.text_permission = false then 'N' end phone_ptc_flag,\n" +
            "\t  CASE \n" +
            "\t  \tWHEN cust_phone.call_permission is null then 'U'  \n" +
            "\t\tWHEN cust_phone.call_permission = true then 'Y'\n" +
            "\t\tWHEN cust_phone.call_permission = false then 'N' end dncl_suppresion,\n" +
            "\t  phone.phone_number phone_number,\n" +
            "\t  cust.first_name first_name,\n" +
            "\t  cust.last_name last_name,\n" +
            "\t  cust_extn.business_name business_name,\n" +
            "\t  cust_extn.industry_code industry_code,\n" +
            "\t  addr.city city,\n" +
            "\t  addr.province province,\n" +
            "\t  pref.preference_type hd_ca_pro_src_id\n" +
            "    FROM hdpc_customer cust\n" +
            "    JOIN hdpc_file_customer file_cust\n" +
            "        ON cust.customer_id = file_cust.customer_id\n" +
            "    LEFT JOIN hdpc_customer_email cust_email\n" +
            "        ON cust.customer_id = cust_email.customer_id\n" +
            "    LEFT JOIN hdpc_email email\n" +
            "        ON cust_email.email_id = email.email_id\n" +
            "    LEFT JOIN hdpc_customer_phone cust_phone\n" +
            "        ON cust.customer_id = cust_phone.customer_id\n" +
            "    LEFT JOIN hdpc_phone phone\n" +
            "        ON cust_phone.phone_id = phone.phone_id\n" +
            "    LEFT JOIN hdpc_customer_extn cust_extn\n" +
            "        ON cust.customer_id = cust_extn.customer_id\n" +
            "    LEFT JOIN hdpc_customer_address cust_addr\n" +
            "        ON cust.customer_id = cust_addr.customer_id\n" +
            "    LEFT JOIN hdpc_address addr\n" +
            "        ON cust_addr.address_id = addr.address_id\n" +
            "    LEFT JOIN hdpc_customer_preference pref\n" +
            "        ON cust.customer_id = pref.customer_id\n" +
            "\tWHERE pref.preference_type = 8 --Preference Type for CRM\n" +
            "\tGROUP BY email.email,\n" +
            "\tcust_email.effective_date,\n" +
            "\temail.source_type,\n" +
            "\temail.status_id,\n" +
            "\tcust_email.permission_val,\n" +
            "\tcust.language_pref,\n" +
            "\tpref.opt_in_date,\n" +
            "\tpref.preference_type,\n" +
            "    addr.postal_code,\n" +
            "\tcust_extn.customer_nbr,\n" +
            "\tcust_phone.text_permission,\n" +
            "\tcust_phone.call_permission,\n" +
            "\tphone.phone_number,\n" +
            "\tcust.first_name,\n" +
            "\tcust.last_name,\n" +
            "\tcust_extn.business_name,\n" +
            "\tcust_extn.industry_code,\n" +
            "\taddr.city,\n" +
            "\taddr.province";

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

    String SQL_SELECT_OUTBOUND_DB_READER_STEP2 = "SELECT email_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_pro_flag, postal_cd, customer_nbr, phone_ptc_flag, dncl_suppression_flag, phone_number, first_name, last_name, business_name, industry_code, city, province, hd_ca_pro_src_id\n" +
            "\tFROM public.hdpc_out_daily_compliant";

    String SQL_TRUNCATE_COMPLIANT_TABLE = "TRUNCATE TABLE public.hdpc_out_daily_compliant";
}

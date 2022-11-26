package ca.homedepot.preference.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public class OutboundSqlQueriesConstants
{

	public static final String SQL_GET_CRM_OUTBOUND = "WITH\n"
			+ "    cust_with_pref AS -- getting customers that must contain \"pro\" value active as preference flag\n    (\n"
			+ "        SELECT pref.customer_id\n" + "            FROM  public.hdpc_customer_preference pref \n"
			+ "            JOIN public.hdpc_master mast\n" + "                ON pref.preference_type = mast.master_id\n"
			+ "            JOIN public.hdpc_master_key mast_key\n" + "                ON mast.key_id = mast_key.key_id\n"
			+ "            WHERE mast_key.key_value = 'PREFERENCE_FLAG'\n),\n" + "    min_opt_in_date AS\n    (\n"
			+ "        SELECT  customer_id\n" + "              , MIN(opt_in_date) opt_in_date\n"
			+ "            FROM  public.hdpc_customer_pref_hist\n" + "            WHERE permission_val\n"
			+ "        GROUP BY customer_id \n" + "    ) ,\n" + "    pref_per_cust AS\n    (\n"
			+ "        SELECT    cust.customer_id\n" + "                , cust.first_name first_name\n"
			+ "                , cust.last_name last_name\n" + "                , email.email\n"
			+ "                , cust_email.effective_date    effective_date\n"
			+ "                                                                , cust_email.effective_date early_opt_in_date\n"
			+ "                , email.source_type source_id\n" + "                , rel_id.old_id    email_status\n"
			+ "                                                , CASE\n"
			+ "                WHEN cust_email.permission_val is null then 'U'  \n"
			+ "                   WHEN cust_email.permission_val = true then 'Y'\n"
			+ "                 WHEN cust_email.permission_val = false then 'N' end email_permission\n"
			+ "                               , cust.language_pref language_preference\n"
			+ "            , CASE WHEN cust_email.permission_val\n                  AND email.status_id <> 0 \n"
			+ "                         then 'Y' \n" + "                  else 'N' \n"
			+ "                    end cnd_compliant_flag\n" + "            , MAX(CASE WHEN mast.value_val = 'hd_ca'\n"
			+ "                   AND pref.permission_val \n" + "                        then 'Y'\n"
			+ "                    else 'N' end) email_pref_hd_ca\n" + "            , MAX(CASE WHEN mast.value_val = 'garden_club'\n"
			+ "                  AND pref.permission_val \n" + "                          then 'Y' \n"
			+ "                    else 'N' end) email_pref_garden_club\n" + "            , MAX(CASE WHEN mast.value_val = 'Pro'\n"
			+ "                AND pref.permission_val \n" + "                       then 'Y' \n"
			+ "                    else 'N' end) email_pref_pro\n"
			+ "                                                , MAX(CASE WHEN mast.value_val = 'Pro' THEN pref.source_type else 0 end) hd_ca_pro_src_id\n"
			+ "            , MIN(COALESCE(min_opt.opt_in_date, pref.opt_in_date))\n"
			+ "                                                , MAX(CASE WHEN phone.phone_type = 10 THEN phone.phone_number ELSE '' END) phone_number\n"
			+ "                                                , MAX(CASE WHEN phone.phone_type = 12 THEN phone.phone_number ELSE '' END) cell_number\n"
			+ "                                                , MAX(CASE WHEN cust_phone.text_permission AND phone.phone_type = 12 THEN 'Y' \n"
			+ "                                                                                                WHEN cust_phone.text_permission = FALSE AND phone.phone_type = 12 THEN 'N'\n"
			+ "                                                                                                WHEN cust_phone.text_permission IS NULL AND phone.phone_type = 12 THEN 'U' END\n"
			+ "                  ) phone_ptc_flag\n"
			+ "                                                , MAX(CASE WHEN cust_phone.call_permission THEN 'Y'\n"
			+ "                                                                                                WHEN cust_phone.call_permission = FALSE THEN 'N'\n"
			+ "                                                                                                WHEN cust_phone.call_permission IS NULL THEN 'U' END\n"
			+ "                  ) dncl_suppresion\n" + "        FROM public.hdpc_customer cust\n"
			+ "        JOIN cust_with_pref  preference\n" + "            ON cust.customer_id = preference.customer_id\n"
			+ "        JOIN public.hdpc_customer_preference pref\n" + "            ON cust.customer_id = pref.customer_id\n"
			+ "        JOIN public.hdpc_master mast\n" + "            ON pref.preference_type = mast.master_id\n"
			+ "        JOIN public.hdpc_master_key mast_key\n" + "            ON mast.key_id = mast_key.key_id\n"
			+ "        LEFT JOIN min_opt_in_date min_opt\n" + "            ON cust.customer_id = min_opt.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_email cust_email\n"
			+ "                                                ON cust_email.customer_id = cust.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_email email\n"
			+ "                                                ON email.email_id = cust_email.email_id\n"
			+ "                                LEFT JOIN public.hdpc_master_id_rel rel_id\n"
			+ "                                                ON rel_id.pcam_id = email.status_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_phone cust_phone\n"
			+ "                                                ON cust.customer_id = cust_phone.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_phone phone\n"
			+ "                                                                                                ON cust_phone.phone_id = phone.phone_id                                                                                                                                 \n"
			+ "        WHERE mast_key.key_value = 'PREFERENCE_FLAG' \n" + "    GROUP BY cust.customer_id\n"
			+ "                                                , email.email\n"
			+ "                                                , cust_email.effective_date\n"
			+ "                                                , email.source_type \n"
			+ "                                                , email.status_id  \n"
			+ "                                                , cust.first_name \n"
			+ "                               , cust.last_name \n"
			+ "                                                                                                \n"
			+ "            , CASE\n                WHEN cust_email.permission_val is null then 'U'  \n"
			+ "                 WHEN cust_email.permission_val = true then 'Y'\n"
			+ "                 WHEN cust_email.permission_val = false then 'N' end \n"
			+ "                               , cust.language_pref \n            , CASE WHEN cust_email.permission_val\n"
			+ "                    AND email.status_id <> 0 \n                           then 'Y' \n"
			+ "                   else 'N' \n" + "                    end\n"
			+ "                                                , rel_id.old_id\n" + "    )\n" + "    SELECT cust.*\n"
			+ "      , cust_extn.org_name business_name\n      , cust_extn.industry_code industry_code\n"
			+ "                  , cust_extn.customer_nbr customer_nbr\n" + "      , addr.city city\n"
			+ "                  , addr.postal_code src_postal_code\n" + "      , addr.province province\n"
			+ "        FROM pref_per_cust cust\n" + "                                LEFT JOIN public.hdpc_customer_extn cust_extn\n"
			+ "                                                ON cust.customer_id = cust_extn.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_address cust_addr\n"
			+ "                                                ON cust.customer_id = cust_addr.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_address addr\n"
			+ "                                                ON cust_addr.address_id = addr.address_id;\n";

	public static final String SQL_INSERT_STG_PREFERENCE_OUTBOUND = "INSERT INTO public.hdpc_out_daily_compliant(\n"
			+ "\temail_addr, \n" + "\tcan_ptc_effective_date,\n" + "\tcan_ptc_source_id,\n" + "\temail_status, \n"
			+ "\tcan_ptc_flag, \n" + "\tlanguage_preference,\n" + "\tearly_opt_in_date, \n" + "\tcnd_compliant_flag, \n"
			+ "\thd_ca_flag, \n" + "\thd_ca_garden_club_flag,\n" + "\thd_ca_pro_flag, \n" + "\tpostal_cd, \n" + "\tcustomer_nbr,\n"
			+ "\tphone_ptc_flag, \n" + "\tdncl_suppression_flag, \n" + "\tphone_number, \n" + "\tfirst_name, \n" + "\tlast_name, \n"
			+ "\tbusiness_name, \n" + "\tindustry_code, city, \n" + "\tprovince, \n" + "\thd_ca_pro_src_id)\n" + "\tVALUES (\n"
			+ "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n"
			+ "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?,\n" + "?\n" + ")";

	public static final String SQL_SELECT_OUTBOUND_DB_READER_STEP2 = "SELECT email_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_pro_flag, postal_cd, customer_nbr, phone_ptc_flag, dncl_suppression_flag, phone_number, first_name, last_name, business_name, industry_code, city, province, hd_ca_pro_src_id\n"
			+ "\tFROM public.hdpc_out_daily_compliant";

	public static final String SQL_TRUNCATE_COMPLIANT_TABLE = "TRUNCATE TABLE public.hdpc_out_daily_compliant";

	/**
	 * For Citi Suppression
	 */

	public static final String SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1 = "WITH\n\tphone_home AS\n\t(\n"
			+ "\t\tSELECT master_tb.master_id\n" + "\t\t\tFROM  hdpc_master master_tb\n" + "\t\t\tJOIN hdpc_master_key masterkey \n"
			+ "\t\t\t\tON masterkey.key_id = master_tb.key_id\n" + "\t\t\tWHERE \tmaster_tb.value_val \t= 'home' \n"
			+ "\t\t\t\tAND masterkey.key_value ='PHONE_TYPE'\n" + "\t),\n" + "\tphone_cellphone AS\n" + "\t(\n"
			+ "\t\tSELECT master_tb.master_id\n" + "\t\t\tFROM  hdpc_master master_tb\n" + "\t\t\tJOIN hdpc_master_key masterkey \n"
			+ "\t\t\t\tON masterkey.key_id = master_tb.key_id\n" + "\t\t\tWHERE \tmaster_tb.value_val \t= 'cellphone' \n"
			+ "\t\t\t\tAND masterkey.key_value ='PHONE_TYPE'\n" + "\t)\n" + "\n" + "SELECT \n" + "cust.first_name as FIRST_NAME, \n"
			+ "\tcust.middle_name as MIDDLE_INITIAL,\n" + "\tcust.last_name as LAST_NAME, \n"
			+ "\taddr.address_line_1 as addr_line_1,\n" + "\taddr.address_line_2 as addr_line_2, \n" + "\taddr.city as city, \n"
			+ "\taddr.province as state_cd, \n" + "\taddr.postal_code as postal_cd, \n" + "\temail.email as email_addr, \n"
			+ "\tphone.phone as phone,\n" + "\tsms_mobile.SMS_Mobile_Phone,\n" + "\tcust_extn.org_name as business_name,\n"
			+ "\tCASE\n\t\tWHEN cust_addr.permission_val is null then 'U'\n" + "\t\tWHEN cust_addr.permission_val = true then 'Y'\n"
			+ "\t\tWHEN cust_addr.permission_val = false then 'N'\n" + "\tEND as DM_Opt_Out,\n\tCASE\n"
			+ "\t\tWHEN cust_email.permission_val is null then 'U'\n" + "\t\tWHEN cust_email.permission_val = true then 'Y'\n"
			+ "\t\tWHEN cust_email.permission_val = false then 'N'\n" + "\tEND as Email_Opt_Out,\n"
			+ "\tCASE\n\t\tWHEN Phone_Opt_Out is null then 'U'\n" + "\t\tWHEN Phone_Opt_Out = true then 'Y'\n"
			+ "\t\tWHEN Phone_Opt_Out = false then 'N'\n" + "\tEND as Phone_Opt_Out,\n"
			+ "\tCASE\n\t\tWHEN sms_opt_out is null then 'U'\n" + "\t\tWHEN sms_opt_out = true then 'Y'\n"
			+ "\t\tWHEN sms_opt_out = false then 'N'\n" + "\tEND as  sms_opt_out\n" + "FROM hdpc_customer cust\n"
			+ "LEFT JOIN hdpc_customer_email cust_email\n" + "\tON cust.customer_id = cust_email.customer_id\n"
			+ "LEFT JOIN hdpc_email email\n" + "\tON cust_email.email_id = email.email_id\n" + "LEFT JOIN (\n"
			+ "\t\tSELECT custphone.customer_id\n" + "\t\t\t, hdpc_phone.phone_id\n" + "\t\t\t, phone_number as phone\n"
			+ "\t\t\t, call_permission as Phone_Opt_Out\n" + "\t\tFROM hdpc_phone\n" + "\t\tJOIN hdpc_customer_phone custphone \n"
			+ "\t\t\tON custphone.phone_id = hdpc_phone.phone_id\n" + "\t\tJOIN phone_home master_tb \n"
			+ "\t\t\tON master_tb.master_id = hdpc_phone.phone_type\n" + "\t\tWHERE custphone.active\n" + "\t\t) as phone \n"
			+ "\tON phone.customer_id = cust.customer_id\n" + "LEFT JOIN (\n" + "\t\tSELECT  custphone.customer_id\n"
			+ "\t\t\t, hdpc_phone.phone_id as sms_id\n" + "\t\t\t, phone_number as SMS_Mobile_Phone\n"
			+ "\t\t\t, text_permission as sms_opt_out \n" + "\t\tFROM hdpc_phone \n" + "\t\tJOIN hdpc_customer_phone custphone \n"
			+ "\t\t\tON custphone.phone_id = hdpc_phone.phone_id\n" + "\t\tJOIN phone_cellphone master_tb \n"
			+ "\t\t\tON master_tb.master_id = hdpc_phone.phone_type\n" + "\t\tWHERE  custphone.active\n" + "\t\t) as sms_mobile \n"
			+ "\tON sms_mobile.customer_id = cust.customer_id\n" + "LEFT JOIN hdpc_customer_extn cust_extn\n"
			+ "\tON cust.customer_id = cust_extn.customer_id\n" + "LEFT JOIN hdpc_customer_address cust_addr\n"
			+ "\tON cust.customer_id = cust_addr.customer_id\n" + "LEFT JOIN hdpc_address addr\n"
			+ "\tON cust_addr.address_id = addr.address_id\n" + "GROUP BY first_name\n" + "\t\t, MIDDLE_INITIAL\n"
			+ "\t\t, last_name\n" + "\t\t, addr_line_1\n" + "\t\t, addr_line_2\n" + "\t\t, city\n" + "\t\t, state_cd\n"
			+ "\t\t, postal_cd\n" + "\t\t, email_addr\n" + "\t\t, phone\n" + "\t\t, sms_mobile_phone\n" + "\t\t, business_name\n"
			+ "\t\t, Phone_Opt_Out\n" + "\t\t, sms_Opt_Out\n" + "\t\t, DM_Opt_Out\n" + "\t\t, Email_Opt_Out, org_name \n" + ";\n";

	public static final String SQL_SELECT_CITI_SUPPRESION_TABLE = "SELECT first_name, middle_initial, last_name, addr_line_1, addr_line_2, city, state_cd, postal_cd, email_addr, phone, sms_mobile_phone, business_name, dm_opt_out, email_opt_out, phone_opt_out, sms_opt_out\n"
			+ "\tFROM public.hdpc_out_citi_suppresion;";

	public static final String SQL_INSERT_CITI_SUPPRESION = "INSERT INTO public.hdpc_out_citi_suppresion(\n"
			+ "\tfirst_name, middle_initial, last_name, addr_line_1, addr_line_2, city, state_cd, postal_cd, email_addr, phone, sms_mobile_phone, business_name, dm_opt_out, email_opt_out, phone_opt_out, sms_opt_out)\n"
			+ "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String SQL_TRUNCATE_CITI_SUPPRESION = "TRUNCATE TABLE public.hdpc_out_citi_suppresion";

	public static final String SQL_GET_EMAIL_PREFERENCES_OUTBOUND = "with email_status\n" + "AS\n" + "(\n"
			+ "    SELECT email_stat.master_id\n" + "        , id_rel.old_id\n" + "        FROM public.hdpc_master email_stat\n"
			+ "        JOIN public.hdpc_master_key master_key\n            ON email_stat.key_id = master_key.key_id\n"
			+ "        JOIN public.hdpc_master_id_rel id_rel\n" + "            ON email_stat.master_id = id_rel.pcam_id\n"
			+ "            AND id_rel.type = 'email_status_id'\n" + "        WHERE master_key.key_value = 'EMAIL_STATUS'\n" + ")\n"
			+ ", source_id \n" + "AS\n" + "(\n" + "    SELECT email_src.master_id\n" + "        , id_rel.old_id\n"
			+ "        FROM public.hdpc_master email_src\n        JOIN public.hdpc_master_key master_key\n"
			+ "            ON email_src.key_id = master_key.key_id\n" + "        JOIN public.hdpc_master_id_rel id_rel\n"
			+ "            ON email_src.master_id = id_rel.pcam_id\n" + "            AND id_rel.type = 'source_id'\n"
			+ "        WHERE master_key.key_value = 'SOURCE_ID'\n" + ")\n" + ", earliest_opt_in_date AS\n" + "(\n"
			+ "    select email.email_id\n" + "        , MIN(COALESCE(hist.effective_date, email.effective_date)) earliest_date\n"
			+ "        from public.hdpc_email email\n" + "        left join public.hdpc_email_hist hist\n"
			+ "            on email.email_id = hist.email_id\n" + "    group by email.email_id\n" + ")\n" + ", preference_type AS\n"
			+ "(\n" + "    SELECT pref.master_id\n" + "        , pref.value_val pref_val\n"
			+ "        FROM public.hdpc_master pref\n        JOIN public.hdpc_master_key master_key\n"
			+ "            ON pref.key_id = master_key.key_id\n" + "        where master_key.key_value = 'PREFERENCE_FLAG'\n" + ")\n"
			+ ", phone_type AS\n" + "(\n" + "    SELECT mast.master_id\n" + "        FROM public.hdpc_master mast\n"
			+ "        JOIN public.hdpc_master_key master_key\n            ON mast.key_id = master_key.key_id\n"
			+ "        WHERE master_key.key_value = 'PHONE_TYPE'\n" + "            AND mast.value_val = 'home'\n" + ")\n"
			+ ", customer_phone AS\n" + "(\n" + "    select cust_phone.customer_id\n" + "        , cust_phone.call_permission\n"
			+ "        , phone.phone_number\n" + "        from public.hdpc_customer_phone cust_phone\n"
			+ "        join public.hdpc_phone phone\n" + "            on cust_phone.phone_id = phone.phone_id\n"
			+ "        join phone_type\n" + "            on phone.phone_type = phone_type.master_id\n" + ")\n" + "\n"
			+ "select email.email email_address\n" + "    , cust_email.effective_date as_of_date\n"
			+ "    , source_id.old_id source_id\n"
			+ "    , CASE WHEN email_stat.old_id = 0 THEN '00' ELSE CAST(email_stat.old_id AS TEXT) END email_status\n"
			+ "    , CASE cust_email.permission_val\n" + "        WHEN TRUE   THEN 'Y'\n" + "        WHEN FALSE  THEN 'N'\n"
			+ "        ELSE 'U' \n" + "        END email_ptc\n" + "    , cust.language_pref language_preference\n"
			+ "    , earliest_opt_in_date.earliest_date earliest_opt_in_date\n" + "    , CASE \n"
			+ "        WHEN    cust_email.permission_val\n" + "            AND email_stat.old_id in (0,51)\n" + "        THEN 'Y'\n"
			+ "        ELSE 'N'\n" + "        END hd_canada_email_compliant_flag\n" + "    , MAX( CASE \n"
			+ "            WHEN pref_type.pref_val = 'hd_ca' \n" + "             THEN  \n"
			+ "                   CASE WHEN pref.permission_val THEN 'Y'\n"
			+ "                         WHEN pref.permission_val = FALSE THEN 'N'\n                        ELSE 'U'\n"
			+ "                    END\n            ELSE NULL\n            END\n        ) HD_Canada_Flag\n" + "    , MAX( CASE \n"
			+ "            WHEN pref_type.pref_val = 'garden_club' \n" + "              THEN  \n"
			+ "                  CASE WHEN pref.permission_val THEN 'Y'\n"
			+ "                        WHEN pref.permission_val = FALSE THEN 'N'\n" + "                       ELSE 'U'\n"
			+ "                    END\n            ELSE NULL\n           END\n        ) garden_club_flag\n"
			+ "                , MAX( CASE \n" + "            WHEN pref_type.pref_val = 'new_mover' \n" + "            THEN  \n"
			+ "                 CASE WHEN pref.permission_val THEN 'Y'\n"
			+ "                      WHEN pref.permission_val = FALSE THEN 'N'\n" + "                      ELSE 'U'\n"
			+ "                    END\n            ELSE NULL\n           END\n        ) new_mover_flag\n"
			+ "                , MAX( CASE \n" + "            WHEN pref_type.pref_val = 'Pro' \n" + "           THEN  \n"
			+ "                    CASE WHEN pref.permission_val THEN 'Y' \n"
			+ "                       WHEN pref.permission_val = FALSE THEN 'N'\n" + "                    ELSE 'U'\n"
			+ "                    END\n            ELSE NULL\n            END\n        ) pro_flag\n"
			+ "                , CASE WHEN customer_phone.call_permission THEN 'Y' ELSE 'N' END phone_ptc_flag\n"
			+ "                , cust.first_name\n" + "                , cust.last_name\n" + "                , addr.postal_code\n"
			+ "\t\t\t\t, addr.province\n" + "                , addr.city            \n"
			+ "                , customer_phone.phone_number\n" + "                , cust_extn.org_name business_name\n"
			+ "                , cust_extn.business_type business_type\n" + "                , cust_extn.move_date\n"
			+ "                , cust_extn.dwelling_type\n" + "    from public.hdpc_email email\n"
			+ "    join earliest_opt_in_date\n" + "        on email.email_id = earliest_opt_in_date.email_id\n"
			+ "    left join public.hdpc_customer_email cust_email\n" + "       on email.email_id = cust_email.email_id\n"
			+ "    left join public.hdpc_customer cust\n" + "        on cust_email.customer_id = cust.customer_id\n"
			+ "    left join public.hdpc_customer_preference pref\n" + "        on cust.customer_id = pref.customer_id\n"
			+ "    left join public.hdpc_customer_address cust_addr\n" + "        on cust.customer_id = cust_addr.customer_id\n"
			+ "    left join public.hdpc_address addr\n" + "        on cust_addr.address_id = addr.address_id\n"
			+ "    left join public.hdpc_customer_extn cust_extn\n" + "        on cust.customer_id = cust_extn.customer_id\n"
			+ "    left join customer_phone\n" + "        on cust.customer_id = customer_phone.customer_id\n"
			+ "    left join preference_type pref_type\n" + "        on pref.preference_type = pref_type.master_id\n"
			+ "    left join email_status email_stat\n" + "        on email.status_id = email_stat.master_id\n"
			+ "    left join source_id\n" + "        on email.source_type = source_id.master_id\n" + "    group by email.email\n"
			+ "            , cust_email.effective_date \n" + "            , source_id.old_id \n"
			+ "            , CASE WHEN email_stat.old_id = 0 THEN '00' ELSE CAST(email_stat.old_id AS TEXT) END\n"
			+ "            , CASE cust_email.permission_val\n" + "                WHEN TRUE   THEN 'Y'\n"
			+ "                WHEN FALSE  THEN 'N'\n" + "                ELSE 'U' \n" + "                END \n"
			+ "            , cust.language_pref \n" + "            , earliest_opt_in_date.earliest_date\n" + "            , CASE \n"
			+ "                WHEN    cust_email.permission_val\n" + "                    AND email_stat.old_id in (0,51)\n"
			+ "                THEN 'Y'\n" + "                ELSE 'N'\n" + "                END\n"
			+ "            , customer_phone.call_permission\n" + "            , cust.first_name\n" + "            , cust.last_name\n"
			+ "            , addr.postal_code\n" + "            , addr.province\n" + "            , addr.city            \n"
			+ "            , customer_phone.phone_number\n" + "            , cust_extn.org_name \n"
			+ "            , cust_extn.business_type \n" + "            , cust_extn.move_date\n"
			+ "            , cust_extn.dwelling_type;\n";

	public static final String SQL_SELECT_SALESFORCE_EXTRACT_TABLE = "SELECT email_address, as_of_date, source_id, email_status, email_ptc, language_preference, earliest_opt_in_date, hd_canada_email_compliant_flag, hd_canada_flag, garden_club_flag, new_mover_flag, pro_flag, phone_ptc_flag, first_name, last_name, postal_code, province, city, phone_number, business_name, business_type, move_date, dwelling_type\n"
			+ "\tFROM public.hdpc_out_salesforce_extract;";

	public static final String SQL_INSERT_SALESFORCE_EXTRACT = "INSERT INTO public.hdpc_out_salesforce_extract(\n"
			+ "\temail_address, as_of_date, source_id, email_status, email_ptc, language_preference, earliest_opt_in_date, hd_canada_email_compliant_flag, hd_canada_flag, garden_club_flag, new_mover_flag, pro_flag, phone_ptc_flag, first_name, last_name, postal_code, province, city, phone_number, business_name, business_type, move_date, dwelling_type)\n"
			+ "\tVALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

	public static final String SQL_TRUNCATE_SALESFORCE_EXTRACT = "TRUNCATE TABLE public.hdpc_out_salesforce_extract";

	public static final String SQL_SELECT_FOR_INTERNAL_DESTINATION = "WITH\n"
			+ "    cust_with_pref AS -- getting customers that must contain \"pro\" value active as preference flag\n    (\n"
			+ "        SELECT pref.customer_id\n" + "            FROM  public.hdpc_customer_preference pref \n"
			+ "            JOIN public.hdpc_master mast\n" + "                ON pref.preference_type = mast.master_id\n"
			+ "            JOIN public.hdpc_master_key mast_key\n" + "                ON mast.key_id = mast_key.key_id\n"
			+ "            WHERE mast_key.key_value = 'PREFERENCE_FLAG'\n" + "                --AND mast.value_val = 'Pro'\n"
			+ "                --AND pref.permission_val\n" + "    ),\n" + "    min_opt_in_date AS\n    (\n"
			+ "        SELECT  customer_id\n" + "              , MIN(opt_in_date) opt_in_date\n"
			+ "            FROM  public.hdpc_customer_pref_hist\n" + "            WHERE permission_val\n"
			+ "        GROUP BY customer_id \n" + "    ) ,\n" + "    pref_per_cust AS\n    (\n"
			+ "        SELECT    cust.customer_id\n" + "                , cust.first_name first_name\n"
			+ "                , cust.last_name last_name\n" + "                , email.email\n"
			+ "                , cust_email.effective_date    effective_date\n"
			+ "                                                                , cust_email.effective_date early_opt_in_date\n"
			+ "                , email.source_type source_id\n" + "                , rel_id.old_id    email_status\n"
			+ "                                                , CASE\n"
			+ "                 WHEN cust_email.permission_val is null then 'U'  \n"
			+ "                      WHEN cust_email.permission_val = true then 'Y'\n"
			+ "                 WHEN cust_email.permission_val = false then 'N' end email_permission\n"
			+ "                               , cust.language_pref language_preference\n"
			+ "            , CASE WHEN cust_email.permission_val\n                    AND email.status_id <> 0 \n"
			+ "                        then 'Y' \n" + "                    else 'N' \n"
			+ "                    end cnd_compliant_flag\n" + "            , MAX(CASE WHEN mast.value_val = 'hd_ca'\n"
			+ "                   AND pref.permission_val \n" + "                        then 'Y'\n"
			+ "                    else 'N' end) email_pref_hd_ca\n" + "            , MAX(CASE WHEN mast.value_val = 'garden_club'\n"
			+ "                  AND pref.permission_val \n" + "                     then 'Y' \n"
			+ "                    else 'N' end) email_pref_garden_club\n" + "            , MAX(CASE WHEN mast.value_val = 'Pro'\n"
			+ "                 AND pref.permission_val \n" + "                      then 'Y' \n"
			+ "                    else 'N' end) email_pref_pro\n" + "\t\t\t, MAX(CASE WHEN mast.value_val = 'new_mover'\n"
			+ "             AND pref.permission_val \n                       then 'Y' \n"
			+ "                    else 'N' end) email_pref_new_mover\n"
			+ "                                                , MAX(CASE WHEN mast.value_val = 'Pro' THEN pref.source_type else 0 end) hd_ca_pro_src_id\n"
			+ "            , MIN(COALESCE(min_opt.opt_in_date, pref.opt_in_date))\n"
			+ "                                                , MAX(CASE WHEN phone.phone_type = 10 THEN phone.phone_number ELSE '' END) phone_number\n"
			+ "                                                , MAX(CASE WHEN phone.phone_type = 12 THEN phone.phone_number ELSE '' END) cell_number\n"
			+ "                                                , MAX(CASE WHEN cust_phone.text_permission AND phone.phone_type = 12 THEN 'Y' \n"
			+ "                                                                                                WHEN cust_phone.text_permission = FALSE AND phone.phone_type = 12 THEN 'N'\n"
			+ "                                                                                                WHEN cust_phone.text_permission IS NULL AND phone.phone_type = 12 THEN 'U' END\n"
			+ "                  ) phone_ptc_flag\n"
			+ "                                                , MAX(CASE WHEN cust_phone.call_permission THEN 'Y' \n"
			+ "                                                                                                WHEN cust_phone.call_permission = FALSE THEN 'N'\n"
			+ "                                                                                                WHEN cust_phone.call_permission IS NULL THEN 'U' END\n"
			+ "                  ) dncl_suppresion\n" + "        FROM public.hdpc_customer cust\n"
			+ "        JOIN cust_with_pref  preference\n" + "            ON cust.customer_id = preference.customer_id\n"
			+ "        JOIN public.hdpc_customer_preference pref\n" + "            ON cust.customer_id = pref.customer_id\n"
			+ "        JOIN public.hdpc_master mast\n" + "            ON pref.preference_type = mast.master_id\n"
			+ "        JOIN public.hdpc_master_key mast_key\n" + "            ON mast.key_id = mast_key.key_id\n"
			+ "        LEFT JOIN min_opt_in_date min_opt\n" + "            ON cust.customer_id = min_opt.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_email cust_email\n"
			+ "                                                ON cust_email.customer_id = cust.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_email email\n"
			+ "                                                ON email.email_id = cust_email.email_id\n"
			+ "                                LEFT JOIN public.hdpc_master_id_rel rel_id\n"
			+ "                                                ON rel_id.pcam_id = email.status_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_phone cust_phone\n"
			+ "                                                ON cust.customer_id = cust_phone.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_phone phone\n"
			+ "                                                                                                ON cust_phone.phone_id = phone.phone_id                                                                                                                                 \n"
			+ "        WHERE mast_key.key_value = 'PREFERENCE_FLAG' \n" + "    GROUP BY cust.customer_id\n"
			+ "                                                , email.email\n"
			+ "                                                , cust_email.effective_date\n"
			+ "                                                , email.source_type \n"
			+ "                                                , email.status_id  \n"
			+ "                                                , cust.first_name \n"
			+ "                               , cust.last_name \n"
			+ "                                                                                                \n"
			+ "            , CASE\n" + "                 WHEN cust_email.permission_val is null then 'U'  \n"
			+ "                  WHEN cust_email.permission_val = true then 'Y'\n"
			+ "                 WHEN cust_email.permission_val = false then 'N' end \n"
			+ "                               , cust.language_pref \n            , CASE WHEN cust_email.permission_val\n"
			+ "                    AND email.status_id <> 0 \n" + "                        then 'Y' \n"
			+ "                    else 'N' \n" + "                    end\n"
			+ "                                                , rel_id.old_id\n" + "    )\n" + "    SELECT cust.*\n"
			+ "      , cust_extn.org_name as business_name\n" + "      , cust_extn.industry_code industry_code\n"
			+ "      , cust_extn.org_name business_name\n" + "      , cust_extn.industry_code industry_code\n"
			+ "                  , cust_extn.customer_nbr customer_nbr\n" + "\t\t\t\t  , cust_extn.move_date move_date\n"
			+ "\t\t\t\t  , cust_extn.dwelling_type dwelling_type\n" + "      , addr.city city\n"
			+ "                  , addr.postal_code src_postal_code\n" + "      , addr.province province\n"
			+ "        FROM pref_per_cust cust\n" + "                                LEFT JOIN public.hdpc_customer_extn cust_extn\n"
			+ "                                                ON cust.customer_id = cust_extn.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_customer_address cust_addr\n"
			+ "                                                ON cust.customer_id = cust_addr.customer_id\n"
			+ "                                LEFT JOIN public.hdpc_address addr\n"
			+ "                                                ON cust_addr.address_id = addr.address_id;\n";

	public static final String SQL_TRUNCATE_PROGRAM_COMPLIANT = "TRUNCATE TABLE public.hdpc_out_program_compliant";

	public static final String SQL_INSERT_PROGRAM_COMPLIANT = "INSERT INTO public.hdpc_out_program_compliant(\n"
			+ "\temail_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_new_mover_flag, hd_ca_new_mover_eff_date, hd_ca_pro_flag, phone_ptc_flag, first_name, last_name, postal_cd, province, city, phone_number, business_name, industry_code, dwelling_type, move_date)\n"
			+ "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String SQL_SELECT_PROGRAM_COMPLIANT = "SELECT email_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_new_mover_flag, hd_ca_new_mover_eff_date, hd_ca_pro_flag, phone_ptc_flag, first_name, last_name, postal_cd, province, city, phone_number, business_name, industry_code, dwelling_type, move_date\n"
			+ "\tFROM public.hdpc_out_program_compliant;";

	/**
	 * Loyalty complaint queries
	 */

	public static final String SQL_INSERT_LOYALTY_COMPLAINT = "INSERT INTO public.hdpc_out_loyalty_compliant(\n"
			+ "\temail_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, first_name, last_name, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_pro_flag, postal_cd, city, customer_nbr, province)\n"
			+ "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String SQL_SELECT_LOYALTY_COMPLAINT = "SELECT email_addr, can_ptc_effective_date, can_ptc_source_id, email_status, can_ptc_flag, first_name, last_name, language_preference, early_opt_in_date, cnd_compliant_flag, hd_ca_flag, hd_ca_garden_club_flag, hd_ca_pro_flag, postal_cd, city, customer_nbr, province\n"
			+ "\tFROM public.hdpc_out_loyalty_compliant;";
	public static final String SQL_TRUNCATE_LOYALTY_COMPLIANT_TABLE = "TRUNCATE TABLE public.hdpc_out_loyalty_compliant";

	public static final String SQL_TRUNCATE_FLEX_ATTRIBUTE = "TRUNCATE TABLE public.hdpc_out_flex_attributes";
	public static final String SQL_INSERT_FLEX_ATTRIBUTE = "INSERT INTO hdpc_out_flex_attributes(\n"
			+ "\tfile_id, sequence_nbr, email_addr, hd_hh_id, hd_ind_id, customer_nbr, store_nbr, org_name, company_cd, cust_type_cd, source_id, effective_date, last_update_date, industry_code, company_name, contact_first_name, contact_last_name, contact_role)\n"
			+ "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public static final String SQL_SELECT_FOR_FLEX_ATTRIBUTES_INTERNAL_DESTINATION = "select MAX(hfc.file_id) file_id\n"
			+ "     , hfc.sequence_nbr, he.email email_addr, ha.address_id hd_hh_id\n"
			+ "     , c.customer_id hd_ind_id, cx.customer_nbr, cx.store_nbr, cx.org_name\n"
			+ "     , cx.industry_code company_cd, cx.cust_type cust_type_cd, c.source_type source_id\n"
			+ "     , ce.updated_date last_update_date, cx.industry_code, cx.org_name company_name\n"
			+ "     , c.first_name contact_first_name, c.last_name contact_last_name\n" + "     , '' contact_role\n"
			+ "     , max(ce.effective_date) effective_date from hdpc_customer c  join hdpc_customer_email ce\n"
			+ "              on c.customer_id = ce.customer_id AND ce.active\n"
			+ "         left join hdpc_email he on ce.email_id = he.email_id\n"
			+ "         left join hdpc_customer_address ca on c.customer_id = ca.customer_id\n"
			+ "         left join hdpc_address ha on ha.address_id = c.def_address_id\n"
			+ "         left join hdpc_customer_extn cx on c.customer_id = cx.customer_id\n"
			+ "         left join hdpc_file_customer hfc on c.customer_id = hfc.customer_id\n"
			+ " group by hfc.sequence_nbr, he.email, ha.address_id, c.customer_id\n"
			+ "       , cx.customer_nbr, cx.store_nbr, cx.org_name, cx.industry_code\n"
			+ "       , cx.cust_type, c.source_type, ce.updated_date, cx.industry_code\n"
			+ "       , cx.org_name, c.first_name, c.last_name;";

	public static final String SQL_SELECT_FLEX_ATTRIBUTES = "SELECT file_id, sequence_nbr, email_addr, hd_hh_id, hd_ind_id, "
			+ "customer_nbr, store_nbr, org_name, company_cd, cust_type_cd, source_id, effective_date, last_update_date, "
			+ "industry_code, company_name, contact_first_name, contact_last_name, contact_role "
			+ "FROM public.hdpc_out_flex_attributes;";


}

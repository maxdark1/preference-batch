package ca.homedepot.preference.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportsQueries
{

	public static String DAILY_COUNT_REPORT_EMAIL_PREFERENCES_QUERY = "\n" +
			"SELECT 'Most recent files' as \"1\",'' as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\"\n" +
			"UNION ALL\n" +
			"SELECT 'Filename(Inbounds)' as \"1\",'# of records' as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\"\n" +
			"UNION ALL\n" +
			"SELECT file_name as \"1\", quantity_loaded as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\" FROM public.hdpc_email_notification\n" +
			"WHERE (type_file like '%INBOUND%' OR type_file like '%inbound%')\n" +
			"\t\tAND CAST(inserted_date as DATE) >= CAST(now() - '7 day'::INTERVAL as DATE) \n" +
			"UNION ALL\n" +
			"SELECT 'Filename(Outbounds)' as \"1\",'# of records' as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\"\n" +
			"UNION ALL\n" +
			"SELECT file_name as \"1\", quantity_loaded as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\" FROM public.hdpc_email_notification\n" +
			"WHERE (type_file like '%OUTBOUND%' OR type_file like '%outbound%')\n" +
			"       AND CAST(inserted_date as DATE) >= CAST(now() - '7 day'::INTERVAL as DATE) \n" +
			"UNION ALL"
			+ "SELECT *, '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\" FROM (\n"
			+ "\t(SELECT 'SOURCE FILE EMAILS LOADED' as \"1\", ' ' as \"2\")\n" + "\tUNION ALL\n"
			+ "\t(SELECT to_char(CAST(custem.updated_date as date), 'YYYYMMDD') as \"1\" , CAST(COUNT(*) as VARCHAR)  as \"2\" \n"
			+ "\tFROM hdpc_customer customer\n"
			+ "\tLEFT JOIN hdpc_customer_email custem ON customer.customer_id = custem.customer_id\n"
			+ "\tJOIN hdpc_email email ON email.email_id = custem.email_id\n"
			+ "\tWHERE CAST(custem.updated_date as DATE) >= CAST(now() - '7 day'::INTERVAL as DATE) \n"
			+ "\tgroup by to_char(CAST(custem.updated_date as date), 'YYYYMMDD')\n"
			+ "\torder by to_char(CAST(custem.updated_date as date), 'YYYYMMDD') desc) \n" + ") t\n" + "UNION ALL\n"
			+ "SELECT '' as \"1\", '' as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\"\n"
			+ "UNION ALL\n" + "SELECT * FROM (\n"
			+ "\t(SELECT 'SOURCE FILE PREFERENCES LOADED' as \"1\", 'HD.CA Y' as \"2\", 'HD.CA N' as \"3\", 'HD_CA_GARDEN_CLUB Y' as \"4\",\t\n"
			+ "\t 'HD_CA_GARDEN_CLUB N' as \"5\", 'HD_CA_NEW_MOVER Y' as \"6\",'HD_CA_NEW_MOVER N' as \"7\",'HD_CA_PR Y' as \"8\", 'HD_CA_PR N' as \"9\")\n"
			+ "\tUNION ALL\n"
			+ "\t(SELECT to_char(CAST(cp.updated_date as date), 'YYYYMMDD') AS \"SOURCE FILE PREFERENCES LOADED\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 6 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD.CA Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 6 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD.CA N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 7 AND permission_val = true)THEN 1 END) as VARCHAR) AS \"HD_CA_GARDEN_CLUB Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 7 AND permission_val = false) THEN 1 END)as VARCHAR) AS \"HD_CA_GARDEN_CLUB N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 9 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD_CA_NEW_MOVER Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 9 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD_CA_NEW_MOVER N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 8 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD_CA_PR Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 8 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD_CA_PR N\"\n"
			+ "\t   from hdpc_customer_preference cp\n"
			+ "\tWHERE CAST(cp.updated_date as DATE) >= CAST(now() - '7 day'::INTERVAL as DATE)\n"
			+ "\tgroup by CAST(cp.updated_date as date)\n" + "\torder by CAST(cp.updated_date as date) desc)\n" + ") t2";

	public static String DAILY_COUNT_REPORT_OVERALL_PREFERENCES = "WITH dates as (\n"
			+ "\tSELECT DISTINCT CAST(custp.opt_in_date as DATE) count_date\n" + "\tFROM hdpc_customer_preference custp\n"
			+ "\tWHERE CAST(custp.opt_in_date as date) >= CAST(now() - '7 day'::INTERVAL as DATE)\n" + ")\t\n" + ",sum_pref as \n"
			+ "(SELECT CAST(custp.opt_in_date as DATE) sum_date, preference_type,  \n"
			+ "\t  SUM(CASE WHEN(permission_val = true) THEN 1 ELSE 0 END) as Y,\n"
			+ "      SUM(CASE WHEN(permission_val = false) THEN 1 ELSE 0 END) as N, \n"
			+ "\t  SUM(CASE WHEN(permission_val is NULL) THEN 1 ELSE 0 END) as U\n" + "\tFROM hdpc_customer_preference custp\n"
			+ "\tGROUP BY preference_type, CAST(custp.opt_in_date as DATE)\n" + "ORDER BY CAST(custp.opt_in_date as DATE) DESC),\n"
			+ "sum_thd_compliant as \n" + "(SELECT CAST(custem.effective_date as DATE) as compliant_date,\n"
			+ " COUNT(CASE WHEN custem.email_id IS NOT NULL AND (custem.permission_val OR old_id = 0) THEN 1 END) as THD_Y,\n"
			+ " COUNT(CASE WHEN custem.email_id IS NOT NULL AND (custem.permission_val = false OR old_id != 0) THEN 1 END) as THD_N,\n"
			+ " COUNT(CASE WHEN custem.permission_val IS NULL THEN 1 END) as THD_U\n" + " FROM hdpc_customer custom\n"
			+ "LEFT JOIN hdpc_customer_email custem ON custem.customer_id = custom.customer_id\n"
			+ "LEFT JOIN hdpc_email email ON email.email_id = custem.email_id\n"
			+ "LEFT JOIN hdpc_master_id_rel idrel ON idrel.pcam_id = email.status_id\n"
			+ "GROUP BY CAST(custem.effective_date as DATE) )\n" + "SELECT to_char(d.count_date, 'YYYYMMDD') as count_date,\n"
			+ "\t   SUM(compliant.THD_Y) as \"THD_Y\",\n"
			+ "\t   SUM(compliant.THD_Y) - lag(SUM(compliant.THD_Y)) OVER (ORDER BY d.count_date) as \"DIFF_THD_Y\",\n"
			+ "\t   SUM(compliant.THD_N) as \"THD_N\",\n"
			+ "\t   SUM(compliant.THD_N) - lag(SUM(compliant.THD_N)) OVER (ORDER BY d.count_date) as \"DIFF_THD_N\",\n"
			+ "\t   SUM(compliant.THD_U)  as \"THD_U\",\n"
			+ "\t   SUM(compliant.THD_U)  - lag(SUM(compliant.THD_U)) OVER (ORDER BY d.count_date) as \"DIFF_THD_U\",\n" + "\n"
			+ "\n" + "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.Y END) AS \"GC_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.Y END) - lag(SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.Y END)) OVER (ORDER BY d.count_date) as \"DIFF_GC_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.N END) AS \"GC_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.N END) - lag(SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.N END)) OVER (ORDER BY d.count_date) as \"DIFF_GC_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.U END) AS \"GC_U\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.U END) - lag(SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.U END)) OVER (ORDER BY d.count_date) as \"DIFF_GC_U\",\n"
			+ "\n" + "       SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.Y END) AS \"NM_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.Y END) - lag(SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.Y END)) OVER (ORDER BY d.count_date) as \"DIFF_NM_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.N END) AS \"NM_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.N END) - lag(SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.N END)) OVER (ORDER BY d.count_date) as \"DIFF_NM_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.U END) AS \"NM_U\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.U END) - lag(SUM(CASE WHEN(custp2.preference_type = 9) THEN custp2.U END)) OVER (ORDER BY d.count_date) as \"DIFF_NM_U\",\n"
			+ "\t  \n" + "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.Y END) AS \"PRO_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.Y END) - lag(SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.Y END)) OVER (ORDER BY d.count_date) as \"DIFF_PRO_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.N END) AS \"PRO_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.N END) - lag(SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.N END)) OVER (ORDER BY d.count_date) as \"DIFF_PRO_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.U END) AS \"PRO_U\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.U END) - lag(SUM(CASE WHEN(custp2.preference_type = 8) THEN custp2.U END)) OVER (ORDER BY d.count_date) as \"DIFF_PRO_U\",\n"
			+ "\n" + "\n" + "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.Y END) AS \"CA_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.Y END) - lag(SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.Y END)) OVER (ORDER BY d.count_date) as \"DIFF_CA_Y\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.N END)  AS \"CA_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.N END) - lag(SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.N END)) OVER (ORDER BY d.count_date) as \"DIFF_CA_N\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.U END) AS \"CA_U\",\n"
			+ "\t   SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.U END) - lag(SUM(CASE WHEN(custp2.preference_type = 6) THEN custp2.U END)) OVER (ORDER BY d.count_date) as \"DIFF_CA_U\"\n"
			+ "\t   \n" + "\t   \t   \n" + "FROM dates d\n" + "LEFT JOIN sum_pref custp2 ON d.count_date >= custp2.sum_date\n"
			+ "LEFT JOIN sum_thd_compliant compliant ON d.count_date >= compliant.compliant_date\n" + "group by d.count_date\n"
			+ "ORDER BY count_date DESC;\n";



}

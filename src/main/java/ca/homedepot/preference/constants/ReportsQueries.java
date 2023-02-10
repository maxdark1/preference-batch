package ca.homedepot.preference.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportsQueries
{

	public static String DAILY_COUNT_REPORT_EMAIL_PREFERENCES_QUERY = "\n"
			+ "SELECT *, '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\" FROM (\n" +
			// FILE EMAIL LOADED
			"\t(SELECT 'SOURCE FILE EMAILS LOADED' as \"1\", ' ' as \"2\")\n" + "\tUNION ALL\n"
			+ "\t(SELECT to_char(CAST(custem.inserted_date as date), 'YYYYMMDD') as \"1\" , CAST(COUNT(*) as VARCHAR)  as \"2\" \n"
			+ "\tFROM hdpc_customer customer\n" + "\tJOIN hdpc_customer_email custem ON customer.customer_id = custem.customer_id\n"
			+ "\tJOIN hdpc_email email ON email.email_id = custem.email_id\n"
			+ "\tWHERE CAST(custem.inserted_date as date) >= CAST(now() - '7 day'::INTERVAL as DATE) \n"
			+ "\tgroup by to_char(CAST(custem.inserted_date as date), 'YYYYMMDD')\n"
			+ "\torder by to_char(CAST(custem.inserted_date as date), 'YYYYMMDD') desc) \n" + ") t\n" + "UNION ALL\n"
			+ "SELECT '' as \"1\", '' as \"2\", '' as \"3\", '' as \"4\", '' as \"5\", '' as \"6\", '' as \"7\", '' as \"8\", '' as \"9\"\n"
			+ "UNION ALL\n" + "SELECT * FROM (\n" +
			// FILE PREFERENCES LOADED
			"\t(SELECT 'SOURCE FILE PREFERENCES LOADED' as \"1\", 'HD.CA Y' as \"2\", 'HD.CA N' as \"3\", 'HD_CA_GARDEN_CLUB Y' as \"4\",\t\n"
			+ "\t 'HD_CA_GARDEN_CLUB N' as \"5\", 'HD_CA_NEW_MOVER Y' as \"6\",'HD_CA_NEW_MOVER N' as \"7\",'HD_CA_PR Y' as \"8\", 'HD_CA_PR N' as \"9\")\n"
			+ "\tUNION ALL\n"
			+ "\t(SELECT to_char(CAST(cp.inserted_date as date), 'YYYYMMDD') AS \"SOURCE FILE PREFERENCES LOADED\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 6 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD.CA Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 6 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD.CA N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 7 AND permission_val = true)THEN 1 END) as VARCHAR) AS \"HD_CA_GARDEN_CLUB Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 7 AND permission_val = false) THEN 1 END)as VARCHAR) AS \"HD_CA_GARDEN_CLUB N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 9 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD_CA_NEW_MOVER Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 9 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD_CA_NEW_MOVER N\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 8 AND permission_val = true) THEN 1 END) as VARCHAR) AS \"HD_CA_PR Y\",\n"
			+ "\t   CAST(count(CASE WHEN(preference_type = 8 AND permission_val = false) THEN 1 END) as VARCHAR) AS \"HD_CA_PR N\"\n"
			+ "\t   from hdpc_customer_preference cp\n"
			+ "\tWHERE CAST(cp.inserted_date as date) >= CAST(now() - '7 day'::INTERVAL as DATE)\n"
			+ "\tgroup by CAST(cp.inserted_date as date)\n" + "\torder by CAST(cp.inserted_date as date) desc)\n" + ") t2\n";

	public static String DAILY_COUNT_REPORT_OVERALL_PREFERENCES = "WITH dates as (\n"
			+ "\tSELECT DISTINCT CAST(custp.opt_in_date as DATE) count_date\n" + "\tFROM pcam.hdpc_customer_preference custp\n"
			+ "\tWHERE  CAST(custp.inserted_date as date) >= CAST(now() - '7 day'::INTERVAL as DATE) or CAST(custp.updated_date as date) >= CAST(now() - '7 day'::INTERVAL as DATE) \n"
			+ "),\n" + "sum_pref as \n" + "(SELECT CAST(custp.opt_in_date as DATE) sum_date, preference_type,  \n"
			+ "\t  SUM(CASE WHEN(permission_val = true) THEN 1 ELSE 0 END) as Y,\n"
			+ "      SUM(CASE WHEN(permission_val = false) THEN 1 ELSE 0 END) as N, \n"
			+ "\t  SUM(CASE WHEN(permission_val is NULL) THEN 1 ELSE 0 END) as U\n" + "\tFROM pcam.hdpc_customer_preference custp\n"
			+ "\tGROUP BY preference_type, CAST(custp.opt_in_date as DATE)\n" + "ORDER BY CAST(custp.opt_in_date as DATE) DESC)\n"
			+ "SELECT to_char(d.count_date,'YYYYMMDD') as count_date,\n" + "\t   '' as \"THD_Y\",\n" + "\t   '' as \"DIFF_THD_Y\",\n"
			+ "\t   '' as \"THD_N\",\n" + "\t   '' as \"DIFF_THD_N\",\n" + "\t   '' as \"THD_U\",\n" + "\t   '' as \"DIFF_THD_U\",\n"
			+ "\n" + "\n" + "\t   SUM(CASE WHEN(custp2.preference_type = 7) THEN custp2.Y END) AS \"GC_Y\",\n"
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
			+ "group by d.count_date\n" + "ORDER BY count_date DESC;\n";



}

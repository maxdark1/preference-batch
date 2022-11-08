package ca.homedepot.preference.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SchedulerConfigConstants
{

	public static final String DIRECTORY = "directory";

	public static final String SOURCE = "source";

	public static final String JOB_STR = "job_name";
	public static final String[] CITI_SUPRESSION_NAMES = new String[]
			{ "FirstName", "MiddleInitial", "LastName", "AddrLine1", "AddrLine2", "City", "StateCd", "PostalCd", "EmailAddr", "Phone",
					"SmsMobilePhone", "BusinessName", "DmOptOut", "EmailOptOut", "PhoneOptOut", "SmsOptOut" };
	public  static final String[] LOYALTY_COMPLIANT_NAMES = new String[]
			{ "EmailAddr", "CanPtcEffectiveDate", "CanPtcSourceId", "EmailStatus", "CanPtcFlag", "FirstName", "LastName",
					"LanguagePreference", "EarlyOptInDate", "CndCompliantFlag", "HdCaFlag", "HdCaGardenClubFlag", "HdCaProFlag",
					"PostalCd", "City", "CustomerNbr", "Province" };
}

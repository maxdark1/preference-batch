package ca.homedepot.preference.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SchedulerConfigConstants
{

	public static final String DIRECTORY = "directory";

	public static final String SOURCE = "source";

	public static final String JOB_STR = "job_name";
	public static final String FLEX_ATTRIBUTE = "flex_attribute";

	private static final String FIRSTNAME = "FirstName";

	private static final String LASTNAME = "LastName";
	public static final String[] CITI_SUPRESSION_NAMES = new String[]
	{ FIRSTNAME, "MiddleInitial", LASTNAME, "AddrLine1", "AddrLine2", "City", "StateCd", "PostalCd", "EmailAddr", "Phone",
			"SmsMobilePhone", "BusinessName", "DmOptOut", "EmailOptOut", "PhoneOptOut", "SmsOptOut" };
	public static final String[] LOYALTY_COMPLIANT_NAMES = new String[]
	{ "EmailAddr", "CanPtcEffectiveDate", "CanPtcSourceId", "EmailStatus", "CanPtcFlag", FIRSTNAME, LASTNAME, "LanguagePreference",
			"EarlyOptInDate", "CndCompliantFlag", "HdCaFlag", "HdCaGardenClubFlag", "HdCaProFlag", "PostalCd", "City", "CustomerNbr",
			"Province" };

	public static final String[] SALESFORCE_EXTRACT_NAMES = new String[]
	{ "EmailAddress", "AsOfDate", "SourceId", "EmailStatus", "EmailPtc", "LanguagePreference", "EarliestOptInDate",
			"HdCanadaEmailCompliantFlag", "HdCanadaFlag", "GardenClubFlag", "NewMoverFlag", "ProFlag", "PhonePtcFlag", FIRSTNAME,
			LASTNAME, "PostalCode", "Province", "City", "PhoneNumber", "BusinessName", "BusinessType", "MoveDate", "DwellingType" };

	public static final String[] DAILY_COUNT_REPORT_STEP1_DTO = new String[]
	{ "field1", "field2", "field3", "field4", "field5", "field6", "field7", "field8", "field9" };

	public static final String[] DAILY_COUNT_REPORT_STEP2_DTO = new String[]
	{ "countDate", "thdY", "diffThdY", "thdN", "diffThdN", "thdU", "diffthdU", "gcY", "diffGCY", "gcN", "diffGCN", "gcU",
			"diffGCU", "nmY", "diffNMY", "nmN", "diffNMN", "nmU", "diffNMU", "proY", "diffPROY", "proN", "diffPRON", "proU",
			"diffPROU", "caY", "diffCAY", "caN", "diffCAN", "caU", "diffCAU" };
}

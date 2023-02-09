package ca.homedepot.preference.util.validation;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.processor.MasterProcessor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

@UtilityClass
@Slf4j
public class InboundValidator
{

	public static String validEmailPattern;

	public static final SimpleDateFormat srcDateSDF = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");

	public static final String[] FIELD_NAMES_REGISTRATION = new String[]
	{ "Language_Preference", "AsOfDate", "Email_Address", "Email_Permission", "Phone_Permission", "Phone_Number",
			"Phone_Extension", "Title", "First_Name", "Last_Name", "Address_1", "Address_2", "City", "Province", "Postal_Code",
			"Mail_Permission", "EmailPrefHDCA", "GardenClub", "EmailPrefPRO", "NewMover", "For_Future_Use", "Source_ID", "SMS_Flag",
			"Fax_Number", "Fax_Extension", "Content_1", "Value_1", "Content_2", "Value_2", "Content_3", "Value_3", "Content_4",
			"Value_4", "Content_5", "Value_5", "Content_6", "Value_6", "Content_7", "Value_7", "Content_8", "Value_8", "Content_9",
			"Value_9", "Content_10", "Value_10", "Content_11", "Value_11", "Content_12", "Value_12", "Content_13", "Value_13",
			"Content_14", "Value_14", "Content_15", "Value_15", "Content_16", "Value_16", "Content_17", "Value_17", "Content_18",
			"Value_18", "Content_19", "Value_19", "Content_20", "Value_20" };

	public static final String[] FIELD_OBJ_NAMES_INBOUND_REGISTRATION = new String[]
	{ "languagepreference", "asOfDate", "emailaddress", "emailpermission", "phonepermission", "phonenumber", "phoneextension",
			"title", "firstname", "lastname", "address1", "address2", "city", "province", "postalcode", "mailpermission",
			"emailPrefHDCA", "gardenClub", "emailPrefPRO", "newMover", "forfutureuse", "sourceiD", "sMSflag", "faxnumber",
			"faxextension", "content1", "value1", "content2", "value2", "content3", "value3", "content4", "value4", "content5",
			"value5", "content6", "value6", "content7", "value7", "content8", "value8", "content9", "value9", "content10", "value10",
			"content11", "value11", "content12", "value12", "content13", "value13", "content14", "value14", "content15", "value15",
			"content16", "value16", "content17", "value17", "content18", "value18", "content19", "value19", "content20", "value20" };

	/**
	 * Sets valid email pattern
	 *
	 * @param validEmailPattern
	 */
	public static void setValidEmailPattern(String validEmailPattern)
	{
		InboundValidator.validEmailPattern = validEmailPattern;
	}


	/**
	 * Error's validation.
	 *
	 * @param errors
	 *
	 * @return
	 */

	public static void isValidationsErros(StringBuilder errors)
	{
		if (errors.length() > 0)
			throw new ValidationException(" The item processed has the above validation's errors: " + errors);
	}

	/**
	 * Error's validation.
	 *
	 * @param field,
	 *           value, maxLength, error
	 *
	 * @return String If there is any error, it returns String with maxLength
	 */
	public static String validateMaxLengthNotReq(String field, String value, int maxLength, StringBuilder error)
	{
		if (value != null)
			return validateMaxLength(field, value, maxLength, error);
		return value;
	}

	public static String validateMaxLengthNotReq(String field, String value, int maxLength, StringBuilder error, Boolean nulleable)
	{
		if (value != null)
		{
			if (value.isBlank())
				return null;
			return validateMaxLength(field, value, maxLength, error);
		}
		return value;
	}

	/**
	 * Validates field length
	 *
	 * @param field,
	 *           value, maxLength, error
	 *
	 * @return String If there is any error, it returns String with maxLength
	 */
	public static String validateMaxLength(String field, String value, int maxLength, StringBuilder error)
	{

		if (value != null && value.length() > maxLength)
		{
			error.append("The length of ").append(field).append("field  must be").append(maxLength).append(" caracters or fewer.\n");
			return value.substring(0, maxLength);
		}
		return value;


	}

	/**
	 * Validate Lenguage Pref
	 *
	 * @param item,
	 *           error
	 *
	 * @return It update message error if there's any Error
	 */
	public static void validateLanguagePref(InboundRegistration item, StringBuilder error)
	{
		if (!item.getLanguagePreference().toUpperCase().trim().matches("e|E|f|F|fr|FR|en|EN|UNK"))
			error.append("invalid value for language_pref {}: ").append(item.getLanguagePreference())
					.append(" not matches with: E, EN, F, FR\n");
	}

	/**
	 * Validate email format
	 *
	 * @param email,
	 *           error
	 *
	 * @return Validate email according to email pattern (on configuration file)
	 */
	public static void validateEmailFormat(String email, StringBuilder error)
	{
		if (email != null && !email.matches(validEmailPattern))
			error.append(" email address does not have a valid format {}: ").append(email).append(",");

	}

	/**
	 * Validate date format
	 *
	 * @param date,
	 *           error
	 *
	 * @return Validate date format, if there's any error update error message
	 */
	public static Date validateDateFormat(String date, StringBuilder error)
	{
		Date asOfDate = new Date();
		try
		{
			asOfDate = srcDateSDF.parse(date);
			validateDayMonth(date, "-", error);
			return asOfDate;
		}
		catch (Exception ex)
		{
			error.append("Invalid date format ").append(date).append(",");
			return asOfDate;
		}
	}


	/**
	 * Validate Day and Month value
	 *
	 * @param date,
	 *           separator, error
	 *
	 * @return Validate Day and Moth value, if there's any error update error message
	 */
	public static void validateDayMonth(String date, String separator, StringBuilder error)
	{
		String[] mmddyy = date.split(" ")[0].split(separator);
		int month = Integer.parseInt(mmddyy[0]);
		validateMonth(month, error);
		int day = Integer.parseInt(mmddyy[1]);
		int year = Integer.parseInt(mmddyy[2]);
		validateDay(day, month, year, error);

	}

	/**
	 * Validate Month's value
	 *
	 * @param month,
	 *           error
	 *
	 * @return Validate Moth's value, if there's any error update error message
	 */
	public static void validateMonth(int month, StringBuilder error)
	{
		if (!(month >= 1 && month <= 12))
			error.append(" Invalid Month: ").append(month).append(" ,");
	}

	/**
	 * Validate day's value
	 *
	 * @param day,
	 *           month, year, error
	 *
	 * @return Validate day's value according to Month and year value, if there's any error update error message
	 */
	public static void validateDay(int day, int month, int year, StringBuilder error)
	{
		GregorianCalendar calendar = new GregorianCalendar();
		int maxDays = 31;
		if (month == 4 || month == 5 || month == 9 || month == 11)
			maxDays = 30;
		if (month == 2)
		{
			maxDays = calendar.isLeapYear(year) ? 29 : 28;
		}
		if (day < 1 || day > maxDays)
			error.append(" Invalid day: ").append(day).append(",");
	}

	/**
	 * Validate number format
	 *
	 * @param item,
	 *           error
	 *
	 * @return Validate number format for fields with number values, if there's any error, update error message
	 */
	public static void validateNumberFormat(InboundRegistration item, StringBuilder error)
	{
		Integer value = null;
		value = validateIsNumber(item.getEmailPermission(), error);
		validValueNumber(value, "email_permission", error);

		if (item.getPhonePermission() != null)
		{
			value = validateIsNumber(item.getPhonePermission(), error);
			validValueNumber(value, "phone_permission", error);
		}

		value = validateIsNumber(item.getMailPermission(), error);
		validValueNumber(value, "mail_permission", error);

		value = validateIsNumber(item.getEmailPrefHDCA(), error);
		validValueNumber(value, "email_pref_hd_ca", error);

		value = validateIsNumber(item.getGardenClub(), error);
		validValueNumber(value, "email_pref_garden_club", error);

		value = validateIsNumber(item.getEmailPrefPRO(), error);
		validValueNumber(value, "email_pref_pro", error);

		value = validateIsNumber(item.getNewMover(), error);
		validValueNumber(value, "email_pref_new_mover", error);

		if (item.getValue5() != null)
		{
			value = validateIsNumber(item.getValue5(), error);
			if (value != null && value != 1 && value != 2 && value != 5)
				error.append("invalid value for field {}: value5 =" + value);
		}
	}

	/**
	 * Gets source_id for specific record
	 * 
	 * @param value
	 * @param source
	 * @param error
	 * @return corresponding master_id
	 */
	public static BigDecimal validateSourceID(String value, String source, StringBuilder error)
	{

		/**
		 * If it doesn't have assign any Source_ID
		 */
		if (value == null || value.isEmpty() || value.isBlank())
		{
			return MasterProcessor.getSourceID("SOURCE_ID", getSource(source)).getMasterId();
		}
		BigDecimal masterId = MasterProcessor.getSourceID(value);
		/**
		 * If it has one assign, validates that is a VALID masterId
		 */
		if (masterId.equals(new BigDecimal("-400")))
		{
			error.append("  Not a valid Source ID ").append(value).append(" for this source. ,");
			return null;
		}
		return masterId;
	}

	/**
	 * Gets Source name that's being use in persistence
	 * 
	 * @param source
	 * @return
	 */
	public static String getSource(String source)
	{

		switch (source)
		{
			case SourceDelimitersConstants.HYBRIS:
				return "nurun";
			case SourceDelimitersConstants.CRM:
				return "CANADA SAP CRM";
			default:
				/**
				 * FB_SFMC
				 */
				return "Facebook Opt in campaign";
		}
	}

	/**
	 * Validate if is number
	 *
	 * @param number,
	 *           error
	 *
	 * @return Integer Validate if it is number, if there's any error, update error message
	 */
	public static Integer validateIsNumber(String number, StringBuilder error)
	{
		Integer value = null;
		try
		{
			value = Integer.parseInt(number);
		}
		catch (NumberFormatException ex)
		{
			error.append("invalid number format,");
		}

		return value;
	}

	/**
	 * Validate if is number is valid
	 *
	 * @param value,
	 *           field, error
	 *
	 * @return Validate if is it a valid value, if there is any error, update error message
	 */
	public static void validValueNumber(Integer value, String field, StringBuilder error)
	{
		if (value != null && (value < -1 || value > 1))
			error.append(" invalid value for field {}: ").append(field).append(",");
	}

	/**
	 * Validate required fields
	 *
	 * @param item,
	 *           error
	 *
	 * @return Validate if field is required, if there is any error, update error message
	 */
	public static void validateIsRequired(InboundRegistration item, StringBuilder error)
	{
		if (item == null)
		{
			error.append(" Item should be present,");
			return;
		}
		validateRequired(item.getLanguagePreference(), "language_pref", error);
		validateRequired(item.getAsOfDate(), "as_of_date", error);
		validateRequired(item.getEmailPermission(), "email_permission", error);
		validateRequired(item.getMailPermission(), "mail_permission", error);
		validateRequired(item.getEmailPrefHDCA(), "email_pref_hd_ca", error);
		validateRequired(item.getGardenClub(), "email_pref_garden_club", error);
		validateRequired(item.getEmailPrefPRO(), "email_pref_pro", error);
		validateRequired(item.getNewMover(), "email_pref_new_mover", error);
		validateRequired(item.getContent1(), "content1", error);
		validateRequired(item.getContent2(), "content2", error);
		validateRequired(item.getContent3(), "content3", error);
		validateRequired(item.getContent5(), "content5", error);
		validateRequired(item.getContent6(), "content6", error);
	}

	/**
	 * Validate required
	 *
	 * @param value,
	 *           field, error
	 *
	 * @return Validate if required value is not Null or blank, if there is any error, update error message
	 */
	public static void validateRequired(String value, String field, StringBuilder error)
	{
		if (value == null || value.isBlank())
		{
			error.append(field).append(" should be present,");
		}
	}


	public static Date moveDate(String date)
	{

		Date asOfDate = null;
		try
		{
			asOfDate = simpleDateFormat.parse(date);
			return asOfDate;
		}
		catch (Exception ex)
		{
			log.error(" MoveDate has not a right format. ");
			return asOfDate;
		}

	}
}



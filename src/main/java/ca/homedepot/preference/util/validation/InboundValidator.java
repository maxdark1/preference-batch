package ca.homedepot.preference.util.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;

public class InboundValidator
{
	public static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String[] FIELD_NAMES_REGISTRATION = new String[]
	{ "Language_Preference", "AsOfDate", "Email_Address", "Email_Permission", "Phone_Permission", "Phone_Number",
			"Phone_Extension", "Title", "First_Name", "Last_Name", "Address_1", "Address_2", "City", "Province", "Postal_Code",
			"Mail_Permission", "EmailPrefHDCA", "GardenClub", "EmailPrefPRO", "NewMover", "For_Future_Use", "Source_ID", "SMS_Flag",
			"Fax_Number", "Fax_Extension", "Content_1", "Value_1", "Content_2", "Value_2", "Content_3", "Value_3", "Content_4",
			"Value_4", "Content_5", "Value_5", "Content_6", "Value_6", "Content_7", "Value_7", "Content_8", "Value_8", "Content_9",
			"Value_9", "Content_10", "Value_10", "Content_11", "Value_11", "Content_12", "Value_12", "Content_13", "Value_13",
			"Content_14", "Value_14", "Content_15", "Value_15", "Content_16", "Value_16", "Content_17", "Value_17", "Content_18",
			"Value_18", "Content_19", "Value_19", "Content_20", "Value_20" };


	/*
	 * File's validation field's name.
	 * 
	 * @param
	 * 
	 * @return lineCallBackHandler
	 */

	public static void isValidationsErros(StringBuilder errors){
		if(errors.length() > 0)
			throw new ValidationException(" The item processed has the above validations erros: \n" + errors);
	}

	public static String validateMaxLengthNotReq(String field, String value, int maxLength, StringBuilder error)
	{
		if (value != null)
			return validateMaxLength(field, value, maxLength, error);
		return value;
	}

	public static String validateMaxLength(String field, String value, int maxLength, StringBuilder error)
	{

		if (value != null &&(value.length() > maxLength))
		{
			error.append(String.format("The length of %s field  must be %d caracters or fewer.\n", field, maxLength));
			return value.substring(0, maxLength);
		}
		return value;


	}

	public static void validateLanguagePref(InboundRegistration item, StringBuilder error)
	{
		if (!item.getLanguage_Preference().trim().matches("e|E|f|F|fr|FR|en|EN"))
			error.append("invalid value for language_pref {}: ").append(item.getLanguage_Preference()).append(" not matches with: E, EN, F, FR\n");
	}

	public static void validateEmailFormat(String email, StringBuilder error)
	{

		if (email != null)
			if (!email.matches(VALID_EMAIL_PATTERN))
				error.append(" email address does not have a valid format {}: " ).append( email).append("\n");
//				throw new ValidationException(" email address does not have a valid format {}: " + email);

	}

	public static Date validateDateFormat(String date, StringBuilder error)
	{

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date asOfDate = new Date();
		try
		{
			asOfDate = simpleDateFormat.parse(date);
			validateDayMonth(date, error);
			return asOfDate;
		}
		catch (Exception ex)
		{
			error.append("Invalid date format ").append(date).append("\n");
			return asOfDate;
//			throw new ValidationException("invalid date format");
		}
	}

	public static void validateDayMonth(String date, StringBuilder error)
	{
		String[] mmddyy = date.split(" ")[0].split("-");
		int month = Integer.valueOf(mmddyy[0]);
		validateMonth(month, error);
		int day = Integer.valueOf(mmddyy[1]);
		int year = Integer.valueOf(mmddyy[2]);
		validateDay(day, month, year, error);

	}

	public static void validateMonth(int month, StringBuilder error){
		if(!(month >=1 && month <= 12))
			error.append(" Invalid Month: " ).append(month).append(" \n");
	}
	public static  void validateDay(int day, int month, int year, StringBuilder error){
		GregorianCalendar calendar = new GregorianCalendar();
		int maxDays = 31;
		if(month == 4 || month == 5 || month == 9 || month == 11)
			maxDays = 30;
		if(month == 2)
		{
			maxDays = calendar.isLeapYear(year)? 29:28;
		}
		if(day < 1 || day > maxDays)
			error.append(" Invalid day: ").append(day).append("\n");
	}

	public static void validateNumberFormat(InboundRegistration item, StringBuilder error)
	{
		Integer value = null;
		value = validateIsNumber(item.getEmail_Permission(), error);
		validValue_Number(value, "email_permission", error);

		if (item.getPhone_Permission() != null)
		{
			value = validateIsNumber(item.getPhone_Permission(), error);
			validValue_Number(value, "phone_permission", error);
		}

		value = validateIsNumber(item.getMail_Permission(), error);
		validValue_Number(value, "mail_permission", error);

		value = validateIsNumber(item.getEmailPrefHDCA(), error);
		validValue_Number(value, "email_pref_hd_ca", error);

		value = validateIsNumber(item.getGardenClub(), error);
		validValue_Number(value, "email_pref_garden_club", error);

		value = validateIsNumber(item.getEmailPrefPRO(), error);
		validValue_Number(value, "email_pref_pro", error);

		value = validateIsNumber(item.getNewMover(), error);
		validValue_Number(value, "email_pref_new_mover", error);

		if (item.getValue_5() != null)
		{
			value = validateIsNumber(item.getValue_5(), error);
			if (value != null && value != 1 && value != 2 && value != 5)
				error.append("invalid value for field {}: value5 =\n" + value);
				//throw new ValidationException();
		}

		if (item.getSource_ID() != null && !item.getSource_ID().isBlank())
		{
			value = validateIsNumber(item.getSource_ID().trim(), error);
			item.setSource_ID(value != null ? String.valueOf(value): "0");
		}
		else
		{
			item.setSource_ID("0");
		}
	}

	public static Integer validateIsNumber(String number, StringBuilder error)
	{
		Integer value = null;
		try
		{
			value = Integer.parseInt(number);
		}
		catch (NumberFormatException ex)
		{
			error.append("invalid number format\n");
//			throw new ValidationException("invalid number format");
		}

		return value;
	}

	public static void validValue_Number(Integer value, String field, StringBuilder error)
	{
		if (value != null &&(value < -1 || value > 1))
			error.append(" invalid value for field {}: ").append(field).append("\n");
			//throw new ValidationException("invalid value for field {}: " + field);
	}

	public static void validateIsRequired(InboundRegistration item, StringBuilder error)
	{
		if (item == null)
		{
			error.append(" Item should be present\n");
			return;
			//throw new ValidationException(" Item should be present");
		}
		validateRequired(item.getLanguage_Preference(), "language_pref", error);
		validateRequired(item.getAsOfDate(), "as_of_date", error);
		validateRequired(item.getEmail_Permission(), "email_permission", error);
		validateRequired(item.getMail_Permission(), "mail_permission", error);
		validateRequired(item.getEmailPrefHDCA(), "email_pref_hd_ca", error);
		validateRequired(item.getGardenClub(), "email_pref_garden_club", error);
		validateRequired(item.getEmailPrefPRO(), "email_pref_pro", error);
		validateRequired(item.getNewMover(), "email_pref_new_mover", error);
		validateRequired(item.getSource_ID(), "source_id", error);
		validateRequired(item.getContent_1(), "content1", error);
		validateRequired(item.getContent_2(), "content2", error);
		validateRequired(item.getContent_3(), "content3", error);
		validateRequired(item.getContent_5(), "content5", error);
		validateRequired(item.getContent_6(), "content6", error);
	}

	public static void validateRequired(String value, String field, StringBuilder error)
	{
		if (value == null || value.isBlank())
		{
			error.append(field).append(" should be present\n");
//			throw new ValidationException(field + " should be present");
		}
	}
}

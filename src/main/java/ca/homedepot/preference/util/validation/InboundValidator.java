package ca.homedepot.preference.util.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutboundRegistration;

public class InboundValidator
{
	public static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String[] FIELD_NAMES = new String[]
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

	public static LineCallbackHandler lineCallbackHandler()
	{
		return line -> {
			String[] header = line.split("\\|");
			if (!Arrays.equals(header, FIELD_NAMES))
				throw new ValidationException(" Invalid header {}: " + Arrays.toString(header));
		};
	}

	public static void validateMaxLengthNotReq(String field, String value, int maxLength)
	{
		if (value != null)
			validateMaxLength(field, value, maxLength);
	}

	public static void validateMaxLength(String field, String value, int maxLength)
	{
		if (value.length() > maxLength)
			throw new ValidationException(String.format("The length of %s field  must be %d caracters or fewer.", field, maxLength));


	}

	public static void validateLanguagePref(InboundRegistration item)
	{

		if (!item.getLanguage_Preference().trim().matches("e|E|f|F|fr|FR|en|EN"))
			throw new ValidationException(
					"invalid value for language_pref {}: " + item.getLanguage_Preference() + " not matches with: E, EN, F, FR");
	}

	public static void validateEmailFormat(InboundRegistration item)
	{

		if (item.getEmail_Address() != null)
			if (!item.getEmail_Address().matches(VALID_EMAIL_PATTERN))
				throw new ValidationException(" email address does not have a valid format {}: " + item.getEmail_Address());

	}

	public static Date validateDateFormat(String date)
	{

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		try
		{
			Date asOfDate = simpleDateFormat.parse(date);
			return asOfDate;
		}
		catch (ParseException ex)
		{
			throw new ValidationException("invalid date format");
		}
	}

	public static void validateNumberFormat(InboundRegistration item, OutboundRegistration.OutboundRegistrationBuilder builder)
	{
		Integer value = null;
		value = validateIsNumber(item.getEmail_Permission());
		validValue_Number(value, "email_permission");

		if (item.getPhone_Permission() != null)
		{
			value = validateIsNumber(item.getPhone_Permission());
			validValue_Number(value, "phone_permission");
		}

		value = validateIsNumber(item.getMail_Permission());
		validValue_Number(value, "mail_permission");

		value = validateIsNumber(item.getEmailPrefHDCA());
		validValue_Number(value, "email_pref_hd_ca");

		value = validateIsNumber(item.getGardenClub());
		validValue_Number(value, "email_pref_garden_club");

		value = validateIsNumber(item.getEmailPrefPRO());
		validValue_Number(value, "email_pref_pro");

		value = validateIsNumber(item.getNewMover());
		validValue_Number(value, "email_pref_new_mover");

		if (item.getValue_5() != null)
		{
			value = validateIsNumber(item.getValue_5());
			if (value != 1 && value != 2 && value != 5)
				throw new ValidationException("invalid value for field {}: value5");
		}

		if (item.getSource_ID() != null && !item.getSource_ID().isBlank())
		{
			value = validateIsNumber(item.getSource_ID().trim());
			builder.source_id(value != null ? Long.valueOf(value) : 0L);
		}
		else
		{
			builder.source_id(0L);
		}
	}

	public static Integer validateIsNumber(String number)
	{
		Integer value = null;
		try
		{
			value = Integer.parseInt(number);
		}
		catch (NumberFormatException ex)
		{
			throw new ValidationException("invalid number format");
		}

		return value;
	}

	public static void validValue_Number(Integer value, String field)
	{
		if (value < -1 || value > 1)
			throw new ValidationException("invalid value for field {}: " + field);
	}

	public static void validateIsRequired(InboundRegistration item)
	{
		if (item == null)
		{
			throw new ValidationException(" Item should be present");
		}
		validateRequired(item.getLanguage_Preference(), "language_pref");
		validateRequired(item.getAsOfDate(), "as_of_date");
		validateRequired(item.getEmail_Permission(), "email_permission");
		validateRequired(item.getMail_Permission(), "mail_permission");
		validateRequired(item.getEmailPrefHDCA(), "email_pref_hd_ca");
		validateRequired(item.getGardenClub(), "email_pref_garden_club");
		validateRequired(item.getEmailPrefPRO(), "email_pref_pro");
		validateRequired(item.getNewMover(), "email_pref_new_mover");
		validateRequired(item.getSource_ID(), "source_id");
		validateRequired(item.getContent_1(), "content1");
		validateRequired(item.getContent_2(), "content2");
		validateRequired(item.getContent_3(), "content3");
		validateRequired(item.getContent_5(), "content5");
		validateRequired(item.getContent_6(), "content6");
	}

	public static void validateRequired(String value, String field)
	{
		if (value == null || value.isBlank())
		{
			throw new ValidationException(field + " should be present");
		}
	}
}

package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.OutboundRegistration;
import org.springframework.batch.item.ItemProcessor;

import ca.homedepot.preference.model.InboundRegistration;
import org.springframework.batch.item.validator.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegistrationItemProcessor implements ItemProcessor<InboundRegistration, OutboundRegistration>
{

	private final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public OutboundRegistration process(InboundRegistration item) throws Exception
	{

		OutboundRegistration.OutboundRegistrationBuilder builder = OutboundRegistration.builder();

		validate(item, builder);

		return builder.build();
	}

	private void validate(final InboundRegistration item, final OutboundRegistration.OutboundRegistrationBuilder builder)
	{
		validateIsRequired(item);
		validateNumberFormat(item);
		validateDateFormat(item, builder);
		validateEmailFormat(item);
		validateLanguagePref(item);
	}

	private void validateLanguagePref(InboundRegistration item) {

		if(!item.getLanguagePref().matches("e|E|f|F|fr|FR|en|EN"))
			throw new ValidationException("invalid value for language_pref {}: " + item.getLanguagePref() + " not matches with: E, EN, F, FR");
	}

	private void validateEmailFormat(InboundRegistration item)
	{

		if (item.getEmailAddr() != null)
			if (!item.getEmailAddr().matches(VALID_EMAIL_PATTERN))
				throw new ValidationException(" email address does not have a valid format {}: " + item.getEmailAddr());

	}

	private void validateDateFormat(InboundRegistration item, OutboundRegistration.OutboundRegistrationBuilder builder)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		try
		{
			Date asOfDate = simpleDateFormat.parse(item.getAsOfDate());
			builder.updated_date(asOfDate);
		}
		catch (ParseException ex)
		{
			throw new ValidationException("invalid date format");
		}


	}

	private void validateNumberFormat(InboundRegistration item)
	{
		Integer value = null;
		value = validateIsNumber(item.getEmailPermission());
		validValueNumber(value, "email_permission");

		if(item.getPhonePermission() != null) {
			value = validateIsNumber(item.getPhonePermission());
			validValueNumber(value, "phone_permission");
		}

		value = validateIsNumber(item.getMailPermission());
		validValueNumber(value, "mail_permission");

		value = validateIsNumber(item.getEmailPrefHdCa());
		validValueNumber(value, "email_pref_hd_ca");

		value = validateIsNumber(item.getEmailPrefGardenClub());
		validValueNumber(value, "email_pref_garden_club");

		value = validateIsNumber(item.getEmailPrefPro());
		validValueNumber(value, "email_pref_pro");

		value = validateIsNumber(item.getEmailPrefNewMover());
		validValueNumber(value, "email_pref_new_mover");

		if(item.getValue5() != null) {
			value = validateIsNumber(item.getValue5());
			if(value != 1 && value != 2 && value != 5)
				throw new ValidationException("invalid value for field {}: value5");
		}
	}

	private Integer validateIsNumber(String number)
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

	private void validValueNumber(Integer value, String field)
	{
		if (value < -1 || value > 1)
			throw new ValidationException("invalid value for field {}: " + field);
	}

	private void validateIsRequired(InboundRegistration item)
	{
		if (item == null)
		{
			throw new ValidationException(" Item should be present");
		}

		if (item.getLanguagePref() == null)
		{
			throw new ValidationException("language_pref should be present");
		}
		if (item.getAsOfDate() == null)
		{
			throw new ValidationException("as_of_date should be present");
		}
		if (item.getEmailPermission() == null)
		{
			throw new ValidationException("email_permission should be present");
		}
		if (item.getMailPermission() == null)
		{
			throw new ValidationException("mail_permission should be present");
		}
		if (item.getEmailPrefHdCa() == null)
		{
			throw new ValidationException("email_pref_hd_ca should be present");
		}
		if (item.getEmailPrefGardenClub() == null)
		{
			throw new ValidationException("email_pref_garden_club should be present");
		}
		if (item.getEmailPrefPro() == null)
		{
			throw new ValidationException("email_pref_pro should be present");
		}
		if (item.getEmailPrefNewMover() == null)
		{
			throw new ValidationException("email_pref_new_mover should be present");
		}
		if (item.getSourceId() == null)
		{
			throw new ValidationException("source_id should be present");
		}
		if (item.getContent1() == null)
		{
			throw new ValidationException("content1 should be present");
		}
		if (item.getContent2() == null)
		{
			throw new ValidationException("content2 should be present");
		}
		if (item.getContent3() == null)
		{
			throw new ValidationException("content3 should be present");
		}
		if (item.getContent5() == null)
		{
			throw new ValidationException("content5 should be present");
		}
		if (item.getContent6() == null)
		{
			throw new ValidationException("content6 should be present");
		}


	}

}

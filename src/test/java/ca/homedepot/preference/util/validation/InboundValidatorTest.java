package ca.homedepot.preference.util.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.InboundRegistration;

class InboundValidatorTest
{

	InboundRegistration input;

	@BeforeEach
	public void setup()
	{
		input = new InboundRegistration();
		input.setAsOfDate("08-26-2022 10:10:10");
		input.setLanguage_Preference("E");
		input.setEmail_Permission("1");
		input.setMail_Permission("1");
		input.setEmailPrefHDCA("1");
		input.setGardenClub("1");
		input.setEmailPrefPRO("1");
		input.setNewMover("-1");
		input.setSource_ID("12345");
		input.setContent_1("CUSTOMER_NBR");
		input.setContent_2("STORE_NBR");
		input.setContent_3("ORG_NAME");
		input.setContent_5("CUST_TYPE_CODE");
		input.setContent_6("CELL_PHONE");
	}

	@Test
	void validateMaxLengthNotReq()
	{
		String field = "test_field", value = "Wrong length test";
		int maxLength = 3;

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateMaxLengthNotReq(field, value, maxLength);
		});
		assertTrue(exception.getMessage().contains(field));
	}

	@Test
	void validateLanguagePref()
	{
		input.setLanguage_Preference("ENGLISH");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateLanguagePref(input);
		});

		assertTrue(exception.getMessage().contains(input.getLanguage_Preference()));
	}

	@Test
	void validateEmailFormat()
	{
		input.setEmail_Address("item");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateEmailFormat(input.getEmail_Address());
		});
	}

	@Test
	void validateDateFormatValid() throws ParseException
	{
		String date = "09-06-2022 2:23:23";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		Date validatedDate = InboundValidator.validateDateFormat(date);

		assertEquals(validatedDate, simpleDateFormat.parse(date));
	}

	@Test
	void validateDateFormatInvalid()
	{
		String date = "09/06/2022 2:23:23";

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateDateFormat(date);
		});
		assertTrue(exception.getMessage().contains("invalid date format"));
	}

	@Test
	void validateNumberFormat()
	{

	}

	@Test
	void validateIsNumber()
	{
		String number = "4";
		Integer value = InboundValidator.validateIsNumber(number);

		assertEquals(value, Integer.parseInt(number));

		number = "a";

		String finalNumber = number;
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateIsNumber(finalNumber);
		});

		assertTrue(exception.getMessage().contains("invalid number format"));

	}

	@Test
	void validValue_Number()
	{
		String field = "test_field";

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validValue_Number(2, field);
		});

		assertTrue(exception.getMessage().contains(field));
	}

	@Test
	void validateIsRequired()
	{

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateIsRequired(null);
		});

		assertTrue(exception.getMessage().contains("should be present"));
		input.setLanguage_Preference(null);

		ValidationException exception1 = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateIsRequired(input);
		});

		assertTrue(exception1.getMessage().contains("language_pref"));
	}

	@Test
	void validateRequired()
	{
		String value = "", field = "test_field";

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.validateRequired(value, field);
		});

		assertTrue(exception.getMessage().contains(field));


	}
}
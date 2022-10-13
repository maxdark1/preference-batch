package ca.homedepot.preference.util.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.InboundRegistration;

class InboundValidatorTest
{

	InboundRegistration input;


	StringBuilder error ;

	@BeforeAll
	static void settingUp(){
		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(new BigDecimal("1"), BigDecimal.ONE, "SOURCE", "CRM", true));
		masterList.add(new Master(new BigDecimal("2"), BigDecimal.ONE, "SOURCE", "hybris", true));
		masterList.add(new Master(new BigDecimal("3"), BigDecimal.ONE, "SOURCE", "manual_update", true));
		masterList.add(new Master(new BigDecimal("4"), BigDecimal.ONE, "SOURCE", "citi_bank", true));
		masterList.add(new Master(new BigDecimal("5"), BigDecimal.ONE, "SOURCE", "SFMC", true));
		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT-CAN", true));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT-CAN", true));

		MasterProcessor.setMasterList(masterList);
	}

	@BeforeEach
	public void setup()
	{
		InboundValidator.setValidEmailPattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		input = new InboundRegistration();
		error = new StringBuilder();
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

		InboundValidator.validateMaxLengthNotReq(field, value, maxLength, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		assertTrue(exception.getMessage().contains(field));
	}

	@Test
	void validateLanguagePref()
	{
		input.setLanguage_Preference("ENGLISH");

		InboundValidator.validateLanguagePref(input, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains(input.getLanguage_Preference()));
	}

	@Test
	void validateEmailFormat()
	{
		input.setEmail_Address("item");

		InboundValidator.validateEmailFormat(input.getEmail_Address(), error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
	}

	@Test
	void validateDateFormatValid() throws ParseException
	{
		String date = "09-06-2022 2:23:23";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		Date validatedDate = InboundValidator.validateDateFormat(date, error);

		assertEquals(validatedDate, simpleDateFormat.parse(date));
	}

	@Test
	void validateDateFormatInvalid()
	{
		String date = "09/06/2022 2:23:23";

		InboundValidator.validateDateFormat(date, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		assertTrue(exception.getMessage().contains("Invalid date format"));
	}

	@Test
	void validateNumberFormatSourceID()
	{
		InboundRegistration item = new InboundRegistration();
		item.setSource_ID( null);

		InboundValidator.validateNumberFormat(item, error);

		assertEquals(null, item.getSource_ID());
	}

	@Test
	void validateNumberFormatValue5()
	{
		InboundRegistration item = new InboundRegistration();
		item.setValue_5( "a");

		InboundValidator.validateNumberFormat(item, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		assertTrue(exception.getMessage().contains("invalid"));
	}

	@Test
	void validateNumberFormatValue5Numeric()
	{
		InboundRegistration item = new InboundRegistration();
		item.setValue_5( "10");

		InboundValidator.validateNumberFormat(item, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		assertTrue(exception.getMessage().contains("invalid"));
	}
	@Test
	void invalidMonthDateTest()
	{
		StringBuilder error = new StringBuilder();
		String invalidDate = "80-40-2022 2:02:20";

		InboundValidator.validateDateFormat(invalidDate, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains("validations erros"));
	}

	@Test
	void invalidDayLeapYear(){
		StringBuilder error = new StringBuilder();
		String invalidDate = "02-29-2024 2:02:20";

		InboundValidator.validateDateFormat(invalidDate, error);


		assertTrue(error.toString().isEmpty());
	}


	@Test
	void validateNumberFormatPhonePermission()
	{
		InboundRegistration item = new InboundRegistration();
		item.setPhone_Permission( "a");

		InboundValidator.validateNumberFormat(item, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		assertTrue(exception.getMessage().contains("invalid"));

	}

	@Test
	void validateIsNumber()
	{
		String number = "4";
		Integer value = InboundValidator.validateIsNumber(number, error);

		assertEquals(value, Integer.parseInt(number));

		number = "a";

		String finalNumber = number;
		InboundValidator.validateIsNumber(finalNumber, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains("invalid number format"));

	}

	@Test
	void validValue_Number()
	{
		String field = "test_field";
		InboundValidator.validValue_Number(2, field, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains(field));
	}

	@Test
	void validateIsRequired()
	{
		input.setLanguage_Preference(null);

		InboundValidator.validateIsRequired(input, error);
		ValidationException exception1 = assertThrows(ValidationException.class, () -> {
				InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception1.getMessage().contains("language_pref"));
	}

	@Test
	void validateIsRequiredEmptyObj(){

		InboundValidator.validateIsRequired(null, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains("should be present"));
	}

	@Test
	void validateRequired()
	{
		String value = "", field = "test_field";


		InboundValidator.validateRequired(value, field, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains(field));
	}
}
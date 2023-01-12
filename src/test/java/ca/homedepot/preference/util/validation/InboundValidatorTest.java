package ca.homedepot.preference.util.validation;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.processor.MasterProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InboundValidatorTest
{

	InboundRegistration input;


	StringBuilder error;

	@BeforeAll
	static void settingUp()
	{
		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(new BigDecimal("1"), BigDecimal.ONE, "SOURCE", "CRM", true, null));
		masterList.add(new Master(new BigDecimal("2"), BigDecimal.ONE, "SOURCE", "hybris", true, null));
		masterList.add(new Master(new BigDecimal("3"), BigDecimal.ONE, "SOURCE", "manual_update", true, null));
		masterList.add(new Master(new BigDecimal("4"), BigDecimal.ONE, "SOURCE", "citi_bank", true, null));
		masterList.add(new Master(new BigDecimal("5"), BigDecimal.ONE, "SOURCE", "SFMC", true, null));
		masterList.add(new Master(new BigDecimal("21"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT-CAN", true, null));
		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT AOL-CAN", true, null));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT OTH-CAN", true, null));
		masterList.add(new Master(new BigDecimal("24"), BigDecimal.TEN, "SOURCE_ID", "HELD", true, new BigDecimal("50")));
		masterList.add(new Master(new BigDecimal("25"), BigDecimal.TEN, "SOURCE_ID", "UNSUBSCRIBE", true, new BigDecimal("98")));
		masterList.add(new Master(new BigDecimal("26"), BigDecimal.TEN, "SOURCE_ID", "nurun", true, new BigDecimal("123")));

		MasterProcessor.setMasterList(masterList);
	}

	@BeforeEach
	public void setup()
	{
		InboundValidator
				.setValidEmailPattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		input = new InboundRegistration();
		error = new StringBuilder();
		input.setAsOfDate("08-26-2022 10:10:10");
		input.setLanguagePreference("E");
		input.setEmailPermission("1");
		input.setMailPermission("1");
		input.setEmailPrefHDCA("1");
		input.setGardenClub("1");
		input.setEmailPrefPRO("1");
		input.setNewMover("-1");
		input.setSourceID("12345");
		input.setContent1("CUSTOMER_NBR");
		input.setContent2("STORE_NBR");
		input.setContent3("ORG_NAME");
		input.setContent5("CUST_TYPE_CODE");
		input.setContent6("CELL_PHONE");
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
	void validateMaxLengthNotReqNulleable()
	{
		String field = "addr1", value = "";
		int maxLength = 3;

		String result = InboundValidator.validateMaxLengthNotReq(field, value, maxLength, error, true);
		assertNull(result);

	}

	@Test
	void validateLanguagePref()
	{
		input.setLanguagePreference("ENGLISH");

		InboundValidator.validateLanguagePref(input, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains(input.getLanguagePreference()));
	}

	@Test
	void validateEmailFormat()
	{
		input.setEmailAddress("item");

		InboundValidator.validateEmailFormat(input.getEmailAddress(), error);
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
		item.setSourceID(null);

		InboundValidator.validateNumberFormat(item, error);

		assertEquals(null, item.getSourceID());
	}

	@Test
	void validateNumberFormatValue5()
	{
		InboundRegistration item = new InboundRegistration();
		item.setValue5("a");

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
		item.setValue5("10");

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

		assertTrue(exception.getMessage().contains("The item processed has the above validation's errors:"));
	}

	@Test
	void invalidDayLeapYear()
	{
		StringBuilder error = new StringBuilder();
		String invalidDate = "02-29-2024 2:02:20";

		InboundValidator.validateDateFormat(invalidDate, error);


		assertTrue(error.toString().isEmpty());
	}


	@Test
	void validateNumberFormatPhonePermission()
	{
		InboundRegistration item = new InboundRegistration();
		item.setPhonePermission("a");

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
		InboundValidator.validValueNumber(2, field, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception.getMessage().contains(field));
	}

	@Test
	void validateIsRequired()
	{
		input.setLanguagePreference(null);

		InboundValidator.validateIsRequired(input, error);
		ValidationException exception1 = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertTrue(exception1.getMessage().contains("language_pref"));
	}

	@Test
	void validateIsRequiredEmptyObj()
	{

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

	@Test
	void validateSourceIDTest()
	{
		BigDecimal sourceIdNull = InboundValidator.validateSourceID(null, "hybris", error);
		BigDecimal sourceIdNotNull = InboundValidator.validateSourceID("123", "hybris", error);

		assertEquals(new BigDecimal("26"), sourceIdNull);
		assertEquals(new BigDecimal("26"), sourceIdNotNull);
	}

	@Test
	void validateSourceIdTestINvalidSourceID()
	{
		BigDecimal sourceId = InboundValidator.validateSourceID("-2", "hybris", error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertNull(sourceId);
		assertTrue(exception.getMessage().contains("Source ID"));
	}

	@Test
	void getSourceTest()
	{
		String nurun = InboundValidator.getSource("hybris");
		String sapCRM = InboundValidator.getSource("CRM");
		String faceOptIn = InboundValidator.getSource("FB_SFMC");

		assertEquals("nurun", nurun);
		assertEquals("CANADA SAP CRM", sapCRM);
		assertEquals("Facebook Opt in campaign", faceOptIn);
	}

	@Test
	void moveDateTestMakingAnException()
	{
		String dateStr = "something";

		Date dateDT = InboundValidator.moveDate(dateStr);

		assertNull(dateDT);
	}
}
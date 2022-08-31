package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.InboundRegistration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationItemProcessorTest
{
	private RegistrationItemProcessor registrationItemProcessor;

	private InboundRegistration input;

	@Before
	public void setup()
	{
		registrationItemProcessor = new RegistrationItemProcessor();

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
	public void validateInputNull(){
		input = null;
		ValidationException exception = assertThrows(ValidationException.class, ()-> {
			registrationItemProcessor.process(input);
		});

		assertEquals(" Item should be present", exception.getMessage());
	}

	@Test
	public void validateDateFormatTest() throws Exception
	{
		input.setAsOfDate("08-26-2022 10:10:10");
		registrationItemProcessor.process(input);
		assertTrue(true);

		input.setAsOfDate("May 13th, 2022");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("invalid date format"));
	}

	@Test
	public void validateEmailPermissionRequired() throws Exception {
		input.setEmail_Permission(null);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("should be present"));
	}

	@Test
	public void validateRequired() throws Exception
	{
		input.setAsOfDate("02-02-2022 22:22:22");
		 registrationItemProcessor.process(input);
		assertEquals("02-02-2022 22:22:22",input.getAsOfDate());
		input.setAsOfDate(null);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});


		assertTrue(exception.getMessage().contains("should be present"));

	}

	@Test
	public void validateIsNumberTest()
	{
		input.setEmail_Permission("a");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("invalid number format"));

	}

	@Test
	public void validValue_NumberTest()
	{
		input.setEmail_Permission("-2");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertEquals("invalid value for field {}: email_permission", exception.getMessage());
	}

	@Test
	public void validatePhonePermission() throws Exception {
		input.setPhone_Permission("-1");
		registrationItemProcessor.process(input);
		assertEquals("-1", input.getPhone_Permission());

		input.setPhone_Permission("20");
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		input.setPhone_Permission("a");
		ValidationException exception2 = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		input.setPhone_Permission(null);
		registrationItemProcessor.process(input);

		assertTrue(exception.getMessage().contains("invalid value for field"));
		assertTrue(exception2.getMessage().contains("invalid number format"));
		assertNull(input.getPhone_Permission());
	}

	@Test
	public void validateValue_5value() throws Exception {
		input.setValue_5("2");
		registrationItemProcessor.process(input);
		assertEquals("2", input.getValue_5());

		input.setValue_5("a");
		ValidationException exception = assertThrows(ValidationException.class, () ->
		{
			registrationItemProcessor.process(input);
		});
		input.setValue_5("6");
		ValidationException exception1 = assertThrows(ValidationException.class, () ->
		{
			registrationItemProcessor.process(input);
		});
		assertTrue(exception.getMessage().contains("invalid number format"));
		assertTrue(exception1.getMessage().contains("invalid value for field"));

	}

	@Test
	public void validateEmailFormatTest()
	{
		input.setEmail_Address("narutoUzumakianime.com");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains(" email address does not have a valid format {}: "));
	}

	@Test
	public void validateLanguagePrefTest(){
		input.setLanguage_Preference("e");

		assertTrue(true);

		input.setLanguage_Preference("k");
		ValidationException exception = assertThrows(ValidationException.class, ()-> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("invalid value for language_pref {}: "));
	}

	@Test
	public void validateMaxLengthFieldsRequiredFields() {
		input.setContent_1("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
				"reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
				"sunt in culpa qui officia deserunt mollit anim id est laborum.");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("content1"));

		input.setContent_1("2");
		input.setContent_2("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
				"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
				"reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, " +
				"sunt in culpa qui officia deserunt mollit anim id est laborum.");

		exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("content2"));
	}

	@Test
	public void validateMaxLengthFieldsNotRequiredFields(){
		input.setEmail_Address(null);

		assertEquals(null, input.getEmail_Address());

		input.setEmail_Address("reprehenderitinvoluptatevelitesse_cillum-dolore_eufugiatnullapariaturasdasd@email.com");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		ValidationException exception1 = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);} );

		System.out.println(exception1.getMessage());
		assertTrue(exception.getMessage().contains("email_addr"));
	}


}
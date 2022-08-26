package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.InboundRegistration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:resources/test-context.xml")
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
		input.setLanguagePref("E");
		input.setEmailPermission("1");
		input.setMailPermission("1");
		input.setEmailPrefHdCa("1");
		input.setEmailPrefGardenClub("1");
		input.setEmailPrefPro("1");
		input.setEmailPrefNewMover("-1");
		input.setSourceId("12345");
		input.setContent1("CUSTOMER_NBR");
		input.setContent2("STORE_NBR");
		input.setContent3("ORG_NAME");
		input.setContent5("CUST_TYPE_CODE");
		input.setContent6("CELL_PHONE");
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
	public void validateRequired() throws Exception
	{
		input.setAsOfDate(null);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("should be present"));

	}

	@Test
	public void validateIsNumberTest()
	{
		input.setEmailPermission("a");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("invalid number format"));

	}

	@Test
	public void validValueNumberTest()
	{
		input.setEmailPermission("-2");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertEquals("invalid value for field {}: email_permission", exception.getMessage());
	}

	@Test
	public void validateEmailFormatTest()
	{
		input.setEmailAddr("narutoUzumakianime.com");

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains(" email address does not have a valid format {}: "));
	}

	@Test
	public void validateLanguagePrefTest(){
		input.setLanguagePref("e");

		assertTrue(true);

		input.setLanguagePref("k");
		ValidationException exception = assertThrows(ValidationException.class, ()-> {
			registrationItemProcessor.process(input);
		});

		assertTrue(exception.getMessage().contains("invalid value for language_pref {}: "));
	}
}
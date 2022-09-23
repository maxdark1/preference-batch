package ca.homedepot.preference.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;

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
	public void validateRequired() throws Exception
	{
		input.setAsOfDate("02-02-2022 22:22:22");
		registrationItemProcessor.process(input);
		assertEquals("02-02-2022 22:22:22", input.getAsOfDate());
		input.setAsOfDate(null);

		FileInboundStgTable fileInboundStgTable = registrationItemProcessor.process(input);

		assertNull(fileInboundStgTable);
	}

	@Test
	public void validateIsNumberTest() throws Exception
	{
		input.setEmail_Permission("a");

		FileInboundStgTable fileInboundStgTable = registrationItemProcessor.process(input);

		assertNull(fileInboundStgTable);

	}

}
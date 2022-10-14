package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.Master;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		registrationItemProcessor = new RegistrationItemProcessor("hybris");

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

		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(new BigDecimal("1"), BigDecimal.ONE, "SOURCE", "CRM", true));
		masterList.add(new Master(new BigDecimal("2"), BigDecimal.ONE, "SOURCE", "hybris", true));
		masterList.add(new Master(new BigDecimal("3"), BigDecimal.ONE, "SOURCE", "manual_update", true));
		masterList.add(new Master(new BigDecimal("4"), BigDecimal.ONE, "SOURCE", "citi_bank", true));
		masterList.add(new Master(new BigDecimal("5"), BigDecimal.ONE, "SOURCE", "SFMC", true));
		masterList.add(new Master(new BigDecimal("21"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT-CAN", true));
		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT AOL-CAN", true));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT OTH-CAN", true));

		MasterProcessor.setMasterList(masterList);
	}

	@Test
	public void validateRequired() throws Exception
	{
		input.setAsOfDate("02-02-2022 22:22:22");
		registrationItemProcessor.process(input);
		assertEquals("02-02-2022 22:22:22", input.getAsOfDate());
		input.setAsOfDate(null);

		ValidationException validationException = assertThrows(ValidationException.class, () -> {
			FileInboundStgTable fileInboundStgTable = registrationItemProcessor.process(input);
		});

		assertNotNull(validationException);
		assertTrue(validationException.getMessage().contains("The item processed has the above validations erros:"));
	}

	@Test
	public void validateIsNumberTest() throws Exception
	{
		input.setEmail_Permission("a");

		ValidationException validationException = assertThrows(ValidationException.class, () -> {
			FileInboundStgTable fileInboundStgTable = registrationItemProcessor.process(input);
		});

		assertNotNull(validationException);
		assertTrue(validationException.getMessage().contains("The item processed has the above validations erros:"));

	}

}
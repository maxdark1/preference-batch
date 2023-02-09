package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.InboundValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExactTargetEmailProcessorTest
{

	ExactTargetEmailProcessor exactTargetEmailProcessor;

	EmailOptOuts emailOptOuts;



	@BeforeEach
	void setUp()
	{
		exactTargetEmailProcessor = new ExactTargetEmailProcessor();

		Counters counter = new Counters(0, 0, 0);
		List<Counters> counters = new ArrayList<>();
		counters.add(counter);
		exactTargetEmailProcessor.setCounters(counters);

		emailOptOuts = new EmailOptOuts();
		emailOptOuts.setEmailAddress("email@address.com");
		emailOptOuts.setReason("SOME REASON");
		emailOptOuts.setStatus("unsubscribed");
		emailOptOuts.setDateUnsubscribed("09/19/2022 8 :11");
		emailOptOuts.setFileName("fileName_20220122.txt");

		InboundValidator
				.setValidEmailPattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(new BigDecimal("1"), BigDecimal.ONE, "SOURCE", "CRM", true, null));
		masterList.add(new Master(new BigDecimal("2"), BigDecimal.ONE, "SOURCE", "hybris", true, null));
		masterList.add(new Master(new BigDecimal("3"), BigDecimal.ONE, "SOURCE", "manual_update", true, null));
		masterList.add(new Master(new BigDecimal("4"), BigDecimal.ONE, "SOURCE", "citi_bank", true, null));
		masterList.add(new Master(new BigDecimal("5"), BigDecimal.ONE, "SOURCE", "SFMC", true, null));
		masterList.add(new Master(new BigDecimal("21"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT-CAN", true, null));
		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT AOL-CAN", true, null));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT OTH-CAN", true, null));
		masterList
				.add(new Master(new BigDecimal("24"), BigDecimal.TEN, "EMAIL_STATUS", "Hard Bounces", true, new BigDecimal("50")));
		masterList
				.add(new Master(new BigDecimal("25"), BigDecimal.TEN, "EMAIL_STATUS", "ET SPAM List", true, new BigDecimal("98")));

		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT AOL-CAN", true,
				new BigDecimal("50")));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT OTH-CAN", true,
				new BigDecimal("98")));

		MasterProcessor.setMasterList(masterList);
	}

	@Test
	void process() throws Exception
	{

		FileInboundStgTable fileInboundStgTable = exactTargetEmailProcessor.process(emailOptOuts);
		emailOptOuts.setEmailAddress(null);
		Counters counter = new Counters(0, 0, 0);
		List<Counters> counters = new ArrayList<>();
		counters.add(counter);
		exactTargetEmailProcessor.setCounters(counters);

		ValidationException validationException = assertThrows(ValidationException.class,
				() -> exactTargetEmailProcessor.process(emailOptOuts));


		assertTrue(validationException.getMessage().contains("The item processed has the above validation's errors:"));
		assertNotNull(fileInboundStgTable);
	}
}
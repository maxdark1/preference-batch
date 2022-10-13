package ca.homedepot.preference.processor;

import ca.homedepot.preference.util.validation.InboundValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import org.springframework.batch.item.validator.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExactTargetEmailProcessorTest
{

	ExactTargetEmailProcessor exactTargetEmailProcessor;

	EmailOptOuts emailOptOuts;

	@BeforeEach
	void setUp()
	{
		exactTargetEmailProcessor = new ExactTargetEmailProcessor();

		emailOptOuts = new EmailOptOuts();
		emailOptOuts.setEmailAddress("email@address.com");
		emailOptOuts.setReason("SOME REASON");
		emailOptOuts.setStatus("unsubscribed");
		emailOptOuts.setDateUnsubscribed("09/19/2022 8 :11");

		InboundValidator.setValidEmailPattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}

	@Test
	void process() throws Exception
	{

		FileInboundStgTable fileInboundStgTable = exactTargetEmailProcessor.process(emailOptOuts);
		emailOptOuts.setEmailAddress(null);

		ValidationException validationException = assertThrows(ValidationException.class, ()->{
			exactTargetEmailProcessor.process(emailOptOuts);
		});


		assertTrue(validationException.getMessage().contains("The item processed has the above validations erros:"));
		assertNotNull(fileInboundStgTable);
	}
}
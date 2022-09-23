package ca.homedepot.preference.processor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;

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
	}

	@Test
	void process() throws Exception
	{

		FileInboundStgTable fileInboundStgTable = exactTargetEmailProcessor.process(emailOptOuts);
		emailOptOuts.setEmailAddress(null);
		FileInboundStgTable fileInboundStgTableNull = exactTargetEmailProcessor.process(emailOptOuts);
		assertNotNull(fileInboundStgTable);
		assertNull(fileInboundStgTableNull);
	}
}
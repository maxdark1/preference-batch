package ca.homedepot.preference.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceOutboundDtoProcessorTest
{

	private PreferenceOutboundDtoProcessor preferenceOutboundDtoProcessor;

	@Test
	void testEquals()
	{
		preferenceOutboundDtoProcessor = new PreferenceOutboundDtoProcessor();
		Boolean test = preferenceOutboundDtoProcessor.equals(preferenceOutboundDtoProcessor);
		assertTrue(test);
	}

	@Test
	void canEqual()
	{
		preferenceOutboundDtoProcessor = new PreferenceOutboundDtoProcessor();
		Boolean test = preferenceOutboundDtoProcessor.canEqual(preferenceOutboundDtoProcessor);
		assertTrue(test);
	}

	@Test
	void testHashCode()
	{
		preferenceOutboundDtoProcessor = new PreferenceOutboundDtoProcessor();
		int test = preferenceOutboundDtoProcessor.hashCode();
		assertEquals(test, preferenceOutboundDtoProcessor.hashCode());
	}

	@Test
	void testToString()
	{
		preferenceOutboundDtoProcessor = new PreferenceOutboundDtoProcessor();
		String test = preferenceOutboundDtoProcessor.toString();
		assertNotNull(test);
	}


}
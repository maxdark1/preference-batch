package ca.homedepot.preference.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternalOutboundDtoTest
{

	InternalOutboundDto internalOutboundDto = new InternalOutboundDto();



	@Test
	void testEquals()
	{
		internalOutboundDto.setEmailAddr("test@test.com");
		Boolean test = internalOutboundDto.equals(internalOutboundDto);
		assertTrue(test);
	}

	@Test
	void canEqual()
	{
		internalOutboundDto.setEmailAddr("test@test.com");
		Boolean test = internalOutboundDto.canEqual(internalOutboundDto);
		assertTrue(test);
	}

	@Test
	void testHashCode()
	{
		internalOutboundDto.setEmailAddr("test@test.com");
		int test = internalOutboundDto.hashCode();
		assertEquals(test, -2112266818);
	}

	@Test
	void testToString()
	{
		internalOutboundDto.setEmailAddr("test@test.com");
		String test = internalOutboundDto.toString();
		assertEquals(test, internalOutboundDto.toString());
	}


}
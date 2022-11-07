package ca.homedepot.preference.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
		assertEquals(test, 910547593);
	}

	@Test
	void testToString()
	{
		internalOutboundDto.setEmailAddr("test@test.com");
		String test = internalOutboundDto.toString();
		assertEquals(test, internalOutboundDto.toString());
	}

	@Test
	void builder()
	{
		internalOutboundDto = InternalOutboundDto.builder().emailAddr("test@test.com").build();
		assertNotNull(internalOutboundDto);
	}
}
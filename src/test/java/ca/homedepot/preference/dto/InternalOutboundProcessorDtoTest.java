package ca.homedepot.preference.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InternalOutboundProcessorDtoTest
{

	InternalOutboundProcessorDto internalOutboundProcessorDto = new InternalOutboundProcessorDto();



	@Test
	void testEquals()
	{
		internalOutboundProcessorDto.setEmailAddr("test@test.com");
		Boolean test = internalOutboundProcessorDto.equals(internalOutboundProcessorDto);
		assertTrue(test);
	}

	@Test
	void canEqual()
	{
		internalOutboundProcessorDto.setEmailAddr("test@test.com");
		Boolean test = internalOutboundProcessorDto.canEqual(internalOutboundProcessorDto);
		assertTrue(test);
	}

	@Test
	void testHashCode()
	{
		internalOutboundProcessorDto.setEmailAddr("test@test.com");
		int test = internalOutboundProcessorDto.hashCode();
		assertEquals(test, internalOutboundProcessorDto.hashCode());
	}

	@Test
	void testToString()
	{
		internalOutboundProcessorDto.setEmailAddr("test@test.com");
		String test = internalOutboundProcessorDto.toString();
		assertEquals(test, internalOutboundProcessorDto.toString());
	}

}
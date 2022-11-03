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
		assertNotNull(test);
	}

	@Test
	void testToString()
	{
		internalOutboundProcessorDto.setEmailAddr("test@test.com");
		String test = internalOutboundProcessorDto.toString();
		assertNotNull(test);
	}

	@Test
	void builder()
	{
		internalOutboundProcessorDto = InternalOutboundProcessorDto.builder().emailAddr("test@test.com").build();
		assertNotNull(internalOutboundProcessorDto);
	}
}
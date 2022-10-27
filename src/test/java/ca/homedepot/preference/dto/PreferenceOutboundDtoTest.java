package ca.homedepot.preference.dto;

import com.github.javafaker.Bool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceOutboundDtoTest
{

	private PreferenceOutboundDto preferenceOutboundDto;

	@Test
	void testEquals()
	{
		preferenceOutboundDto = new PreferenceOutboundDto();
		Boolean test = preferenceOutboundDto.equals(preferenceOutboundDto);
		assertTrue(test);
	}

	@Test
	void canEqual()
	{
		preferenceOutboundDto = new PreferenceOutboundDto();
		Boolean test = preferenceOutboundDto.canEqual(preferenceOutboundDto);
		assertTrue(test);
	}

    @Test
    void testHashCode() {
        preferenceOutboundDto = new PreferenceOutboundDto();
        int test = preferenceOutboundDto.hashCode();
        assertEquals(test, preferenceOutboundDto.hashCode());
    }

	@Test
	void testToString()
	{
		preferenceOutboundDto = new PreferenceOutboundDto();
		String test = preferenceOutboundDto.toString();
		assertNotNull(test);
	}

	@Test
	void builder()
	{
		preferenceOutboundDto = PreferenceOutboundDto.builder().build();
		assertNotNull(preferenceOutboundDto);
	}
}
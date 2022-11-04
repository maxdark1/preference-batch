package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceOutboundProcessorTest
{
	@Mock
	PreferenceOutboundDto preferenceOutboundDto;
	@InjectMocks
	PreferenceOutboundProcessor preferenceOutboundProcessor;


	@Test
	void process() throws Exception
	{
		String split = SourceDelimitersConstants.SINGLE_DELIMITER_TAB;
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

		preferenceOutboundDto = Mockito.mock(PreferenceOutboundDto.class);

		Mockito.when(preferenceOutboundDto.getEmail()).thenReturn("test@test.com");
		Mockito.when(preferenceOutboundDto.getEffectiveDate()).thenReturn(date);
		Mockito.when(preferenceOutboundDto.getEarlyOptInDate()).thenReturn(date);
		Mockito.when(preferenceOutboundDto.getSourceId()).thenReturn(BigDecimal.TEN);

		PreferenceOutboundProcessor processor = new PreferenceOutboundProcessor();
		PreferenceOutboundDtoProcessor test = processor.process(preferenceOutboundDto);

		assertEquals(test.getEmail(), ("test@test.com" + split));
		assertEquals(test.getEffectiveDate(), (formatter.format(date) + split));
		assertEquals(test.getEarlyOptInDate(), (formatter.format(date) + split));
		assertEquals(test.getSourceId(), (BigDecimal.TEN + split));

	}
}
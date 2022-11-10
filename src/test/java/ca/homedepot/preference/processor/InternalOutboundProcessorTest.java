package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InternalOutboundProcessorTest
{

	@Mock
	InternalOutboundDto internalOutboundDto;

	@InjectMocks
	InternalOutboundProcessor internalOutboundProcessor;

	@Test
	void process() throws Exception
	{
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
		String split = SourceDelimitersConstants.DELIMITER_COMA;
		Date date = new Date();

		internalOutboundDto = Mockito.mock(InternalOutboundDto.class);
		Mockito.when(internalOutboundDto.getEmailAddr()).thenReturn("test@test.com");
		Mockito.when(internalOutboundDto.getCanPtcEffectiveDate()).thenReturn(date);
		Mockito.when(internalOutboundDto.getEarlyOptInIDate()).thenReturn(date);
		Mockito.when(internalOutboundDto.getEmailStatus()).thenReturn(BigDecimal.ZERO);
		InternalOutboundProcessor processor = new InternalOutboundProcessor();
		InternalOutboundProcessorDto test = processor.process(internalOutboundDto);
		assertEquals(test.getEmailAddr(), ("test@test.com" + split));
		assertEquals(test.getCanPtcEffectiveDate(), formatter.format(date) + split);
		assertEquals(test.getEarlyOptInDate(), formatter.format(date) + split);
		assertEquals(test.getEmailStatus(), ("00" + split));
	}
}
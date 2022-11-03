package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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
		String split = SourceDelimitersConstants.DELIMITER_COMA;
		internalOutboundDto = Mockito.mock(InternalOutboundDto.class);
		Mockito.when(internalOutboundDto.getEmailAddr()).thenReturn("test@test.com");
		InternalOutboundProcessor processor = new InternalOutboundProcessor();
		InternalOutboundProcessorDto test = processor.process(internalOutboundDto);
		assertEquals(test.getEmailAddr(), ("test@test.com" + split));
	}
}
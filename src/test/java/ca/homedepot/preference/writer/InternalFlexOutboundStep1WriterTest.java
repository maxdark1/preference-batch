package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.service.OutboundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.List;

import static org.mockito.Mockito.*;

class InternalFlexOutboundStep1WriterTest
{
	@Mock
	OutboundService outboundService;
	@Mock
	Logger log;
	@InjectMocks
	InternalFlexOutboundStep1Writer internalFlexOutboundStep1Writer;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testWrite()
	{
		doNothing().when(outboundService).internalFlexAttributes(any(InternalFlexOutboundDTO.class));
		doNothing().when(log).info(anyString());
		try
		{
			internalFlexOutboundStep1Writer.write(List.of(InternalFlexOutboundDTO.builder().build()));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
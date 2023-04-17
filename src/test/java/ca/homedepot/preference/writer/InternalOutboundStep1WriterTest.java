package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.repositories.entities.impl.OutboundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class InternalOutboundStep1WriterTest
{

	@Mock
	OutboundServiceImpl outboundService;

	@InjectMocks
	@Spy
	InternalOutboundStep1Writer internalOutboundStep1Writer;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void write() throws Exception
	{
		List<InternalOutboundDto> list = new ArrayList<>();
		list.add(new InternalOutboundDto());

		Mockito.when(outboundService.programCompliant(any(InternalOutboundDto.class))).thenReturn(1);
		internalOutboundStep1Writer.write(list);
		Mockito.verify(internalOutboundStep1Writer).write(list);
	}

}
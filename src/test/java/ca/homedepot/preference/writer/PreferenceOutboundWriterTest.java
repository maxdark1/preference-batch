package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.repositories.entities.impl.OutboundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class PreferenceOutboundWriterTest
{

	@Mock
	OutboundServiceImpl outboundService;

	@InjectMocks
	@Spy
	PreferenceOutboundWriter preferenceOutboundWriter;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void write() throws Exception
	{
		List<PreferenceOutboundDto> list = new ArrayList<>();
		list.add(new PreferenceOutboundDto());

		Mockito.doNothing().when(outboundService).preferenceOutbound(any(PreferenceOutboundDto.class));
		preferenceOutboundWriter.write(list);
		Mockito.verify(preferenceOutboundWriter).write(list);

	}

}
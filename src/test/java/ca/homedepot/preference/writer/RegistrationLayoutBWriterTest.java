package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.repositories.entities.impl.FileServiceImpl;
import ca.homedepot.preference.repositories.entities.impl.PreferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class RegistrationLayoutBWriterTest
{

	@Mock
	Logger log = LoggerFactory.getLogger(RegistrationLayoutBWriter.class);
	@Mock
	PreferenceServiceImpl preferenceService;
	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	RegistrationLayoutBWriter layoutBWriter;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void write() throws Exception
	{
		List<RegistrationRequest> items = new ArrayList<>();
		RegistrationResponse registration = new RegistrationResponse(List.of(new Response("1", "Published", "Done")));

		Mockito.when(preferenceService.preferencesSFMCEmailOptOutsLayoutB(items)).thenReturn(registration);
		Mockito.when(fileService.updateInboundStgTableStatus(eq(BigDecimal.ZERO), anyString(), anyString())).thenReturn(1);

		layoutBWriter.write(items);
		Mockito.verify(layoutBWriter).write(items);
	}
}
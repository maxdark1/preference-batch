package ca.homedepot.preference.writer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;

class RegistrationAPIWriterTest
{

	@Mock
	Logger log = LoggerFactory.getLogger(RegistrationAPIWriter.class);
	@Mock
	PreferenceServiceImpl preferenceService;

	@InjectMocks
	RegistrationAPIWriter registrationAPIWriter;

	@BeforeEach
	void setUp()
	{
		preferenceService = Mockito.mock(PreferenceServiceImpl.class);
		registrationAPIWriter = new RegistrationAPIWriter();
		registrationAPIWriter.setPreferenceService(preferenceService);
	}

	@Test
	void write() throws Exception
	{
		List<RegistrationRequest> items = new ArrayList<>();
		RegistrationResponse registration = new RegistrationResponse(List.of(new Response("1", "Published", "Done")));

		Mockito.when(preferenceService.preferencesRegistration(items)).thenReturn(registration);

		registrationAPIWriter.write(items);
	}


}
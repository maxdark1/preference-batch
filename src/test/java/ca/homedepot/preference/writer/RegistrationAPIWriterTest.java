package ca.homedepot.preference.writer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ca.homedepot.preference.service.impl.FileServiceImpl;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class RegistrationAPIWriterTest
{

	@Mock
	Logger log = LoggerFactory.getLogger(RegistrationAPIWriter.class);
	@Mock
	PreferenceServiceImpl preferenceService;
	@Mock
	FileServiceImpl fileService;
	@InjectMocks
	RegistrationAPIWriter registrationAPIWriter;

	@BeforeEach
	void setUp()
	{
		preferenceService = Mockito.mock(PreferenceServiceImpl.class);
		fileService = Mockito.mock(FileServiceImpl.class);
		registrationAPIWriter = new RegistrationAPIWriter();
		registrationAPIWriter.setPreferenceService(preferenceService);
		registrationAPIWriter.setFileService(fileService);
	}

	@Test
	void write() throws Exception
	{
		List<RegistrationRequest> items = new ArrayList<>();
		RegistrationResponse registration = new RegistrationResponse(List.of(new Response("1", "Published", "Done")));

		Mockito.when(preferenceService.preferencesRegistration(items)).thenReturn(registration);
		Mockito.when(fileService.updateInboundStgTableStatus(eq(BigDecimal.ZERO), anyString(), anyString())).thenReturn(1);

		registrationAPIWriter.write(items);
	}


}
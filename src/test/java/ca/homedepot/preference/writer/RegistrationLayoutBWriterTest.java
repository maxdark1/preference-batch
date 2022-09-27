package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class RegistrationLayoutBWriterTest {

    @Mock
    Logger log = LoggerFactory.getLogger(RegistrationLayoutBWriter.class);
    @Mock
    PreferenceServiceImpl preferenceService;

    @InjectMocks
    RegistrationLayoutBWriter layoutBWriter;

    @BeforeEach
    void setUp()
    {
        preferenceService = Mockito.mock(PreferenceServiceImpl.class);
        layoutBWriter = new RegistrationLayoutBWriter();
        layoutBWriter.setPreferenceService(preferenceService);
    }

    @Test
    void write() throws Exception
    {
        List<RegistrationRequest> items = new ArrayList<>();
        RegistrationResponse registration = new RegistrationResponse(List.of(new Response("1", "Published", "Done")));

        Mockito.when(preferenceService.preferencesSFMCEmailOptOutsLayoutB(items)).thenReturn(registration);

        layoutBWriter.write(items);
    }
}
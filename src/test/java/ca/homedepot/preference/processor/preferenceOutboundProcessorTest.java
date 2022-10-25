package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class preferenceOutboundProcessorTest {
    @Mock
    PreferenceOutboundDto preference = new PreferenceOutboundDto();
    preferenceOutboundProcessor proc = new preferenceOutboundProcessor();
    @Test
    void process() throws Exception {
        proc.process(preference);
    }
}
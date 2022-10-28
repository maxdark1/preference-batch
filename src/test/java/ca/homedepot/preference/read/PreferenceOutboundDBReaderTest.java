package ca.homedepot.preference.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceOutboundDBReaderTest {
    @Mock
    DataSource dataSource;

    @InjectMocks
    @Spy
    PreferenceOutboundDBReader preferenceOutboundDBReader;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void outboundDBReader() {
        assertNotNull(preferenceOutboundDBReader.outboundDBReader());
    }

    @Test
    void citiSuppressionDBTableReader() {
        assertNotNull(preferenceOutboundDBReader.citiSuppressionDBTableReader());
    }

    @Test
    void getDataSource() {
        assertNotNull(preferenceOutboundDBReader.getDataSource());
    }
}
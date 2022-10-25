package ca.homedepot.preference.writer;

import ca.homedepot.preference.service.OutboundService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PreferenceOutboundWriterTest {

    @Mock
    OutboundService outboundService;
    @InjectMocks
    PreferenceOutboundWriter writer = new PreferenceOutboundWriter();





    @Test
    @DisplayName("Should write the data into de DB")

    void testWrite() throws Exception {
        //Given



    }

    @Test
    void getDataSource() {
    }

    @Test
    void getOutboundService() {
    }

    @Test
    void setDataSource() {
    }

    @Test
    void setOutboundService() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}
package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class InternalOutboundProcessorTest {


    InternalOutboundDto internalOutboundDto;

    @InjectMocks
    InternalOutboundProcessor internalOutboundProcessor;

    @Test
    void process() throws Exception {
        internalOutboundDto = new InternalOutboundDto();
        internalOutboundDto.setEmailAddr("test@test.com");
        InternalOutboundProcessorDto test = internalOutboundProcessor.process(internalOutboundDto);
        assertEquals(test.getEmailAddr(),"test@test.com");
    }
}
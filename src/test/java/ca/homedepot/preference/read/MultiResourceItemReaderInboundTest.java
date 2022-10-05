package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiResourceItemReaderInboundTest {

    @Mock
    FileService fileService;

    @InjectMocks
    MultiResourceItemReaderInbound multiResourceItemReaderInbound;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void writeFile() {
        String fileName = "fileNAme";
        Boolean status = true;
        multiResourceItemReaderInbound.setSource("hybris");
        multiResourceItemReaderInbound.setFileService(fileService);
        List<Master> masterList = new ArrayList<>();

        masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE,"SOURCE", "hybris", true));
        masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE,"STATUS", "VALID", true));
        masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE,"STATUS", "INVALID", true));
        MasterProcessor.setMasterList(masterList);

        multiResourceItemReaderInbound.writeFile(fileName, status);
    }

}
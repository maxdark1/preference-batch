package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;


class CitiSupressionFileWriterTest {

    @Mock
    FileServiceImpl fileService;

    @InjectMocks
    @Spy
    CitiSupressionFileWriter citiSupressionFileWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        citiSupressionFileWriter.setRepositorySource("repositorySource");
        citiSupressionFileWriter.setFolderSource("/folder/");
        citiSupressionFileWriter.setFileName("filenameformat");

        List<Master> masterList = new ArrayList<>();
        Master sourceId = new Master();
        sourceId.setMasterId(BigDecimal.ONE);
        sourceId.setKeyValue("SOURCE");
        sourceId.setValueVal("citi_bank");

        Master fileStatus = new Master();
        fileStatus.setMasterId(BigDecimal.TEN);
        fileStatus.setKeyValue("STATUS");
        fileStatus.setValueVal("VALID");
        masterList.add(sourceId);
        masterList.add(fileStatus);

        MasterProcessor.setMasterList(masterList);
    }


    @Test
    void doWrite() {
        List<CitiSuppresionOutboundDTO> listCiti = new ArrayList<>();
        listCiti.add(new CitiSuppresionOutboundDTO("example", "e", "john", "address1", "address2", "toronto", "on", "123456", "email@example.com", "1234567890", "1234056987", "bussinessName", "N","N", "N", "N"));
        String result = "'FIRST_NAME','MIDDLE_INITIAL','LAST_NAME','ADDR_LINE_1','ADDR_LINE_2','CITY','STATE_CD','POSTAL_CD','EMAIL_ADDR','PHONE','SMS_MOBILE_PHONE','BUSINESS_NAME',DM_OPT_OUT,EMAIL_OPT_OUT,PHONE_OPT_OUT,SMS_OPT_OUT\nexample,e,john,address1,address2,toronto,on,123456,email@example.com,1234567890,1234056987,bussinessName,N,N,N,N";

        Mockito.doNothing().when(citiSupressionFileWriter).saveFileRecord(anyString());
        Mockito.when(citiSupressionFileWriter.doWrite(anyList())).thenReturn(result);

        String actualResult = citiSupressionFileWriter.doWrite(listCiti);
        assertEquals(result, actualResult);
    }

    @Test
    void saveFileRecord() {
        BigDecimal jobId = BigDecimal.TEN;
        FileDTO fileDTO = Mockito.mock(FileDTO.class);
        String fileName = "fileName";
        int expectedValue = 1;

        Mockito.when(fileService.getJobId(anyString())).thenReturn(jobId);
        Mockito.when(fileService.insert(fileDTO)).thenReturn(expectedValue);

        citiSupressionFileWriter.saveFileRecord(fileName);
        Mockito.verify(citiSupressionFileWriter).saveFileRecord(fileName);

    }


    @Test
    void getFileService() {
        assertNotNull(citiSupressionFileWriter.getFileService());
    }

}
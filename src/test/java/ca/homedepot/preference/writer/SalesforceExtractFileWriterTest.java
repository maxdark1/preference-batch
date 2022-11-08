package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class SalesforceExtractFileWriterTest {

    @Mock
    FileServiceImpl fileService;

    @InjectMocks
    @Spy
    SalesforceExtractFileWriter salesforceExtractFileWriter;

    File file = new File("repositorySource/folder");

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        salesforceExtractFileWriter.setRepositorySource("repositorySource");
        salesforceExtractFileWriter.setFolderSource("/folder/");
        salesforceExtractFileWriter.setFileNameFormat("ET_YYYYMMDD.TXT.PGP");
        salesforceExtractFileWriter.setJobName("Job_Name");

        List<Master> masterList = new ArrayList<>();
        Master sourceId = new Master();
        sourceId.setMasterId(BigDecimal.ONE);
        sourceId.setKeyValue("SOURCE_ID");
        sourceId.setValueVal("citisup");

        Master fileStatus = new Master();
        fileStatus.setMasterId(BigDecimal.TEN);
        fileStatus.setKeyValue("STATUS");
        fileStatus.setValueVal("VALID");
        masterList.add(sourceId);
        masterList.add(fileStatus);

        MasterProcessor.setMasterList(masterList);

        file.mkdirs();
        Format formatter = new SimpleDateFormat("YYYYMMDD");
        String fileName = "ET_YYYYMMDD".replace("YYYYMMDD", formatter.format(new Date()));
        File files = new File("repositorySource/folder/" + fileName);
        files.createNewFile();
    }

    @Test
    void saveFileRecord()
    {
        BigDecimal jobId = BigDecimal.TEN;
        FileDTO fileDTO = Mockito.mock(FileDTO.class);
        int expectedValue = 1;

        Mockito.when(fileService.getJobId(anyString())).thenReturn(jobId);
        Mockito.when(fileService.insert(fileDTO)).thenReturn(expectedValue);

        salesforceExtractFileWriter.saveFileRecord();
        Mockito.verify(salesforceExtractFileWriter).saveFileRecord();
    }

    @Test
    void setResource()
    {
        salesforceExtractFileWriter.setResource();
        Mockito.verify(salesforceExtractFileWriter).setResource();
    }

    @Test
    void getRepositorySource()
    {
        assertEquals("repositorySource", salesforceExtractFileWriter.getRepositorySource());
    }

    @Test
    void getFolderSource()
    {
        assertEquals("/folder/", salesforceExtractFileWriter.getFolderSource());
    }

    @Test
    void getFileNameFormat() { assertEquals("ET_YYYYMMDD.TXT.PGP", salesforceExtractFileWriter.getFileNameFormat()); }

    @Test
    void getJobName()
    {
        salesforceExtractFileWriter.setJobName("Job_Name");
        assertEquals("Job_Name", salesforceExtractFileWriter.getJobName());
    }

    @Test
    void getFileName()
    {
        salesforceExtractFileWriter.setFileName("fileName");
        assertEquals("fileName", salesforceExtractFileWriter.getFileName());
    }


    @Test
    void getFileService()
    {
        salesforceExtractFileWriter.setFileService(fileService);
        assertNotNull(salesforceExtractFileWriter.getFileService());
    }

}
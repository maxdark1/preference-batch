package ca.homedepot.preference.listener.skipers;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class SkipListenerLayoutCTest {

    @Mock
    FileServiceImpl fileService;
    @InjectMocks
    SkipListenerLayoutC skipListenerLayoutC;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        skipListenerLayoutC.setJobName("JOB_NAME");
    }

    @Test
    void onSkipInRead() {
        Throwable t = Mockito.mock(Throwable.class);
        skipListenerLayoutC.onSkipInRead(t);
    }

    @Test
    void onSkipInWrite() {
        Throwable t = Mockito.mock(Throwable.class);
        FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);

        skipListenerLayoutC.onSkipInWrite(fileInboundStgTable, t);
    }

    @Test
    void onSkipInProcess() {
        BigDecimal jobId = BigDecimal.ONE, fileId = BigDecimal.ONE;
        String fileName = "TEST";
        FileInboundStgTable fileInboundStgTable = Mockito.mock(FileInboundStgTable.class);
        InboundRegistration item = new InboundRegistration();
        item.setFileName(fileName);
        item.setLanguage_Preference("F");
        Throwable t = Mockito.mock(Throwable.class);

        Mockito.when(fileService.getJobId(anyString())).thenReturn(jobId);
        Mockito.when(fileService.getFile(eq(fileName), eq(jobId))).thenReturn(fileId);
        Mockito.when(fileService.insertInboundStgError(eq(fileInboundStgTable))).thenReturn(1);

        skipListenerLayoutC.onSkipInProcess(item, t);
    }

    @Test
    void getJobName() {
        assertEquals("JOB_NAME",skipListenerLayoutC.getJobName());
    }
}
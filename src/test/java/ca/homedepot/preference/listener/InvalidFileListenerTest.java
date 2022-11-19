package ca.homedepot.preference.listener;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvalidFileListenerTest {

    @Mock
    private InvalidFileListener invalidFileListener;


    @Test
    void beforeStepNotFiles() {
        StepExecution stepExecution = new StepExecution("readInboundCSVFileStep",new JobExecution(10L));
        invalidFileListener = new InvalidFileListener();
        invalidFileListener.beforeStep(stepExecution);
    }

    @Test
    void beforeStepWithFiles() {
        List<Resource> files = new ArrayList<>();
        Resource tmpFile = new FileSystemResource("test.txt");
        files.add(tmpFile);
        Map<String, List<Resource>> map = new HashMap<>();
        map.put("INVALID",files);
        StepExecution stepExecution = new StepExecution("readInboundCSVFileStep",new JobExecution(10L));
        invalidFileListener = new InvalidFileListener();
        invalidFileListener.setDirectory("Directory");
        invalidFileListener.setProcess("Project");
        InvalidFileListener spy = Mockito.spy(invalidFileListener);
        Mockito.doReturn(map).when(spy).getResources("Directory","Project");
        invalidFileListener.beforeStep(stepExecution);
    }

    @Test
    void afterStep() {
        StepExecution stepExecution = new StepExecution("readInboundCSVFileStep",new JobExecution(10L));
        invalidFileListener = new InvalidFileListener();
        invalidFileListener.afterStep(stepExecution);
    }

    @Test
    void checkExecution() {
        invalidFileListener = new InvalidFileListener();
        invalidFileListener.checkExecution("nothing");
        invalidFileListener.checkExecution("readInboundCSVFileStep");
    }



    @Test
    void getResources() {
        invalidFileListener = new InvalidFileListener();
        invalidFileListener.getResources("Directory","Project");
    }
}
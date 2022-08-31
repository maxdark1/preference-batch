package ca.homedepot.preference.config;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBatchTest
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {SchedulerConfig.class})
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
public class SchedulerConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Mock
    private JobBuilderFactory jobBuilderFactory;

    @Mock
    private StepBuilderFactory stepBuilderFactory;

    @Mock
    private DataSource dataSource;

    @Mock
    private BatchTasklet batchTasklet;

    @Mock
    private JobListener jobListener;

    @Mock
    private PlatformTransactionManager platformTransactionManager;

    @InjectMocks
    private SchedulerConfig schedulerConfig;

    @BeforeEach
    public void setUp() {
        jobBuilderFactory = Mockito.mock(JobBuilderFactory.class);
        stepBuilderFactory = Mockito.mock(StepBuilderFactory.class);
        dataSource = Mockito.mock(DataSource.class);
        batchTasklet = Mockito.mock(BatchTasklet.class);
        jobListener = Mockito.mock(JobListener.class);
        platformTransactionManager = Mockito.mock(PlatformTransactionManager.class);

        schedulerConfig = new SchedulerConfig(jobBuilderFactory, stepBuilderFactory, platformTransactionManager);

    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testInboundFileReader() {
        schedulerConfig.fileinRegistration = "OPTIN_STANDARD_FLEX_YYYYMMDD";
        assertNotNull(schedulerConfig);
        assertNotNull(schedulerConfig.inboundFileReader());

    }

    @Test
    public void inboundFileProcessor() {
        fail();
    }

    @Test
    public void inboundRegistrationFileWriter() {
        assertNotNull(schedulerConfig);
        assertNotNull(schedulerConfig.inboundRegistrationDBWriter());
    }

    @Test
    public void emailwriter() {
        fail();
    }

    @Test
    public void processJob() {
        fail();
    }

    @Test
    public void readInboundCSVFileStep() {
        fail();
    }

    @Test
    public void orderStep4() {
        fail();
    }


}
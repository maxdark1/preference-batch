package ca.homedepot.preference.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;

class SchedulerConfigTest
{

	@Mock
	public JobBuilderFactory jobBuilderFactory;
	@Mock
	public StepBuilderFactory stepBuilderFactory;
	@Mock
	public StepBuilder stepBuilder;
	@Mock
	public TaskletStep step;
	@Mock
	public RegistrationItemWriterListener writerListener;
	@Mock
	public DataSource dataSource;
	@Mock
	public BatchTasklet batchTasklet;
	@Mock
	public JobListener jobListener;
	@Mock
	public PlatformTransactionManager platformTransactionManager;
	@InjectMocks
	public SchedulerConfig schedulerConfig;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;
	@Mock
	private SimpleStepBuilder simpleStepBuilder;
	@Mock
	private JobLauncher jobLauncher;

	private static void setFinalStaticField(Class<?> clazz, String fieldName, Object value)
			throws NoSuchFieldException, IllegalAccessException
	{

		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);

		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, value);
	}

	@BeforeEach
	public void setUp() throws NoSuchFieldException, IllegalAccessException
	{
		jobBuilderFactory = Mockito.mock(JobBuilderFactory.class);
		stepBuilderFactory = Mockito.mock(StepBuilderFactory.class);
		dataSource = Mockito.mock(DataSource.class);
		batchTasklet = Mockito.mock(BatchTasklet.class);
		jobListener = Mockito.mock(JobListener.class);
		jobLauncher = Mockito.mock(JobLauncher.class);
		platformTransactionManager = Mockito.mock(PlatformTransactionManager.class);
		simpleStepBuilder = Mockito.mock(SimpleStepBuilder.class);
		stepBuilder = Mockito.mock(StepBuilder.class);
		writerListener = Mockito.mock(RegistrationItemWriterListener.class);
		step = Mockito.mock(TaskletStep.class);

		schedulerConfig = new SchedulerConfig(jobBuilderFactory, stepBuilderFactory, jobLauncher, platformTransactionManager);
		schedulerConfig.setDataSource(dataSource);
		schedulerConfig.chunkValue = 100;
		schedulerConfig.fileinRegistration = "TEST_FILE";
		schedulerConfig.setWriterListener(writerListener);

		setFinalStaticField(SchedulerConfig.class, "JOB_NAME_REGISTRATION_INBOUND", "registrationInbound");

	}

	@AfterEach
	public void tearDown()
	{
	}

	@Test
	public void testInboundFileReader() throws Exception
	{
		schedulerConfig.fileinRegistration = "OPTIN_STANDARD_FLEX_YYYYMMDD";
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundFileReader());

	}

	@Test
	public void inboundFileProcessor()
	{

		assertNotNull(schedulerConfig.inboundFileProcessor());
	}


	//    @Test
	//    public void processRegistrationInbound() throws Exception {
	//        schedulerConfig.processRegistrationInbound();
	//       Mockito.verify(schedulerConfig).processRegistrationInbound();
	//    }

	@Test
	public void inboundRegistrationFileWriter()
	{

		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundRegistrationDBWriter());
	}

	@Test
	public void readInboundCSVFileStep() throws Exception
	{

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(eq(100))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(FlatFileItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundCSVFileStep1());
	}


}
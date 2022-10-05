package ca.homedepot.preference.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.sql.DataSource;

import ca.homedepot.preference.listener.APIWriterListener;
import ca.homedepot.preference.listener.StepErrorLoggingListener;
import ca.homedepot.preference.processor.ExactTargetEmailProcessor;
import ca.homedepot.preference.writer.RegistrationAPIWriter;
import ca.homedepot.preference.writer.RegistrationLayoutBWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;

class SchedulerConfigTest
{

	@Mock
	public JobBuilderFactory jobBuilderFactory;

	@Mock
	public JobBuilder jobBuilder;

	@Mock
	public JobBuilderHelper jobBuilderHelper;

	@Mock
	public SimpleJobBuilder simpleJobBuilder;

	@Mock
	public FlowBuilder.TransitionBuilder transitionBuilder;

	@Mock
	public FlowJobBuilder flowJobBuilder;

	@Mock
	public FlowBuilder flowBuilder;

	@Mock
	public Job job;

	@Mock
	public StepBuilderFactory stepBuilderFactory;
	@Mock
	public StepBuilder stepBuilder;
	@Mock
	public TaskletStep step;
	@Mock
	public RegistrationItemWriterListener writerListener;
	@Mock
	RegistrationAPIWriter apiWriter;
	@Mock
	public DataSource dataSource;
	@Mock
	public BatchTasklet batchTasklet;
	@Mock
	public JobListener jobListener;
	@Mock
	public PlatformTransactionManager platformTransactionManager;
	@Mock
	APIWriterListener apiWriterListener;
	@Mock
	RegistrationLayoutBWriter layoutBWriter;
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
		apiWriterListener = Mockito.mock(APIWriterListener.class);
		layoutBWriter = Mockito.mock(RegistrationLayoutBWriter.class);

		jobBuilderHelper = Mockito.mock(JobBuilderHelper.class);
		jobBuilder = Mockito.mock(JobBuilder.class);
		simpleJobBuilder = Mockito.mock(SimpleJobBuilder.class);
		transitionBuilder = Mockito.mock(FlowBuilder.TransitionBuilder.class);
		flowBuilder = Mockito.mock(FlowBuilder.class);
		flowJobBuilder = Mockito.mock(FlowJobBuilder.class);
		job = Mockito.mock(Job.class);

		writerListener = Mockito.mock(RegistrationItemWriterListener.class);
		apiWriter = Mockito.mock(RegistrationAPIWriter.class);
		step = Mockito.mock(TaskletStep.class);

		schedulerConfig = new SchedulerConfig(jobBuilderFactory, stepBuilderFactory, jobLauncher, platformTransactionManager);
		schedulerConfig.setDataSource(dataSource);
		schedulerConfig.setJobListener(jobListener);
		schedulerConfig.chunkValue = 100;
		schedulerConfig.hybrisRegistrationFile = "TEST_FILE";
		schedulerConfig.setHybrisWriterListener(writerListener);
		schedulerConfig.setExactTargetEmailWriterListener(writerListener);
		schedulerConfig.setLayoutBWriter(layoutBWriter);
		setFinalStaticField(SchedulerConfig.class, "JOB_NAME_REGISTRATION_INBOUND", "registrationInbound");


	}

	@AfterEach
	public void tearDown()
	{
	}

	@Test
	public void testInboundFileReader() throws Exception
	{
		schedulerConfig.hybrisRegistrationFile = "OPTIN_STANDARD_FLEX_YYYYMMDD";
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundFileReader());
	}

	@Test
	public void testinboundEmailPreferencesSMFCReader(){
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader());
	}

	@Test
	public void testingestOptOutsGmailClientUnsubscribedReader(){
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.ingestOptOutsGmailClientUnsubscribedReader());
	}

	@Test
	public void testlineTokenizer(){
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.lineTokenizer());
	}

	@Test
	public void testInboundEmailPreferencesSMFCReader() throws Exception
	{
		schedulerConfig.hybrisRegistrationFile = "ET.CAN.YYYYMMDD";
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader());

	}

	@Test
	public void inboundFileProcessor()
	{

		assertNotNull(schedulerConfig.inboundFileProcessor());
	}

	// TODO NullPointerException
	@Test
	public void processRegistrationInbound() throws Exception
	{
		Mockito.when(jobBuilderFactory.get(eq("job_name"))).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.incrementer(new RunIdIncrementer())).thenReturn(jobBuilder);
		Mockito.when(jobBuilderHelper.listener(jobListener)).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.start(step)).thenReturn(simpleJobBuilder);
		Mockito.when(simpleJobBuilder.on(eq(PreferenceBatchConstants.COMPLETED_STATUS))).thenReturn(transitionBuilder);
		Mockito.when(transitionBuilder.to(eq(step))).thenReturn(flowBuilder);
		Mockito.when(flowBuilder.build()).thenReturn(flowJobBuilder);
		Mockito.when(flowJobBuilder.build()).thenReturn(job);

		//assertNotNull(schedulerConfig.registrationInbound());
	}

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
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundCSVFileStep1());
	}


	@Test
	public void readSFMCOptOutsStep1()
	{

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(eq(100))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(ExactTargetEmailProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSFMCOptOutsStep1("JobName"));
	}

	@Test
	void readInboundDBStep2() throws Exception {
		schedulerConfig.setApiWriter(apiWriter);
		schedulerConfig.setApiWriterListener(apiWriterListener);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(eq(100))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(apiWriterListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(apiWriter)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundBDStep2());
	}

	@Test
	void readDBSFMCOptOutsStep2(){
		schedulerConfig.setApiWriterListener(apiWriterListener);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(eq(100))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(apiWriterListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(RegistrationLayoutBWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readDBSFMCOptOutsStep2());
	}


	@Test
	void testExtactExactTargetEmailProcessor()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.extactExactTargetEmailProcessor());
	}



}
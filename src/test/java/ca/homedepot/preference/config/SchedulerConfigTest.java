package ca.homedepot.preference.config;

import static ca.homedepot.preference.constants.PreferenceBatchConstants.COMPLETED_STATUS;
import static ca.homedepot.preference.constants.PreferenceBatchConstants.FLEX_INTERNAL_HEADERS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.FieldPosition;
import java.text.Format;

import javax.sql.DataSource;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.listener.StepErrorLoggingListener;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutB;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutC;
import ca.homedepot.preference.processor.ExactTargetEmailProcessor;
import ca.homedepot.preference.processor.InternalOutboundProcessor;
import ca.homedepot.preference.processor.PreferenceOutboundProcessor;
import ca.homedepot.preference.read.PreferenceOutboundDBReader;
import ca.homedepot.preference.read.PreferenceOutboundReader;
import ca.homedepot.preference.service.OutboundService;
import ca.homedepot.preference.service.impl.OutboundServiceImpl;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import java.text.Format;
import java.text.SimpleDateFormat;


class SchedulerConfigTest
{

	@Mock
	JobBuilderFactory jobBuilderFactory;

	@Mock
	JobBuilder jobBuilder;

	@Mock
	JobBuilderHelper jobBuilderHelper;

	@Mock
	SimpleJobBuilder simpleJobBuilder;

	@Mock
	FlowBuilder.TransitionBuilder transitionBuilder;

	@Mock
	FlowJobBuilder flowJobBuilder;

	@Mock
	FlowBuilder flowBuilder;

	@Mock
	FaultTolerantStepBuilder faultTolerantStepBuilder;

	@Mock
	Job job;

	@Mock
	StepBuilderFactory stepBuilderFactory;
	@Mock
	StepBuilder stepBuilder;
	@Mock
	TaskletStep step;
	@Mock
	RegistrationItemWriterListener writerListener;
	@Mock
	RegistrationAPIWriter apiWriter;
	@Mock
	DataSource dataSource;
	@Mock
	BatchTasklet batchTasklet;
	@Mock
	JobListener jobListener;
	@Mock
	SkipListenerLayoutC skipListenerLayoutC;
	@Mock
	SkipListenerLayoutB skipListenerLayoutB;
	@Mock
	StepErrorLoggingListener stepErrorLoggingListener;
	@Mock
	PlatformTransactionManager platformTransactionManager;
	@Mock
	RegistrationLayoutBWriter layoutBWriter;

	@Mock
	PreferenceOutboundReader preferenceOutboundReader;
	@Mock
	PreferenceOutboundWriter preferenceOutboundWriter;

	@Mock
	PreferenceOutboundDBReader preferenceOutboundDBReader;

	@Mock
	PreferenceOutboundProcessor preferenceOutboundProcessor;

	@Mock
	PreferenceOutboundFileWriter preferenceOutboundFileWriter;

	@Mock
	InternalOutboundStep1Writer internalOutboundStep1Writer;
	@Mock
	InternalOutboundProcessor internalOutboundProcessor;
	@Mock
	InternalOutboundFileWriter internalOutboundFileWriter;
	@Spy
	@InjectMocks
	SchedulerConfig schedulerConfig;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;
	@Mock
	private SimpleStepBuilder simpleStepBuilder;
	@Mock
	private JobLauncher jobLauncher;


	private void setFinalStaticField(Class<?> clazz, String fieldName, Object value)
			throws NoSuchFieldException, IllegalAccessException
	{

		Field field = clazz.getDeclaredField(fieldName);
		boolean isAccesible = field.canAccess(schedulerConfig);
		field.setAccessible(true);

		Field modifiers = Field.class.getDeclaredField("modifiers");
		boolean isModifierAccesible = modifiers.canAccess(schedulerConfig);
		modifiers.setAccessible(true);
		modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, value);
		field.setAccessible(isAccesible);
		modifiers.setAccessible(isModifierAccesible);

	}

	@BeforeEach
	void setUp() throws NoSuchFieldException, IllegalAccessException
	{
		MockitoAnnotations.openMocks(this);

		preferenceOutboundReader.setDataSource(dataSource);
		preferenceOutboundDBReader.setDataSource(dataSource);

		schedulerConfig.setDataSource(dataSource);
		schedulerConfig.setJobListener(jobListener);
		schedulerConfig.setChunkValue(100);
		schedulerConfig.setChunkOutboundLoyalty(100);
		schedulerConfig.setChunkOutboundInternal(100);
		schedulerConfig.setCrmPath("crm/");
		schedulerConfig.setHybrisPath("hybris/");
		schedulerConfig.setFbSFMCPath("fbsfmc/");
		schedulerConfig.setCitiPath("citi/");
		schedulerConfig.setSalesforcePath("salesforce/");
		schedulerConfig.setLoyaltyCompliantPath("loyalty/");
		schedulerConfig.setFolderInbound("inbound/");
		schedulerConfig.setFolderError("error/");
		schedulerConfig.setDailyCompliantrepositorySource("daily/");
		schedulerConfig.setDailyCompliantNameFormat("nameFormat/");
		schedulerConfig.setWeeklyCompliantNameFormat("weekly");
		schedulerConfig.setInternalCANameFormat("ca");
		schedulerConfig.setInternalGardenNameFormat("garden");
		schedulerConfig.setInternalMoverNameFormat("mover");
		schedulerConfig.setInternalRepository("repoInternal/");
		schedulerConfig.setInternalFolder("folder");
		schedulerConfig.setHybrisCrmRegistrationFile("TEST_FILE");
		schedulerConfig.setCitiFileNameFormat("archive_YYYYMMDD.txt");
		schedulerConfig.setHybrisCrmRegistrationFile("hybris_file");
		schedulerConfig.setFileExtTargetEmail("extTarget_");
		schedulerConfig.setFileRegistrationFbSfmc("fbsfmc_");
		schedulerConfig.setSalesforcefileNameFormat("salesforce");
		schedulerConfig.setSfmcPath("sfmc/");
		schedulerConfig.setFolderOutbound("oubound/");
		schedulerConfig.setDailyCompliantfolderSource("daily/");
		schedulerConfig.setFolderProcessed("processed/");
		schedulerConfig.setExtensionRegex("txt");
		schedulerConfig.setEmailRegex("email@test.com");
		schedulerConfig.setHybrisWriterListener(writerListener);
		schedulerConfig.setExactTargetEmailWriterListener(writerListener);
		schedulerConfig.setFbsfmcWriterListener(writerListener);
		schedulerConfig.setLayoutBWriter(layoutBWriter);
		schedulerConfig.setSkipListenerLayoutB(skipListenerLayoutB);
		schedulerConfig.setSkipListenerLayoutC(skipListenerLayoutC);
		schedulerConfig.setStepListener(stepErrorLoggingListener);
		schedulerConfig.setBatchTasklet(batchTasklet);
		schedulerConfig.setApiWriter(apiWriter);
		schedulerConfig.setPreferenceOutboundReader(preferenceOutboundReader);
		schedulerConfig.setPreferenceOutboundDBReader(preferenceOutboundDBReader);
		schedulerConfig.setPreferenceOutboundWriter(preferenceOutboundWriter);
		schedulerConfig.setPreferenceOutboundProcessor(preferenceOutboundProcessor);
		schedulerConfig.setPreferenceOutboundFileWriter(preferenceOutboundFileWriter);
		schedulerConfig.setInternalOutboundStep1Writer(internalOutboundStep1Writer);
		schedulerConfig.setInternalOutboundProcessor(internalOutboundProcessor);
		schedulerConfig.setInternalOutboundFileWriter(internalOutboundFileWriter);
		//setFinalStaticField(schedulerConfig.getClass(), "JOB_NAME_REGISTRATION_INBOUND", "registrationInbound");


	}

	@AfterEach
	void tearDown()
	{
	}

	@Test
	void setUpTest()
	{
		assertNotNull(schedulerConfig);
		schedulerConfig.setUpListener();
	}

	@Test
	void testInboundFileReader() throws Exception
	{
		schedulerConfig.hybrisCrmRegistrationFile = "OPTIN_STANDARD_FLEX_YYYYMMDD";
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundFileReader());
	}

	@Test
	void testinboundEmailPreferencesSMFCReader()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader());
	}

	@Test
	void testingestOptOutsGmailClientUnsubscribedReader()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.ingestOptOutsGmailClientUnsubscribedReader());
	}

	@Test
	void testlineTokenizer()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.lineTokenizer(SourceDelimitersConstants.DELIMITER_TAB,
				InboundValidator.FIELD_OBJ_NAMES_INBOUND_REGISTRATION));
	}

	@Test
	void testInboundEmailPreferencesSMFCReader()
	{
		schedulerConfig.hybrisCrmRegistrationFile = "ET.CAN.YYYYMMDD";
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader());

	}

	@Test
	void inboundFileProcessor()
	{

		assertNotNull(schedulerConfig.layoutCProcessor("hybris"));
	}

	@Test
	void inboundRegistrationFileWriter()
	{

		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundRegistrationDBWriter());
	}

	@Test
	void readInboundCSVFileStep() throws Exception
	{

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundHybrisFileStep1("JOB_NAME"));
	}

	@Test
	void readInboundCSVFileCRMStep1() throws Exception
	{
		schedulerConfig.setCrmWriterListener(writerListener);


		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundCRMFileStep1("JOB_NAME"));
	}


	@Test
	void readSFMCOptOutsStep1()
	{

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(ExactTargetEmailProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutB.class))).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSFMCOptOutsStep1("JobName"));
	}

	@Test
	void readInboundFBSFMCFileStep1() throws Exception
	{
		schedulerConfig.setFbsfmcWriterListener(writerListener);


		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readInboundFBSFMCFileStep1("JOB_NAME"));
	}

	@Test
	void readInboundDBStep2() throws Exception
	{

		schedulerConfig.setApiWriter(apiWriter);
		schedulerConfig.setChunkLayoutC(10);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(10)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(apiWriter)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readLayoutCInboundBDStep2());
	}

	@Test
	void readDBSFMCOptOutsStep2()
	{
		schedulerConfig.setChunkLayoutB(20);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(20)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(RegistrationLayoutBWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readDBSFMCOptOutsStep2());
	}

	@Test
	void loyaltyComplaintDBReaderStep1()
	{
		JdbcCursorItemReader<InternalOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();

		when(preferenceOutboundReader.outboundLoyaltyComplaintWeekly()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.loyaltyComplaintDBReaderStep1());
	}

	@Test
	void loyaltyComplaintDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<LoyaltyCompliantDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.weeklyCompliantNameFormat = "archive_YYYYMMDD.txt";
		when(preferenceOutboundDBReader.loyaltyComplaintDBTableReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(FileWriterOutBound.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.loyaltyComplaintDBReaderFileWriterStep2());
	}

	@Test
	void salesforceExtractDBReaderStep1()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.salesforcefileNameFormat = "archive_YYYYMMDD.txt";
		schedulerConfig.setChunkOutboundSalesforce(100);
		when(preferenceOutboundReader.salesforceExtractOutboundDBReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.salesforceExtractDBReaderStep1());
	}

	@Test
	void salesforceExtractDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.salesforcefileNameFormat = "archive_YYYYMMDD.txt";
		schedulerConfig.setChunkOutboundSalesforce(100);
		when(preferenceOutboundDBReader.salesforceExtractDBTableReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(SalesforceExtractFileWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.salesforceExtractDBReaderFileWriterStep2());
	}

	@Test
	void citiSuppresionDBReaderStep1()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCiti(100);
		when(preferenceOutboundReader.outboundCitiSuppresionDBReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.citiSuppresionDBReaderStep1());
	}

	@Test
	void citiSuppresionDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCiti(100);
		when(preferenceOutboundDBReader.citiSuppressionDBTableReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(FileWriterOutBound.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.citiSuppresionDBReaderFileWriterStep2());
	}

	@Test
	void readSendPreferencesToCRMStep1()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCRM(100);

		when(preferenceOutboundReader.outboundDBReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(PreferenceOutboundWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToCRMStep1());
	}

	@Test
	void readSendPreferencesToCRMStep2()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCRM(100);
		when(preferenceOutboundDBReader.outboundDBReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(PreferenceOutboundProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(PreferenceOutboundFileWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToCRMStep2());
	}

	@Test
	void outboundLayoutComplaintWeekly()
	{
		assertNotNull(schedulerConfig.outboundLayoutComplaintWeekly());
	}

	@Test
	void testExtactExactTargetEmailProcessor()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.layoutBProcessor());
	}

	@Test
	void readSendPreferencesToInternalStep1()
	{
		JdbcCursorItemReader<InternalOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();

		when(preferenceOutboundReader.outboundInternalDBReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(InternalOutboundStep1Writer.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToInternalStep1());
	}

	@Test
	void readSendPreferencesToInternalStep2()
	{
		JdbcCursorItemReader<InternalOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		when(preferenceOutboundDBReader.outboundInternalDbReader()).thenReturn(jdbcCursorItemReader);

		when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.processor(any(InternalOutboundProcessor.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.writer(any(InternalOutboundFileWriter.class))).thenReturn(simpleStepBuilder);
		when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToInternalStep2());
	}

	@Test
	void testSendPreferencesToFlexInternalDestination() throws IOException
	{

		OutboundService outboundService = mock(OutboundServiceImpl.class);

		doNothing().when(outboundService).createFlexAttributesFile(anyString(), anyString(), anyString(), anyString());
		JobBuilder mockJobBuilder = mock(JobBuilder.class);
		SimpleJobBuilder mockSJobBuilder = mock(SimpleJobBuilder.class);
		FlowBuilder.TransitionBuilder transitionBuilder1 = mock(FlowBuilder.TransitionBuilder.class);
		FlowBuilder flowBuilder1 = mock(FlowBuilder.class);
		when(jobBuilderFactory.get(anyString())).thenReturn(mockJobBuilder);
		when(mockJobBuilder.incrementer(any(JobParametersIncrementer.class))).thenReturn(mockJobBuilder);
		when(mockJobBuilder.listener(any(JobListener.class))).thenReturn(mockJobBuilder);
		when(mockJobBuilder.start(any(Step.class))).thenReturn(mockSJobBuilder);
		when(mockSJobBuilder.on(anyString())).thenReturn(transitionBuilder1);
		when(transitionBuilder1.to(any(Step.class))).thenReturn(flowBuilder);
		when(flowBuilder1.build()).thenReturn(flowJobBuilder);
		when(flowJobBuilder.build()).thenReturn(job);

		Format formatter = mock(Format.class);
		when(formatter.format(any(), any(), any(FieldPosition.class))).thenReturn(new StringBuffer("20221125 101210"));

		try
		{
			assertNotNull(schedulerConfig.sendPreferencesToFlexInternalDestination());
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}
	}
}
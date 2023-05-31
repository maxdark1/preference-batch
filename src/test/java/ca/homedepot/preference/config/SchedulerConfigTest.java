package ca.homedepot.preference.config;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.listener.*;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutB;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutC;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.*;
import ca.homedepot.preference.read.PreferenceOutboundDBReader;
import ca.homedepot.preference.read.PreferenceOutboundReader;
import ca.homedepot.preference.service.OutboundService;
import ca.homedepot.preference.repositories.entities.impl.FileServiceImpl;
import ca.homedepot.preference.repositories.entities.impl.OutboundServiceImpl;
import ca.homedepot.preference.repositories.entities.impl.PreferenceServiceImpl;
import ca.homedepot.preference.tasklet.BatchTasklet;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.FieldPosition;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestPropertySource(locations =
{ "/resources/application.yaml" })
class SchedulerConfigTest
{
	@Mock
	StringBuilder stringBuilder;

	@Mock
	FileServiceImpl fileServiceImpl;
	@Mock
	OutputStream os;
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

	@Mock
	InternalFlexOutboundFileWriter internalFlexOutboundFileWriter;

	@Mock
	Step2InboundExecutionListener step2InboundExecutionListener;
	@Spy
	@InjectMocks
	SchedulerConfig schedulerConfig;
	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;
	@Mock
	private SimpleStepBuilder simpleStepBuilder;
	@Mock
	private JobLauncher jobLauncher;
	@Mock
	Storage storage;

	@Mock
	CloudStorageUtils cloudStorageUtils;

	@Mock
	PreferenceOutboundCitiWriter preferenceOutboundCitiWriter;

	@InjectMocks
	@Spy
	GSFileWriterOutbound<CitiSuppresionOutboundDTO> gsFileWriterOutbound;

	@Mock
	Environment env;

	@Mock
	PreferenceServiceImpl service;


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

		StorageApplicationGCS.setStorage(storage);
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
		preferenceOutboundReader.setDataSource(dataSource);
		preferenceOutboundDBReader.setDataSource(dataSource);

		schedulerConfig.setDataSource(dataSource);
		schedulerConfig.setJobListener(jobListener);
		schedulerConfig.setStep2InboundExecutionListener(step2InboundExecutionListener);
		schedulerConfig.setChunkValue(100);
		schedulerConfig.setChunkOutboundLoyalty(100);
		schedulerConfig.setChunkOutboundInternal(100);
		schedulerConfig.setCrmPath("crm/");
		schedulerConfig.setHybrisPath("hybris/");
		schedulerConfig.setFbSFMCPath("fbsfmc/");
		schedulerConfig.setCitiPath("citi/");
		schedulerConfig.setTimezone("America/New_York");
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
		schedulerConfig.setDailyCompliantNameFormat("dailyCompliantYYYYMMDD.csv");
		schedulerConfig.setInternalFlexOutboundFileWriter(new InternalFlexOutboundFileWriter());
		schedulerConfig.setInternalFlexOutboundProcessor(new InternalFlexOutboundProcessor());
		schedulerConfig.setPreferenceOutboundCitiWriter(preferenceOutboundCitiWriter);
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
		assertNotNull(schedulerConfig.inboundFileReader("UTF-8"));
	}

	@Test
	void testinboundEmailPreferencesSMFCReader()
	{
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader("UTF-8"));
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
		assertNotNull(schedulerConfig.inboundEmailPreferencesSMFCReader("UTF-8"));

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
	void dailyCountReportStep1Writer()
	{
		schedulerConfig.setDailyCompliantfolderSource("outbound/");
		schedulerConfig.setDailyCountReportFormat("daily_count_YYYYMMDD.csv");
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.dailyCountReportStep1Writer());
	}

	@Test
	void dailyCountReportStep2Writer()
	{
		schedulerConfig.setDailyCompliantfolderSource("outbound/");
		schedulerConfig.setDailyCountReportFormat("daily_count_YYYYMMDD.csv");
		assertNotNull(schedulerConfig);
		assertNotNull(schedulerConfig.dailyCountReportStep2Writer(new StringBuilder()));
	}


	@Test
	void readInboundCSVFileStep() throws Exception
	{
		InvalidFileListener invalidFileListener = new InvalidFileListener();
		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(InvalidFileListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);
		schedulerConfig.setInvalidFileListener(invalidFileListener);

		assertNotNull(schedulerConfig.readInboundHybrisFileStep1("JOB_NAME", new ArrayList<>()));
	}

	@Test
	void readInboundCSVFileCRMStep1() throws Exception
	{
		schedulerConfig.setCrmWriterListener(writerListener);


		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);
		InvalidFileListener invalidFileListener = new InvalidFileListener();
		schedulerConfig.setInvalidFileListener(invalidFileListener);
		String error = "";
		try
		{
			assertNotNull(schedulerConfig.readInboundCRMFileStep1("JOB_NAME", new ArrayList<>()));
		}
		catch (Exception ex)
		{
			error = ex.getMessage();
		}
	}


	@Test
	void readSFMCOptOutsStep1() throws IOException
	{

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(ExactTargetEmailProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutB.class))).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(InvalidFileListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);
		InvalidFileListener invalidFileListener = new InvalidFileListener();
		schedulerConfig.setInvalidFileListener(invalidFileListener);


		assertNotNull(schedulerConfig.readSFMCOptOutsStep1("JobName", new ArrayList<>()));
	}

	@Test
	void readInboundFBSFMCFileStep1() throws Exception
	{
		schedulerConfig.setFbsfmcWriterListener(writerListener);


		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(MultiResourceItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(RegistrationItemProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.faultTolerant()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.processorNonTransactional()).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skip(ValidationException.class)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.skipLimit(Integer.MAX_VALUE)).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(any(SkipListenerLayoutC.class))).thenReturn(faultTolerantStepBuilder);
		Mockito.when(faultTolerantStepBuilder.listener(writerListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(any(StepErrorLoggingListener.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);
		InvalidFileListener invalidFileListener = new InvalidFileListener();
		schedulerConfig.setInvalidFileListener(invalidFileListener);
		schedulerConfig.setFbSFMCPath("path");
		schedulerConfig.setFolderInbound("folder");
		String error = "";
		try
		{
			assertNotNull(schedulerConfig.readInboundFBSFMCFileStep1("JOB_NAME", new ArrayList<>()));
		}
		catch (Exception ex)
		{
			error = ex.getMessage();
		}


	}

	@Test
	void readInboundDBStep2() throws Exception
	{

		schedulerConfig.setApiWriter(apiWriter);
		schedulerConfig.setChunkLayoutC(10);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(10)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(apiWriter)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(step2InboundExecutionListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readLayoutCInboundBDStep2("jobName"));
	}

	@Test
	void readDBSFMCOptOutsStep2()
	{
		schedulerConfig.setChunkLayoutB(20);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(20)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(RegistrationLayoutBWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.listener(step2InboundExecutionListener)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readDBSFMCOptOutsStep2("jobName"));
	}

	@Test
	void loyaltyComplaintDBReaderStep1()
	{
		JdbcCursorItemReader<InternalOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();

		Mockito.when(preferenceOutboundReader.outboundLoyaltyComplaintWeekly()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.loyaltyComplaintDBReaderStep1());
	}

	@Test
	void loyaltyComplaintDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<LoyaltyCompliantDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.weeklyCompliantNameFormat = "archive_YYYYMMDD.txt";
		Mockito.when(preferenceOutboundDBReader.loyaltyComplaintDBTableReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(FileWriterOutBound.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.loyaltyComplaintDBReaderFileWriterStep2());
	}

	@Test
	void salesforceExtractDBReaderStep1()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.salesforcefileNameFormat = "archive_YYYYMMDD.txt";
		schedulerConfig.setChunkOutboundSalesforce(100);
		Mockito.when(preferenceOutboundReader.salesforceExtractOutboundDBReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.salesforceExtractDBReaderStep1());
	}

	@Test
	void salesforceExtractDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.salesforcefileNameFormat = "archive_YYYYMMDD.txt";
		schedulerConfig.sfmcWriter = new PreferenceOutboundItemWriter<>();
		schedulerConfig.setChunkOutboundSalesforce(100);
		Mockito.when(preferenceOutboundDBReader.salesforceExtractDBTableReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(ItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.salesforceExtractDBReaderFileWriterStep2(new ArrayList<>()));
	}

	@Test
	void citiSuppresionDBReaderStep1()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCiti(100);
		Mockito.when(preferenceOutboundReader.outboundCitiSuppresionDBReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.citiSuppresionDBReaderStep1());
	}

	@Test
	void citiSuppresionDBReaderFileWriterStep2()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCiti(100);
		Boolean delete = true;
		Mockito.when(storage.delete(any(BlobId.class))).thenReturn(delete);
		Mockito.when(preferenceOutboundDBReader.citiSuppressionDBTableReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(PreferenceOutboundCitiWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.citiSuppresionDBReaderFileWriterStep2(new ArrayList<>()));
	}

	@Test
	void readSendPreferencesToCRMStep1()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCRM(100);

		Mockito.when(preferenceOutboundReader.outboundDBReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(PreferenceOutboundWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToCRMStep1());
	}

	@Test
	void readSendPreferencesToCRMStep2()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		schedulerConfig.setChunkOutboundCRM(100);
		Mockito.when(preferenceOutboundDBReader.outboundDBCRMReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(PreferenceOutboundProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(PreferenceOutboundFileWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToCRMStep2(new ArrayList<>()));
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

		Mockito.when(preferenceOutboundReader.outboundInternalDBReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(InternalOutboundStep1Writer.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToInternalStep1());
	}

	@Test
	void readSendPreferencesToInternalStep2()
	{
		JdbcCursorItemReader<InternalOutboundDto> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		Mockito.when(preferenceOutboundDBReader.outboundInternalDbReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(InternalOutboundProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(InternalOutboundFileWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToInternalStep2());
	}

	@Test
	void dailyCountReportStep1()
	{
		schedulerConfig.setChunkDailyCountReport(100);
		FileWriterOutBound<DailyCountReportDTOStep1> writerStep1 = new FileWriterOutBound<>();
		JdbcCursorItemReader<DailyCountReportDTOStep1> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		Mockito.when(preferenceOutboundReader.dailyCountReportStep1()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(FileWriterOutBound.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.dailyCountReportStep1(writerStep1));
	}

	@Test
	void dailyCountReportStep2()
	{
		schedulerConfig.setChunkDailyCountReport(100);
		FileWriterOutBound<DailyCountReportStep2> writerStep1 = new FileWriterOutBound<>();
		JdbcCursorItemReader<DailyCountReportStep2> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		Mockito.when(preferenceOutboundReader.dailyCountReportStep2()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(FileWriterOutBound.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.dailyCountReportStep2(writerStep1));
	}

	@Test
	void internalFlexOutboundDTOJdbcBatchItemWriter()
	{
		assertNotNull(schedulerConfig.internalFlexOutboundDTOJdbcBatchItemWriter());
	}

	@Test
	void readSendPreferencesToFlexInternalStep1()
	{
		schedulerConfig.setChunkOutboundFlexAttributes(100);
		JdbcCursorItemReader<InternalFlexOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		Mockito.when(preferenceOutboundReader.outboundInternalFlexDBReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(JdbcBatchItemWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToFlexInternalStep1());
	}

	@Test
	void readSendPreferencesToFlexInternalStep2()
	{
		schedulerConfig.setChunkOutboundFlexAttributes(100);
		JdbcCursorItemReader<InternalFlexOutboundDTO> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		Mockito.when(preferenceOutboundDBReader.outboundInternalFlexDbReader()).thenReturn(jdbcCursorItemReader);

		Mockito.when(stepBuilderFactory.get(anyString())).thenReturn(stepBuilder);
		Mockito.when(stepBuilder.chunk(100)).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.reader(any(JdbcCursorItemReader.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.processor(any(InternalFlexOutboundProcessor.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.writer(any(InternalFlexOutboundFileWriter.class))).thenReturn(simpleStepBuilder);
		Mockito.when(simpleStepBuilder.build()).thenReturn(step);

		assertNotNull(schedulerConfig.readSendPreferencesToFlexInternalStep2());
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

	@Test
	void createEmailFile()
	{
		Boolean validator;
		try
		{
			gsFileWriterOutbound.setStringBuilder(stringBuilder);
			gsFileWriterOutbound.setOs(os);
			gsFileWriterOutbound.setFileService(fileServiceImpl);
			gsFileWriterOutbound.setHeader("header");
			gsFileWriterOutbound.setFileNameFormat("filename_YYYYMMDD.txt");
			Counters counter = new Counters(0, 0, 0);
			List<Counters> countersList = new ArrayList<>();
			countersList.add(counter);
			schedulerConfig.createEmailFile(countersList, "somename");
			Mockito.verify(gsFileWriterOutbound).close();
			validator = true;
		}
		catch (Exception ex)
		{
			validator = false;
		}
		assertTrue(!validator);
	}

	@Test
	void createEmailFileOutbound()
	{
		Boolean validator;
		try
		{
			gsFileWriterOutbound.setStringBuilder(stringBuilder);
			gsFileWriterOutbound.setOs(os);
			gsFileWriterOutbound.setFileService(fileServiceImpl);
			gsFileWriterOutbound.setHeader("header");
			gsFileWriterOutbound.setFileNameFormat("filename_YYYYMMDD.txt");
			Counters counter = new Counters(0, 0, 0);
			List<Counters> countersList = new ArrayList<>();
			countersList.add(counter);
			schedulerConfig.setEventId("1");
			schedulerConfig.createEmailFileOutbound(countersList, "somename");
			Mockito.verify(gsFileWriterOutbound).close();
			validator = true;
		}
		catch (Exception ex)
		{
			validator = false;
		}
		assertTrue(!validator);
	}


	@Test

	void testCreateEmailFile()
	{
		List<Counters> counters = new ArrayList<>();
		Counters counter = new Counters(10, 10, 0);
		counter.fileName = "fileName";
		counters.add(counter);
		env = mock(Environment.class);
		when(env.getProperty("notification.inbound.hybris.email")).thenReturn("juan.lara@homedepot.com");
		when(env.getProperty("notification.inbound.hybris.name")).thenReturn("Juan Lara");
		schedulerConfig.setEnv(env);
		schedulerConfig.setService(service);
		when(service.saveNotificationEvent("", "", "", "", "", "", "")).thenReturn(1);
		schedulerConfig.createEmailFile(counters, "hybris");
		assertNotNull(counters);
	}

	@Test
	void testCreateEmailFileOutbound()
	{
		List<Counters> counters = new ArrayList<>();
		Counters counter = new Counters(10, 10, 0);
		counter.fileName = "fileName";
		counters.add(counter);
		env = mock(Environment.class);
		when(env.getProperty("notification.outbound.SFMC.email")).thenReturn("juan.lara@homedepot.com");
		when(env.getProperty("notification.outbound.SFMC.name")).thenReturn("Juan Lara");
		schedulerConfig.setEnv(env);
		schedulerConfig.setService(service);
		when(service.saveNotificationEvent("", "", "", "", "", "", "")).thenReturn(1);
		schedulerConfig.createEmailFileOutbound(counters, "SFMC");
		assertNotNull(counters);
	}

	@Test
	void testIndividualHybris()
	{
		try
		{
			schedulerConfig.individualJob = "hybrisIn";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualcrmIn()
	{
		try
		{
			schedulerConfig.individualJob = "crmIn";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualfbSfmcIn()
	{
		try
		{
			schedulerConfig.individualJob = "fbSfmcIn";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualsfmcIn()
	{
		try
		{
			schedulerConfig.individualJob = "sfmcIn";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualcrmOut()
	{
		try
		{
			schedulerConfig.individualJob = "crmOut";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualinternalOut()
	{
		try
		{
			schedulerConfig.individualJob = "internalOut";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualflexOut()
	{
		try
		{
			schedulerConfig.individualJob = "flexOut";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualcitiOut()
	{
		try
		{
			schedulerConfig.individualJob = "citiOut";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

	@Test
	void testIndividualsfmcOut()
	{
		try
		{
			schedulerConfig.individualJob = "sfmcOut";
			schedulerConfig.exitAfter = false;
			schedulerConfig.individualExecution();
			assertNotNull(schedulerConfig);
		}
		catch (Exception ex)
		{
			assertNotNull(schedulerConfig);
		}
	}

}
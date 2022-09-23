package ca.homedepot.preference.config;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.processor.ExactTargetEmailProcessor;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.RegistrationAPIWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * The type Scheduler config.
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig extends DefaultBatchConfigurer
{
	private static final String JOB_NAME_REGISTRATION_INBOUND = "registrationInbound";

	private static final String JOB_NAME_EXTACT_TARGET_EMAIL = "IngestSFMCOptOuts";
	/**
	 * The Job builder factory.
	 */
	private final JobBuilderFactory jobBuilderFactory;
	/**
	 * The Step builder factory.
	 */
	private final StepBuilderFactory stepBuilderFactory;
	private final JobLauncher jobLauncher;
	/**
	 * The Transaction manager.
	 */
	@Qualifier("visitorTransactionManager")
	private final PlatformTransactionManager transactionManager;
	@Value("${analytic.file.registration}")
	String registrationAnalyticsFile;
	@Value("${analytic.file.email}")
	String emailAnalyticsFile;
	@Value("${process.analytics.chunk}")
	Integer chunkValue;
	@Value("${inbound.source.path}")
	String path;
	@Value("${inbound.file.registration}")
	String fileinRegistration;

	@Value("${inbound.file.ingestSFMC}")
	String fileExtTargetEmail;
	@Value("${sub.activity.days}")
	Integer subactivity;
	@Autowired
	private DataSource dataSource;
	/**
	 * The Batch tasklet.
	 */
	@Autowired
	private BatchTasklet batchTasklet;
	/**
	 * The Job listener.
	 */
	@Autowired
	private JobListener jobListener;
	@Autowired
	private RegistrationItemWriterListener hybrisWriterListener;

	@Autowired
	private RegistrationItemWriterListener exactTargetEmailWriterListener;
	@Autowired
	private RegistrationAPIWriter apiWriter;
	@Autowired
	private MasterProcessor masterProcessor;

	@Autowired
	public void setUpListener()
	{
		jobListener.setPreferenceService(batchTasklet.getBackinStockService());

		hybrisWriterListener.setFileName(fileinRegistration);
		hybrisWriterListener.setJobName(JOB_NAME_REGISTRATION_INBOUND);


		apiWriter.setPreferenceService(batchTasklet.getBackinStockService());


		exactTargetEmailWriterListener.setFileName(fileExtTargetEmail);
		//exactTargetEmailWriterListener.setJobName(JOB_NAME_EXTACT_TARGET_EMAIL);

	}

	public void setHybrisWriterListener(RegistrationItemWriterListener hybrisWriterListener)
	{
		this.hybrisWriterListener = hybrisWriterListener;
	}

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}
	/*
	 * SCHEDULING JOBS
	 */

	@PostConstruct
	public void getMasterInfo()
	{
		masterProcessor.getMasterInfo();
		hybrisWriterListener.setSourceIDMasterObj(MasterProcessor.getSourceId("SOURCE","hybris"));
		exactTargetEmailWriterListener.setSourceIDMasterObj(MasterProcessor.getSourceId("SOURCE","SFMC"));
	}
	///***************************************************

	@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_INBOUND, String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(registrationInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	//@Scheduled(cron = "${cron.job.ingestSFMC}")
	public void processsSFMCOptOutsEmail() throws Exception
	{
		log.info(" Ingest SFMC Opt-Outs Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_EXTACT_TARGET_EMAIL, String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(sfmcOptOutsEmailClient(), param);
		log.info("Ingest SFMC Opt-Outs Job finished with status :" + execution.getStatus());
	}

	/*
	 * Read inbound files
	 */
	@Bean
	public FlatFileItemReader<InboundRegistration> inboundFileReader()
	{
		return new FlatFileItemReaderBuilder<InboundRegistration>().name("inboundFileReader")
				.resource(new FileSystemResource(path+"INBOUND\\"+fileinRegistration)).delimited().delimiter("|").names(InboundValidator.FIELD_NAMES_REGISTRATION)
				.targetType(InboundRegistration.class).linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(InboundValidator.lineCallbackHandler()).build();
	}

	public FlatFileItemReader<EmailOptOuts> inboundEmailPreferencesSMFCReader()
	{


		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.resource(new FileSystemResource(path+"INBOUND\\"+fileExtTargetEmail)).lineTokenizer(new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB){{
					setNames(ExactTargetEmailValidation.FIELD_NAMES_SFMC_OPTOUTS);
				}})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<EmailOptOuts>(){{
					setTargetType(EmailOptOuts.class);
				}})
				.linesToSkip(1)
				/* Validation file's header */

				.skippedLinesCallback(ExactTargetEmailValidation.lineCallbackHandler())
			.build();
	}



	///****************************************************************************

	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReader()
	{
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS);
		reader.setRowMapper(new RegistrationrowMapper());

		return reader;
	}

	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReaderSFMC()
	{
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS);
		reader.setRowMapper(new SFMCRowMapper());

		return reader;
	}


	@Bean
	public RegistrationItemProcessor inboundFileProcessor()
	{
		return new RegistrationItemProcessor();
	}

	@Bean
	public ExactTargetEmailProcessor extactExactTargetEmailProcessor()
	{
		return new ExactTargetEmailProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<FileInboundStgTable> inboundRegistrationDBWriter()
	{
		JdbcBatchItemWriter<FileInboundStgTable> writer = new JdbcBatchItemWriter<>();

		writer.setDataSource(dataSource);
		writer.setSql(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_REGISTRATION);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<FileInboundStgTable>());

		return writer;
	}


	/**
	 * Process job.
	 *
	 * @return the job
	 */

	@Bean
	public Job registrationInbound() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCSVFileStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS).to(readInboundBDStep2()).build()
				.build();

	}

	@Bean
	public Job sfmcOptOutsEmailClient() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_EXTACT_TARGET_EMAIL).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readSFMCOptOutsStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readDBSFMCOptOutsStep2())
				.build().build();
	}

	/**
	 * Order step 1 step.
	 *
	 * @return the step
	 */

	@Bean
	public Step readInboundCSVFileStep1() throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(inboundFileReader()).processor(inboundFileProcessor()).listener(hybrisWriterListener)
				.writer(inboundRegistrationDBWriter()).build();
	}

	@Bean
	public Step readInboundBDStep2() throws Exception
	{
		return stepBuilderFactory.get("readInboundBDStep").<RegistrationRequest, RegistrationRequest> chunk(chunkValue)
				.reader(inboundDBReader()).writer(apiWriter).build();
	}

	/*
	 * SFMC Opt-Outs STEPS
	 */
	@Bean
	public Step readSFMCOptOutsStep1()
	{
		return stepBuilderFactory.get("readSFMCOptOutsStep1").<EmailOptOuts, FileInboundStgTable> chunk(chunkValue)
				.reader(inboundEmailPreferencesSMFCReader()).processor(extactExactTargetEmailProcessor())
				.listener(exactTargetEmailWriterListener).writer(inboundRegistrationDBWriter()).build();
	}


	@Bean
	public Step readDBSFMCOptOutsStep2()
	{

		return stepBuilderFactory.get("readDBSFMCOptOutsStep2").<RegistrationRequest, RegistrationRequest>chunk(chunkValue).reader(inboundDBReaderSFMC())
				.writer(layoutBWriter).build();

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * override to do not set data a source. Spring initialize will use a Map based JobRepository instead of database. This
	 * is to avoid spring batch from creating batch related metadata tables in the scheduling schemas.
	 */

	@Override
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * Getting actual date for processing.
	 */

	private String getActualDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Instant now = Instant.now();
		Instant yesterday = now.minus(subactivity, ChronoUnit.DAYS);
		return sdf.format(Date.from(yesterday));
	}


	public String getFile(String baseName)
	{

		if (fileinRegistration.equals(baseName))
		{
			FileUtil.setRegistrationFile(baseName);
		}
		else
		{
			FileUtil.setFileExtTargetEmail(baseName);
		}
		return baseName;
	}




}

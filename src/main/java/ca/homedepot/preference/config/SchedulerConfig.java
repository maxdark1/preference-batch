package ca.homedepot.preference.config;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.RegistrationAPIWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.launch.JobLauncher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;


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
	/**
	 * The Job builder factory.
	 */
	private final JobBuilderFactory jobBuilderFactory;

	/**
	 * The Step builder factory.
	 */
	private final StepBuilderFactory stepBuilderFactory;



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


	private final JobLauncher jobLauncher;

	@Value("${analytic.file.registration}")
	String registrationAnalyticsFile;

	@Value("${analytic.file.email}")
	String emailAnalyticsFile;

	@Value("${process.analytics.chunk}")
	Integer chunkValue;

	@Value("${inbound.file.registration}")
	String fileinRegistration;

	@Value("${sub.activity.days}")
	Integer subactivity;


	@Autowired
	private RegistrationItemWriterListener writerListener;


	private static final String JOB_NAME_REGISTRATION_INBOUND = "registrationInbound";

	@Autowired
	private RegistrationAPIWriter apiWriter;

	@Autowired
	private MasterProcessor masterProcessor;




	@Autowired
	public void setUpListener(){
		jobListener.setDataSource(dataSource);
		jobListener.setPreferenceService(batchTasklet.getBackinStockService());

		writerListener.setFileRegistration(fileinRegistration);
		writerListener.setJobName(JOB_NAME_REGISTRATION_INBOUND);
		writerListener.setJobListener(jobListener);
		writerListener.setDataSource(dataSource);
		apiWriter.setPreferenceService(batchTasklet.getBackinStockService());
	}

	public void setWriterListener(RegistrationItemWriterListener writerListener){
		this.writerListener = writerListener;
	}


	@PostConstruct
	public void getMasterInfo(){
		masterProcessor.getMasterInfo();
		writerListener.setMasterProcessor(masterProcessor);
	}


	/*
	* 	SCHEDULING JOBS
	* */

	@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :"+ new Date());
		JobParameters param = new JobParametersBuilder().addString(JOB_NAME_REGISTRATION_INBOUND,
				String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(registrationInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}
	///***************************************************

	/*
	 * Read inbound file
	 */
	@Bean
	public FlatFileItemReader<InboundRegistration> inboundFileReader() throws Exception {


		return new FlatFileItemReaderBuilder<InboundRegistration>()
				.name("inboundFileReader")
				.resource(new FileSystemResource(fileinRegistration))
				.delimited()
				.delimiter("|")
				.names(InboundValidator.FIELD_NAMES)
				.targetType(InboundRegistration.class)
				.linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(InboundValidator.lineCallbackHandler())
				.build();
	}

	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReader() {
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql( "SELECT * FROM pcam.hdpc_file_inbound_stg WHERE file_id = (" + SqlQueriesConstants.SQL_SELECT_LAST_FILE +")");
		reader.setRowMapper(new RegistrationrowMapper());

		return reader;
	}

	@Bean
	public RegistrationItemProcessor inboundFileProcessor()
	{
		return new RegistrationItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<OutboundRegistration> inboundRegistrationDBWriter(){
		JdbcBatchItemWriter<OutboundRegistration> writer = new JdbcBatchItemWriter<>();

		writer.setDataSource(dataSource);
		writer.setSql(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_REGISTRATION);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<OutboundRegistration>());

		return writer;
	}


	/**
	 * The Transaction manager./*
	 */
	@Qualifier("visitorTransactionManager")
	private final PlatformTransactionManager transactionManager;


	/**
	 * Process job.
	 *
	 * @return the job
	 */


	@Bean
	public Job registrationInbound() throws Exception {
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_INBOUND)
				.incrementer(new RunIdIncrementer())
				.listener(jobListener)
				.start(readInboundCSVFileStep1())
				.on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readInboundBDStep2())
				.build()
				.build();

	}

	/**
	 * Order step 1 step.
	 *
	 * @return the step
	 */

	@Bean
	public Step readInboundCSVFileStep1() throws Exception {
		return stepBuilderFactory.get("readInboundCSVFileStep")
				.<InboundRegistration, OutboundRegistration> chunk(chunkValue)
 				.reader(inboundFileReader())
				.processor(inboundFileProcessor())
				.listener(writerListener)
				.writer(inboundRegistrationDBWriter())
				.build();
	}

	@Bean
	public Step readInboundBDStep2() throws Exception{
		return stepBuilderFactory.get("readInboundBDStep")
				.<RegistrationRequest, RegistrationRequest> chunk(chunkValue)
				.reader(inboundDBReader())
				.writer(apiWriter)
				.build();
	}



	@Bean
	public Step orderStep4()
	{

		return stepBuilderFactory.get("orderStep4").tasklet(batchTasklet).transactionManager(transactionManager).build();

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

	/**
	 * Gets file name.
	 *
	 * @param baseName
	 *           the base name
	 * @return the file name
	 */
	private String getFile(String baseName)
	{
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		String fileName = baseName + "_" + sdf.format(currentDate) + ".csv";
		if (registrationAnalyticsFile.equals(baseName))
		{
			FileUtil.setRegistrationFile(fileName);
		}
		else
		{
			FileUtil.setEmailanalyticsFile(fileName);
		}
		File file = new File(fileName);
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			log.error("Error while creating file " + fileName);
		}
		return file.getName();
	}


}

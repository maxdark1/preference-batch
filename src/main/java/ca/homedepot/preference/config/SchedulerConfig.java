package ca.homedepot.preference.config;


import javax.sql.DataSource;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.listener.RegistrationItemReaderListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.repositories.entities.FileEntity;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.RegistrationAPIWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.*;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * The type Scheduler config.
 */
@Slf4j
@Configuration
@EnableBatchProcessing
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

	private RegistrationItemReaderListener registrationItemReaderListener;
	private static final String JOB_NAME_REGISTRATION_INBOUND = "registrationInbound";

	@Autowired
	private RegistrationAPIWriter apiWriter;

	@Autowired
	public void setRegistrationItemReaderListener(){
		registrationItemReaderListener = new RegistrationItemReaderListener();
		registrationItemReaderListener.setDataSource(dataSource);
		registrationItemReaderListener.setPreferenceService(batchTasklet.getBackinStockService());

	}

	@Autowired
	public void setUpListener(){
		jobListener.setDataSource(dataSource);
		jobListener.setPreferenceService(batchTasklet.getBackinStockService());
		writerListener.setFileRegistration(fileinRegistration);
		writerListener.setJobName(JOB_NAME_REGISTRATION_INBOUND);
		writerListener.setJobListener(jobListener);
		writerListener.setDataSource(dataSource);
	}

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
		reader.setSql("SELECT * FROM pcam.hdpc_file_inbound_stg ORDER BY file_id DESC");
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
				.next(readInboundBDStep2())
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
				.listener(registrationItemReaderListener)
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

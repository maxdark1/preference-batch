package ca.homedepot.preference.config;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
import ca.homedepot.preference.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.EmailAnalytics;
import ca.homedepot.preference.processor.EmailAnalyticsItemProcessor;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * The type Scheduler config.
 */
@Slf4j
@Configuration
//@Import({DatasourceConfiguration.class})
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

	private static final String[] FIELD_NAMES = new String[]
	{ "Language_Preference", "AsOfDate", "Email_Address", "Email_Permission", "Phone_Permission", "Phone_Number",
			"Phone_Extension", "Title", "First_Name", "Last_Name", "Address_1", "Address_2", "City", "Province", "Postal_Code",
			"Mail_Permission", "EmailPrefHDCA", "GardenClub", "EmailPrefPRO", "NewMover", "For_Future_Use", "Source_ID", "SMS_Flag",
			"Fax_Number", "Fax_Extension", "Content_1", "Value_1", "Content_2", "Value_2", "Content_3", "Value_3", "Content_4",
			"Value_4", "Content_5", "Value_5", "Content_6", "Value_6", "Content_7", "Value_7", "Content_8", "Value_8", "Content_9",
			"Value_9", "Content_10", "Value_10", "Content_11", "Value_11", "Content_12", "Value_12", "Content_13", "Value_13",
			"Content_14", "Value_14", "Content_15", "Value_15", "Content_16", "Value_16", "Content_17", "Value_17", "Content_18",
			"Value_18", "Content_19", "Value_19", "Content_20", "Value_20" };

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

	//    @Bean
	//    public JdbcCursorItemReader<Registration> reader() {
	//        JdbcCursorItemReader<Registration> cursorItemReader = new JdbcCursorItemReader<Registration>();
	//        cursorItemReader.setDataSource(dataSource);
	//
	//        cursorItemReader
	//                .setSql("SELECT createdts,article_id,action_type,email_id FROM registration_analytics WHERE DATE(createdts) = '" +
	//                        getActualDate() + "'");
	//        cursorItemReader.setRowMapper(new RegistrationrowMapper());
	//
	//        return cursorItemReader;
	//    }


	/*
	 * Read inbound file
	 */
	@Bean
	public FlatFileItemReader<InboundRegistration> inboundFileReader()
	{
		return new FlatFileItemReaderBuilder().name("inboundFileReader").resource(new ClassPathResource(fileinRegistration))
				.delimited().names(FIELD_NAMES).fieldSetMapper(new BeanWrapperFieldSetMapper()
				{
					{
						setTargetType(InboundRegistration.class);
					}
				}).build();
	}

	@Bean
	public RegistrationItemProcessor inboundFileProcessor()
	{
		return new RegistrationItemProcessor();
	}

	//    @Bean
	//    public EmailAnalyticsItemProcessor emailprocessor() {
	//        return new EmailAnalyticsItemProcessor();
	//    }

	@Bean
	public FlatFileItemWriter<OutboundRegistration> inboundFileWriter()
	{
		FlatFileItemWriter<OutboundRegistration> writer = new FlatFileItemWriter<OutboundRegistration>();

		writer.setResource(new FileSystemResource(getFile(registrationAnalyticsFile)));
		writer.setHeaderCallback(new FlatFileHeaderCallback()
		{
			public void writeHeader(Writer writer) throws IOException
			{
				writer.write("DATE | ARTICLE | ACTION | KEY");
			}
		});
		DelimitedLineAggregator<OutboundRegistration> lineAggregator = new DelimitedLineAggregator<OutboundRegistration>();
		lineAggregator.setDelimiter("|");

		BeanWrapperFieldExtractor<OutboundRegistration> fieldExtractor = new BeanWrapperFieldExtractor<OutboundRegistration>();
		fieldExtractor.setNames(new String[]
		{ PreferenceBatchConstants.CREATEDTS, PreferenceBatchConstants.ARTICLE_ID, PreferenceBatchConstants.ACTION_TYPE,
				PreferenceBatchConstants.EMAIL_ID });
		lineAggregator.setFieldExtractor(fieldExtractor);

		writer.setLineAggregator(lineAggregator);

		return writer;
	}

	@Bean
	public FlatFileItemWriter<EmailAnalytics> emailwriter()
	{
		FlatFileItemWriter<EmailAnalytics> writer = new FlatFileItemWriter<EmailAnalytics>();

		writer.setResource(new FileSystemResource(getFile(emailAnalyticsFile)));
		writer.setHeaderCallback(new FlatFileHeaderCallback()
		{
			public void writeHeader(Writer writer) throws IOException
			{
				writer.write("DATE | ARTICLE | INVENTORY | EMAILTYPE | KEY");
			}
		});
		DelimitedLineAggregator<EmailAnalytics> lineAggregator = new DelimitedLineAggregator<EmailAnalytics>();
		lineAggregator.setDelimiter("|");

		BeanWrapperFieldExtractor<EmailAnalytics> fieldExtractor = new BeanWrapperFieldExtractor<EmailAnalytics>();
		fieldExtractor.setNames(new String[]
		{ PreferenceBatchConstants.CREATEDTS, PreferenceBatchConstants.ARTICLE_ID, PreferenceBatchConstants.INVENTORY,
				PreferenceBatchConstants.EMAIL_TYPE, PreferenceBatchConstants.EMAIL_ID });
		lineAggregator.setFieldExtractor(fieldExtractor);

		writer.setLineAggregator(lineAggregator);

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
	public Job processJob()
	{
		System.out.println("\n JOB IN PROGRESS \n ");

		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCSVFileStep()).build();

	}

	/**
	 * Order step 1 step.
	 *
	 * @return the step
	 */

	@Bean
	@JobScope
	public Step readInboundCSVFileStep()
	{
		return stepBuilderFactory.get("readInboundCSVFileStep").<InboundRegistration, OutboundRegistration> chunk(chunkValue)
				.reader(inboundFileReader()).processor(inboundFileProcessor()).writer(inboundFileWriter()).build();
	}

	@Bean
	@JobScope
	public Step orderStep4()
	{

		return stepBuilderFactory.get("orderStep4").tasklet(batchTasklet).transactionManager(transactionManager).build();

	}


	//    @Bean
	//    @JobScope
	//    public Step goesToAPI() {
	//        return stepBuilderFactory.get("goesToAPI").<EmailAnalytics, EmailAnalytics>
	//                chunk(chunkValue).reader(inboundFileReader()).processor(emailprocessor()).writer(emailwriter()).build();
	//    }
	/*
	 * @Bean public Step orderStep3() { return
	 * stepBuilderFactory.get("orderStep3").tasklet(uploadTasklet).transactionManager(transactionManager).build(); }
	 *
	 *
	 *
	 *
	 */

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

package ca.homedepot.preference.config;


import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
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
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.EmailAnalytics;
import ca.homedepot.preference.model.Registration;
import ca.homedepot.preference.processor.EmailAnalyticsItemProcessor;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.springframework.context.annotation.Import;
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
@EnableBatchProcessing
@EnableScheduling
@Import(DataSourceAutoConfiguration.class)
@EntityScan("ca.homedepot.preference")

public class SchedulerConfig
{
	/**
	 * The Job builder factory.
	 */
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	/**
	 * The Step builder factory.
	 */
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	/**
	 * The Job listener.
	 */
	@Autowired
	private JobListener jobListener;

	/**
	 * The Batch tasklet.
	 */
	@Autowired
	private BatchTasklet batchTasklet;


	/**
	 * The Transaction manager./*
	 */
	@Qualifier("visitorTransactionManager")
	private PlatformTransactionManager transactionManager;

	@Value("${analytic.file.registration}")
	String registrationAnalyticsFile;

	@Value("${analytic.file.email}")
	String emailAnalyticsFile;

	@Value("${process.analytics.chunk}")
	Integer chunkValue;

	@Value("${sub.activity.days}")
	Integer subactivity;

	@Bean
	public JdbcCursorItemReader<Registration> reader()
	{
		JdbcCursorItemReader<Registration> cursorItemReader = new JdbcCursorItemReader<Registration>();
		cursorItemReader.setDataSource(dataSource);

		cursorItemReader.setSql(
				"SELECT createdts,article_id,action_type,email_id FROM hdpc_job WHERE DATE(createdts) = '" + getActualDate() + "'");
		cursorItemReader.setRowMapper(new RegistrationrowMapper());

		return cursorItemReader;
	}

	@Bean
	public JdbcCursorItemReader<EmailAnalytics> emailreader()
	{
		JdbcCursorItemReader<EmailAnalytics> cursorItemReader = new JdbcCursorItemReader<EmailAnalytics>();
		cursorItemReader.setDataSource(dataSource);

		cursorItemReader
				.setSql("SELECT createdts,article_id,inventory,email_type,email_id FROM email_analytics WHERE DATE(createdts) = '"
						+ getActualDate() + "'");
		cursorItemReader.setRowMapper(new EmailAnalyticsrowMapper());

		return cursorItemReader;
	}

	@Bean
	public RegistrationItemProcessor processor()
	{
		return new RegistrationItemProcessor();
	}

	@Bean
	public EmailAnalyticsItemProcessor emailprocessor()
	{
		return new EmailAnalyticsItemProcessor();
	}

	@Bean
	public FlatFileItemWriter<Registration> writer()
	{
		FlatFileItemWriter<Registration> writer = new FlatFileItemWriter<Registration>();

		writer.setResource(new FileSystemResource(getFile(registrationAnalyticsFile)));
		writer.setHeaderCallback(new FlatFileHeaderCallback()
		{
			public void writeHeader(Writer writer) throws IOException
			{
				writer.write("DATE | ARTICLE | ACTION | KEY");
			}
		});
		DelimitedLineAggregator<Registration> lineAggregator = new DelimitedLineAggregator<Registration>();
		lineAggregator.setDelimiter("|");

		BeanWrapperFieldExtractor<Registration> fieldExtractor = new BeanWrapperFieldExtractor<Registration>();
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
	 * Process job.
	 *
	 * @return the job
	 */

	@Bean
	public Job processJob()
	{
		System.out.println("\n JOB IN PROGRESS \n ");

		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(jobListener).start(orderStep2())
				.build();

	}

	/**
	 * Order step 1 step.
	 *
	 * @return the step
	 */

	@Bean
	@JobScope
	public Step orderStep1()
	{
		return stepBuilderFactory.get("orderStep1").<Registration, Registration> chunk(chunkValue).reader(reader())
				.processor(processor()).writer(writer()).build();
	}

	//    @Bean
	//    @JobScope
	//    public Step orderStep4() {
	//
	//        return stepBuilderFactory.get("orderStep4").tasklet(batchTasklet).transactionManager(transactionManager).build();
	//
	//    }


	@Bean
	@JobScope
	public Step orderStep2()
	{
		return stepBuilderFactory.get("orderStep2").<EmailAnalytics, EmailAnalytics> chunk(chunkValue).reader(emailreader())
				.processor(emailprocessor()).writer(emailwriter()).build();
	}
	/*
	 * @Bean public Step orderStep3() { return
	 * stepBuilderFactory.get("orderStep3").tasklet(uploadTasklet).transactionManager(transactionManager).build(); }
	 *
	 *
	 *
	 *
	 */



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

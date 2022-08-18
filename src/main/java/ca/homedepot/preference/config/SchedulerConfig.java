package ca.homedepot.preference.config;


import javax.sql.DataSource;

import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.EmailAnalytics;
import ca.homedepot.preference.model.Registration;
import ca.homedepot.preference.processor.EmailAnalyticsItemProcessor;
import ca.homedepot.preference.processor.RegistrationItemProcessor;
import ca.homedepot.preference.tasklet.BatchTasklet;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * The type Scheduler config.
 */
@Slf4j

@Configuration

@EnableBatchProcessing

@RequiredArgsConstructor
public class SchedulerConfig extends DefaultBatchConfigurer {
    /**
     * The Job builder factory.
     */
    private final JobBuilderFactory jobBuilderFactory;

    /**
     * The Step builder factory.
     */
    private final StepBuilderFactory stepBuilderFactory;

    //Autowired
    private DataSource dataSource;

    @Value("${analytic.file.registration}")
    String registrationAnalyticsFile;

    @Value("${analytic.file.email}")
    String emailAnalyticsFile;

    @Value("${process.analytics.chunk}")
    Integer chunkValue;

    @Value("${sub.activity.days}")
    Integer subactivity;

    @Bean
    public JdbcCursorItemReader<Registration> reader() {
        JdbcCursorItemReader<Registration> cursorItemReader = new JdbcCursorItemReader<Registration>();
        cursorItemReader.setDataSource(dataSource);
        /*
         * cursorItemReader
         * .setSql("SELECT createdts,article_id,action_type,email_id FROM registration_analytics WHERE DATE(createdts) = '" +
         * getActualDate() + "'"); cursorItemReader.setRowMapper(new RegistrationrowMapper());
         */
        return cursorItemReader;
    }

    @Bean
    public JdbcCursorItemReader<EmailAnalytics> emailreader() {
        JdbcCursorItemReader<EmailAnalytics> cursorItemReader = new JdbcCursorItemReader<EmailAnalytics>();
        cursorItemReader.setDataSource(dataSource);
        /*
         * cursorItemReader
         * .setSql("SELECT createdts,article_id,inventory,email_type,email_id FROM email_analytics WHERE DATE(createdts) = '" +
         * getActualDate() + "'"); cursorItemReader.setRowMapper(new EmailAnalyticsrowMapper());
         */
        return cursorItemReader;
    }

    @Bean
    public RegistrationItemProcessor processor() {
        return new RegistrationItemProcessor();
    }

    @Bean
    public EmailAnalyticsItemProcessor emailprocessor() {
        return new EmailAnalyticsItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Registration> writer() {
        FlatFileItemWriter<Registration> writer = new FlatFileItemWriter<Registration>();
        /*
         * writer.setResource(new FileSystemResource(getFile(registrationAnalyticsFile))); writer.setHeaderCallback(new
         * FlatFileHeaderCallback() { public void writeHeader(Writer writer) throws IOException {
         * writer.write("DATE | ARTICLE | ACTION | KEY"); } }); DelimitedLineAggregator<Registration> lineAggregator = new
         * DelimitedLineAggregator<Registration>(); lineAggregator.setDelimiter("|");
         *
         * BeanWrapperFieldExtractor<Registration> fieldExtractor = new BeanWrapperFieldExtractor<Registration>();
         * fieldExtractor.setNames(new String[] { PreferenceBatchConstants.CREATEDTS, PreferenceBatchConstants.ARTICLE_ID,
         * PreferenceBatchConstants.ACTION_TYPE, PreferenceBatchConstants.EMAIL_ID });
         * lineAggregator.setFieldExtractor(fieldExtractor);
         *
         * writer.setLineAggregator(lineAggregator);
         */
        return writer;
    }

    @Bean
    public FlatFileItemWriter<EmailAnalytics> emailwriter() {
        FlatFileItemWriter<EmailAnalytics> writer = new FlatFileItemWriter<EmailAnalytics>();
        /*
         * writer.setResource(new FileSystemResource(getFile(emailAnalyticsFile))); writer.setHeaderCallback(new
         * FlatFileHeaderCallback() { public void writeHeader(Writer writer) throws IOException {
         * writer.write("DATE | ARTICLE | INVENTORY | EMAILTYPE | KEY"); } }); DelimitedLineAggregator<EmailAnalytics>
         * lineAggregator = new DelimitedLineAggregator<EmailAnalytics>(); lineAggregator.setDelimiter("|");
         *
         * BeanWrapperFieldExtractor<EmailAnalytics> fieldExtractor = new BeanWrapperFieldExtractor<EmailAnalytics>();
         * fieldExtractor.setNames(new String[] { PreferenceBatchConstants.CREATEDTS, PreferenceBatchConstants.ARTICLE_ID,
         * PreferenceBatchConstants.INVENTORY, PreferenceBatchConstants.EMAIL_TYPE, PreferenceBatchConstants.EMAIL_ID });
         * lineAggregator.setFieldExtractor(fieldExtractor);
         *
         * writer.setLineAggregator(lineAggregator);
         */
        return writer;
    }

    /**
     * The Job listener.
     */
    private JobListener jobListener;

    /**
     * The Batch tasklet.
     */
    private BatchTasklet batchTasklet;


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
    public Job processJob() {

        return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(jobListener).start(orderStep1())
                .build();

    }

    /**
     * Order step 1 step.
     *
     * @return the step
     */

    @Bean
    public Step orderStep1() {
        return stepBuilderFactory.get("orderStep1").<Registration, Registration>chunk(chunkValue).reader(reader())
                .processor(processor()).writer(writer()).build();
    }

    @Bean
    public Step orderStep4() {

        return
                stepBuilderFactory.get("orderStep4").tasklet(batchTasklet).transactionManager(transactionManager).build();

    }


    /*
     * }
     *
     * @Bean public Step orderStep2() { return stepBuilderFactory.get("orderStep2").<EmailAnalytics, EmailAnalytics>
     * chunk(chunkValue).reader(emailreader()) .processor(emailprocessor()).writer(emailwriter()).build(); }
     *
     * @Bean public Step orderStep3() { return
     * stepBuilderFactory.get("orderStep3").tasklet(uploadTasklet).transactionManager(transactionManager).build(); }
     *

     *
     *
     *//**
     * {@inheritDoc}
     *
     * override to do not set data a source. Spring initialize will use a Map based JobRepository instead of database.
     * This is to avoid spring batch from creating batch related metadata tables in the scheduling schemas.
     */
    /*
     * @Override public void setDataSource(DataSource dataSource) { }
     *
     *//**
     * Getting actual date for processing.
     */
    /*
     * private String getActualDate() { SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Instant now =
     * Instant.now(); Instant yesterday = now.minus(subactivity, ChronoUnit.DAYS); return sdf.format(Date.from(yesterday));
     * }
     *
     *//**
     * Gets file name.
     *
     * @param baseName
     *           the base name
     * @return the file name
     *//*
     * private String getFile(String baseName) { Date currentDate = new Date(); SimpleDateFormat sdf = new
     * SimpleDateFormat("ddMMyyyyhhmmss"); String fileName = baseName + "_" + sdf.format(currentDate) + ".csv"; if
     * (registrationAnalyticsFile.equals(baseName)) { FileUtil.setRegistrationFile(fileName); } else {
     * FileUtil.setEmailanalyticsFile(fileName); } File file = new File(fileName); try { file.createNewFile(); } catch
     * (IOException e) { log.error("Error while creating file " + fileName); } return file.getName(); }
     */

}

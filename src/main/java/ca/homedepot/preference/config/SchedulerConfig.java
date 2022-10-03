package ca.homedepot.preference.config;


import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.bind.ValidationException;

import ca.homedepot.preference.listener.APIWriterListener;
import ca.homedepot.preference.listener.StepErrorLoggingListener;
import ca.homedepot.preference.read.MultiResourceItemReaderInbound;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import ca.homedepot.preference.writer.RegistrationLayoutBWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import ca.homedepot.preference.writer.RegistrationAPIWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static ca.homedepot.preference.util.validation.ExactTargetEmailValidation.FIELD_NAMES_SFMC_OPTOUTS;


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

	private static final String JOB_NAME_REGISTRATION_CRM_INBOUND = "registrationCRMInbound";

	private static final String JOB_NAME_EXTACT_TARGET_EMAIL = "ingestSFMCOptOuts";
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
	@Value("${process.analytics.chunk}")
	Integer chunkValue;
	/*
	* The folders paths
	* */
	@Value("${folders.crm.path}")
	String crmPath;
	@Value("${folders.hybris.path}")
	String hybrisPath;
	@Value("${folders.fb-sfmc.path}")
	String fbSFMCPath;
	@Value("${folders.sfmc.path}")
	String sfmcPath;
	/*
	* Folders ERROR, INBOUND AND PROCCESED
	* */
	@Value("${folders.inbound}")
	String folderInbound;
	@Value("${folders.error}")
	String folderError;
	@Value("${folders.processed}")
	String folderProcessed;


	@Value("${inbound.files.registration}")
	String hybrisRegistrationFile;

	@Value("${inbound.files.sfmcUnsubscribedOutlook}")
	String fileExtTargetEmail;

	@Value("${inbound.files.registrationFbSfmc}")
	String fileRegistrationFbSfmc;

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

	private RegistrationItemWriterListener crmWriterListener;

	@Autowired
	private RegistrationItemWriterListener exactTargetEmailWriterListener;
	@Autowired
	private RegistrationAPIWriter apiWriter;
	@Autowired
	private APIWriterListener apiWriterListener;
	@Autowired
	private RegistrationLayoutBWriter layoutBWriter;
	@Autowired
	private MasterProcessor masterProcessor;

	@Autowired
	public void setUpListener()
	{
		jobListener.setPreferenceService(batchTasklet.getBackinStockService());

		//hybrisWriterListener.setFileName(hybrisRegistrationFile);
		hybrisWriterListener.setJobName(JOB_NAME_REGISTRATION_INBOUND);

		apiWriter.setPreferenceService(batchTasklet.getBackinStockService());
		layoutBWriter.setPreferenceService(batchTasklet.getBackinStockService());

		exactTargetEmailWriterListener = new RegistrationItemWriterListener();
		exactTargetEmailWriterListener.setFileService(hybrisWriterListener.getFileService());
		crmWriterListener = new RegistrationItemWriterListener();
		crmWriterListener.setFileService(hybrisWriterListener.getFileService());
		crmWriterListener.setJobName(JOB_NAME_REGISTRATION_CRM_INBOUND);
		//exactTargetEmailWriterListener.setFileName(fileExtTargetEmail);
		exactTargetEmailWriterListener.setJobName(JOB_NAME_EXTACT_TARGET_EMAIL);
		FileUtil.setCrmPath(crmPath);
		FileUtil.setHybrisPath(hybrisPath);
		FileUtil.setError(folderError);
		FileUtil.setProcessed(folderProcessed);
		FileUtil.setInbound(folderInbound);
	}

	public void setHybrisWriterListener(RegistrationItemWriterListener hybrisWriterListener)
	{
		this.hybrisWriterListener = hybrisWriterListener;
	}

	public void setApiWriter(RegistrationAPIWriter apiWriter) {
		this.apiWriter = apiWriter;
	}

	public void setApiWriterListener(APIWriterListener apiWriterListener) {
		this.apiWriterListener = apiWriterListener;
	}

	public void setExactTargetEmailWriterListener(RegistrationItemWriterListener exactTargetEmailWriterListener){
		this.exactTargetEmailWriterListener = exactTargetEmailWriterListener;
	}

	public void setLayoutBWriter(RegistrationLayoutBWriter layoutBWriter) {
		this.layoutBWriter = layoutBWriter;
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
		crmWriterListener.setSourceIDMasterObj(MasterProcessor.getSourceId("SOURCE", "CRM"));
	}
	///***************************************************

	//@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", hybrisPath+folderInbound)
				.addString("document", hybrisRegistrationFile)
				.addString("source", "hybris")
				.toJobParameters();

		JobExecution execution = jobLauncher.run(registrationInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationCRMInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_CRM_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", crmPath+folderInbound)
				.addString("document", hybrisRegistrationFile)
				.addString("source", "CRM")
				.toJobParameters();

        JobExecution execution = jobLauncher.run(registrationCRMInbound(), param);
        log.info("Registration Inbound finished with status :" + execution.getStatus());


	}

	//@Scheduled(cron = "${cron.job.ingestSFMCOutlookUnsubscribed}")
	public void processsSFMCOptOutsEmail() throws Exception
	{
		log.info(" Ingest SFMC Opt-Outs Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_EXTACT_TARGET_EMAIL, String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution execution = jobLauncher.run(sfmcOptOutsEmailOutlookClient(), param);
		log.info("Ingest SFMC Opt-Outs Job finished with status :" + execution.getStatus());
	}

	/*
	 * Read inbound files
	 */
	/*
	 * MultipleResourceItemReader for the now, yesterday and tomorrow
	 * */
	@StepScope
	public MultiResourceItemReaderInbound<InboundRegistration> multiResourceItemReaderInboundFileReader(@Value("#{jobParameters['directory']}") String directory,
																										@Value("#{jobParameters['document']}") String document,
																										@Value("#{jobParameters['source']}") String source){
		MultiResourceItemReaderInbound<InboundRegistration> multiReaderResourceInbound = new MultiResourceItemReaderInbound<>(source);
		multiReaderResourceInbound.setName("multiResourceItemReaderInboundFileReader");

		multiReaderResourceInbound.setResources(getResources(directory, document, source));
		multiReaderResourceInbound.setDelegate(inboundFileReader());
		multiReaderResourceInbound.setStrict(false);
		return multiReaderResourceInbound;
	}
	/*
	* FlatItemReader to use on hybris
	* */
	@StepScope
	public FlatFileItemReader<InboundRegistration> inboundFileReader()
	{
		return new FlatFileItemReaderBuilder<InboundRegistration>()
				.name("inboundFileReader").delimited().delimiter("|").names(InboundValidator.FIELD_NAMES_REGISTRATION)
				.targetType(InboundRegistration.class).linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(FileValidation.lineCallbackHandler(InboundValidator.FIELD_NAMES_REGISTRATION, "\\|")).build();
	}

	public FlatFileItemReader<EmailOptOuts> inboundEmailPreferencesSMFCReader()
	{
		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.resource(new FileSystemResource(crmPath+"INBOUND\\"+fileExtTargetEmail))
				.lineTokenizer(lineTokenizer())
				.targetType(EmailOptOuts.class)

				.linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(FileValidation.lineCallbackHandler(FIELD_NAMES_SFMC_OPTOUTS, "\\t"))
				.saveState(true)

			.build();
	}

	public FlatFileItemReader<EmailOptOuts> ingestOptOutsGmailClientUnsubscribedReader()
	{
		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.lineTokenizer(lineTokenizer())
				.targetType(EmailOptOuts.class)
				.linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(ExactTargetEmailValidation.lineCallbackHandler())
				.build();
	}

	public DelimitedLineTokenizer lineTokenizer(){
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
		delimitedLineTokenizer.setNames(FIELD_NAMES_SFMC_OPTOUTS);
		return delimitedLineTokenizer;
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
	public Job registrationInbound() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCSVFileStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS).to(readInboundBDStep2()).build()
				.build();

	}

	public Job registrationCRMInbound() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_CRM_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCSVFileCRMStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS).to(readInboundBDStep2()).build()
				.build();

	}

	@Bean
	public Job sfmcOptOutsEmailOutlookClient()
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

	public Step readInboundCSVFileStep1() throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(hybrisPath+folderInbound,hybrisRegistrationFile, "hybris"))
				.processor(inboundFileProcessor()).listener(hybrisWriterListener).writer(inboundRegistrationDBWriter()).listener(new StepErrorLoggingListener()).build();
	}

	public Step readInboundCSVFileCRMStep1() throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileCRMStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(crmPath+folderInbound,hybrisRegistrationFile, "CRM"))
				.processor(inboundFileProcessor()).listener(crmWriterListener).writer(inboundRegistrationDBWriter())
				.listener(new StepErrorLoggingListener()).build();
	}

	@Bean
	public Step readInboundBDStep2() throws Exception
	{
		return stepBuilderFactory.get("readInboundBDStep").<RegistrationRequest, RegistrationRequest> chunk(chunkValue)
				.reader(inboundDBReader()).listener(apiWriterListener).writer(apiWriter).build();
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
				.listener(apiWriterListener).writer(layoutBWriter).build();

	}

	@Bean
	public Step readSFMCOptOutsGmailStep(){
		return stepBuilderFactory.get("readSFMCOptOutsStep1").<EmailOptOuts, FileInboundStgTable> chunk(chunkValue)
				.reader(ingestOptOutsGmailClientUnsubscribedReader()).processor(extactExactTargetEmailProcessor())
				.listener(exactTargetEmailWriterListener).writer(inboundRegistrationDBWriter()).build();
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



	public Resource[] getResources(String folder, String baseName, String source)
	{
		List<String> filesName = FileUtil.getFilesOnFolder(folder, baseName, source);

		Resource[] resources = new Resource[filesName.size()];

		for(int i = 0; i< resources.length;i++){
			resources[i] = new FileSystemResource(filesName.get(i));
		}

		return resources;
	}




}

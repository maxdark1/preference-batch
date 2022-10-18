package ca.homedepot.preference.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.listener.APIWriterListener;
import ca.homedepot.preference.listener.StepErrorLoggingListener;
import ca.homedepot.preference.listener.skipers.SkipListenerLayoutB;
import ca.homedepot.preference.listener.skipers.SkipListenerLayoutC;
import ca.homedepot.preference.read.MultiResourceItemReaderInbound;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import ca.homedepot.preference.writer.RegistrationLayoutBWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
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

	private static final String JOB_NAME_REGISTRATION_FBSFMC_INBOUND = "registrationFBSFMCGardenClubInbound";

	private static final String JOB_NAME_EXTACT_TARGET_EMAIL = "ingestSFMCOptOuts";

    private static final String JOB_NAME_SEND_PREFERENCES_TO_CRM = "sendPreferencesToCRM";
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
	@Value("${preference.centre.chunk}")
	Integer chunkValue;
	@Value("${preference.centre.layoutc.chunk}")
	Integer chunkLayoutC;
	@Value("${preference.centre.layoutb.chunk}")
	Integer chunkLayoutB;
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
	@Value("${folders.fb-sfmc.path}")
	String fbsfmcPath;
	/*
	* Folders ERROR, INBOUND AND PROCCESED
	* */
	@Value("${folders.inbound}")
	String folderInbound;
	@Value("${folders.error}")
	String folderError;
	@Value("${folders.processed}")
	String folderProcessed;
    @Value("${folders.outbound}")
    String folderOutbound;
	@Value("${inbound.files.registration}")
	String hybrisCrmRegistrationFile;

	@Value("${inbound.files.sfmcUnsubscribedOutlook}")
	String fileExtTargetEmail;

	@Value("${inbound.files.registrationFbSfmc}")
	String fileRegistrationFbSfmc;

	@Value("${validation.extension}")
	String extensionRegex;

	@Value("${validation.email}")
	String emailRegex;
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

	private RegistrationItemWriterListener fbsfmcWriterListener;
	@Autowired
	private RegistrationItemWriterListener exactTargetEmailWriterListener;
	@Autowired
	private RegistrationAPIWriter apiWriter;
	@Autowired
	private APIWriterListener apiWriterListener;
	@Autowired
	private SkipListenerLayoutB skipListenerLayoutB;

	@Autowired
	private SkipListenerLayoutC skipListenerLayoutC;
	@Autowired
	private RegistrationLayoutBWriter layoutBWriter;
	@Autowired
	private MasterProcessor masterProcessor;

	@Autowired
	private StepErrorLoggingListener stepListener;

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
		apiWriter.setFileService(hybrisWriterListener.getFileService());
		layoutBWriter.setFileService(hybrisWriterListener.getFileService());
		crmWriterListener = new RegistrationItemWriterListener();
		crmWriterListener.setFileService(hybrisWriterListener.getFileService());
		crmWriterListener.setJobName(JOB_NAME_REGISTRATION_CRM_INBOUND);
		exactTargetEmailWriterListener.setJobName(JOB_NAME_EXTACT_TARGET_EMAIL);
		fbsfmcWriterListener = new RegistrationItemWriterListener();
		fbsfmcWriterListener.setFileService(hybrisWriterListener.getFileService());
		fbsfmcWriterListener.setJobName(JOB_NAME_REGISTRATION_FBSFMC_INBOUND);


		FileUtil.setCrmPath(crmPath);
		FileUtil.setHybrisPath(hybrisPath);
		FileUtil.setSfmcPath(sfmcPath);
		FileUtil.setFbsfmcPath(fbsfmcPath);
		FileUtil.setError(folderError);
		FileUtil.setProcessed(folderProcessed);
		FileUtil.setInbound(folderInbound);

		FileValidation.setFbSFMCBaseName(fileRegistrationFbSfmc);
		FileValidation.setHybrisBaseName(hybrisCrmRegistrationFile);
		FileValidation.setSfmcBaseName(fileExtTargetEmail);
		FileValidation.setExtensionRegex(extensionRegex);

		InboundValidator.setValidEmailPattern(emailRegex);
	}

	public void setCrmWriterListener(RegistrationItemWriterListener crmWriterListener) {
		this.crmWriterListener = crmWriterListener;
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


	public void setSkipListenerLayoutB(SkipListenerLayoutB skipListenerLayoutB) {
		this.skipListenerLayoutB = skipListenerLayoutB;
	}

	public void setSkipListenerLayoutC(SkipListenerLayoutC skipListenerLayoutC) {
		this.skipListenerLayoutC = skipListenerLayoutC;
	}

	public void setStepListener(StepErrorLoggingListener stepListener) {
		this.stepListener = stepListener;
	}

	public void setBatchTasklet(BatchTasklet batchTasklet) {
		this.batchTasklet = batchTasklet;
	}

	public void setFbsfmcWriterListener(RegistrationItemWriterListener fbsfmcWriterListener) {
		this.fbsfmcWriterListener = fbsfmcWriterListener;
	}

	/*
	 * SCHEDULING JOBS
	 */

	@PostConstruct
	public void getMasterInfo()
	{
		masterProcessor.getMasterInfo();
	}
	///***************************************************

	//@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationHybrisInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", hybrisPath+folderInbound)
				.addString("document", hybrisCrmRegistrationFile)
				.addString("source", SourceDelimitersConstants.HYBRIS)
				.addString("job_name", JOB_NAME_REGISTRATION_INBOUND)
				.toJobParameters();

		JobExecution execution = jobLauncher.run(registrationHybrisInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	//@Scheduled(cron = "${cron.job.registration}")
	public void processRegistrationCRMInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_CRM_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", crmPath+folderInbound)
				.addString("source", SourceDelimitersConstants.CRM)
				.addString("job_name", JOB_NAME_REGISTRATION_CRM_INBOUND)
				.toJobParameters();

        JobExecution execution = jobLauncher.run(registrationCRMInbound(), param);
        log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	//@Scheduled(cron = "${cron.job.registrationFBSFMC}")
	public void processFBSFMCInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_FBSFMC_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", fbSFMCPath+folderInbound)
				.addString("source", SourceDelimitersConstants.FB_SFMC)
				.addString("job_name", JOB_NAME_REGISTRATION_FBSFMC_INBOUND)
				.toJobParameters();

		JobExecution execution = jobLauncher.run(registrationFBSFMCGardenClubInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	//@Scheduled(cron = "${cron.job.ingestSFMCOutlookUnsubscribed}")
	public void processsSFMCOptOutsEmail() throws Exception
	{
		log.info(" Ingest SFMC Opt-Outs Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_EXTACT_TARGET_EMAIL, String.valueOf(System.currentTimeMillis()))
				.addString("directory", sfmcPath+folderInbound)
				.addString("source", SourceDelimitersConstants.SFMC)
				.addString("job_name", JOB_NAME_EXTACT_TARGET_EMAIL)
				.toJobParameters();
		JobExecution execution = jobLauncher.run(sfmcOptOutsEmailOutlookClient(), param);
		log.info("Ingest SFMC Opt-Outs Job finished with status :" + execution.getStatus());
	}

    @Scheduled(cron = "${cron.job.sendPreferencesToCRM}")
    public void sendPreferencesToCRM() throws Exception
    {
        log.info(" Send Preferences To CRM Job started at: {} ", new Date());
        JobParameters param = new JobParametersBuilder()
                .addString(JOB_NAME_SEND_PREFERENCES_TO_CRM, String.valueOf(System.currentTimeMillis()))
                .addString("directory", crmPath+folderOutbound)
                .addString("source", SourceDelimitersConstants.CRM)
                .addString("job_name",JOB_NAME_SEND_PREFERENCES_TO_CRM)
                .toJobParameters();
        JobExecution execution = jobLauncher.run(crmSendPreferencesToCRM(), param);
        log.info(" Send Preferences To CRM Job finished with status : " + execution.getStatus());
    }

	/*
	 * Read inbound files
	 */

	/*
	 * MultipleResourceItemReaders
	 * Use to read the existing files on the directory
	 * */
	@StepScope
	public MultiResourceItemReaderInbound<InboundRegistration> multiResourceItemReaderInboundFileReader(@Value("#{jobParameters['directory']}") String directory,
																										@Value("#{jobParameters['source']}") String source,
																										@Value("#{jobParameters['job_name']}") String jobName){
		MultiResourceItemReaderInbound<InboundRegistration> multiReaderResourceInbound = new MultiResourceItemReaderInbound<>(source);
		multiReaderResourceInbound.setName("multiResourceItemReaderInboundFileReader");
		multiReaderResourceInbound.setJobName(jobName);
		multiReaderResourceInbound.setFileService(hybrisWriterListener.getFileService());

		multiReaderResourceInbound.setResources(getResources(directory, source));
		multiReaderResourceInbound.setDelegate(inboundFileReader());
		multiReaderResourceInbound.setStrict(false);
		return multiReaderResourceInbound;
	}

	@StepScope
	public MultiResourceItemReader<EmailOptOuts> multiResourceItemReaderSFMCUnsubcribed(@Value("#{jobParameters['directory']}") String directory,
																						@Value("#{jobParameters['source']}") String source,
																						@Value("#{jobParameters['job_name']}") String jobName){
		MultiResourceItemReaderInbound<EmailOptOuts> multiReaderResourceInbound = new MultiResourceItemReaderInbound<>(source);
		multiReaderResourceInbound.setJobName(jobName);
		multiReaderResourceInbound.setFileService(hybrisWriterListener.getFileService());
		multiReaderResourceInbound.setName("multiResourceItemReaderSFMCUnsubcribed");

		multiReaderResourceInbound.setResources(getResources(directory, source));
		multiReaderResourceInbound.setDelegate(inboundEmailPreferencesSMFCReader());
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
				.skippedLinesCallback(FileValidation.lineCallbackHandler(InboundValidator.FIELD_NAMES_REGISTRATION, SourceDelimitersConstants.DELIMITER_PIPELINE)).build();
	}

	@StepScope
	public FlatFileItemReader<EmailOptOuts> inboundEmailPreferencesSMFCReader()
	{
		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.lineTokenizer(lineTokenizer())
				.targetType(EmailOptOuts.class)

				.linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(FileValidation.lineCallbackHandler(FIELD_NAMES_SFMC_OPTOUTS, SourceDelimitersConstants.DELIMITER_TAB))
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
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS_NOT_SFMC+"'"+JOB_NAME_EXTACT_TARGET_EMAIL+"'"+SqlQueriesConstants.SQL_CONDITION_IP);
		reader.setRowMapper(new RegistrationRowMapper());

		return reader;
	}

	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReaderSFMC()
	{
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS_SFMC+"'"+JOB_NAME_EXTACT_TARGET_EMAIL+"'"+SqlQueriesConstants.SQL_CONDITION_IP);
		reader.setRowMapper(new SFMCRowMapper());

		return reader;
	}


	public RegistrationItemProcessor inboundFileProcessor(String source)
	{
		return new RegistrationItemProcessor(source);
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
    public Job crmSendPreferencesToCRM() throws Exception
    {
		return jobBuilderFactory.get("First Job").start(readSendPreferencesToCRMStep1())
				.build();
    }
	public Job registrationHybrisInbound() throws Exception
	{

	}

	public Job registrationCRMInbound() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_CRM_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCRMFileStep1(JOB_NAME_REGISTRATION_CRM_INBOUND)).on(PreferenceBatchConstants.COMPLETED_STATUS).to(readLayoutCInboundBDStep2()).build()
				.build();

	}

	public Job registrationFBSFMCGardenClubInbound() throws Exception
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_FBSFMC_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundFBSFMCFileStep1(JOB_NAME_REGISTRATION_FBSFMC_INBOUND)).on(PreferenceBatchConstants.COMPLETED_STATUS).to(readLayoutCInboundBDStep2()).build()
				.build();

	}

	public Job sfmcOptOutsEmailOutlookClient()
	{
		return jobBuilderFactory.get(JOB_NAME_EXTACT_TARGET_EMAIL).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readSFMCOptOutsStep1(JOB_NAME_EXTACT_TARGET_EMAIL)).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readDBSFMCOptOutsStep2())
				.build().build();
	}


	/**
	 * Order step 1 step.
	 *
	 * @return the step
	 */

    public Step readSendPreferencesToCRMStep1() throws Exception
    {
		return stepBuilderFactory.get("readSendPreferencesToCRMStep1").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				System.out.println("Step 1 Logic");
				return RepeatStatus.FINISHED;
			}
		}).build();
    }


	public Step readInboundHybrisFileStep1(String jobName) throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(hybrisPath+folderInbound, SourceDelimitersConstants.HYBRIS, jobName)) // change source to constants
				.processor(inboundFileProcessor(SourceDelimitersConstants.HYBRIS)).faultTolerant().processorNonTransactional().skip(ValidationException.class)
				.skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC).listener(hybrisWriterListener)
				.writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}

	public Step readInboundCRMFileStep1(String jobName) throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileCRMStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(crmPath+folderInbound, SourceDelimitersConstants.CRM, jobName))
				.processor(inboundFileProcessor(SourceDelimitersConstants.CRM))
				.faultTolerant().processorNonTransactional().skip(ValidationException.class)
				.skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC)
				.listener(crmWriterListener).writer(inboundRegistrationDBWriter())
				.listener(stepListener).build();
	}

	public Step readInboundFBSFMCFileStep1(String jobName) throws Exception
	{
		return stepBuilderFactory.get("readInboundCSVFileCRMStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(fbSFMCPath+folderInbound, SourceDelimitersConstants.FB_SFMC, jobName))
				.processor(inboundFileProcessor(SourceDelimitersConstants.FB_SFMC)).faultTolerant().processorNonTransactional().skip(ValidationException.class)
				.skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC)
				.listener(fbsfmcWriterListener).writer(inboundRegistrationDBWriter())
				.listener(stepListener).build();
	}

	@Bean
	public Step readLayoutCInboundBDStep2() throws Exception
	{
		return stepBuilderFactory.get("readInboundBDStep").<RegistrationRequest, RegistrationRequest> chunk(chunkLayoutC)
				.reader(inboundDBReader()).listener(apiWriterListener).writer(apiWriter).build();
	}

	/*
	 * SFMC Opt-Outs Unsubscribed
	 */
	public Step readSFMCOptOutsStep1(String jobName)
	{
		return stepBuilderFactory.get("readSFMCOptOutsStep1").<EmailOptOuts, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderSFMCUnsubcribed(sfmcPath+folderInbound, SourceDelimitersConstants.SFMC, jobName))
				.processor(extactExactTargetEmailProcessor()).faultTolerant().processorNonTransactional().skip(ValidationException.class)
				.skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutB).listener(exactTargetEmailWriterListener).writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}


	@Bean
	public Step readDBSFMCOptOutsStep2()
	{

		return stepBuilderFactory.get("readDBSFMCOptOutsStep2").<RegistrationRequest, RegistrationRequest>chunk(chunkLayoutB).reader(inboundDBReaderSFMC())
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



	public Map<String, List<Resource>> getResources(String folder, String source)
	{
		return FileUtil.getFilesOnFolder(folder, source);
	}




}

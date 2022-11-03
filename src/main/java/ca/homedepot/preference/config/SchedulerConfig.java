package ca.homedepot.preference.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.listener.StepErrorLoggingListener;
import ca.homedepot.preference.mapper.CitiSuppresionPreparedStatement;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutB;
import ca.homedepot.preference.listener.skippers.SkipListenerLayoutC;
import ca.homedepot.preference.processor.*;
import ca.homedepot.preference.read.MultiResourceItemReaderInbound;
import ca.homedepot.preference.read.PreferenceOutboundDBReader;
import ca.homedepot.preference.read.PreferenceOutboundReader;
import ca.homedepot.preference.service.OutboundService;
import ca.homedepot.preference.service.impl.OutboundServiceImpl;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import ca.homedepot.preference.writer.*;
import io.grpc.Internal;
import org.springframework.batch.core.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.ValidationException;
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
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.listener.RegistrationItemWriterListener;
import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.tasklet.BatchTasklet;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
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
	/**
	 * Constants for job's names
	 */
	private static final String JOB_NAME_REGISTRATION_INBOUND = "registrationInbound";

	private static final String JOB_NAME_REGISTRATION_CRM_INBOUND = "registrationCRMInbound";

	private static final String JOB_NAME_REGISTRATION_FBSFMC_INBOUND = "registrationFBSFMCGardenClubInbound";

	private static final String JOB_NAME_EXTACT_TARGET_EMAIL = "ingestSFMCOptOuts";

	private static final String JOB_NAME_SEND_PREFERENCES_TO_CRM = "sendPreferencesToCRM";

	private static final String JOB_NAME_CITI_SUPPRESION = "sendCitiSuppresionToCiti";

	private static final String JOB_NAME_INTERNAL_DESTINATION = "SendPreferencesToInternalDestination";
	/**
	 * The Job builder factory.
	 */
	private final JobBuilderFactory jobBuilderFactory;
	/**
	 * The Step builder factory.
	 */
	private final StepBuilderFactory stepBuilderFactory;

	/**
	 * The job Launcher
	 */
	private final JobLauncher jobLauncher;
	/**
	 * The Transaction manager.
	 */
	@Qualifier("visitorTransactionManager")
	private final PlatformTransactionManager transactionManager;

	/**
	 * The chunk value
	 *
	 */
	@Value("${preference.centre.chunk}")
	Integer chunkValue;

	/**
	 * The chunk value for LayoutC
	 *
	 */
	@Value("${preference.centre.layoutc.chunk}")
	Integer chunkLayoutC;

	/**
	 * The chunk value for LayoutB
	 *
	 */
	@Value("${preference.centre.layoutb.chunk}")
	Integer chunkLayoutB;
	@Value("${preference.centre.outboundCRM.chunk}")
	Integer chunkOutboundCRM;
	@Value("${preference.centre.outboundCiti.chunk}")
	Integer chunkOutboundCiti;
	@Value("${preference.centre.outboundInternal.chunk}")
	Integer chunkOutboundInternal;
	/**
	 * The folders paths
	 */
	/**
	 * the CRM path
	 */

	@Value("${folders.crm.path}")
	String crmPath;
	/**
	 * The hybris path
	 */
	@Value("${folders.hybris.path}")
	String hybrisPath;

	/**
	 * The fb sfmc path
	 */
	@Value("${folders.fb-sfmc.path}")
	String fbSFMCPath;

	/**
	 * The sfmc path
	 */
	@Value("${folders.sfmc.path}")
	String sfmcPath;

	/**
	 * The citi path
	 */
	@Value("${folders.citi.path}")
	String citiPath;

	/**
	 * Folders ERROR, INBOUND AND PROCCESED
	 */
	@Value("${folders.inbound}")
	String folderInbound;
	@Value("${folders.error}")
	String folderError;
	@Value("${folders.processed}")
	String folderProcessed;
	@Value("${folders.outbound}")
	String folderOutbound;
	@Value("${folders.crm.path}")
	String dailyCompliantrepositorySource;
	@Value("${folders.outbound}")
	String dailyCompliantfolderSource;
	@Value("${outbound.files.compliant}")
	String dailyCompliantNameFormat;
	@Value("${outbound.files.internalCa}")
	String InternalCANameFormat;
	@Value("${outbound.files.internalGarden}")
	String InternalGardenNameFormat;
	@Value("${outbound.files.internalMover}")
	String InternalMoverNameFormat;
	@Value("${folders.internal.path}")
	String InternalRepository;
	@Value("${folders.outbound}")
	String InternalFolder;






	/**
	 * Document's base name
	 *
	 *
	 */
	@Value("${inbound.files.registration}")
	String hybrisCrmRegistrationFile;


	@Value("${inbound.files.sfmcUnsubscribedOutlook}")
	String fileExtTargetEmail;

	@Value("${inbound.files.registrationFbSfmc}")
	String fileRegistrationFbSfmc;

	@Value("${outbound.citi.mastersuppresion}")
	String citiFileNameFormat;

	/**
	 * Patterns for validations
	 *
	 */

	/**
	 * The extension pattern
	 */
	@Value("${validation.extension}")
	String extensionRegex;

	/**
	 * The email pattern
	 */
	@Value("${validation.email}")
	String emailRegex;

	/**
	 * The Data Source
	 */
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
	/**
	 * The hybris writer listener
	 */

	@Autowired
	private RegistrationItemWriterListener hybrisWriterListener;
	/**
	 * The crm writer listener
	 */
	private RegistrationItemWriterListener crmWriterListener;

	/**
	 * The fbsfmc writer listener
	 */
	private RegistrationItemWriterListener fbsfmcWriterListener;
	/**
	 * The layoutB writer listener
	 */
	@Autowired
	private RegistrationItemWriterListener exactTargetEmailWriterListener;

	/**
	 * The Api writer
	 */
	@Autowired
	private RegistrationAPIWriter apiWriter;


	/**
	 * The skip listener layoutB
	 */
	@Autowired
	private SkipListenerLayoutB skipListenerLayoutB;
	/**
	 * The skip listener LayoutC
	 */
	@Autowired
	private SkipListenerLayoutC skipListenerLayoutC;
	/**
	 * LayoutB writer
	 */
	@Autowired
	private RegistrationLayoutBWriter layoutBWriter;
	/**
	 * Master Processor
	 */
	@Autowired
	private MasterProcessor masterProcessor;
	/**
	 * The step Listener
	 *
	 */
	@Autowired
	private StepErrorLoggingListener stepListener;

	/**
	 *
	 * Initialize Listeners
	 *
	 * @return void
	 */
	@Autowired
	private PreferenceOutboundWriter preferenceOutboundWriter;
	@Autowired
	private PreferenceOutboundDBReader preferenceOutboundDBReader;

	@Autowired
	private PreferenceOutboundProcessor preferenceOutboundProcessor;
	@Autowired
	private PreferenceOutboundFileWriter preferenceOutboundFileWriter;
	@Autowired
	private PreferenceOutboundReader preferenceOutboundReader;
	@Autowired
	private CitiSupressionFileWriter citiSupressionFileWriter;
	@Autowired
	private InternalOutboundStep1Writer internalOutboundStep1Writer;
	@Autowired
	private InternalOutboundProcessor internalOutboundProcessor;
	@Autowired
	private InternalOutboundFileWriter internalOutboundFileWriter;

	@Autowired
	public void setUpListener()
	{
		jobListener.setPreferenceService(batchTasklet.getBackinStockService());
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
		FileUtil.setFbsfmcPath(fbSFMCPath);
		FileUtil.setError(folderError);
		FileUtil.setProcessed(folderProcessed);
		FileUtil.setInbound(folderInbound);

		FileValidation.setFbSFMCBaseName(fileRegistrationFbSfmc);
		FileValidation.setHybrisBaseName(hybrisCrmRegistrationFile);
		FileValidation.setSfmcBaseName(fileExtTargetEmail);
		FileValidation.setExtensionRegex(extensionRegex);

		InboundValidator.setValidEmailPattern(emailRegex);
	}

	/**
	 * Set CRM writer listener
	 *
	 * @param crmWriterListener
	 *
	 */
	public void setCrmWriterListener(RegistrationItemWriterListener crmWriterListener)
	{
		this.crmWriterListener = crmWriterListener;
	}

	/**
	 * Set hybris writer listener.
	 *
	 * @param hybrisWriterListener
	 *
	 */
	public void setHybrisWriterListener(RegistrationItemWriterListener hybrisWriterListener)
	{
		this.hybrisWriterListener = hybrisWriterListener;
	}

	/**
	 * Set API writer.
	 *
	 * @param apiWriter
	 *
	 */
	public void setApiWriter(RegistrationAPIWriter apiWriter)
	{
		this.apiWriter = apiWriter;
	}


	/**
	 * Set LayoutB writer listener.
	 *
	 * @param exactTargetEmailWriterListener
	 *
	 */
	public void setExactTargetEmailWriterListener(RegistrationItemWriterListener exactTargetEmailWriterListener)
	{
		this.exactTargetEmailWriterListener = exactTargetEmailWriterListener;
	}

	/**
	 * Set LayoutB API writer
	 *
	 * @param layoutBWriter
	 *
	 */
	public void setLayoutBWriter(RegistrationLayoutBWriter layoutBWriter)
	{
		this.layoutBWriter = layoutBWriter;
	}

	/**
	 * Set job Listener
	 *
	 * @param jobListener
	 *
	 */

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}

	/**
	 * Set skip Listener for LayoutB
	 *
	 * @param skipListenerLayoutB
	 *
	 */

	public void setSkipListenerLayoutB(SkipListenerLayoutB skipListenerLayoutB)
	{
		this.skipListenerLayoutB = skipListenerLayoutB;
	}

	/**
	 * Set skip Listener for LayoutC
	 *
	 * @param skipListenerLayoutC
	 *
	 */

	public void setSkipListenerLayoutC(SkipListenerLayoutC skipListenerLayoutC)
	{
		this.skipListenerLayoutC = skipListenerLayoutC;
	}

	/**
	 * Set step Listener
	 *
	 * @param stepListener
	 *
	 */
	public void setStepListener(StepErrorLoggingListener stepListener)
	{
		this.stepListener = stepListener;
	}

	/**
	 * Set batch tasklet
	 *
	 * @param batchTasklet
	 *
	 */
	public void setBatchTasklet(BatchTasklet batchTasklet)
	{
		this.batchTasklet = batchTasklet;
	}

	/**
	 * Set FB SFMC Writer Listener
	 *
	 * @param fbsfmcWriterListener
	 *
	 */
	public void setFbsfmcWriterListener(RegistrationItemWriterListener fbsfmcWriterListener)
	{
		this.fbsfmcWriterListener = fbsfmcWriterListener;
	}

	/**
	 * After scheduled is construct, it will save in static mode the Master's information
	 *
	 * @return void
	 *
	 */
	@PostConstruct
	public void getMasterInfo()
	{
		masterProcessor.getMasterInfo();
	}

	/**
	 * Triggers hybris job in a determinated period of time
	 *
	 * The fields read from the left to the right (for scheduled param) second, minute, hour, day of month, month, day of
	 * week
	 *
	 * @return void
	 *
	 */
	@Scheduled(cron = "${cron.job.hybrisIngestion}")
	public void processRegistrationHybrisInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", hybrisPath + folderInbound).addString("document", hybrisCrmRegistrationFile)
				.addString("source", SourceDelimitersConstants.HYBRIS).addString("job_name", JOB_NAME_REGISTRATION_INBOUND)
				.toJobParameters();

		JobExecution execution = jobLauncher.run(registrationHybrisInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	/**
	 * Triggers CRM job in a determinated period of time
	 *
	 * The fields read from the left to the right (for scheduled param) second, minute, hour, day of month, month, day of
	 * week
	 *
	 * @return void
	 *
	 */
	@Scheduled(cron = "${cron.job.crmIngestion}")
	public void processRegistrationCRMInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_CRM_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", crmPath + folderInbound).addString("source", SourceDelimitersConstants.CRM)
				.addString("job_name", JOB_NAME_REGISTRATION_CRM_INBOUND).toJobParameters();

		JobExecution execution = jobLauncher.run(registrationCRMInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	/**
	 * Triggers FB SFMC job in a determinated period of time
	 *
	 * The fields read from the left to the right (for scheduled param) second, minute, hour, day of month, month, day of
	 * week
	 *
	 * @return void
	 *
	 */
	@Scheduled(cron = "${cron.job.fbsfmcIngestion}")
	public void processFBSFMCInbound() throws Exception
	{
		log.info(" Registration Inbound : Registration Job started at :" + new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_REGISTRATION_FBSFMC_INBOUND, String.valueOf(System.currentTimeMillis()))
				.addString("directory", fbSFMCPath + folderInbound).addString("source", SourceDelimitersConstants.FB_SFMC)
				.addString("job_name", JOB_NAME_REGISTRATION_FBSFMC_INBOUND).toJobParameters();

		JobExecution execution = jobLauncher.run(registrationFBSFMCGardenClubInbound(), param);
		log.info("Registration Inbound finished with status :" + execution.getStatus());
	}

	/**
	 *
	 * Triggers SFMC job in a determinated period of time
	 *
	 * The fields read from the left to the right (for scheduled param) second, minute, hour, day of month, month, day of
	 * week
	 *
	 * @throws Exception
	 */
	@Scheduled(cron = "${cron.job.ingestSFMCOutlookUnsubscribed}")
	public void processsSFMCOptOutsEmail() throws Exception
	{
		log.info(" Ingest SFMC Opt-Outs Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_EXTACT_TARGET_EMAIL, String.valueOf(System.currentTimeMillis()))
				.addString("directory", sfmcPath + folderInbound).addString("source", SourceDelimitersConstants.SFMC)
				.addString("job_name", JOB_NAME_EXTACT_TARGET_EMAIL).toJobParameters();
		JobExecution execution = jobLauncher.run(sfmcOptOutsEmailOutlookClient(), param);
		log.info("Ingest SFMC Opt-Outs Job finished with status :" + execution.getStatus());
	}

	/**
	 * Triggers CRM Outbound Process in a determinated period of time
	 * 
	 * @throws Exception
	 */
	@Scheduled(cron = "${cron.job.sendPreferencesToCRM}")
	public void sendPreferencesToCRM() throws Exception
	{
		log.info(" Send Preferences To CRM Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_SEND_PREFERENCES_TO_CRM, String.valueOf(System.currentTimeMillis()))
				.addString("directory", crmPath + folderOutbound).addString("source", SourceDelimitersConstants.CRM)
				.addString("job_name", JOB_NAME_SEND_PREFERENCES_TO_CRM).toJobParameters();
		JobExecution execution = jobLauncher.run(crmSendPreferencesToCRM(), param);
		log.info(" Send Preferences To CRM Job finished with status : " + execution.getStatus());
	}

	@Scheduled(cron = "${cron.job.sendPreferencesToInternalDestination}")
	public void sendPreferencesToInternal() throws Exception
	{
		log.info(" Send Preferences To Internal Destination Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_INTERNAL_DESTINATION, String.valueOf(System.currentTimeMillis()))
				.addString("directory", crmPath + folderOutbound).addString("source", SourceDelimitersConstants.CRM)
				.addString("job_name", JOB_NAME_INTERNAL_DESTINATION).toJobParameters();
		JobExecution execution = jobLauncher.run(sendPreferencesToInternalDestination(), param);
		log.info(" Send Preferences To Internal Destination Job finished with status : " + execution.getStatus());
	}

	@Scheduled(cron = "${cron.job.sendPreferencesToCitiSuppresion}")
	public void sendCitiSuppresionToCitiSuppresion() throws Exception
	{
		log.info(" Send Preferences To CRM Job started at: {} ", new Date());
		JobParameters param = new JobParametersBuilder()
				.addString(JOB_NAME_CITI_SUPPRESION, String.valueOf(System.currentTimeMillis()))
				.addString("job_name", JOB_NAME_CITI_SUPPRESION).toJobParameters();
		JobExecution execution = jobLauncher.run(sendCitiSuppresionToCiti(), param);
		log.info(" Send Preferences To CRM Job finished with status : " + execution.getStatus());
	}


	/**
	 * Read inbound files
	 */

	/**
	 * MultipleResourceItemReaders Use to read the existing files on the directory /** Create Multi Resource reader for
	 * LayoutC
	 *
	 * @param directory:
	 *           directory where the file comes from
	 * @param source:
	 *           source where the file comes from
	 * @param jobName:
	 *           job that is in execution
	 *
	 * @return MultiResourceItemReader<InboundRegistration>
	 */
	@StepScope
	public MultiResourceItemReaderInbound<InboundRegistration> multiResourceItemReaderInboundFileReader(
			@Value("#{jobParameters['directory']}") String directory, @Value("#{jobParameters['source']}") String source,
			@Value("#{jobParameters['job_name']}") String jobName)
	{
		MultiResourceItemReaderInbound<InboundRegistration> multiReaderResourceInbound = new MultiResourceItemReaderInbound<>(
				source);
		multiReaderResourceInbound.setName("multiResourceItemReaderInboundFileReader");
		multiReaderResourceInbound.setJobName(jobName);
		multiReaderResourceInbound.setFileService(hybrisWriterListener.getFileService());

		multiReaderResourceInbound.setResources(getResources(directory, source));
		multiReaderResourceInbound.setDelegate(inboundFileReader());
		multiReaderResourceInbound.setStrict(false);
		return multiReaderResourceInbound;
	}

	/**
	 * Create Multi Resource reader for LayoutB
	 *
	 * @param directory:
	 *           directory where the file comes from
	 * @param source:
	 *           source where the file comes from
	 * @param jobName:
	 *           job that is in execution
	 *
	 * @return MultiResourceItemReader<EmailOptOuts>
	 */

	@StepScope
	public MultiResourceItemReader<EmailOptOuts> multiResourceItemReaderSFMCUnsubcribed(
			@Value("#{jobParameters['directory']}") String directory, @Value("#{jobParameters['source']}") String source,
			@Value("#{jobParameters['job_name']}") String jobName)
	{
		MultiResourceItemReaderInbound<EmailOptOuts> multiReaderResourceInbound = new MultiResourceItemReaderInbound<>(source);
		multiReaderResourceInbound.setJobName(jobName);
		multiReaderResourceInbound.setFileService(hybrisWriterListener.getFileService());
		multiReaderResourceInbound.setName("multiResourceItemReaderSFMCUnsubcribed");

		multiReaderResourceInbound.setResources(getResources(directory, source));
		multiReaderResourceInbound.setDelegate(inboundEmailPreferencesSMFCReader());
		multiReaderResourceInbound.setStrict(false);
		return multiReaderResourceInbound;

	}

	/**
	 * Create ItemReader for LayoutC file
	 *
	 * @return FlatFileItemReader<InboundRegistration>
	 *
	 */
	@StepScope
	public FlatFileItemReader<InboundRegistration> inboundFileReader()
	{
		return new FlatFileItemReaderBuilder<InboundRegistration>().name("inboundFileReader").delimited().delimiter("|")
				.names(InboundValidator.FIELD_NAMES_REGISTRATION).targetType(InboundRegistration.class).linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(FileValidation.lineCallbackHandler(InboundValidator.FIELD_NAMES_REGISTRATION,
						SourceDelimitersConstants.DELIMITER_PIPELINE))
				.build();
	}

	/**
	 * Create ItemReader for LayoutB file
	 *
	 * @return FlatFileItemReader<EmailOptOuts>
	 */
	@StepScope
	public FlatFileItemReader<EmailOptOuts> inboundEmailPreferencesSMFCReader()
	{
		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.lineTokenizer(lineTokenizer()).targetType(EmailOptOuts.class).linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(
						FileValidation.lineCallbackHandler(FIELD_NAMES_SFMC_OPTOUTS, SourceDelimitersConstants.DELIMITER_TAB))
				.build();
	}

	/**
	 * Create ItemReader for LayoutB file
	 *
	 * @return FlatFileItemReader<EmailOptOuts>
	 */

	public FlatFileItemReader<EmailOptOuts> ingestOptOutsGmailClientUnsubscribedReader()
	{
		return new FlatFileItemReaderBuilder<EmailOptOuts>().name("inboundEmailPreferencesSMFCReader")
				.lineTokenizer(lineTokenizer()).targetType(EmailOptOuts.class).linesToSkip(1)
				/* Validation file's header */
				.skippedLinesCallback(ExactTargetEmailValidation.lineCallbackHandler()).build();
	}

	/**
	 * Create line tokenizer with TAB separator
	 *
	 * @return DelimitedLineTokenizer
	 */

	public DelimitedLineTokenizer lineTokenizer()
	{
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
		delimitedLineTokenizer.setNames(FIELD_NAMES_SFMC_OPTOUTS);
		return delimitedLineTokenizer;
	}

	/**
	 * Gets LayoutC (hyrbis, CRM or FB_SFMC) request from persistence.
	 *
	 * @return JdbcCursorItemReader<RegistrationRequest>
	 */
	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReader()
	{
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS_NOT_SFMC + "'" + JOB_NAME_EXTACT_TARGET_EMAIL + "'"
				+ SqlQueriesConstants.SQL_CONDITION_IP);
		reader.setRowMapper(new RegistrationRowMapper());

		return reader;
	}

	/**
	 * Gets SFMC request from persistence.
	 *
	 * @return JdbcCursorItemReader<RegistrationRequest>
	 */

	@Bean
	public JdbcCursorItemReader<RegistrationRequest> inboundDBReaderSFMC()
	{
		JdbcCursorItemReader<RegistrationRequest> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_LAST_FILE_INSERTED_RECORDS_SFMC + "'" + JOB_NAME_EXTACT_TARGET_EMAIL + "'"
				+ SqlQueriesConstants.SQL_CONDITION_IP);
		reader.setRowMapper(new SFMCRowMapper());

		return reader;
	}


	/**
	 * Returns LayoutC processor
	 *
	 * @return RegistrationItemProcessor
	 *
	 */
	public RegistrationItemProcessor layoutCProcessor(String source)
	{
		return new RegistrationItemProcessor(source);
	}

	/**
	 * Returns LayoutB processor
	 *
	 * @return ExactTargetEmailProcessor
	 *
	 */
	@Bean
	public ExactTargetEmailProcessor layoutBProcessor()
	{
		return new ExactTargetEmailProcessor();
	}

	/**
	 * Writer for persistence LayoutC
	 *
	 * @return JdbcBatchItemWriter<FileInboundStgTable>
	 */
	@Bean
	public JdbcBatchItemWriter<FileInboundStgTable> inboundRegistrationDBWriter()
	{
		JdbcBatchItemWriter<FileInboundStgTable> writer = new JdbcBatchItemWriter<>();

		writer.setDataSource(dataSource);
		writer.setSql(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_REGISTRATION);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<FileInboundStgTable>());

		return writer;
	}

	@Bean
	public JdbcBatchItemWriter<CitiSuppresionOutboundDTO> outboundDTOJdbcBatchItemWriter()
	{
		JdbcBatchItemWriter<CitiSuppresionOutboundDTO> writer = new JdbcBatchItemWriter<>();

		writer.setDataSource(dataSource);
		writer.setSql(OutboundSqlQueriesConstants.SQL_INSERT_CITI_SUPPRESION);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<CitiSuppresionOutboundDTO>());
		writer.setItemPreparedStatementSetter(new CitiSuppresionPreparedStatement());

		return writer;
	}


	public CitiSupressionFileWriter citiSupressionFileWriter()
	{

		citiSupressionFileWriter = new CitiSupressionFileWriter();

		citiSupressionFileWriter.setFileService(hybrisWriterListener.getFileService());
		citiSupressionFileWriter.setFolderSource(folderOutbound);
		citiSupressionFileWriter.setRepositorySource(citiPath);
		citiSupressionFileWriter.setFileNameFormat(citiFileNameFormat);
		citiSupressionFileWriter.setJobName(JOB_NAME_CITI_SUPPRESION);
		citiSupressionFileWriter.setResource();

		return citiSupressionFileWriter;
	}

	/**
	 * Hybris job process.
	 *
	 * @return the job
	 */
	public Job registrationHybrisInbound()
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundHybrisFileStep1(JOB_NAME_REGISTRATION_INBOUND)).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readLayoutCInboundBDStep2()).build().build();
	}

	/**
	 * Crm outbound job process.
	 * 
	 * @return
	 */

	public Job crmSendPreferencesToCRM()
	{
		OutboundService outboundService = new OutboundServiceImpl();
		try
		{
			outboundService.createFile(dailyCompliantrepositorySource, dailyCompliantfolderSource, dailyCompliantNameFormat);
		}
		catch (Exception ex)
		{
			log.error("Error during the creation of CRM Preferences File: " + ex.getMessage());
		}

		return jobBuilderFactory.get(JOB_NAME_SEND_PREFERENCES_TO_CRM).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readSendPreferencesToCRMStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readSendPreferencesToCRMStep2()).build().build();
	}

	public Job sendPreferencesToInternalDestination()
	{
		//Generate the 3 Files
		OutboundService outboundService = new OutboundServiceImpl();
		try
		{
			outboundService.createCaFile(InternalRepository, InternalFolder, InternalCANameFormat);
			outboundService.createGardenClubFile(InternalRepository, InternalFolder, InternalGardenNameFormat);
			outboundService.createNewMoverFile(InternalRepository, InternalFolder, InternalMoverNameFormat);
		}
		catch (Exception ex)
		{
			log.error("Error during the creation of Internal Destination Files" + ex.getMessage());
		}

		//Execute the Job
		return jobBuilderFactory.get(JOB_NAME_INTERNAL_DESTINATION).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readSendPreferencesToInternalStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readSendPreferencesToInternalStep2()).build().build();
	}


	public Step readSendPreferencesToInternalStep1()
	{
		return stepBuilderFactory.get(JOB_NAME_INTERNAL_DESTINATION + "Step1")
				.<InternalOutboundDto, InternalOutboundDto> chunk(chunkOutboundInternal)
				.reader(preferenceOutboundReader.outboundInternalDBReader()).writer(internalOutboundStep1Writer).build();
	}


	public Step readSendPreferencesToInternalStep2()
	{
		return stepBuilderFactory.get(JOB_NAME_INTERNAL_DESTINATION + "Step2")
				.<InternalOutboundDto, InternalOutboundProcessorDto> chunk(chunkOutboundInternal)
				.reader(preferenceOutboundDBReader.outboundInternalDbReader()).processor(internalOutboundProcessor)
				.writer(internalOutboundFileWriter).build();
	}


	/**
	 * CRM job process
	 *
	 * @return Job
	 *
	 */
	public Job registrationCRMInbound()
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_CRM_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundCRMFileStep1(JOB_NAME_REGISTRATION_CRM_INBOUND)).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readLayoutCInboundBDStep2()).build().build();

	}

	/**
	 * FB_SFMC job process
	 *
	 * @return Job
	 *
	 */

	public Job registrationFBSFMCGardenClubInbound()
	{
		return jobBuilderFactory.get(JOB_NAME_REGISTRATION_FBSFMC_INBOUND).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readInboundFBSFMCFileStep1(JOB_NAME_REGISTRATION_FBSFMC_INBOUND)).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readLayoutCInboundBDStep2()).build().build();

	}

	/**
	 * SFMC Opt Outs Job
	 *
	 * @return Job
	 *
	 */
	public Job sfmcOptOutsEmailOutlookClient()
	{
		return jobBuilderFactory.get(JOB_NAME_EXTACT_TARGET_EMAIL).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(readSFMCOptOutsStep1(JOB_NAME_EXTACT_TARGET_EMAIL)).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(readDBSFMCOptOutsStep2()).build().build();
	}

	public Job sendCitiSuppresionToCiti()
	{
		return jobBuilderFactory.get(JOB_NAME_CITI_SUPPRESION).incrementer(new RunIdIncrementer()).listener(jobListener)
				.start(citiSuppresionDBReaderStep1()).on(PreferenceBatchConstants.COMPLETED_STATUS)
				.to(citiSuppresionDBReaderFileWriterStep2()).build().build();
	}


	/**
	 * Step 1 for Send Preferences to CRM Outbound
	 * 
	 * @return
	 */
	public Step readSendPreferencesToCRMStep1()
	{
		return stepBuilderFactory.get("readSendPreferencesToCRMStep1")
				.<PreferenceOutboundDto, PreferenceOutboundDto> chunk(chunkOutboundCRM)
				.reader(preferenceOutboundReader.outboundDBReader()).writer(preferenceOutboundWriter).build();
	}

	/**
	 * Step 2 for Send Preferences to CRM Outbound
	 * 
	 * @return
	 */
	public Step readSendPreferencesToCRMStep2()
	{
		return stepBuilderFactory.get("readSendPreferencesToCRMStep2")
				.<PreferenceOutboundDto, PreferenceOutboundDtoProcessor> chunk(chunkOutboundCRM)
				.reader(preferenceOutboundDBReader.outboundDBReader()).processor(preferenceOutboundProcessor)
				.writer(preferenceOutboundFileWriter).build();
	}

	/**
	 * Step 1 for hybris process.
	 *
	 * @param jobName
	 *           The job_name that is processing
	 * @return the step
	 */
	public Step readInboundHybrisFileStep1(String jobName)
	{
		return stepBuilderFactory.get("readInboundCSVFileStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(
						multiResourceItemReaderInboundFileReader(hybrisPath + folderInbound, SourceDelimitersConstants.HYBRIS, jobName)) // change source to constants
				.processor(layoutCProcessor(SourceDelimitersConstants.HYBRIS)).faultTolerant().processorNonTransactional()
				.skip(ValidationException.class).skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC)
				.listener(hybrisWriterListener).writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}

	/**
	 * Step 1 for CRM proocess.
	 *
	 * @param jobName
	 *           The job_name that is processing
	 * @return the step
	 */

	public Step readInboundCRMFileStep1(String jobName)
	{
		return stepBuilderFactory.get("readInboundCSVFileCRMStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(crmPath + folderInbound, SourceDelimitersConstants.CRM, jobName))
				.processor(layoutCProcessor(SourceDelimitersConstants.CRM)).faultTolerant().processorNonTransactional()
				.skip(ValidationException.class).skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC)
				.listener(crmWriterListener).writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}

	/**
	 * Step 1 for FB_SFMC proocess.
	 *
	 * @param jobName
	 *           The job_name that is processing
	 * @return the step
	 */

	public Step readInboundFBSFMCFileStep1(String jobName)
	{
		return stepBuilderFactory.get("readInboundCSVFileCRMStep").<InboundRegistration, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderInboundFileReader(fbSFMCPath + folderInbound, SourceDelimitersConstants.FB_SFMC,
						jobName))
				.processor(layoutCProcessor(SourceDelimitersConstants.FB_SFMC)).faultTolerant().processorNonTransactional()
				.skip(ValidationException.class).skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutC)
				.listener(fbsfmcWriterListener).writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}

	/**
	 * Step 2 for SFMC proocess.
	 *
	 * @param jobName
	 *           The job_name that is processing
	 * @return the step
	 */
	public Step readSFMCOptOutsStep1(String jobName)
	{
		return stepBuilderFactory.get("readSFMCOptOutsStep1").<EmailOptOuts, FileInboundStgTable> chunk(chunkValue)
				.reader(multiResourceItemReaderSFMCUnsubcribed(sfmcPath + folderInbound, SourceDelimitersConstants.SFMC, jobName))
				.processor(layoutBProcessor()).faultTolerant().processorNonTransactional().skip(ValidationException.class)
				.skipLimit(Integer.MAX_VALUE).listener(skipListenerLayoutB).listener(exactTargetEmailWriterListener)
				.writer(inboundRegistrationDBWriter()).listener(stepListener).build();
	}

	/**
	 * Step 2 for LayoutC send request.
	 *
	 * @return the step
	 *
	 */

	@Bean
	public Step readLayoutCInboundBDStep2()
	{
		return stepBuilderFactory.get("readInboundBDStep").<RegistrationRequest, RegistrationRequest> chunk(chunkLayoutC)
				.reader(inboundDBReader()).writer(apiWriter).build();
	}

	/**
	 * Step 2 for LayoutB send request.
	 *
	 * @return the step
	 *
	 */

	@Bean
	public Step readDBSFMCOptOutsStep2()
	{

		return stepBuilderFactory.get("readDBSFMCOptOutsStep2").<RegistrationRequest, RegistrationRequest> chunk(chunkLayoutB)
				.reader(inboundDBReaderSFMC()).writer(layoutBWriter).build();

	}

	/**
	 * Out bound process
	 */
	public Step citiSuppresionDBReaderStep1()
	{
		return stepBuilderFactory.get("citiSuppresionDBReaderStep1")
				.<CitiSuppresionOutboundDTO, CitiSuppresionOutboundDTO> chunk(chunkOutboundCiti)
				.reader(preferenceOutboundReader.outboundCitiSuppresionDBReader()).writer(outboundDTOJdbcBatchItemWriter()).build();
	}


	public Step citiSuppresionDBReaderFileWriterStep2()
	{
		return stepBuilderFactory.get("citiSuppresionDBReaderFileWriterStep2")
				.<CitiSuppresionOutboundDTO, CitiSuppresionOutboundDTO> chunk(chunkOutboundCiti)
				.reader(preferenceOutboundDBReader.citiSuppressionDBTableReader()).writer(citiSupressionFileWriter()).build();
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
	 * @param folder
	 *           Folder's name, depending on source type
	 * @param source
	 *           Source type can be: hybris, SFMC, CRM and FB_SFMC
	 *
	 * @return Map<String List<Resource>> Map of VALID and INVALID fiels
	 */
	public Map<String, List<Resource>> getResources(String folder, String source)
	{
		return FileUtil.getFilesOnFolder(folder, source);
	}




}

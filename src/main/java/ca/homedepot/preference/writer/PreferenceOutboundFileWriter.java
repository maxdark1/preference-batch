package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_SEND_PREFERENCES_TO_CRM;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;
import static ca.homedepot.preference.writer.GSFileWriterOutbound.createFileOnGCS;

@Slf4j
@Component
public class PreferenceOutboundFileWriter implements ItemWriter<PreferenceOutboundDtoProcessor>, ItemStream
{
	@Value("${folders.crm.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.compliant}")
	protected String fileNameFormat;

	protected String sourceId;
	@Autowired
	private FileService fileService;

	private JobListener jobListener;

	private final Format formatter = new SimpleDateFormat("yyyyMMdd");

	private int quantityRecords = 0;
	StringBuilder fileBuilder = new StringBuilder();

	private List<Counters> counters;

	private Counters counter = new Counters(0, 0, 0);

	public void setCounters(List<Counters> counters)
	{
		this.counters = counters;
	}

	/**
	 * Method used to generate a plain text file
	 *
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends PreferenceOutboundDtoProcessor> items) throws Exception
	{
		sourceId = items.get(0).getSourceId().replace(SINGLE_DELIMITER_TAB, "");
		for (PreferenceOutboundDtoProcessor preference : items)
		{
			fileBuilder.append(preference.getEmail()).append(preference.getEffectiveDate()).append(preference.getSourceId())
					.append(preference.getEmailStatus()).append(preference.getPhonePtcFlag()).append(preference.getLanguagePref())
					.append(preference.getEarlyOptInDate()).append(preference.getCndCompliantFlag())
					.append(preference.getEmailPrefHdCa()).append(preference.getEmailPrefGardenClub())
					.append(preference.getEmailPrefPro()).append(preference.getPostalCode()).append(preference.getCustomerNbr())
					.append(preference.getPhonePtcFlag()).append(preference.getDnclSuppresion()).append(preference.getPhoneNumber())
					.append(preference.getFirstName()).append(preference.getLastName()).append(preference.getBusinessName())
					.append(preference.getIndustryCode()).append(preference.getCity()).append(preference.getProvince())
					.append(preference.getHdCaProSrcId());
			quantityRecords++;
		}
	}


	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 *
	 * @param file
	 * @throws IOException
	 */


	/**
	 * This method registry in file table the generated file
	 *
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_SEND_PREFERENCES_TO_CRM,
				JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), INSERTEDBY, new Date(), null, null);

		fileService.insertOldId(file);
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Daily Compliant Writer Start");
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Chunck Executed");
	}

	@Override
	public void close() throws ItemStreamException
	{
		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
		jobListener.setFiles(fileName);
		createFileOnGCS(CloudStorageUtils.generatePath(folderSource, fileName), JOB_NAME_SEND_PREFERENCES_TO_CRM,
				fileBuilder.toString().getBytes());
		fileBuilder = new StringBuilder();
		setFileRecord(fileName);
		counter.quantityRecords = quantityRecords;
		counter.fileName = fileName;
		counter.date = new Date().toString();
		counters.add(counter);
		quantityRecords = 0;
	}

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}
}

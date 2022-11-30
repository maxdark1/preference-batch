package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_SEND_PREFERENCES_TO_CRM;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Slf4j
@Component
public class PreferenceOutboundFileWriter implements ItemWriter<PreferenceOutboundDtoProcessor>
{
	@Value("${folders.crm.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.compliant}")
	protected String fileNameFormat;

	private String sourceId;
	@Autowired
	private FileService fileService;

	private final Format formatter = new SimpleDateFormat("yyyyMMdd");

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
		StringBuilder fileBuilder = new StringBuilder();

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
		}

		generateFileGCS(fileBuilder.toString(), "");

	}


	/**
	 * Generate file for GCP purposes
	 */
	private void generateFileGCS(String file, String header)
	{
		String fileName = fileNameFormat.replace(YYYYMMDD_FILE, formatter.format(new Date()));
		setFileRecord(fileName);
		file = header + file;
		byte[] content = file.getBytes();
		GSFileWriterOutbound.createFileOnGCS(CloudStorageUtils.generatePath(folderSource, fileName), content);
	}

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

		fileService.insert(file);
	}

}

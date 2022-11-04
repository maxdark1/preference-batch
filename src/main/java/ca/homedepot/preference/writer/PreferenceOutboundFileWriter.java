package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.*;
import java.util.Date;

@Slf4j
@Component
public class PreferenceOutboundFileWriter implements ItemWriter<PreferenceOutboundDtoProcessor>
{
	@Value("${folders.crm.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSorce;
	@Value("${outbound.files.compliant}")
	protected String file_name_format;
	private FileOutputStream writer;


	private String sourceId;
	@Autowired
	private FileService fileService;

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
		sourceId = items.get(0).getSourceId().replace("\t", "");
		String file = "";

		for (PreferenceOutboundDtoProcessor preference : items)
		{
			String line = "";
			line = preference.getEmail();
			line += preference.getEffectiveDate();
			line += preference.getSourceId();
			line += preference.getEmailStatus();
			line += preference.getPhonePtcFlag();
			line += preference.getLanguagePref();
			line += preference.getEarlyOptInDate();
			line += preference.getCndCompliantFlag();
			line += preference.getEmailPrefHdCa();
			line += preference.getEmailPrefGardenClub();
			line += preference.getEmailPrefPro();
			line += preference.getPostalCode();
			line += preference.getCustomerNbr();
			line += preference.getPhonePtcFlag();
			line += preference.getDnclSuppresion();
			line += preference.getPhoneNumber();
			line += preference.getFirstName();
			line += preference.getLastName();
			line += preference.getBusinessName();
			line += preference.getIndustryCode();
			line += preference.getCity();
			line += preference.getProvince();
			line += preference.getHdCaProSrcId();
			file += line;
		}

		generateFile(file);

	}

	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void generateFile(String file) throws Exception
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String fileName = file_name_format.replace("YYYYMMDD", formatter.format(new Date()));

		writer = new FileOutputStream(repositorySource + folderSorce + fileName, true);
		byte toFile[] = file.getBytes();
		writer.write(toFile);
		writer.close();
		setFileRecord(fileName);
	}

	/**
	 * This method registry in file table the generated file
	 * 
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId("sendPreferencesToCRM");
		Master fileStatus = MasterProcessor.getSourceID("STATUS", SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

}

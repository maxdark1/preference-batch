package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobExecution;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.*;
import java.util.Date;

@Slf4j
@Component
@Data
public class PreferenceOutboundFileWriter implements ItemWriter<PreferenceOutboundDto>, StepExecutionListener
{
	@Value("${folders.crm.path}")
	private String repository_source;
	@Value("${folders.outbound}")
	private String folder_source;
	@Value("${outbound.files.compliant}")
	private String file_name_format;
	private FileOutputStream writer;

	private StepExecution stepExecution;

	private BigDecimal sourceId;
	@Autowired
	private FileService fileService;

    /**
     * Method used to generate a plain text file
     * @param items items to be written
     * @throws Exception
     */
    @Override
    public void write(List<? extends PreferenceOutboundDto> items) throws Exception {
        sourceId = items.get(0).getSourceId();
        String split = SourceDelimitersConstants.SINGLE_DELIMITER_TAB;
        String file = PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS;
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

        for (PreferenceOutboundDto preference: items) {
            String line = "";
            line = preference.getEmail() + split;
            line += formatter.format(preference.getEffectiveDate()) + split;
            line += preference.getSourceId() + split;
            line += preference.getEmailStatus() +split;
            line += preference.getPhonePtcFlag() + split;
            line += preference.getLanguagePref() +split;
            line += formatter.format(preference.getEarlyOptInDate()) + split;
            line += preference.getCndCompliantFlag() + split;
            line += preference.getEmailPrefHdCa() + split;
            line += preference.getEmailPrefGardenClub() + split;
            line += preference.getEmailPrefPro() + split;
            line += preference.getPostalCode() + split;
            line += preference.getCustomerNbr() + split;
            line += preference.getPhonePtcFlag() + split;
            line += preference.getDnclSuppresion() + split;
            line += preference.getPhoneNumber() + split;
            line += preference.getFirstName() + split;
            line += preference.getLastName() + split;
            line += preference.getBusinessName() + split;
            line += preference.getIndustryCode() + split;
            line += preference.getCity() + split;
            line += preference.getProvince() + split;
            line += preference.getHdCaProSrcId() + "\n";
            file += line;
    }

		generateFile(file);

	}

    /**
     * This Method saves in a plain text file the string that receives as parameter
     * @param file
     * @throws IOException
     */
    private void generateFile(String file) throws IOException {
        Format formatter = new SimpleDateFormat("yyyyMMdd");
        String fileName = file_name_format.replace("yyyyMMdd", formatter.format(new Date()));

        writer = new FileOutputStream(repository_source + folder_source + fileName,false);
        byte toFile[] = file.getBytes();
        writer.write(toFile);
        writer.close();
        setFileRecord(fileName);
    }

    /**
     * This method registry in file table the generated file
     * @param fileName
     */
    private void setFileRecord(String fileName){
        BigDecimal jobId = fileService.getJobId("sendPreferencesToCRM");
        Master fileStatus = MasterProcessor.getSourceID("STATUS", SourceDelimitersConstants.VALID);
        FileDTO file = new FileDTO(null, fileName, jobId, sourceId, fileStatus.getValueVal(), fileStatus.getMasterId(),
                new Date(), new Date(), "BATCH", new Date(), null, null);

        fileService.insert(file);
    }

}

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
public class PreferenceOutboundFileWriter implements ItemWriter<PreferenceOutboundDto>, StepExecutionListener {
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
    private FileService fileService ;

    @Override
    public void write(List<? extends PreferenceOutboundDto> items) throws Exception {
        sourceId = items.get(0).getSource_id();
        String split = SourceDelimitersConstants.SINGLE_DELIMITER_TAB;
        String line = "";
        String file = PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS;
        Format formatter = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");

        for (PreferenceOutboundDto preference: items) {
            line = "";
            line = preference.getEmail() + split;
            line += formatter.format(preference.getEffective_date()) + split;
            line += preference.getSource_id() + split;
            line += preference.getEmail_status() +split;
            line += preference.getPhone_ptc_flag() + split;
            line += preference.getLanguage_pref() +split;
            line += formatter.format(preference.getEarly_opt_in_date()) + split;
            line += preference.getCnd_compliant_flag() + split;
            line += preference.getEmail_pref_hd_ca() + split;
            line += preference.getEmail_pref_garden_club() + split;
            line += preference.getEmail_pref_pro() + split;
            line += preference.getPostal_code() + split;
            line += preference.getCustomer_nbr() + split;
            line += preference.getPhone_ptc_flag() + split;
            line += preference.getDncl_suppresion() + split;
            line += preference.getPhone_number() + split;
            line += preference.getFirst_name() + split;
            line += preference.getLast_name() + split;
            line += preference.getBusiness_name() + split;
            line += preference.getIndustry_code() + split;
            line += preference.getCity() + split;
            line += preference.getProvince() + split;
            line += preference.getHd_ca_pro_src_id() + "\n";
            file += line;
    }

        generateFile(file);

    }

    public void generateFile(String file) throws IOException {
        Format formatter = new SimpleDateFormat("YYYYMMDD");
        String file_name = file_name_format.replaceAll("YYYYMMDD", formatter.format(new Date()));

        writer = new FileOutputStream(repository_source + folder_source + file_name,false);
        byte toFile[] = file.getBytes();
        writer.write(toFile);
        writer.close();
        setFileRecord(file_name);
    }

    public void setFileRecord(String file_name){
        ExecutionContext stepExec = this.stepExecution.getExecutionContext();
        JobExecution jobExec = this.stepExecution.getJobExecution();
        BigDecimal jobId = fileService.getJobId("sendPreferencesToCRM");
        fileService.insert(file_name,"VALID",sourceId,new Date(),jobId,new Date(),"BATCH",BigDecimal.valueOf(19),new Date());

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}

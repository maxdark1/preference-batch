package ca.homedepot.preference.listener;


import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.model.OutboundRegistration;
import ca.homedepot.preference.repositories.entities.FileEntity;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.service.FileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class RegistrationItemWriterListener implements ItemWriteListener<OutboundRegistration> {


    private final FileService fileService;

    private String fileRegistration;
    private String jobName;

    private JobListener jobListener;

    private List<FileEntity> fileEntities;

    private DataSource dataSource;

    private BigDecimal file_id;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void beforeWrite(List<? extends OutboundRegistration> items) {
        file_id = writeFile();

        System.out.println(file_id);
        items.forEach(item -> item.setFile_id(file_id));
    }

    public BigDecimal writeFile(){

        BigDecimal job_id = fileService.getJobId(jobName);


        fileService.insert(fileRegistration, true, new BigDecimal("20220902122936254875"), new Date(), fileService.getJobId(jobName), new Date(), "BATCH");
        return fileService.getFile(fileRegistration, job_id);
    }

    @Override
    public void afterWrite(List<? extends OutboundRegistration> items) {

    }

    @Override
    public void onWriteError(Exception exception, List<? extends OutboundRegistration> items) {

    }
    public FileEntity getFileEntity(String fileName, String jobName){
        System.out.println(fileEntities.toString());
        return fileEntities.get(fileEntities.size()-1);
    }
    public void setJobListener(JobListener jobListener) {
        fileEntities = new ArrayList<>();
        this.jobListener = jobListener;
    }
}

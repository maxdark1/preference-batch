package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

//TODO
@Component
@Data
//@AllArgsConstructor
public class ItemReaderListener implements ItemReadListener<Object> {

    private final FileService fileService;

    private String fileName;
    private String jobName;

    private BigDecimal fileID;
    private Master master;

    @Override
    public void beforeRead() {
        BigDecimal jobId = fileService.getJobId(jobName);
        BigDecimal masterId = master.getMaster_id();

        //fileService.insert(fileName, "G", masterId, new Date(), jobId, new Date(), "BATCH");
    }

    @Override
    public void afterRead(Object item) {

    }

    @Override
    public void onReadError(Exception ex) {

    }
}

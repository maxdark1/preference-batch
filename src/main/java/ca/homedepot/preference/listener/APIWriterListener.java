package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.FileService;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class APIWriterListener implements ItemWriteListener<RegistrationRequest> {

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService){
        this.fileService = fileService;
    }

    @Override
    public void beforeWrite(List<? extends RegistrationRequest> items) {

    }

    @Override
    public void afterWrite(List<? extends RegistrationRequest> items) {
        List<BigDecimal> filesId = getMapFileNameFileId(items);

        filesId.forEach(fileId ->  fileService.updateInboundStgTableStatus(fileId,"S", "IP"));
    }

    public List<BigDecimal> getMapFileNameFileId(List<? extends RegistrationRequest> items){
        return items.stream().map(RegistrationRequest::getFileId).distinct().collect(Collectors.toList());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends RegistrationRequest> items) {

    }
}

package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.service.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@Getter
public class RegistrationDBRederListener implements ItemReadListener<RegistrationRequest> {

    private BigDecimal file_id;

    private FileService fileService;

    @Autowired
    private void setFileService(FileService fileService){
        this.fileService = fileService;
    }

    @Override
    public void beforeRead() {
        file_id = fileService.getLasFile();
    }

    @Override
    public void afterRead(RegistrationRequest item) {

    }

    @Override
    public void onReadError(Exception ex) {

    }
}

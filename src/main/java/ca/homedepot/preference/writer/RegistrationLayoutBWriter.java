package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.PreferenceService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Setter
public class RegistrationLayoutBWriter implements ItemWriter<RegistrationRequest> {
    private PreferenceService preferenceService;

    private FileService fileService;

    @Override
    public void write(List<? extends RegistrationRequest> items) throws Exception
    {
        RegistrationResponse response = preferenceService.preferencesSFMCEmailOptOutsLayoutB(items);

        response.getRegistration().forEach(resp -> fileService.updateInboundStgTableStatus( new BigDecimal(resp.getId()), resp.getStatus().substring(0,1), "IP"));
        log.info("Service Response {} :", response);

    }
}

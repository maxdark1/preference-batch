package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Setter
public class RegistrationLayoutBWriter implements ItemWriter<RegistrationRequest> {
    private PreferenceService preferenceService;

    @Override
    public void write(List<? extends RegistrationRequest> items) throws Exception
    {
        RegistrationResponse response = preferenceService.preferencesSFMCEmailOptOutsLayoutB(items);

        log.info("Service Response {} :", response.toString());

    }
}

package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@Setter
public class RegistrationAPIWriter implements ItemWriter<RegistrationRequest> {

    private PreferenceService preferenceService;


    @Value("${service.preference.baseurl}")
    public String baseUrl;


    @Override
    public void write(List<? extends RegistrationRequest> items) throws Exception {
        RegistrationResponse response = preferenceService.preferencesRegistration(items);

        log.info("Service Response {} :", response.toString());

    }
}

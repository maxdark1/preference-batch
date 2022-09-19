package ca.homedepot.preference.writer;

import java.util.List;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Slf4j
@Component
@Setter
public class RegistrationAPIWriter implements ItemWriter<RegistrationRequest>
{
	private PreferenceService preferenceService;


	@Override
	public void write(List<? extends RegistrationRequest> items) throws Exception
	{
		RegistrationResponse response = preferenceService.preferencesRegistration(items);

		log.info("Service Response {} :", response.toString());

	}
}

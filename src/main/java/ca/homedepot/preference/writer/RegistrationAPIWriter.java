package ca.homedepot.preference.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

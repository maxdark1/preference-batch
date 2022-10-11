package ca.homedepot.preference.writer;

import java.math.BigDecimal;
import java.util.List;

import ca.homedepot.preference.service.FileService;
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

	private FileService fileService;


	@Override
	public void write(List<? extends RegistrationRequest> items) throws Exception
	{
		RegistrationResponse response = preferenceService.preferencesRegistration(items);

		response.getRegistration().forEach(resp -> fileService.updateInboundStgTableStatus( new BigDecimal(resp.getId()),resp.getStatus().substring(0,1)));
		log.info("Service Response {} :", response);

	}
}

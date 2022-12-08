package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.PreferenceService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class APIWriterListener implements ItemWriteListener<RegistrationRequest>
{
	private PreferenceService preferenceService;

	private String jobName;

	@Autowired
	public void setPreferenceService(PreferenceService preferenceService)
	{
		this.preferenceService = preferenceService;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	@SneakyThrows
	@Override
	public void beforeWrite(List<? extends RegistrationRequest> items)
	{
		Response response = preferenceService.isServiceAvailable();
		log.info(" Service is available on Job {} to send items through API got response {}.", jobName,
				new Gson().toJson(response));
	}

	@Override
	public void afterWrite(List<? extends RegistrationRequest> items)
	{
		log.info(" Items has been sent throw end point without problems. ");
	}

	@Override
	public void onWriteError(Exception exception, List<? extends RegistrationRequest> items)
	{
		log.error(" PREFERENCE BATCH ERROR - An error occurs while sending to API: {}", exception.getMessage());
	}
}

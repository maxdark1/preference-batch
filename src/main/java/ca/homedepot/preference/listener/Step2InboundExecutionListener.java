package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.PreferenceService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Step2InboundExecutionListener implements StepExecutionListener
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


	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		Response response = preferenceService.isServiceAvailable();
		log.info(" Service is available on Job {} to send items through API got response {}.", jobName,
				new Gson().toJson(response));
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		return ExitStatus.COMPLETED;
	}
}

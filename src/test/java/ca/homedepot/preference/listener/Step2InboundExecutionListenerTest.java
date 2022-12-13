package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.PreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Step2InboundExecutionListenerTest
{

	@Mock
	PreferenceService preferenceService;

	@Mock
	Response response;

	@Mock
	StepExecution stepExecution;

	@InjectMocks
	@Spy
	Step2InboundExecutionListener step2InboundExecutionListener;



	List<RegistrationRequest> items;



	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		RegistrationRequest item = new RegistrationRequest();
		item.setFileId(new BigDecimal("12345"));
		RegistrationRequest item2 = new RegistrationRequest();
		item2.setFileId(new BigDecimal("36918"));
		RegistrationRequest item3 = new RegistrationRequest();
		item3.setFileId(new BigDecimal("24680"));
		RegistrationRequest item4 = new RegistrationRequest();
		item4.setFileId(new BigDecimal("12345"));

		step2InboundExecutionListener.setJobName("jobName");

		items = List.of(item, item2, item3, item4);
	}

	@Test
	void beforeWrite()
	{
		Mockito.when(preferenceService.isServiceAvailable()).thenReturn(response);
		step2InboundExecutionListener.beforeStep(stepExecution);
		Mockito.verify(step2InboundExecutionListener).beforeStep(stepExecution);

	}

	@Test
	void afterStep()
	{
		ExitStatus exitStatus = step2InboundExecutionListener.afterStep(stepExecution);
		Mockito.verify(step2InboundExecutionListener).afterStep(stepExecution);
		assertEquals(ExitStatus.COMPLETED, exitStatus);

	}


}
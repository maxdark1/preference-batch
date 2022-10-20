package ca.homedepot.preference.listener;

import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.validator.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class StepErrorLoggingListenerTest
{

	@Mock
	FileServiceImpl fileService;

	@Mock
	JobExecution jobExecution;

	@Mock
	StepExecution stepExecution;

	@InjectMocks
	StepErrorLoggingListener stepErrorLoggingListener;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void beforeStep()
	{
		stepErrorLoggingListener.beforeStep(stepExecution);
	}

	@Test
	void afterStepFailedCase()
	{
		stepExecution = new StepExecution("TEST step", jobExecution, 1L);
		stepExecution.addFailureException(new ValidationException("This is just an Example"));

		ExitStatus exitStatus = stepErrorLoggingListener.afterStep(stepExecution);
		assertEquals(ExitStatus.FAILED, exitStatus);
	}

	@Test
	void moveFile()
	{

	}
}
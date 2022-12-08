package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.Response;
import ca.homedepot.preference.service.PreferenceService;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class APIWriterListenerTest
{

	@Mock
	PreferenceService preferenceService;

	@Mock
	Response response;
	@InjectMocks
	@Spy
	APIWriterListener apiWriterListener;



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

		apiWriterListener.setJobName("jobName");

		items = List.of(item, item2, item3, item4);
	}

	@Test
	void beforeWrite()
	{
		Mockito.when(preferenceService.isServiceAvailable()).thenReturn(response);
		apiWriterListener.beforeWrite(items);
		Mockito.verify(apiWriterListener).beforeWrite(items);

	}

	@Test
	void afterWrite()
	{
		apiWriterListener.afterWrite(items);
		Mockito.verify(apiWriterListener).afterWrite(items);

	}


	@Test
	void onWriteError()
	{
		Exception exception = new Exception();
		apiWriterListener.onWriteError(exception, items);
		Mockito.verify(apiWriterListener).onWriteError(exception, items);
	}
}
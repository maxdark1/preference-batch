package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class APIWriterListenerTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	APIWriterListener apiWriterListener;

	List<RegistrationRequest> items;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);

		RegistrationRequest item = new RegistrationRequest();
		item.setFileId(new BigDecimal("12345"));
		RegistrationRequest item2 = new RegistrationRequest();
		item2.setFileId(new BigDecimal("36918"));
		RegistrationRequest item3 = new RegistrationRequest();
		item3.setFileId(new BigDecimal("24680"));
		RegistrationRequest item4 = new RegistrationRequest();
		item4.setFileId(new BigDecimal("12345"));

		items = List.of(item, item2, item3, item4);
	}

	@Test
	void beforeWrite()
	{
		apiWriterListener.beforeWrite(items);
	}

	@Test
	void afterWrite()
	{
		apiWriterListener.afterWrite(items);
	}

	@Test
	void getMapFileNameFileId()
	{
		List<BigDecimal> filesId = apiWriterListener.getMapFileNameFileId(items);

		assertEquals(3, filesId.size());
	}

	@Test
	void onWriteError()
	{
		apiWriterListener.onWriteError(new Exception(), items);
	}
}
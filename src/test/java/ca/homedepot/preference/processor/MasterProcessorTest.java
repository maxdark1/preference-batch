package ca.homedepot.preference.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;

class MasterProcessorTest
{

	@Mock
	PreferenceServiceImpl preferenceService;



	@Mock
	List<Master> masterList;

	List<Master> masterInfo;

	@Mock
	Stream<Master> masterStream;

	@InjectMocks
	MasterProcessor masterProcessor;

	Master master;



	@BeforeEach
	void setUp()
	{
		preferenceService = Mockito.mock(PreferenceServiceImpl.class);
		masterProcessor = Mockito.mock(MasterProcessor.class);
		masterList = Mockito.mock((new ArrayList<Master>()).getClass());

		masterProcessor.setPreferenceService(preferenceService);


		master = new Master();
		master.setMaster_id(new BigDecimal("12345"));
		master.setKey_value("SOURCE");
		master.setValue_val("hybris");

		masterInfo = new ArrayList<>();
		masterInfo.add(master);
		masterProcessor.setMasterList(masterInfo);

	}

	@Test
	void getMasterInfo()
	{
		masterProcessor.getMasterInfo();
		Mockito.doNothing().when(masterProcessor).getMasterInfo();

		Mockito.verify(masterProcessor).getMasterInfo();

	}


	@Test
	void getSourceId()
	{

		Master master1 = masterProcessor.getSourceId("SOURCE", "hybris");

		assertEquals(this.master.getMaster_id(), master1.getMaster_id());
	}

	@Test
	void getMasterList()
	{
		assertNotNull(MasterProcessor.getMasterList());
	}
}
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
import org.mockito.MockitoAnnotations;

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
		MockitoAnnotations.initMocks(this);

		master = new Master();
		master.setMaster_id(new BigDecimal("1"));
		master.setKey_value("SOURCE");
		master.setValue_val("hybris");
		master.setOld_id(BigDecimal.ONE);

		Master master2 = new Master();
		master2.setMaster_id(new BigDecimal("2"));
		master2.setKey_value("SOURCE_ID");
		master2.setValue_val("nurun");
		master2.setOld_id(BigDecimal.ONE);

		masterInfo = new ArrayList<>();
		masterInfo.add(master);
		masterInfo.add(master2);
		masterProcessor.setPreferenceService(preferenceService);
		masterProcessor.setMasterList(masterInfo);

	}

	@Test
	void getMasterInfo()
	{

		Mockito.when(preferenceService.getMasterInfo()).thenReturn(masterList);

		masterProcessor.getMasterInfo();
		assertNotNull(MasterProcessor.getMasterList());

	}


	@Test
	void getSourceId()
	{
		Master master1 = masterProcessor.getSourceID("SOURCE", "hybris");

		assertEquals(this.master.getMaster_id(), master1.getMaster_id());
	}

	@Test
	void getSourceIDByOldIDTest()
	{
		BigDecimal masterValid = MasterProcessor.getSourceID("1");
		BigDecimal masterInvalid = MasterProcessor.getSourceID("2");

		assertEquals(BigDecimal.valueOf(2), masterValid);
		assertEquals(new BigDecimal("-400"), masterInvalid);
	}


	@Test
	void getValueValTest()
	{
		String ValueVal = MasterProcessor.getValueVal(BigDecimal.ONE);

		assertEquals("hybris", ValueVal);
	}

	@Test
	void getMasterList()
	{
		assertNotNull(MasterProcessor.getMasterList());
	}
}
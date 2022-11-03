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

	Master master;



	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.initMocks(this);

		master = new Master();
		master.setMasterId(new BigDecimal("1"));
		master.setKeyValue("SOURCE");
		master.setValueVal("hybris");
		master.setOldID(BigDecimal.ONE);

		Master master2 = new Master();
		master2.setMasterId(new BigDecimal("2"));
		master2.setKeyValue("SOURCE_ID");
		master2.setValueVal("nurun");
		master2.setOldID(BigDecimal.ONE);

		masterInfo = new ArrayList<>();
		masterInfo.add(master);
		masterInfo.add(master2);
		MasterProcessor.setPreferenceService(preferenceService);
		MasterProcessor.setMasterList(masterInfo);

	}

	@Test
	void getMasterInfo()
	{

		Mockito.when(preferenceService.getMasterInfo()).thenReturn(masterList);

		MasterProcessor.getMasterInfo();
		assertNotNull(MasterProcessor.getMasterList());

	}


	@Test
	void getSourceId()
	{
		Master master1 = MasterProcessor.getSourceID("SOURCE", "hybris");

		assertEquals(this.master.getMasterId(), master1.getMasterId());
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
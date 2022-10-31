package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreferenceOutboundFileWriterTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	PreferenceOutboundFileWriter preferenceOutboundFileWriter;

	List<PreferenceOutboundDtoProcessor> items;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
		preferenceOutboundFileWriter.setRepository_source("");
		preferenceOutboundFileWriter.setFolder_source("");
		preferenceOutboundFileWriter.setFile_name_format("LOYALTY_DAILY_YYYYMMDD.txt");

		PreferenceOutboundDtoProcessor preferenceOutboundDto = new PreferenceOutboundDtoProcessor();
		preferenceOutboundDto.setSourceId(BigDecimal.ONE.toString());
		preferenceOutboundDto.setEffectiveDate(new Date().toString());
		preferenceOutboundDto.setEarlyOptInDate(new Date().toString());

		items = new ArrayList<>();
		items.add(preferenceOutboundDto);

		List<Master> masterList = new ArrayList<>();
		Master sourceId = new Master();
		sourceId.setMasterId(BigDecimal.ONE);
		sourceId.setKeyValue("SOURCE");
		sourceId.setValueVal("citi_bank");

		Master fileStatus = new Master();
		fileStatus.setMasterId(BigDecimal.TEN);
		fileStatus.setKeyValue("STATUS");
		fileStatus.setValueVal("VALID");
		masterList.add(sourceId);
		masterList.add(fileStatus);

		MasterProcessor.setMasterList(masterList);
	}

	@Test
	void write() throws Exception
	{

		preferenceOutboundFileWriter.write(items);
		Mockito.verify(preferenceOutboundFileWriter).write(items);

	}
}
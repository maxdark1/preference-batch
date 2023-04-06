package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.ExecutionContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.CITI_SUP;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class PreferenceOutboundCitiWriterTest
{

	@Mock
	Storage storage;
	@Mock
	JobListener jobListener;

	CloudStorageUtils cloudStorageUtils;
	@InjectMocks
	@Spy
	PreferenceOutboundCitiWriter preferenceOutboundCitiWriter;

	List<CitiSuppresionOutboundDTO> items;

	@Mock
	ExecutionContext executionContext;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		cloudStorageUtils = new CloudStorageUtils();
		cloudStorageUtils.setProjectId("projectID");
		cloudStorageUtils.setBucketName("bucketName");
		StorageApplicationGCS.setStorage(storage);
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
		preferenceOutboundCitiWriter.fileNameFormat = "THDIN_THDDNS_MASTER_SUPPRESSION_YYYYMMDD.txt.pgp";
		preferenceOutboundCitiWriter.folderSource = "";
		preferenceOutboundCitiWriter.repositorySource = "";

		CitiSuppresionOutboundDTO citiDto = new CitiSuppresionOutboundDTO();
		citiDto.setPhone(BigDecimal.ONE.toString());
		citiDto.setFirstName("NAME");
		citiDto.setLastName("LAST NAME");

		items = new ArrayList<>();
		items.add(citiDto);

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

		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("10"), new BigDecimal("10"), "SOURCE_ID", "IN PROGRESS", true, BigDecimal.TEN));

		MasterProcessor.setMasterList(masterList);

		executionContext = new ExecutionContext();
		preferenceOutboundCitiWriter.setJobListener(jobListener);
	}



	@Test
	void write() throws Exception
	{

		preferenceOutboundCitiWriter.write(items);
		Mockito.verify(preferenceOutboundCitiWriter).write(items);

	}

	@Test
	void open()
	{
		preferenceOutboundCitiWriter.open(executionContext);
		Mockito.verify(preferenceOutboundCitiWriter).open(executionContext);
	}

	@Test
	void update()
	{
		preferenceOutboundCitiWriter.update(executionContext);
		Mockito.verify(preferenceOutboundCitiWriter).update(executionContext);
	}

	@Test
	void close()
	{
		try
		{
			Counters counter = new Counters(0, 0, 0);
			List<Counters> counters = new ArrayList<>();
			counters.add(counter);
			preferenceOutboundCitiWriter.source = CITI_SUP;
			preferenceOutboundCitiWriter.setCounters(counters);
			preferenceOutboundCitiWriter.sourceId = BigDecimal.TEN.toString();
			preferenceOutboundCitiWriter.setJobListener(jobListener);
			preferenceOutboundCitiWriter.close();
			Mockito.verify(preferenceOutboundCitiWriter).close();
		}
		catch (Exception ex)
		{
			assertNotNull(preferenceOutboundCitiWriter);
		}
	}
}
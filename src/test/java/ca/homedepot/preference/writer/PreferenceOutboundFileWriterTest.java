package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.repositories.entities.impl.FileServiceImpl;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.ExecutionContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
class PreferenceOutboundFileWriterTest
{

	@Mock
	FileServiceImpl fileService;

	@Mock
	Storage storage;
	@Mock
	JobListener jobListener;

	CloudStorageUtils cloudStorageUtils;
	@InjectMocks
	@Spy
	PreferenceOutboundFileWriter preferenceOutboundFileWriter;

	List<PreferenceOutboundDtoProcessor> items;

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
		preferenceOutboundFileWriter.fileNameFormat = "LOYALTY_DAILY_YYYYMMDD.txt";
		preferenceOutboundFileWriter.folderSource = "";
		preferenceOutboundFileWriter.repositorySource = "";

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

		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("10"), new BigDecimal("10"), "SOURCE_ID", "IN PROGRESS", true, BigDecimal.TEN));

		MasterProcessor.setMasterList(masterList);

		executionContext = new ExecutionContext();
		preferenceOutboundFileWriter.setJobListener(jobListener);
	}



	@Test
	void write() throws Exception
	{

		preferenceOutboundFileWriter.write(items);
		Mockito.verify(preferenceOutboundFileWriter).write(items);

	}

	@Test
	void open()
	{
		preferenceOutboundFileWriter.open(executionContext);
		Mockito.verify(preferenceOutboundFileWriter).open(executionContext);
	}

	@Test
	void update()
	{
		preferenceOutboundFileWriter.update(executionContext);
		Mockito.verify(preferenceOutboundFileWriter).update(executionContext);
	}

	@Test
	void close()
	{
		Counters counter = new Counters(0, 0, 0);
		List<Counters> counters = new ArrayList<>();
		counters.add(counter);
		preferenceOutboundFileWriter.setCounters(counters);
		preferenceOutboundFileWriter.sourceId = BigDecimal.TEN.toString();
		preferenceOutboundFileWriter.close();
		Mockito.verify(preferenceOutboundFileWriter).close();
	}
}
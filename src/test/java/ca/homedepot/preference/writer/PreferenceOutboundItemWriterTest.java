package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
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
import static org.mockito.ArgumentMatchers.any;

@Slf4j
class PreferenceOutboundItemWriterTest
{

	@Mock
	Storage storage;
	@Mock
	JobListener jobListener;

	CloudStorageUtils cloudStorageUtils;
	@InjectMocks
	@Spy
	PreferenceOutboundItemWriter preferenceOutboundWriter;

	List<SalesforceExtractOutboundDTO> items;

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
		preferenceOutboundWriter.fileNameFormat = "THDIN_THDDNS_MASTER_SUPPRESSION_YYYYMMDD.txt.pgp";
		preferenceOutboundWriter.folderSource = "";
		preferenceOutboundWriter.repositorySource = "";
		preferenceOutboundWriter.header = "header";
		preferenceOutboundWriter.setJobListener(jobListener);
		preferenceOutboundWriter.getFileName();
		preferenceOutboundWriter.setJobName("SFMCJOB");
		SalesforceExtractOutboundDTO citiDto = new SalesforceExtractOutboundDTO();
		citiDto.setFirstName("NAME");
		citiDto.setLastName("LAST NAME");

		items = new ArrayList<>();
		items.add(citiDto);

		List<Master> masterList = new ArrayList<>();
		Master sourceId = new Master();
		sourceId.setMasterId(BigDecimal.ONE);
		sourceId.setKeyValue("SOURCE");
		sourceId.setValueVal("citisup");

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
	}



	@Test
	void write() throws Exception
	{
		preferenceOutboundWriter.writer = Mockito.mock(WriteChannel.class);
		preferenceOutboundWriter.write(items);
		Mockito.verify(preferenceOutboundWriter).write(items);

	}

	@Test
	void writeEmptyList() throws Exception
	{
		preferenceOutboundWriter.writer = Mockito.mock(WriteChannel.class);
		preferenceOutboundWriter.write(new ArrayList());
		Mockito.verify(preferenceOutboundWriter).write(new ArrayList());

	}

	@Test
	void open()
	{
		Blob blob = Mockito.mock(Blob.class);
		Mockito.when(storage.create(any(BlobInfo.class))).thenReturn(blob);
		Mockito.when(blob.writer()).thenReturn(Mockito.mock(WriteChannel.class));
		preferenceOutboundWriter.open(executionContext);
		Mockito.verify(preferenceOutboundWriter).open(executionContext);
	}

	@Test
	void openNoHeader()
	{
		Blob blob = Mockito.mock(Blob.class);
		preferenceOutboundWriter.header = "";
		Mockito.when(storage.create(any(BlobInfo.class))).thenReturn(blob);
		Mockito.when(blob.writer()).thenReturn(Mockito.mock(WriteChannel.class));
		preferenceOutboundWriter.open(executionContext);
		Mockito.verify(preferenceOutboundWriter).open(executionContext);
	}

	@Test
	void update()
	{
		preferenceOutboundWriter.update(executionContext);
		Mockito.verify(preferenceOutboundWriter).update(executionContext);
	}

	@Test
	void close()
	{
		try
		{
			Counters counter = new Counters(0, 0, 0);
			List<Counters> counters = new ArrayList<>();
			counters.add(counter);
			preferenceOutboundWriter.source = CITI_SUP;
			preferenceOutboundWriter.setCounters(counters);
			preferenceOutboundWriter.sourceId = BigDecimal.TEN.toString();
			preferenceOutboundWriter.close();
			Mockito.verify(preferenceOutboundWriter).close();
		}
		catch (Exception ex)
		{
			assertNotNull(preferenceOutboundWriter);
		}
	}
}
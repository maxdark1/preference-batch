package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.ExecutionContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
class InternalOutboundFileWriterTest
{



	String internalFolder;

	@Mock
	FileServiceImpl fileService;

	@Mock
	JobListener jobListener;

	@InjectMocks
	@Spy
	InternalOutboundFileWriter internalOutboundFileWriter;

	List<InternalOutboundProcessorDto> items;

	@Mock
	BlobInfo.Builder builder;

	CloudStorageUtils cloudStorageUtils;

	@Mock
	ExecutionContext executionContext;

	@Mock
	Storage storage;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		cloudStorageUtils = new CloudStorageUtils();
		cloudStorageUtils.setProjectId("projectId");
		cloudStorageUtils.setBucketName("bucketName");
		StorageApplicationGCS.setStorage(storage);
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
		internalOutboundFileWriter.folderSource = "";
		internalOutboundFileWriter.repositorySource = "";
		internalOutboundFileWriter.caFileFormat = "CA_COMPLIANT_FILE_YYYYMMDD.csv";
		internalOutboundFileWriter.gardenFileFormat = "GARDEN_CLUB_COMPLIANT_FILE_YYYYMMDD.csv";
		internalOutboundFileWriter.moverFileFormat = "NEW_MOVER_COMPLIANT_FILE_YYYYMMDD.csv";

		InternalOutboundProcessorDto internalOutboundProcessorDto = new InternalOutboundProcessorDto();
		internalOutboundProcessorDto.setCanPtcSourceId(BigDecimal.ONE.toString());
		internalOutboundProcessorDto.setCanPtcEffectiveDate(new Date().toString());
		internalOutboundProcessorDto.setEarlyOptInDate(new Date().toString());

		items = new ArrayList<>();
		items.add(internalOutboundProcessorDto);

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

		MasterProcessor.setMasterList(masterList);

		executionContext = new ExecutionContext();
		internalOutboundFileWriter.setJobListener(jobListener);
	}

	@Test
	void write() throws Exception
	{
		internalOutboundFileWriter.write(items);
		Mockito.verify(internalOutboundFileWriter).write(items);
	}

	@AfterAll
	static void tearDown() throws IOException
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd");
	}

	@Test
	void open()
	{
		internalOutboundFileWriter.open(executionContext);
		Mockito.verify(internalOutboundFileWriter).open(executionContext);
	}

	@Test
	void update()
	{
		internalOutboundFileWriter.update(executionContext);
		Mockito.verify(internalOutboundFileWriter).update(executionContext);
	}

	@Test
	void close()
	{
		internalOutboundFileWriter.sourceId = BigDecimal.TEN.toString();
		internalOutboundFileWriter.close();
		Mockito.verify(internalOutboundFileWriter).close();
	}
}
package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GSFileWriterOutboundTest
{

	@Mock
	StringBuilder stringBuilder;

	@Mock
	FileServiceImpl fileServiceImpl;
	@Mock
	CloudStorageUtils cloudStorageUtils;

	@Mock
	Storage storage;
	@Mock
	OutputStream os;
	@InjectMocks
	@Spy
	GSFileWriterOutbound<CitiSuppresionOutboundDTO> gsFileWriterOutbound;

	List<CitiSuppresionOutboundDTO> citiSuppresionOutboundDTOList;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		cloudStorageUtils = new CloudStorageUtils();
		cloudStorageUtils.setProjectId("projectID");
		cloudStorageUtils.setBucketName("BucketName");
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
		StorageApplicationGCS.setStorage(storage);
		gsFileWriterOutbound.setStringBuilder(stringBuilder);
		gsFileWriterOutbound.setOs(os);
		gsFileWriterOutbound.setFileService(fileServiceImpl);
		gsFileWriterOutbound.setHeader("header");
		gsFileWriterOutbound.setFileNameFormat("filename_YYYYMMDD.txt");

		citiSuppresionOutboundDTOList = new ArrayList<>();
		CitiSuppresionOutboundDTO citi = new CitiSuppresionOutboundDTO();
		citi.setCity("City");
		citi.setAddrLine1("addrline1");
		citi.setPhone("phone");

		citiSuppresionOutboundDTOList.add(citi);

		List<Master> masterList = new ArrayList<>();
		Master sourceId = new Master();
		sourceId.setMasterId(BigDecimal.ONE);
		sourceId.setKeyValue("SOURCE_ID");
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
	}

	@Test
	void close()
	{
		Counters counter = new Counters(0, 0, 0);
		List<Counters> counters = new ArrayList<>();
		counters.add(counter);
		gsFileWriterOutbound.setSource("citisup");
		gsFileWriterOutbound.setCounters(counters);
		gsFileWriterOutbound.close();
		Mockito.verify(gsFileWriterOutbound).close();
	}


	@Test
	void getTempFile()
	{
		File tempFile = FileUtil.createTempFile("file.txt");
		gsFileWriterOutbound.setTempFile(tempFile);
		assertEquals(tempFile, gsFileWriterOutbound.getTempFile());
	}

	@Test
	void getOs()
	{
		assertEquals(os, gsFileWriterOutbound.getOs());
	}

	@Test
	void getStringBuilder()
	{
		assertEquals(stringBuilder, gsFileWriterOutbound.getStringBuilder());
	}


}
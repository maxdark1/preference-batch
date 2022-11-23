package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static ca.homedepot.preference.constants.SchedulerConfigConstants.CITI_SUPRESSION_NAMES;
import static org.junit.jupiter.api.Assertions.*;

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
	}

	@Test
	void close()
	{
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
package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiResourceItemReaderInboundTest
{

	@Mock
	FileService fileService;

	@InjectMocks
	@Spy
	MultiResourceItemReaderInbound multiResourceItemReaderInbound;


	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.initMocks(this);
		multiResourceItemReaderInbound.setSource("hybris");
		FileValidation.setExtensionRegex(".txt|.TXT");
		FileValidation.setFbSFMCBaseName("fbsfmc_");
		FileUtil.setHybrisPath("test");
		FileUtil.setInbound("/inbound/");
		FileUtil.setProcessed("/processed/");
		FileUtil.setError("/error/");

		File file = new File("test/inbound/archive_20220222.txt");
		file.mkdirs();

		file = new File("test/processed");
		file.mkdirs();

		file = new File("test/error");
		file.mkdirs();
	}


	@AfterEach
	void tearDown() throws IOException
	{
		File file = new File("test");
		FileUtils.deleteDirectory(file);
	}

	@Test
	void writeFile()
	{
		String fileName = "fileNAme";
		Boolean status = true;
		multiResourceItemReaderInbound.setSource("hybris");
		multiResourceItemReaderInbound.setFileService(fileService);
		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "SOURCE", "hybris", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "VALID", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "INVALID", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));

		MasterProcessor.setMasterList(masterList);

		multiResourceItemReaderInbound.writeFile(fileName, status);
		Mockito.verify(multiResourceItemReaderInbound).writeFile(fileName, status);
	}

	@Test
	void writeFileFalseStatus()
	{
		String fileName = "fileNAme";
		Boolean status = false;
		multiResourceItemReaderInbound.setSource("FB_SFMC");
		multiResourceItemReaderInbound.setFileService(fileService);
		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "SOURCE", "SFMC", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "VALID", true, null));
		masterList.add(new Master(BigDecimal.ONE, BigDecimal.ONE, "STATUS", "INVALID", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		MasterProcessor.setMasterList(masterList);

		multiResourceItemReaderInbound.writeFile(fileName, status);
		Mockito.verify(multiResourceItemReaderInbound).writeFile(fileName, status);
	}

	@Test
	void read() throws Exception
	{
		Object value = new Object();
		Resource resource = new FileSystemResource("test/archive_20220222.txt");
		multiResourceItemReaderInbound.setResources(new Resource[] {});
		Mockito.when(multiResourceItemReaderInbound.read()).thenReturn(value);
		Mockito.when(multiResourceItemReaderInbound.getCurrentResource()).thenReturn(resource);

		multiResourceItemReaderInbound.read();
		Mockito.verify(multiResourceItemReaderInbound, Mockito.times(2)).read();
	}

	@Test
	void setSource()
	{
		String source = "source";
		multiResourceItemReaderInbound.setSource(source);
		Mockito.verify(multiResourceItemReaderInbound).setSource(source);
	}

	@Test
	void setResources()
	{
		Resource resource = new FileSystemResource("test/archive_20220222.txt");
		Resource[] resources = new Resource[]
		{ resource };
		multiResourceItemReaderInbound.setResources(resources);

		Mockito.verify(multiResourceItemReaderInbound).setResources(resources);
	}

}
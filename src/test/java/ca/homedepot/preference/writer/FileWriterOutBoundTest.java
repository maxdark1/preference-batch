package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


class FileWriterOutBoundTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	FileWriterOutBound fileWriterOutBound;

	File file = new File("repositorySource/folder");

	@BeforeEach
	void setUp() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		fileWriterOutBound.setRepositorySource("repositorySource");
		fileWriterOutBound.setFolderSource("/folder/");
		fileWriterOutBound.setFileNameFormat("filenameformat_YYYYMMDD");

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

		file.mkdirs();
		Format formatter = new SimpleDateFormat("YYYYMMDD");
		String fileName = "filenameformat_YYYYMMDD".replace("YYYYMMDD", formatter.format(new Date()));
		File files = new File("repositorySource/folder/" + fileName);
		files.createNewFile();
	}

	@AfterAll
	static void tearDown() throws IOException
	{
		File file = new File("repositorySource");
		FileUtils.deleteDirectory(file);
	}


	@Test
	void write() throws Exception
	{
		fileWriterOutBound.setFileName("fileName_YYYYMMDD");
		List<CitiSuppresionOutboundDTO> listCiti = new ArrayList<>();
		listCiti.add(new CitiSuppresionOutboundDTO("example", "e", "john", "address1", "address2", "toronto", "on", "123456",
				"email@example.com", "1234567890", "1234056987", "bussinessName", "N", "N", "N", "N"));

		Mockito.doNothing().when(fileWriterOutBound).saveFileRecord();
		Mockito.doNothing().when(fileWriterOutBound).write(listCiti);

		fileWriterOutBound.write(listCiti);
		Mockito.verify(fileWriterOutBound).write(listCiti);

	}

	@Test
	void saveFileRecord()
	{
		BigDecimal jobId = BigDecimal.TEN;
		FileDTO fileDTO = Mockito.mock(FileDTO.class);
		String fileName = "fileName";
		int expectedValue = 1;

		Mockito.when(fileService.getJobId(anyString())).thenReturn(jobId);
		Mockito.when(fileService.insert(fileDTO)).thenReturn(expectedValue);

		fileWriterOutBound.saveFileRecord();
		Mockito.verify(fileWriterOutBound).saveFileRecord();

	}

	@Test
	void setResourcetest()
	{
		fileWriterOutBound.setRepositorySource("repositorySource");
		fileWriterOutBound.setFolderSource("/folder/");
		fileWriterOutBound.setFileName("filenameformat_YYYYMMDD");

		Mockito.doNothing().when(fileWriterOutBound).setResource();
		fileWriterOutBound.setResource();
		Mockito.verify(fileWriterOutBound).setResource();
	}

	@Test
	void getFileService()
	{
		assertNotNull(fileWriterOutBound.getFileService());
	}

	@Test
	void setResource()
	{

		fileWriterOutBound.setResource();
		Mockito.verify(fileWriterOutBound).setResource();
	}

	@Test
	void getRepositorySource()
	{
		assertEquals("repositorySource", fileWriterOutBound.getRepositorySource());
	}

	@Test
	void getFolderSource()
	{
		assertEquals("/folder/", fileWriterOutBound.getFolderSource());
	}

	@Test
	void getFileNameFormat()
	{
		assertEquals("filenameformat_YYYYMMDD", fileWriterOutBound.getFileNameFormat());
	}

	@Test
	void getJobName()
	{
		fileWriterOutBound.setJobName("JOB_NAME");
		assertEquals("JOB_NAME", fileWriterOutBound.getJobName());
	}

	@Test
	void getFileName()
	{
		fileWriterOutBound.setFileName("fileName");
		assertEquals("fileName", fileWriterOutBound.getFileName());
	}
}
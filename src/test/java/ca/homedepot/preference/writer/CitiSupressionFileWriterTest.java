package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


class CitiSupressionFileWriterTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	CitiSupressionFileWriter citiSupressionFileWriter;

	File file = new File("repositorySource/folder");

	@BeforeEach
	void setUp() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		citiSupressionFileWriter.setRepositorySource("repositorySource");
		citiSupressionFileWriter.setFolderSource("/folder/");
		citiSupressionFileWriter.setFileNameFormat("filenameformat_YYYYMMDD");

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

	@AfterEach
	void onTestFinish()
	{
		file = new File("repositorySource");
		file.delete();
	}


	@Test
	void write() throws Exception
	{
		citiSupressionFileWriter.setFileName("fileName_YYYYMMDD");
		List<CitiSuppresionOutboundDTO> listCiti = new ArrayList<>();
		listCiti.add(new CitiSuppresionOutboundDTO("example", "e", "john", "address1", "address2", "toronto", "on", "123456",
				"email@example.com", "1234567890", "1234056987", "bussinessName", "N", "N", "N", "N"));

		Mockito.doNothing().when(citiSupressionFileWriter).saveFileRecord();
		Mockito.doNothing().when(citiSupressionFileWriter).write(listCiti);

		citiSupressionFileWriter.write(listCiti);
		Mockito.verify(citiSupressionFileWriter).write(listCiti);

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

		citiSupressionFileWriter.saveFileRecord();
		Mockito.verify(citiSupressionFileWriter).saveFileRecord();

	}

	@Test
	void setResourcetest()
	{
		citiSupressionFileWriter.setRepositorySource("repositorySource");
		citiSupressionFileWriter.setFolderSource("/folder/");
		citiSupressionFileWriter.setFileName("filenameformat_YYYYMMDD");

		Mockito.doNothing().when(citiSupressionFileWriter).setResource();
		citiSupressionFileWriter.setResource();
	}

	@Test
	void getFileService()
	{
		assertNotNull(citiSupressionFileWriter.getFileService());
	}

	@Test
	void setResource()
	{

		citiSupressionFileWriter.setResource();
		Mockito.verify(citiSupressionFileWriter).setResource();
	}

	@Test
	void getRepositorySource()
	{
		assertEquals("repositorySource", citiSupressionFileWriter.getRepositorySource());
	}

	@Test
	void getFolderSource()
	{
		assertEquals("/folder/", citiSupressionFileWriter.getFolderSource());
	}

	@Test
	void getFileNameFormat()
	{
		assertEquals("filenameformat_YYYYMMDD", citiSupressionFileWriter.getFileNameFormat());
	}

	@Test
	void getJobName()
	{
		citiSupressionFileWriter.setJobName("JOB_NAME");
		assertEquals("JOB_NAME", citiSupressionFileWriter.getJobName());
	}

	@Test
	void getFileName()
	{
		citiSupressionFileWriter.setFileName("fileName");
		assertEquals("fileName", citiSupressionFileWriter.getFileName());
	}
}
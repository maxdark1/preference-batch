package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.repositories.entities.impl.FileServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class SalesforceExtractFileWriterTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	SalesforceExtractFileWriter salesforceExtractFileWriter;

	File file = new File("repositorySource/folder");

	@BeforeEach
	void setUp() throws IOException
	{
		MockitoAnnotations.openMocks(this);

		salesforceExtractFileWriter.setRepositorySource("repositorySource");
		salesforceExtractFileWriter.setFolderSource("/folder/");
		salesforceExtractFileWriter.setFileNameFormat("ET_YYYYMMDD.TXT.PGP");
		salesforceExtractFileWriter.setJobName("Job_Name");

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

		masterList.add(new Master(new BigDecimal("15"), new BigDecimal("5"), "JOB_STATUS", "STARTED", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("17"), new BigDecimal("5"), "JOB_STATUS", "COMPLETED", true, null));
		masterList.add(new Master(new BigDecimal("18"), new BigDecimal("5"), "JOB_STATUS", "ERROR", true, null));

		MasterProcessor.setMasterList(masterList);

		file.mkdirs();
		Format formatter = new SimpleDateFormat("YYYYMMDD");
		String fileName = "ET_YYYYMMDD".replace("YYYYMMDD", formatter.format(new Date()));
		File files = new File("repositorySource/folder/" + fileName);
		files.createNewFile();
	}

	@AfterEach
	void tearDown() throws IOException
	{
		FileUtils.deleteDirectory(new File("repositorySource"));
	}

	@Test
	void saveFileRecord()
	{
		BigDecimal jobId = BigDecimal.TEN;
		FileDTO fileDTO = Mockito.mock(FileDTO.class);
		int expectedValue = 1;

		Mockito.when(fileService.getJobId(anyString(), any(BigDecimal.class))).thenReturn(jobId);
		Mockito.when(fileService.insert(fileDTO)).thenReturn(expectedValue);

		salesforceExtractFileWriter.saveFileRecord();
		Mockito.verify(salesforceExtractFileWriter).saveFileRecord();
	}

	@Test
	void setResource()
	{
		salesforceExtractFileWriter.setResource();
		Mockito.verify(salesforceExtractFileWriter).setResource();
	}

	@Test
	void getRepositorySource()
	{
		assertEquals("repositorySource", salesforceExtractFileWriter.getRepositorySource());
	}

	@Test
	void getFolderSource()
	{
		assertEquals("/folder/", salesforceExtractFileWriter.getFolderSource());
	}

	@Test
	void getFileNameFormat()
	{
		assertEquals("ET_YYYYMMDD.TXT.PGP", salesforceExtractFileWriter.getFileNameFormat());
	}

	@Test
	void getJobName()
	{
		salesforceExtractFileWriter.setJobName("Job_Name");
		assertEquals("Job_Name", salesforceExtractFileWriter.getJobName());
	}

	@Test
	void getFileName()
	{
		salesforceExtractFileWriter.setFileName("fileName");
		assertEquals("fileName", salesforceExtractFileWriter.getFileName());
	}


	@Test
	void getFileService()
	{
		salesforceExtractFileWriter.setFileService(fileService);
		assertNotNull(salesforceExtractFileWriter.getFileService());
	}

}
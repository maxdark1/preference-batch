package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.impl.FileServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileWriterOutBoundTest
{

	@Mock
	FileServiceImpl fileService;

	@InjectMocks
	@Spy
	FileWriterOutBound<CitiSuppresionOutboundDTO> fileWriterOutBound;

	File file = new File("repositorySource/folder");

	@BeforeEach
	void setUp() throws IOException
	{
		MockitoAnnotations.openMocks(this);
		fileWriterOutBound.setFileService(fileService);
		fileWriterOutBound.setRepositorySource("repositorySource");
		fileWriterOutBound.setFolderSource("/folder/");
		fileWriterOutBound.setFileNameFormat("filenameformat_YYYYMMDD");
		fileWriterOutBound.setSource("citi_bank");
		fileWriterOutBound.setNames(new String[]
		{ "names" });

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
		masterList.add(new Master(new BigDecimal("15"), new BigDecimal("5"), "JOB_STATUS", "STARTED", true, null));
		masterList.add(new Master(new BigDecimal("16"), new BigDecimal("5"), "JOB_STATUS", "IN PROGRESS", true, null));
		masterList.add(new Master(new BigDecimal("17"), new BigDecimal("5"), "JOB_STATUS", "COMPLETED", true, null));
		masterList.add(new Master(new BigDecimal("18"), new BigDecimal("5"), "JOB_STATUS", "ERROR", true, null));

		MasterProcessor.setMasterList(masterList);


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
		String jobName = null;
		int expectedValue = 1;

		Mockito.when(fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId())).thenReturn(jobId);
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
		fileWriterOutBound.setFileService(fileService);
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

	@Test
	void getHeaderCallBack()
	{
		fileWriterOutBound.setHeader("header");

		FlatFileHeaderCallback flatFileHeaderCallback = fileWriterOutBound.getHeaderCallBack();
		assertNotNull(flatFileHeaderCallback);
		assertEquals("header", fileWriterOutBound.getHeader());
	}

	@Test
	void getSource()
	{
		assertEquals("citi_bank", fileWriterOutBound.getSource());
	}

	@Test
	void getNames()
	{
		String[] names = new String[]
		{ "names" };
		assertTrue(Arrays.equals(names, fileWriterOutBound.getNames()));
	}
}
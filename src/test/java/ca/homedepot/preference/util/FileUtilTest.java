package ca.homedepot.preference.util;

import ca.homedepot.preference.util.validation.FileValidation;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class FileUtilTest
{

	@BeforeEach
	void setup()
	{

		FileValidation.setExtensionRegex(".txt.AXOSTD|.TXT.THD.txt.gpg|.pgp|.txt|.TXT");
		File file = new File("fileUtil/INBOUND/archive_20220211.txt");
		File file2 = new File("fileUtil/INBOUND/archiv_20220212.txt");
		File file3 = new File("fileUtil/PROCESSED/");
		File file4 = new File("fileUtil/ERROR/");

		file.mkdirs();
		file2.mkdirs();
		file3.mkdirs();
		file4.mkdirs();

	}

	@AfterEach
	void tearDown() throws IOException
	{
		File file = new File("fileUtil");
		FileUtils.deleteDirectory(file);
	}

	@Test
	void getRegistrationFile()
	{
		FileUtil.setRegistrationFile("registrationFile");
		assertEquals("registrationFile", FileUtil.getRegistrationFile());

	}

	@Test
	void getFileExtTargetEmail()
	{

		FileUtil.setFileExtTargetEmail("fileExtTargetEmail");
		assertEquals("fileExtTargetEmail", FileUtil.getFileExtTargetEmail());
	}


	@Test
	void getPathTest()
	{
		FileUtil.setHybrisPath("hybris");
		FileUtil.setCrmPath("CRM");
		FileUtil.setSfmcPath("SFMC");
		FileUtil.setFbsfmcPath("FB_SFMC");

		assertEquals("hybris", FileUtil.getPath("hybris"));
		assertEquals("CRM", FileUtil.getPath("CRM"));
		assertEquals("SFMC", FileUtil.getPath("SFMC"));
		assertEquals("FB_SFMC", FileUtil.getPath("Whatever"));
	}

	@Test
	void getCrmPath()
	{
		FileUtil.setCrmPath("CRM");

		assertEquals("CRM", FileUtil.getCrmPath());
	}

	@Test
	void getInbound()
	{
		FileUtil.setInbound("inbound/");

		assertEquals("inbound/", FileUtil.getInbound());
	}

	@Test
	void getError()
	{
		FileUtil.setError("error/");

		assertEquals("error/", FileUtil.getError());
	}

	@Test
	void getProcessed()
	{
		FileUtil.setProcessed("processed/");

		assertEquals("processed/", FileUtil.getProcessed());
	}

	@Test
	void renameFile()
	{
		String fileName = "OPTIN_STANDARD_FLEX_20221216.TXT";
		String fileNameWithout = "OPTIN_STANDARD_FLEX_20221216";

		String newFile = FileUtil.renameFile(fileName);
		assertNotEquals(fileName.length(), newFile.length());
		assertTrue(newFile.contains(fileNameWithout));
	}

	@Test
	void isFBSFMC()
	{
		FileValidation.setFbSFMCBaseName("OPTIN_STANDARD_FLEX_GCFB_");
		String fileName = "OPTIN_STANDARD_FLEX_20221216.TXT";

		assertTrue(!FileUtil.isFBSFMC(fileName));
	}

	@Test
	void getSfmcPath()
	{
		String sfmcPath = "C:/BatchFiles/SFMC";

		FileUtil.setSfmcPath(sfmcPath);
		assertEquals(sfmcPath, FileUtil.getSfmcPath());
	}

	@Test
	void getFbSfmcPath()
	{
		String fbSFMCPath = "C:/BatchFiles/FB_SFMC";

		FileUtil.setFbsfmcPath(fbSFMCPath);

		assertEquals(fbSFMCPath, FileUtil.getFbsfmcPath());
	}

	@Test
	void getFilesOnFolder()
	{
		String path = "fileUtil/INBOUND/";
		String source = "hybris";
		String archive = "archive_";
		FileValidation.setHybrisBaseName(archive);
		Map<String, List<Resource>> files = FileUtil.getFilesOnFolder(path, source);

		int expectedValid = files.get("VALID").size();
		int expectedInvalid = files.get("INVALID").size();

		System.out.println(files.get("INVALID").toString());

		assertNotNull(files);
		assertEquals(expectedValid, files.get("VALID").size());
		assertEquals(expectedInvalid, files.get("INVALID").size());
	}

	@Test
	void moveFile() throws IOException
	{
		String file = "archive_20220211.txt";
		String file2 = "archiv_20220212.txt";

		FileUtil.setError("/ERROR/");
		FileUtil.setProcessed("/PROCESSED/");
		FileUtil.setProcessed("/INBOUND/");
		FileUtil.setInbound("/INBOUND/");
		FileUtil.setHybrisPath("fileUtil/");


		FileUtil.moveFile(file, true, "hybris");
		FileUtil.moveFile(file2, false, "hybris");
		assertEquals("archive_20220211.txt", file);
		assertEquals("archiv_20220212.txt", file2);

	}
}
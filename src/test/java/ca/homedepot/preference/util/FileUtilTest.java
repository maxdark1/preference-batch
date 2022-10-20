package ca.homedepot.preference.util;

import ca.homedepot.preference.util.validation.FileValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest
{
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
	void moveFile() throws Exception
	{
		FileUtil.setHybrisPath("TEST_PATH");

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
		FileValidation.setExtensionRegex(".txt.AXOSTD|.TXT.THD.txt.gpg|.pgp|.txt|.TXT");
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
}
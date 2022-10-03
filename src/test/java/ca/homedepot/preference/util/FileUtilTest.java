package ca.homedepot.preference.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.File;

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
	void getPathTest(){
		FileUtil.setHybrisPath("hybris");
		FileUtil.setCrmPath("CRM");

		assertEquals("hybris", FileUtil.getPath("hybris"));
		assertEquals("CRM", FileUtil.getPath("CRM"));
		assertEquals(null, FileUtil.getPath("Whatever"));
	}

	@Test
	void getCrmPath(){
		FileUtil.setCrmPath("CRM");

		assertEquals("CRM", FileUtil.getCrmPath());
	}

	@Test
	void getInbound(){
		FileUtil.setInbound("inbound/");

		assertEquals("inbound/", FileUtil.getInbound());
	}

	@Test
	void getError(){
		FileUtil.setError("error/");

		assertEquals("error/", FileUtil.getError());
	}

	@Test
	void getProcessed(){
		FileUtil.setProcessed("processed/");

		assertEquals("processed/", FileUtil.getProcessed());
	}

}
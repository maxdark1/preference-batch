package ca.homedepot.preference.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
		FileUtil.setPath("TEST_PATH");

	}
}
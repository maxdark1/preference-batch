package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileValidationTest
{

	@BeforeAll
	static void setup()
	{
		FileValidation.setFbSFMCBaseName("OPTIN_STANDARD_FLEX_GCFB_");
		FileValidation.setHybrisBaseName("OPTIN_STANDARD_FLEX_");
		FileValidation.setSfmcBaseName("ET.CAN.");
		FileValidation.setExtensionRegex(".txt.AXOSTD|.TXT.THD.txt.gpg|.pgp|.txt|.TXT");
	}

	@Test
	void lineCallbackHandler()
	{
		LineCallbackHandler lineCallbackHandler = FileValidation.lineCallbackHandler(new String[]
		{ "STUFF1", "STUFF2" }, "\\|", "UTF-8");

		ValidationException exception = assertThrows(ValidationException.class, () -> lineCallbackHandler.handleLine("a|a"));
		assertTrue(exception.getMessage().contains("Invalid"));
	}

	@Test
	void validateFileName()
	{
		String source = "FB_SFMC";
		String fileNameRightOne = "OPTIN_STANDARD_FLEX_GCFB_20221022.txt",
				fileNameWrongOne = "LALIN_STANDARD_FLEX_GCFB_25102022.TXT";

		boolean fileNameCorrect = FileValidation.validateFileName(fileNameRightOne, source),
				fileNameWrong = FileValidation.validateFileName(fileNameWrongOne, source);

		assertTrue(fileNameCorrect);
		assertTrue(!fileNameWrong);
	}

	@Test
	void validateSimpleFileDateFormat()
	{
		String format = "yyyyMMdd";
		String date = "2/2/2022";

		assertThat(FileValidation.validateSimpleFileDateFormat("20221020", format)).isTrue();
		assertThat(FileValidation.validateSimpleFileDateFormat(date, format)).isFalse();
		assertThat(FileValidation.validateSimpleFileDateFormat("19102022", format)).isFalse();
	}

	@Test
	void getFileExtension()
	{
		String baseName = "OPTIN_STANDARD_FLEX_YYYYMMDD";
		String fileName = "OPTIN_STANDARD_FLEX_YYYYMMDD.txt.AXOSTD";


		assertEquals(".txt.AXOSTD", FileValidation.getExtension(fileName, baseName));
	}

	@Test
	void getFileName()
	{
		String file = "ET.CAN.YYYYMMDD.TXT";
		assertEquals("ET.CAN.YYYYMMDD", FileValidation.getFileName(file));
	}

	@Test
	void getBaseName()
	{
		String fbSFMCbaseName = "OPTIN_STANDARD_FLEX_GCFB_", sfcm = "ET.CAN.", hybris = "OPTIN_STANDARD_FLEX_";

		assertEquals(sfcm, FileValidation.getBaseName("SFMC"));
		assertEquals(fbSFMCbaseName, FileValidation.getBaseName("FB_SFMC"));
		assertEquals(hybris, FileValidation.getBaseName("hybris"));
	}

	@Test
	void gettersBaseNamesFormat()
	{
		String fbSFMCbaseName = "OPTIN_STANDARD_FLEX_GCFB_", sfcm = "ET.CAN.", hybris = "OPTIN_STANDARD_FLEX_";

		assertEquals(sfcm, FileValidation.getSfmcBaseName());
		assertEquals(fbSFMCbaseName, FileValidation.getFbSFMCBaseName());
		assertEquals(hybris, FileValidation.getHybrisBaseName());
	}

	@Test
	void validateExtension()
	{
		String fileName = "ET.CAN.YYYYMMDD.TXT";
		String baseName = FileValidation.getFileName(fileName);
		String extension = FileValidation.getExtension(fileName, baseName);

		assertTrue(FileValidation.validateExtension(extension));
	}
}
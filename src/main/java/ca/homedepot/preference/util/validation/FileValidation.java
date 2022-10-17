package ca.homedepot.preference.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@Slf4j
public class FileValidation
{
	/**
	 * The extension regex
	 */
	private static String extensionRegex;
	/**
	 * The hybris base name
	 */
	private static String hybrisBaseName;
	/**
	 * The FB SFMC base name
	 */
	private static String fbSFMCBaseName;
	/**
	 * The SFMC base name
	 */
	private static String sfmcBaseName;

	/**
	 * Gets hybris base name
	 * 
	 * @return hybris base name
	 */
	public static String getHybrisBaseName()
	{
		return hybrisBaseName;
	}

	/**
	 * Sets hybris base name
	 * 
	 * @param hybrisBaseName
	 */
	public static void setHybrisBaseName(String hybrisBaseName)
	{
		FileValidation.hybrisBaseName = hybrisBaseName;
	}

	/**
	 * Gets FB SFMC base name
	 * 
	 * @return
	 */
	public static String getFbSFMCBaseName()
	{
		return fbSFMCBaseName;
	}

	/**
	 * Sets FB SFMC base name
	 * 
	 * @param fbSFMCBaseName
	 */
	public static void setFbSFMCBaseName(String fbSFMCBaseName)
	{
		FileValidation.fbSFMCBaseName = fbSFMCBaseName;
	}

	/**
	 * Gets SFMC base name
	 * 
	 * @return SFMC base name
	 */
	public static String getSfmcBaseName()
	{
		return sfmcBaseName;
	}

	/**
	 * Sets SFMC base name
	 * 
	 * @param sfmcBaseName
	 */
	public static void setSfmcBaseName(String sfmcBaseName)
	{
		FileValidation.sfmcBaseName = sfmcBaseName;
	}

	/**
	 * Sets extension regex
	 * 
	 * @param extensionRegex
	 */
	public static void setExtensionRegex(String extensionRegex)
	{
		FileValidation.extensionRegex = extensionRegex;
	}

	/**
	 * Returns the line call back Which validates the headers
	 * 
	 * @param headerFile
	 * @param separator
	 * @return line calll back handler
	 */
	public static LineCallbackHandler lineCallbackHandler(String[] headerFile, String separator)
	{
		return line -> {
			String[] header = line.split(separator);
			if (!Arrays.equals(header, headerFile))
				throw new ValidationException(" Invalid header {}: " + Arrays.toString(header));
		};
	}

	/**
	 * Validates file's name
	 * 
	 * @param fileName
	 * @param source
	 * @return is file valid
	 */
	public static boolean validateFileName(String fileName, String source)
	{
		String formatDate = "yyyyMMdd";
		String baseName = getFileName(fileName);
		int start = getBaseName(source).length(), end = start + formatDate.length();
		if (end != baseName.length() || !validateExtension(getExtension(fileName, baseName)))
		{
			return false;
		}
		return validateSimpleFileDateFormat(fileName.substring(start, end), formatDate);
	}

	/**
	 * Returns file's name without extension
	 * 
	 * @param fileName
	 * @return file's name
	 */
	public static String getFileName(String fileName)
	{
		return fileName.replaceAll(extensionRegex, "");
	}

	/**
	 * Gets extension without file's name
	 * 
	 * @param fileName
	 * @param baseName
	 * @return file extension
	 */
	public static String getExtension(String fileName, String baseName)
	{
		return fileName.replaceAll(baseName, "");
	}

	/**
	 * Validate file's extension
	 * 
	 * @param extension
	 * @return is it valid
	 */
	public static Boolean validateExtension(String extension)
	{
		return extension.matches(extensionRegex);
	}

	/**
	 * Gets base name
	 * 
	 * @param source
	 * @return base name
	 */
	public static String getBaseName(String source)
	{

		switch (source)
		{
			case "FB_SFMC":
				return fbSFMCBaseName;
			case "SFMC":
				return sfmcBaseName;
		}
		return hybrisBaseName;
	}

	/**
	 * Validate date format of filename
	 * 
	 * @param date
	 * @param formatDate
	 * @return says that if its valid
	 */
	public static boolean validateSimpleFileDateFormat(String date, String formatDate)
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
			simpleDateFormat.parse(date);
			/**
			 * Returns that the date is valid
			 */
			return true;
		}
		catch (Exception e)
		{
			/**
			 * Returns that the file name is not valid
			 */
			return false;
		}
	}
}

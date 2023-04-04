package ca.homedepot.preference.util.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.FB_SFMC;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.SFMC;

@Slf4j
@UtilityClass


public class FileValidation
{
	public final String formatDate = "yyyyMMdd";
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
	public static LineCallbackHandler lineCallbackHandler(String[] headerFile, String separator, String encoding)
	{
		return line -> {
			boolean invalid = false;
			String[] header = line.split(separator);
			String converted = null;
			for (int i = 0; i < header.length; i++)
			{
				if (encoding != "UTF-8")
				{
					try
					{
						byte[] toConvert = header[i].getBytes(encoding);
						converted = new String(toConvert, "UTF-8");
					}
					catch (UnsupportedEncodingException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					converted = header[i] + ".";
				}

				if (converted.contains(headerFile[i] + "."))
				{
					invalid = true;
				}
			}
			if (invalid)
				throw new ValidationException(" Invalid header {} : " + Arrays.toString(header));
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
		String baseName = getFileName(fileName);
		boolean isfileNameWrong = baseName.contains(getBaseName(source));
		//int start = getBaseName(source).length();
		//int end = start + formatDate.length();
		boolean isNotAValidExtension = !validateExtension(getExtension(fileName, baseName));
		if (isNotAValidExtension || !isfileNameWrong)
		{
			return false;
		}
		/**
		 * Keep the code commented to be uncommented again when we know the rigth format of the date return
		 * validateSimpleFileDateFormat(fileName.substring(start, end), formatDate);
		 */
		return true;
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
			case FB_SFMC:
				return fbSFMCBaseName;
			case SFMC:
				return sfmcBaseName;
			default:
				return hybrisBaseName;
		}

	}

	/**
	 * Validate date format of filename
	 * 
	 * @param strDate
	 * @param formatDate
	 * @return says that if its valid
	 */
	public static boolean validateSimpleFileDateFormat(String strDate, String formatDate)
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
			simpleDateFormat.parse(strDate);
			/**
			 * Returns that the date is valid
			 */
			return deepCheckDateFormat(formatDate, strDate);
		}
		catch (Exception e)
		{
			/**
			 * Returns that the file name is not valid
			 */
			return false;
		}
	}

	/**
	 * Ad hoc validation for string date wiht format yyyyMMdd 19-09-1222
	 *
	 * @param format
	 * @param strDate
	 * @return
	 */
	public static boolean deepCheckDateFormat(String format, String strDate)
	{
		Map<Integer, Integer> counter = new LinkedHashMap<>();

		format.chars().forEach(iChar -> {
			if (counter.containsKey(iChar))
			{
				counter.put(iChar, counter.get(iChar) + 1);
			}
			else
			{
				counter.put(iChar, 1);
			}
		});
		int index = 0;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int actualYear = calendar.get(Calendar.YEAR);
		for (Integer key : counter.keySet())
		{
			String token = strDate.substring(index, index + counter.get(key));
			int integerValue = Integer.parseInt(token);
			if (token.length() == 4 && !(integerValue >= 2000 && integerValue <= actualYear))
			{
				return false;
			}
			if (format.substring(index, index + counter.get(key)).equals("MM") && token.length() == 2
					&& !(integerValue >= 1 && integerValue <= 12))
			{
				return false;
			}
			if (token.length() == 2 && !(integerValue >= 1 && integerValue <= 31))
			{
				return false;
			}
			index += counter.get(key);
		}
		return true;
	}
}

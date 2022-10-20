package ca.homedepot.preference.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class FileValidation
{

	private static String extensionRegex;

	private static String hybrisBaseName;

	private static String fbSFMCBaseName;

	private static String sfmcBaseName;


	public static String getHybrisBaseName()
	{
		return hybrisBaseName;
	}

	public static void setHybrisBaseName(String hybrisBaseName)
	{
		FileValidation.hybrisBaseName = hybrisBaseName;
	}

	public static String getFbSFMCBaseName()
	{
		return fbSFMCBaseName;
	}

	public static void setFbSFMCBaseName(String fbSFMCBaseName)
	{
		FileValidation.fbSFMCBaseName = fbSFMCBaseName;
	}

	public static String getSfmcBaseName()
	{
		return sfmcBaseName;
	}

	public static void setSfmcBaseName(String sfmcBaseName)
	{
		FileValidation.sfmcBaseName = sfmcBaseName;
	}

	public static void setExtensionRegex(String extensionRegex)
	{
		FileValidation.extensionRegex = extensionRegex;
	}

	public static LineCallbackHandler lineCallbackHandler(String[] headerFile, String separator)
	{
		return line -> {
			String[] header = line.split(separator);
			if (!Arrays.equals(header, headerFile))
				throw new ValidationException(" Invalid header {}: " + Arrays.toString(header));
		};
	}

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

	public static String getFileName(String fileName)
	{
		return fileName.replaceAll(extensionRegex, "");
	}

	public static String getExtension(String fileName, String baseName)
	{
		return fileName.replaceAll(baseName, "");
	}

	public static Boolean validateExtension(String extension)
	{
		return extension.matches(extensionRegex);
	}

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

	public static boolean validateSimpleFileDateFormat(String strDate, String formatDate)
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
			simpleDateFormat.parse(strDate);
			return deepCheckDateFormat(formatDate, strDate);
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean deepCheckDateFormat(String format, String strDate)
	{
		assert format.equals("yyyyMMdd");
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
		for (Integer key : counter.keySet())
		{
			String token = strDate.substring(index, index + counter.get(key));
			if (token != null)
			{
				if (token.length() == 4 && Integer.valueOf(token) < 2000)
				{
					return false;
				}
				if (token.length() == 2 && Integer.valueOf(token) < 0 && Integer.valueOf(token) > 12)
				{
					return false;
				}
				if (token.length() == 2 && Integer.valueOf(token) < 0 && Integer.valueOf(token) > 31)
				{
					return false;
				}
			}
			index += counter.get(key);
		}
		return true;
	}
}

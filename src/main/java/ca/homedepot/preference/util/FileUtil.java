package ca.homedepot.preference.util;

import lombok.experimental.UtilityClass;


/**
 * The type File util.
 */
@UtilityClass
public final class FileUtil
{
	private static String registrationFile;
	private static String emailanalyticsFile;

	/**
	 * Gets registration file.
	 *
	 * @return the registration file
	 */
	public static String getRegistrationFile()
	{
		return registrationFile;
	}

	/**
	 * Sets registration file.
	 *
	 * @param registrationFile
	 *           the registration file
	 */
	public static void setRegistrationFile(String registrationFile)
	{
		FileUtil.registrationFile = registrationFile;
	}

	/**
	 * Gets emailanalytics file.
	 *
	 * @return the emailanalytics file
	 */
	public static String getEmailanalyticsFile()
	{
		return emailanalyticsFile;
	}

	/**
	 * Sets emailanalytics file.
	 *
	 * @param emailanalyticsFile
	 *           the emailanalytics file
	 */
	public static void setEmailanalyticsFile(String emailanalyticsFile)
	{
		FileUtil.emailanalyticsFile = emailanalyticsFile;
	}

}

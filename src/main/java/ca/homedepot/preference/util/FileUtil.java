package ca.homedepot.preference.util;

import lombok.experimental.UtilityClass;


/**
 * The type File util.
 */
@UtilityClass
public final class FileUtil
{
	private static String registrationFile;
	private static String fileExtTargetEmail;

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
	public static String getFileExtTargetEmail()
	{
		return fileExtTargetEmail;
	}

	/**
	 * Sets emailanalytics file.
	 *
	 * @param fileExtTargetEmail
	 *           the emailanalytics file
	 */
	public static void setFileExtTargetEmail(String fileExtTargetEmail)
	{
		FileUtil.fileExtTargetEmail = fileExtTargetEmail;
	}

}

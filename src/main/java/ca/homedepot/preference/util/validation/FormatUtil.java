package ca.homedepot.preference.util.validation;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass

public class FormatUtil
{

	public static Integer getIntegerValue(String value)
	{
		Integer intValue = null;

		if (value != null && !value.isEmpty() && !value.isBlank())
			return Integer.parseInt(value);

		return intValue;
	}

	/**
	 * Gets the String value from date
	 *
	 * @param date
	 *
	 * @return String
	 */
	public static String getDate(Date date)
	{
		if (date != null)
			return date.toString();

		return null;
	}

	/**
	 * Trim the String value
	 *
	 * @param column
	 *
	 * @return String
	 *
	 */

	public static String columnTrim(String column)
	{
		return column != null ? column.replaceAll("\\s+", " ").trim() : null;
	}


	/**
	 * Business name - org name processing
	 *
	 * Extracts just the first 30 charcters of the column
	 *
	 * @return String
	 */

	public static String businessNameJust30(String value)
	{
		return value != null && value.length() > 30 ? value.substring(0, 29) : value;
	}
}


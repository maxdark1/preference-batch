package ca.homedepot.preference.util.validation;

import lombok.Generated;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
@Generated

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


}

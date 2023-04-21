package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FormatUtilTest
{

	@Test
	void getIntegerValue()
	{

		String value = "1";
		String wrongValue = null;
		Integer expected = 1;

		Integer current = FormatUtil.getIntegerValue(value);
		Integer currentWrong = FormatUtil.getIntegerValue(wrongValue);

		assertEquals(expected, current);
		assertNull(currentWrong);
	}

	@Test
	void getDate()
	{
		Date date = new Date();
		String dateStr = date.toString();

		String currentDateStr = FormatUtil.getDate(date);
		String wrongCurrentDate = FormatUtil.getDate(null);

		assertEquals(dateStr, currentDateStr);
		assertNull(wrongCurrentDate);
	}

	@Test
	void columnTrim()
	{
		String column = "     toTrim     something";
		String result = FormatUtil.columnTrim(column);
		assertNotEquals(column, result);
		assertNull(FormatUtil.columnTrim(null));
	}

	@Test
	void businesOnlyFirst30Characters()
	{
		String orgName = "A big org name that I can imagine right now to be there in a column";
		String equalsTobe = "equals company";
		String orgNameShorter = FormatUtil.businessNameJust30(orgName);
		assertNotEquals(orgName, orgNameShorter);
		assertEquals(equalsTobe, FormatUtil.businessNameJust30(equalsTobe));
	}
}
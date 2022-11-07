package ca.homedepot.preference.util.validation;

import ca.homedepot.preference.config.RegistrationRowMapper;
import ca.homedepot.preference.config.SFMCRowMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FormatUtilTest {

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
}
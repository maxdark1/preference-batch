package ca.homedepot.preference.config;

import ca.homedepot.preference.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SFMCRowMapperTest
{
	@Mock
	ResultSet resultSet;

	SFMCRowMapper sfmcRowMapper;

	@BeforeEach
	void setup()
	{
		resultSet = Mockito.mock(ResultSet.class);
		sfmcRowMapper = new SFMCRowMapper();
	}

	@Test
	void mapRow() throws SQLException
	{
		int rowNum = 1;
		BigDecimal fileId = BigDecimal.ONE;
		String status = "IP", sequenceNbr = "12345";
		Long sourceId = 123L;
		String emailAddress = "email@address.com", emailStatus = "1", email_address_pref = "1";
		java.sql.Date srcDate = new java.sql.Date(2022, 9, 27);

		Mockito.when(resultSet.getBigDecimal("file_id")).thenReturn(fileId);
		Mockito.when(resultSet.getString("status")).thenReturn(status);
		Mockito.when(resultSet.getString("sequence_nbr")).thenReturn(sequenceNbr);
		Mockito.when(resultSet.getLong("source_id")).thenReturn(sourceId);
		Mockito.when(resultSet.getString("src_email_address")).thenReturn(emailAddress);
		Mockito.when(resultSet.getString("email_status")).thenReturn(emailStatus);
		Mockito.when(resultSet.getString("email_address_pref")).thenReturn(email_address_pref);
		Mockito.when(resultSet.getDate("src_date")).thenReturn(srcDate);

		RegistrationRequest result = sfmcRowMapper.mapRow(resultSet, rowNum);
		assertNotNull(result);

	}

	@Test
	void getDate()
	{
		Date date = new Date();
		String dateStr = date.toString();

		String currentDateStr = SFMCRowMapper.getDate(date);
		String wrongCurrentDate = SFMCRowMapper.getDate(null);

		assertEquals(dateStr, currentDateStr);
		assertNull(wrongCurrentDate);
	}
}
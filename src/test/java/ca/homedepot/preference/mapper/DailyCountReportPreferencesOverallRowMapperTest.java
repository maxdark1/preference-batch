package ca.homedepot.preference.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DailyCountReportPreferencesOverallRowMapperTest
{

	DailyCountReportPreferencesOverallRowMapper dailyCountReportPreferencesOverallRowMapper;

	@Mock
	ResultSet rs;

	int i;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		dailyCountReportPreferencesOverallRowMapper = new DailyCountReportPreferencesOverallRowMapper();
	}

	@Test
	void mapRow() throws SQLException
	{
		dailyCountReportPreferencesOverallRowMapper.mapRow(rs, i);
		assertNotNull(dailyCountReportPreferencesOverallRowMapper);
	}
}
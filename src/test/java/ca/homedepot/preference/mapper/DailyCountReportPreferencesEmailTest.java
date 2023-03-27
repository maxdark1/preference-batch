package ca.homedepot.preference.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DailyCountReportPreferencesEmailTest
{

	DailyCountReportPreferencesEmail dailyCountReportPreferencesEmail;

	@Mock
	ResultSet rs;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void mapRow() throws SQLException
	{
		int n = 0;
		dailyCountReportPreferencesEmail = new DailyCountReportPreferencesEmail();
		dailyCountReportPreferencesEmail.mapRow(rs, n);
		assertNotNull(dailyCountReportPreferencesEmail);
	}
}
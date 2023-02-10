package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.DailyCountReportDTOStep1;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyCountReportPreferencesEmail implements RowMapper<DailyCountReportDTOStep1>
{
	@Override
	public DailyCountReportDTOStep1 mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		DailyCountReportDTOStep1 dailyCountReportDTOStep1 = new DailyCountReportDTOStep1();

		dailyCountReportDTOStep1.setField1(rs.getString("1"));
		dailyCountReportDTOStep1.setField2(rs.getString("2"));
		dailyCountReportDTOStep1.setField3(rs.getString("3"));
		dailyCountReportDTOStep1.setField4(rs.getString("4"));
		dailyCountReportDTOStep1.setField5(rs.getString("5"));
		dailyCountReportDTOStep1.setField6(rs.getString("6"));
		dailyCountReportDTOStep1.setField7(rs.getString("7"));
		dailyCountReportDTOStep1.setField8(rs.getString("8"));
		dailyCountReportDTOStep1.setField9(rs.getString("9"));

		return dailyCountReportDTOStep1;
	}
}

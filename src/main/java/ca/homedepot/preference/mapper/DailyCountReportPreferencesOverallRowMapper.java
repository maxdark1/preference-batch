package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.DailyCountReportStep2;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyCountReportPreferencesOverallRowMapper implements RowMapper<DailyCountReportStep2>
{

	@Override
	public DailyCountReportStep2 mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		DailyCountReportStep2 dailyCountReportStep2 = new DailyCountReportStep2();

		dailyCountReportStep2.setCountDate(rs.getString("count_date"));

		dailyCountReportStep2.setThdY(rs.getString("THD_Y"));
		dailyCountReportStep2.setDiffThdY(rs.getString("DIFF_THD_Y"));
		dailyCountReportStep2.setThdN(rs.getString("THD_N"));
		dailyCountReportStep2.setDiffThdN(rs.getString("DIFF_THD_N"));
		dailyCountReportStep2.setThdU(rs.getString("THD_U"));
		dailyCountReportStep2.setDiffthdU(rs.getString("DIFF_THD_U"));

		dailyCountReportStep2.setGcY(rs.getString("GC_Y"));
		dailyCountReportStep2.setDiffGCY(rs.getString("DIFF_GC_Y"));
		dailyCountReportStep2.setGcN(rs.getString("GC_N"));
		dailyCountReportStep2.setDiffGCN(rs.getString("DIFF_GC_N"));
		dailyCountReportStep2.setGcU(rs.getString("GC_U"));
		dailyCountReportStep2.setDiffGCU(rs.getString("DIFF_GC_U"));

		dailyCountReportStep2.setNmY(rs.getString("NM_Y"));
		dailyCountReportStep2.setDiffNMY(rs.getString("DIFF_NM_Y"));
		dailyCountReportStep2.setNmN(rs.getString("NM_N"));
		dailyCountReportStep2.setDiffNMN(rs.getString("DIFF_NM_N"));
		dailyCountReportStep2.setNmU(rs.getString("NM_U"));
		dailyCountReportStep2.setDiffNMU(rs.getString("DIFF_NM_U"));

		dailyCountReportStep2.setProY(rs.getString("PRO_Y"));
		dailyCountReportStep2.setDiffPROY(rs.getString("DIFF_PRO_Y"));
		dailyCountReportStep2.setProN(rs.getString("PRO_N"));
		dailyCountReportStep2.setDiffPRON(rs.getString("DIFF_PRO_N"));
		dailyCountReportStep2.setProU(rs.getString("PRO_U"));
		dailyCountReportStep2.setDiffPROU(rs.getString("DIFF_PRO_U"));

		dailyCountReportStep2.setCaY(rs.getString("CA_Y"));
		dailyCountReportStep2.setDiffCAY(rs.getString("DIFF_CA_Y"));
		dailyCountReportStep2.setCaN(rs.getString("CA_N"));
		dailyCountReportStep2.setDiffCAN(rs.getString("DIFF_CA_N"));
		dailyCountReportStep2.setCaU(rs.getString("CA_U"));
		dailyCountReportStep2.setDiffCAU(rs.getString("DIFF_CA_U"));
		return dailyCountReportStep2;
	}
}

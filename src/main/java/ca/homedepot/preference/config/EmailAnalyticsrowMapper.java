package ca.homedepot.preference.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.model.EmailAnalytics;

public class EmailAnalyticsrowMapper implements RowMapper<EmailAnalytics>
{

	@Override
	public EmailAnalytics mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		EmailAnalytics emailAnalytics = new EmailAnalytics();
		emailAnalytics.setCreatedts(rs.getTimestamp(PreferenceBatchConstants.CREATEDTS));
		emailAnalytics.setArticleId(rs.getString(PreferenceBatchConstants.ARTICLE_ID_MAPPER));
		emailAnalytics.setInventory(rs.getInt(PreferenceBatchConstants.INVENTORY));
		emailAnalytics.setEmailId(rs.getString(PreferenceBatchConstants.EMAIL_ID_MAPPER));
		emailAnalytics.setEmailType(rs.getString(PreferenceBatchConstants.EMAIL_TYPE_MAPPER));
		return emailAnalytics;
	}

}

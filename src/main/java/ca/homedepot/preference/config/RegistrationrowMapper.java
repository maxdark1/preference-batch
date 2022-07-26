package ca.homedepot.preference.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.model.Registration;

public class RegistrationrowMapper implements RowMapper<Registration>
{

	@Override
	public Registration mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Registration registration = new Registration();
		registration.setCreatedts(rs.getTimestamp(PreferenceBatchConstants.CREATEDTS));
		registration.setArticleId(rs.getString(PreferenceBatchConstants.ARTICLE_ID_MAPPER));
		registration.setActionType(rs.getString(PreferenceBatchConstants.ACTION_TYPE_MAPPER));
		registration.setEmailId(rs.getString(PreferenceBatchConstants.EMAIL_ID_MAPPER));
		return registration;
	}

}

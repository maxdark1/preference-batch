package ca.homedepot.preference.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ca.homedepot.preference.model.InboundRegistration;

public class RegistrationrowMapper implements RowMapper<InboundRegistration>
{

	@Override
	public InboundRegistration mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		InboundRegistration inboundRegistration = new InboundRegistration();
		//		registration.setCreatedts(rs.getTimestamp(PreferenceBatchConstants.CREATEDTS));
		//		registration.setArticleId(rs.getString(PreferenceBatchConstants.ARTICLE_ID_MAPPER));
		//		registration.setActionType(rs.getString(PreferenceBatchConstants.ACTION_TYPE_MAPPER));
		//		registration.setEmailId(rs.getString(PreferenceBatchConstants.EMAIL_ID_MAPPER));
		return inboundRegistration;
	}

}

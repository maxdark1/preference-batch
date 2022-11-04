package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SalesforcePreparedStatement implements ItemPreparedStatementSetter<SalesforceExtractOutboundDTO>
{
	@Override
	public void setValues(SalesforceExtractOutboundDTO item, PreparedStatement ps) throws SQLException
	{
		ps.setString(1, item.getEmailAddress());
		ps.setTimestamp(2, Timestamp.valueOf(item.getAsOfDate()));
		ps.setString(3, item.getSourceId());
		ps.setString(4, item.getEmailStatus());
		ps.setString(5, item.getEmailPtc());
		ps.setString(6, item.getLanguagePreference());
		ps.setTimestamp(7, Timestamp.valueOf(item.getEarliestOptInDate()));
		ps.setString(8, item.getHdCanadaEmailCompliantFlag());
		ps.setString(9, item.getHdCanadaFlag());
		ps.setString(10, item.getGardenClubFlag());
		ps.setString(11, item.getNewMoverFlag());
		ps.setString(12, item.getProFlag());
		ps.setString(13, item.getPhonePtcFlag());
		ps.setString(14, item.getFirstName());
		ps.setString(15, item.getLastName());
		ps.setString(16, item.getPostalCode());
		ps.setString(17, item.getProvince());
		ps.setString(18, item.getCity());
		ps.setString(19, item.getPhoneNumber());
		ps.setString(20, item.getBusinessName());
		ps.setString(21, item.getBusinessType());
		ps.setTimestamp(22, Timestamp.valueOf(item.getMoveDate()));
		ps.setString(23, item.getDwellingType());
	}
}

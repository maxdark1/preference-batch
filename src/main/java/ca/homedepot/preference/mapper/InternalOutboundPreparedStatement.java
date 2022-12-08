package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalOutboundDto;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InternalOutboundPreparedStatement implements ItemPreparedStatementSetter<InternalOutboundDto>
{
	@Override
	public void setValues(InternalOutboundDto item, PreparedStatement ps) throws SQLException
	{

		ps.setString(1, item.getEmailAddr());
		ps.setTimestamp(2, new Timestamp(item.getCanPtcEffectiveDate().getTime()));
		ps.setString(3, item.getCanPtcSourceId().toPlainString());
		String emailStatus = item.getEmailStatus().toPlainString();
		if (emailStatus.equals("0"))
			emailStatus = "00";
		ps.setString(4, emailStatus);
		ps.setString(5, item.getCanPtcGlag());
		ps.setString(6, item.getFirstName());
		ps.setString(7, item.getLastName());
		ps.setString(8, item.getLanguagePreference());
		ps.setTimestamp(9, new Timestamp(item.getEarlyOptInIDate().getTime()));
		ps.setString(10, item.getCndCompliantFlag());
		ps.setString(11, item.getHdCaFlag());
		ps.setString(12, item.getHdCaGardenClubFlag());
		ps.setString(13, item.getHdCaProFlag());
		ps.setString(14, item.getPostalCode());
		ps.setString(15, item.getCity());
		ps.setString(16, item.getCustomerNbr());
		ps.setString(17, item.getProvince());
	}
}

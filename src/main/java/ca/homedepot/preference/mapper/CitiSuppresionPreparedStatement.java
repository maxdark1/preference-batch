package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CitiSuppresionPreparedStatement implements ItemPreparedStatementSetter<CitiSuppresionOutboundDTO>
{
	@Override
	public void setValues(CitiSuppresionOutboundDTO item, PreparedStatement ps) throws SQLException
	{
		System.out.println(item);
		ps.setString(1, item.getFirstName());
		ps.setString(2, item.getMiddleInitial());
		ps.setString(3, item.getLastName());
		ps.setString(4, item.getAddrLine1());
		ps.setString(5, item.getAddrLine2());
		ps.setString(6, item.getCity());
		ps.setString(7, item.getStateCd());
		ps.setString(8, item.getPostalCd());
		ps.setString(9, item.getEmailAddr());
		ps.setString(10, item.getPhone());
		ps.setString(11, item.getSmsMobilePhone());
		ps.setString(12, item.getBusinessName());
		ps.setString(13, item.getDmOptOut());
		ps.setString(14, item.getEmailOptOut());
		ps.setString(15, item.getPhoneOptOut());
		ps.setString(16, item.getSmsOptOut());
	}
}

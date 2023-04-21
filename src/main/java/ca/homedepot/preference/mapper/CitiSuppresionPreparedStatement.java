package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ca.homedepot.preference.util.validation.FormatUtil.businessNameJust30;
import static ca.homedepot.preference.util.validation.FormatUtil.columnTrim;

public class CitiSuppresionPreparedStatement implements ItemPreparedStatementSetter<CitiSuppresionOutboundDTO>
{
	@Override
	public void setValues(CitiSuppresionOutboundDTO item, PreparedStatement ps) throws SQLException
	{
		ps.setString(1, columnTrim(item.getFirstName()));
		ps.setString(2, columnTrim(item.getMiddleInitial()));
		ps.setString(3, columnTrim(item.getLastName()));
		ps.setString(4, columnTrim(item.getAddrLine1()));
		ps.setString(5, columnTrim(item.getAddrLine2()));
		ps.setString(6, columnTrim(item.getCity()));
		ps.setString(7, columnTrim(item.getStateCd()));
		ps.setString(8, columnTrim(item.getPostalCd()));
		ps.setString(9, columnTrim(item.getEmailAddr()));
		ps.setString(10, columnTrim(item.getPhone()));
		ps.setString(11, columnTrim(item.getSmsMobilePhone()));
		ps.setString(12, businessNameJust30(columnTrim(item.getBusinessName())));
		ps.setString(13, columnTrim(item.getDmOptOut()));
		ps.setString(14, columnTrim(item.getEmailOptOut()));
		ps.setString(15, columnTrim(item.getPhoneOptOut()));
		ps.setString(16, columnTrim(item.getSmsOptOut()));

	}
}

package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InternalFlexPreparedStatement implements ItemPreparedStatementSetter<InternalFlexOutboundDTO>
{
	@Override
	public void setValues(InternalFlexOutboundDTO item, PreparedStatement ps) throws SQLException
	{
		ps.setBigDecimal(1, item.getFileId());
		ps.setString(2, item.getSequenceNbr());
		ps.setString(3, item.getEmailAddr());
		ps.setBigDecimal(4, item.getHdHhId());
		ps.setBigDecimal(5, item.getHdIndId());
		ps.setString(6, item.getCustomerNbr());
		ps.setString(7, item.getStoreNbr());
		ps.setString(8, item.getOrgName());
		ps.setString(9, item.getCompanyCd());
		ps.setString(10, item.getCustTypeCd());
		ps.setLong(11, item.getSourceId());
		Timestamp effectiveDate = item.getEffectiveDate() == null ? null : Timestamp.valueOf(item.getEffectiveDate());
		ps.setTimestamp(12, effectiveDate);
		Date lastUpdatedDate = item.getLastUpdateDate() == null ? null : new Date(item.getLastUpdateDate().getTime());
		ps.setDate(13, lastUpdatedDate);
		ps.setString(14, item.getIndustryCode());
		ps.setString(15, item.getCompanyName());
		ps.setString(16, item.getContactFirstName());
		ps.setString(17, item.getContactLastName());
		ps.setString(18, item.getContactRole());
	}
}

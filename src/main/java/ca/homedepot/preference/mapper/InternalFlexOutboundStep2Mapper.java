package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InternalFlexOutboundStep2Mapper implements RowMapper<InternalFlexOutboundDTO>
{
	@Override
	public InternalFlexOutboundDTO mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		return InternalFlexOutboundDTO.builder().fileId(rs.getBigDecimal("file_id")).sequenceNbr(rs.getString("sequence_nbr"))
				.emailAddr(rs.getString("email_addr")).hdHhId(rs.getBigDecimal("hd_hh_id")).hdIndId(rs.getBigDecimal("hd_ind_id"))
				.customerNbr(rs.getString("customer_nbr")).storeNbr(rs.getString("store_nbr")).orgName(rs.getString("org_name"))
				.companyCd(rs.getString("company_cd")).custTypeCd(rs.getString("cust_type_cd")).sourceId(rs.getLong("source_id"))
				.effectiveDate(rs.getDate("effective_date")).lastUpdateDate(rs.getDate("last_update_date"))
				.industryCode(rs.getString("industry_code")).companyName(rs.getString("company_name"))
				.contactFirstName(rs.getString("contact_first_name")).contactLastName(rs.getString("contact_last_name"))
				.contactRole(rs.getString("contact_role")).build();
	}
}

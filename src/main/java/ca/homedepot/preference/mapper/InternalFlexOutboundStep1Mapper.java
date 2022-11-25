package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InternalFlexOutboundStep1Mapper implements RowMapper<InternalFlexOutboundDTO>
{

	@Override
	public InternalFlexOutboundDTO mapRow(ResultSet rs, int rowNum) throws SQLException
	{

		return InternalFlexOutboundDTO.builder().fileId(rs.getBigDecimal("FILE_ID")).sequenceNbr(rs.getString("SEQUENCE_NBR"))
				.emailAddr(rs.getString("EMAIL_ADDR")).hdHhId(rs.getBigDecimal("HD_HH_ID")).hdIndId(rs.getBigDecimal("HD_IND_ID"))
				.customerNbr(rs.getString("CUSTOMER_NBR")).storeNbr(rs.getString("STORE_NBR")).orgName(rs.getString("ORG_NAME"))
				.companyCd(rs.getString("COMPANY_CD")).custTypeCd(rs.getString("CUST_TYPE_CD")).sourceId(rs.getLong("SOURCE_ID"))
				.effectiveDate(rs.getDate("EFFECTIVE_DATE")).lastUpdateDate(rs.getDate("LAST_UPDATE_DATE"))
				.industryCode(rs.getString("INDUSTRY_CODE")).companyName(rs.getString("COMPANY_NAME"))
				.contactFirstName(rs.getString("CONTACT_FIRST_NAME")).contactLastName(rs.getString("CONTACT_LAST_NAME"))
				.contactRole(rs.getString("CONTACT_ROLE")).build();
	}

}

package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CitiSuppresionOutboundMapper implements RowMapper<CitiSuppresionOutboundDTO>
{
	@Override
	public CitiSuppresionOutboundDTO mapRow(ResultSet rs, int rowNum) throws SQLException
	{

		CitiSuppresionOutboundDTO suppression = new CitiSuppresionOutboundDTO();

		suppression.setFirstName(rs.getString("first_name"));
		suppression.setMiddleInitial(rs.getString("middle_initial"));
		suppression.setLastName(rs.getString("last_name"));
		suppression.setAddrLine1(rs.getString("addr_line_1"));
		suppression.setAddrLine2(rs.getString("addr_line_2"));
		suppression.setCity(rs.getString("city"));
		suppression.setStateCd(rs.getString("state_cd"));
		suppression.setPostalCd(rs.getString("postal_cd"));
		suppression.setEmailAddr(rs.getString("email_addr"));
		suppression.setPhone(rs.getString("phone"));
		suppression.setSmsMobilePhone(rs.getString("sms_mobile_phone"));
		suppression.setBusinessName(rs.getString("business_name"));
		suppression.setDmOptOut(rs.getString("dm_opt_out"));
		suppression.setEmailOptOut(rs.getString("email_opt_out"));
		suppression.setPhoneOptOut(rs.getString("phone_opt_out"));
		suppression.setSmsOptOut(rs.getString("sms_opt_out"));
		return suppression;
	}
}

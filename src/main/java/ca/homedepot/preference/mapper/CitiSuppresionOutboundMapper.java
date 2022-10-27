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

		CitiSuppresionOutboundDTO suppresion = new CitiSuppresionOutboundDTO();

		suppresion.setFirstName(rs.getString("first_name"));
		suppresion.setMiddleInitial(rs.getString("middle_initial"));
		suppresion.setLastName(rs.getString("last_name"));
		suppresion.setAddrLine1(rs.getString("addr_line_1"));
		suppresion.setAddrLine2(rs.getString("addr_line_2"));
		suppresion.setCity(rs.getString("city"));
		suppresion.setStateCd(rs.getString("state_cd"));
		suppresion.setEmailAddr(rs.getString("email_addr"));
		suppresion.setPhone(rs.getString("phone"));
		suppresion.setSmsMobilePhone(rs.getString("sms_mobile_phone"));
		suppresion.setBusinessName(rs.getString("business_name"));
		suppresion.setDmOptOut(rs.getString("dm_opt_out"));
		suppresion.setEmailOptOut(rs.getString("email_opt_out"));
		suppresion.setPhoneOptOut(rs.getString("phone_opt_out"));
		suppresion.setSmsOptOut(rs.getString("sms_opt_out"));
		return suppresion;
	}
}

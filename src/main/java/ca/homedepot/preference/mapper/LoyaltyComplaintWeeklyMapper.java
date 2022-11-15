package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.LoyaltyCompliantDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoyaltyComplaintWeeklyMapper implements RowMapper<LoyaltyCompliantDTO>
{
	@Override
	public LoyaltyCompliantDTO mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		LoyaltyCompliantDTO internalOutboundDto = new LoyaltyCompliantDTO();
		internalOutboundDto.setEmailAddr(rs.getString("email_addr"));
		internalOutboundDto.setCanPtcEffectiveDate(rs.getTimestamp("can_ptc_effective_date"));
		internalOutboundDto.setCanPtcSourceId(rs.getBigDecimal("can_ptc_source_id"));
		internalOutboundDto.setEmailStatus(rs.getString("email_status"));
		internalOutboundDto.setCanPtcFlag(rs.getString("can_ptc_flag"));
		internalOutboundDto.setFirstName(rs.getString("first_name"));
		internalOutboundDto.setLastName(rs.getString("last_name"));
		internalOutboundDto.setLanguagePreference(rs.getString("language_preference"));
		internalOutboundDto.setEarlyOptInDate(rs.getTimestamp("early_opt_in_date"));
		internalOutboundDto.setCndCompliantFlag(rs.getString("cnd_compliant_flag"));
		internalOutboundDto.setHdCaFlag(rs.getString("hd_ca_flag"));
		internalOutboundDto.setHdCaGardenClubFlag(rs.getString("hd_ca_garden_club_flag"));
		internalOutboundDto.setHdCaProFlag(rs.getString("hd_ca_pro_flag"));
		internalOutboundDto.setPostalCd(rs.getString("postal_cd"));
		internalOutboundDto.setCity(rs.getString("city"));
		internalOutboundDto.setCustomerNbr(rs.getString("customer_nbr"));
		internalOutboundDto.setProvince(rs.getString("province"));

		return internalOutboundDto;
	}
}

package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalOutboundDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class InternalOutboundStep2Mapper implements RowMapper<InternalOutboundDto>
{
	@Override
	public InternalOutboundDto mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		InternalOutboundDto internalOutboundDto = new InternalOutboundDto();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		internalOutboundDto.setEmailAddr(rs.getString("email_addr"));
		internalOutboundDto.setCanPtcEffectiveDate(rs.getDate("can_ptc_effective_date"));
		internalOutboundDto.setCanPtcSourceId(rs.getBigDecimal("can_ptc_source_id"));
		internalOutboundDto.setEmailStatus(rs.getBigDecimal("email_status"));
		internalOutboundDto.setCanPtcGlag(rs.getString("can_ptc_flag"));
		internalOutboundDto.setLanguagePreference(rs.getString("language_preference"));
		internalOutboundDto.setEarlyOptInIDate(rs.getDate("early_opt_in_date"));
		internalOutboundDto.setCndCompliantFlag(rs.getString("cnd_compliant_flag"));
		internalOutboundDto.setHdCaFlag(rs.getString("hd_ca_flag"));
		internalOutboundDto.setHdCaGardenClubFlag(rs.getString("hd_ca_garden_club_flag"));
		internalOutboundDto.setHdCaNewMoverFlag(rs.getString("hd_ca_new_mover_flag"));
		internalOutboundDto.setHdCaNewMoverEffDate(rs.getDate("hd_ca_new_mover_eff_date"));
		internalOutboundDto.setHdCaProFlag(rs.getString("hd_ca_pro_flag"));
		internalOutboundDto.setPhonePtcFlag(rs.getString("phone_ptc_flag"));
		internalOutboundDto.setFirstName(rs.getString("first_name"));
		internalOutboundDto.setLastName(rs.getString("last_name"));
		internalOutboundDto.setPostalCode(rs.getString("postal_cd"));
		internalOutboundDto.setProvince(rs.getString("province"));
		internalOutboundDto.setCity(rs.getString("city"));
		internalOutboundDto.setPhoneNumber(rs.getString("phone_number"));
		internalOutboundDto.setBussinessName(rs.getString("business_name"));
		internalOutboundDto.setIndustryCode(rs.getString("industry_code"));
		internalOutboundDto.setMoveDate(rs.getDate("move_date") != null ? formatter.format(rs.getDate("move_date")) : null);
		internalOutboundDto.setDwellingType(rs.getString("dwelling_type"));

		return internalOutboundDto;
	}
}

package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalOutboundDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

public class InternalOutboundStep1Mapper implements RowMapper<InternalOutboundDto>
{
	@Override
	public InternalOutboundDto mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		InternalOutboundDto internalOutboundDto = new InternalOutboundDto();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		internalOutboundDto.setEmailAddr(rs.getString("email"));
		internalOutboundDto.setCanPtcEffectiveDate(rs.getDate("effective_date"));
		internalOutboundDto.setCanPtcSourceId(rs.getBigDecimal("source_id"));
		internalOutboundDto.setEmailStatus(rs.getBigDecimal("email_status"));
		internalOutboundDto.setCanPtcGlag(rs.getString("email_permission"));
		internalOutboundDto.setLanguagePreference(rs.getString("language_preference"));
		internalOutboundDto.setEarlyOptInIDate(rs.getDate("effective_date"));
		internalOutboundDto.setCndCompliantFlag(rs.getString("cnd_compliant_flag"));
		internalOutboundDto.setHdCaFlag(rs.getString("email_pref_hd_ca"));
		internalOutboundDto.setHdCaGardenClubFlag(rs.getString("email_pref_garden_club"));
		internalOutboundDto.setHdCaNewMoverFlag(rs.getString("email_pref_new_mover"));
		internalOutboundDto.setHdCaNewMoverEffDate(rs.getDate("effective_date"));
		internalOutboundDto.setHdCaProFlag(rs.getString("email_pref_pro"));
		internalOutboundDto.setPhonePtcFlag(rs.getString("phone_ptc_flag"));
		internalOutboundDto.setFirstName(rs.getString("first_name"));
		internalOutboundDto.setLastName(rs.getString("last_name"));
		internalOutboundDto.setPostalCode(rs.getString("src_postal_code"));
		internalOutboundDto.setProvince(rs.getString("province"));
		internalOutboundDto.setCity(rs.getString("city"));
		internalOutboundDto.setPhoneNumber(rs.getString("phone_number"));
		internalOutboundDto.setBussinessName(rs.getString("business_name"));
		internalOutboundDto.setIndustryCode(rs.getString("industry_code"));
		//TODO: Check how to manage move_date regarding with the format & field type
		internalOutboundDto.setMoveDate(formatter.format(rs.getDate("move_date")));
		internalOutboundDto.setDwellingType(rs.getString("dwelling_type"));

		return internalOutboundDto;
	}
}

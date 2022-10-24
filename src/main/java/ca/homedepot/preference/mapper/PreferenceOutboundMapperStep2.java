package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class PreferenceOutboundMapperStep2 implements RowMapper<PreferenceOutboundDto>
{
	@Override
	public PreferenceOutboundDto mapRow(ResultSet rs, int i) throws SQLException
	{
		PreferenceOutboundDto outboundData = new PreferenceOutboundDto();
		outboundData.setEmail(rs.getString("email_addr") /*!= null ? rs.getString("email") : ""*/);
		outboundData.setEffective_date(rs.getDate("can_ptc_effective_date") /*!= null ? rs.getDate("effective_date") : new Date(0)*/);
		outboundData.setSource_id(rs.getBigDecimal("can_ptc_source_id") /*!= null ? rs.getBigDecimal("source_id") : BigDecimal.ZERO */);
		outboundData.setEmail_status(rs.getBigDecimal("email_status") /*!= null ? rs.getBigDecimal("email_status") : BigDecimal.ZERO*/);
		outboundData.setEmail_permission(rs.getString("can_ptc_flag").charAt(0) /*!= null ? rs.getString("email_permission").charAt(0) : 'N'*/);
		outboundData.setLanguage_pref(rs.getString("language_preference") /*!= null ? rs.getString("language_preference") : ""*/);
		outboundData.setEarly_opt_in_date(rs.getDate("early_opt_in_date") /*!= null ? rs.getDate("early_opt_in_date") : new Date(0)*/);
		outboundData.setCnd_compliant_flag(rs.getString("cnd_compliant_flag").charAt(0) /*!= null ? rs.getString("cnd_compliant_flag").charAt(0) : 'N'*/);
		outboundData.setEmail_pref_hd_ca(rs.getString("hd_ca_flag").charAt(0) /*!= null ? rs.getString("email_pref_hd_ca").charAt(0) : 'N'*/);
		outboundData.setEmail_pref_garden_club(rs.getString("hd_ca_garden_club_flag").charAt(0) /*!= null ? rs.getString("email_pref_garden_club").charAt(0) : 'N'*/);
		outboundData.setEmail_pref_pro(rs.getString("hd_ca_pro_flag").charAt(0) /*!= null ? rs.getString("email_pref_pro").charAt(0) : 'N'*/);
		outboundData.setPostal_code(rs.getString("postal_cd") /*!= null ? rs.getString("src_postal_code") : ""*/);
		outboundData.setCustomer_nbr(rs.getString("customer_nbr") /*!= null ? rs.getString("customer_nbr") : ""*/);
		outboundData.setPhone_ptc_flag(rs.getString("phone_ptc_flag").charAt(0) /*!= null ? rs.getString("phone_ptc_flag").charAt(0) : 'N'*/);
		outboundData.setDncl_suppresion(rs.getString("dncl_suppression_flag").charAt(0) /*!= null ? rs.getString("dncl_suppresion").charAt(0) : 'N'*/);
		outboundData.setPhone_number(rs.getString("phone_number") /*!= null ? rs.getString("phone_number") : ""*/);
		outboundData.setFirst_name(rs.getString("first_name") /*!= null ? rs.getString("first_name") : ""*/);
		outboundData.setLast_name(rs.getString("last_name") /*!= null ? rs.getString("last_name") : ""*/);
		outboundData.setBusiness_name(rs.getString("business_name") /*!= null ? rs.getString("business_name") : ""*/);
		outboundData.setIndustry_code(rs.getString("industry_code") /*!= null ? rs.getString("industry_code") : ""*/);
		outboundData.setCity(rs.getString("city") /*!= null ? rs.getString("city") : ""*/);
		outboundData.setProvince(rs.getString("province") /*!= null ? rs.getString("province") : ""*/);
		outboundData.setHd_ca_pro_src_id(rs.getBigDecimal("hd_ca_pro_src_id") /*!= null ? rs.getBigDecimal("hd_ca_pro_src_id") : BigDecimal.ZERO*/);

		return outboundData;
	}
}

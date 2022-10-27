package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class PreferenceOutboundMapper implements RowMapper<PreferenceOutboundDto>
{
	/**
	 * This mapped is used to get the data for Inbound Tables
	 * @param rs the ResultSet to map (pre-initialized for the current row)
	 * @param i the number of the current row
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PreferenceOutboundDto mapRow(ResultSet rs, int i) throws SQLException
	{
		PreferenceOutboundDto outboundData = new PreferenceOutboundDto();
		outboundData.setEmail(rs.getString("email") /*!= null ? rs.getString("email") : ""*/);
		outboundData.setEffectiveDate(rs.getDate("effective_date") /*!= null ? rs.getDate("effective_date") : new Date(0)*/);
		outboundData.setSourceId(rs.getBigDecimal("source_id") /*!= null ? rs.getBigDecimal("source_id") : BigDecimal.ZERO */);
		outboundData.setEmailStatus(rs.getBigDecimal("email_status") /*!= null ? rs.getBigDecimal("email_status") : BigDecimal.ZERO*/);
		outboundData.setEmailPermission(rs.getString("email_permission").charAt(0) /*!= null ? rs.getString("email_permission").charAt(0) : 'N'*/);
		outboundData.setLanguagePref(rs.getString("language_preference") /*!= null ? rs.getString("language_preference") : ""*/);
		outboundData.setEarlyOptInDate(rs.getDate("early_opt_in_date") /*!= null ? rs.getDate("early_opt_in_date") : new Date(0)*/);
		outboundData.setCndCompliantFlag(rs.getString("cnd_compliant_flag").charAt(0) /*!= null ? rs.getString("cnd_compliant_flag").charAt(0) : 'N'*/);
		outboundData.setEmailPrefHdCa(rs.getString("email_pref_hd_ca").charAt(0) /*!= null ? rs.getString("email_pref_hd_ca").charAt(0) : 'N'*/);
		outboundData.setEmailPrefGardenClub(rs.getString("email_pref_garden_club").charAt(0) /*!= null ? rs.getString("email_pref_garden_club").charAt(0) : 'N'*/);
		outboundData.setEmailPrefPro(rs.getString("email_pref_pro").charAt(0) /*!= null ? rs.getString("email_pref_pro").charAt(0) : 'N'*/);
		outboundData.setPostalCode(rs.getString("src_postal_code") /*!= null ? rs.getString("src_postal_code") : ""*/);
		outboundData.setCustomerNbr(rs.getString("customer_nbr") /*!= null ? rs.getString("customer_nbr") : ""*/);
		outboundData.setPhonePtcFlag(rs.getString("phone_ptc_flag").charAt(0) /*!= null ? rs.getString("phone_ptc_flag").charAt(0) : 'N'*/);
		outboundData.setDnclSuppresion(rs.getString("dncl_suppresion").charAt(0) /*!= null ? rs.getString("dncl_suppresion").charAt(0) : 'N'*/);
		outboundData.setPhoneNumber(rs.getString("phone_number") /*!= null ? rs.getString("phone_number") : ""*/);
		outboundData.setFirstName(rs.getString("first_name") /*!= null ? rs.getString("first_name") : ""*/);
		outboundData.setLastName(rs.getString("last_name") /*!= null ? rs.getString("last_name") : ""*/);
		outboundData.setBusinessName(rs.getString("business_name") /*!= null ? rs.getString("business_name") : ""*/);
		outboundData.setIndustryCode(rs.getString("industry_code") /*!= null ? rs.getString("industry_code") : ""*/);
		outboundData.setCity(rs.getString("city") /*!= null ? rs.getString("city") : ""*/);
		outboundData.setProvince(rs.getString("province") /*!= null ? rs.getString("province") : ""*/);
		outboundData.setHdCaProSrcId(rs.getBigDecimal("hd_ca_pro_src_id") /*!= null ? rs.getBigDecimal("hd_ca_pro_src_id") : BigDecimal.ZERO*/);

		return outboundData;
	}
}

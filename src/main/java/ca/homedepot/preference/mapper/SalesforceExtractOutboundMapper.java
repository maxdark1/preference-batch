package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SalesforceExtractOutboundMapper implements RowMapper<SalesforceExtractOutboundDTO>
{
	@Override
	public SalesforceExtractOutboundDTO mapRow(ResultSet rs, int rowNum) throws SQLException
	{

		SalesforceExtractOutboundDTO salesforce = new SalesforceExtractOutboundDTO();

		salesforce.setEmailAddress(rs.getString("email_address"));
		Timestamp asOfDate = rs.getTimestamp("as_of_date");
		LocalDateTime asOfDateLocalDate = asOfDate == null ? null : asOfDate.toLocalDateTime();
		salesforce.setAsOfDate(asOfDateLocalDate);
		salesforce.setSourceId(rs.getString("source_id"));
		salesforce.setEmailStatus(rs.getString("email_status"));
		salesforce.setEmailPtc(rs.getString("email_ptc"));
		salesforce.setLanguagePreference(rs.getString("language_preference"));
		salesforce.setEarliestOptInDate(
				rs.getTimestamp("earliest_opt_in_date") != null ? rs.getTimestamp("earliest_opt_in_date").toLocalDateTime() : null);
		salesforce.setHdCanadaEmailCompliantFlag(rs.getString("hd_canada_email_compliant_flag"));
		salesforce.setHdCanadaFlag(rs.getString("hd_canada_flag"));
		salesforce.setGardenClubFlag(rs.getString("garden_club_flag"));
		salesforce.setNewMoverFlag(rs.getString("new_mover_flag"));
		salesforce.setProFlag(rs.getString("pro_flag"));
		salesforce.setPhonePtcFlag(rs.getString("phone_ptc_flag"));
		salesforce.setFirstName(rs.getString("first_name"));
		salesforce.setLastName(rs.getString("last_name"));
		salesforce.setPostalCode(rs.getString("postal_code"));
		salesforce.setProvince(rs.getString("province"));
		salesforce.setCity(rs.getString("city"));
		salesforce.setPhoneNumber(rs.getString("phone_number"));
		salesforce.setBusinessName(rs.getString("business_name"));
		salesforce.setBusinessType(rs.getString("business_type"));
		salesforce.setMoveDate(rs.getDate("move_date") != null ? rs.getDate("move_date").toLocalDate() : null);
		salesforce.setDwellingType(rs.getString("dwelling_type"));

		return salesforce;
	}
}

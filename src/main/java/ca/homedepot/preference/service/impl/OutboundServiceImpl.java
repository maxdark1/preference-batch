package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@Data
public class OutboundServiceImpl implements OutboundService
{
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * This methos is used to make a connection with DB and execute a query to get necessary data
	 * 
	 * @param item
	 */
	@Override
	public void preferenceOutbound(PreferenceOutboundDto item)
	{
		jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND, item.getEmail(),
				item.getEffectiveDate(), item.getSourceId(), item.getEmailStatus(), item.getEmailPermission(), item.getLanguagePref(),
				item.getEarlyOptInDate(), item.getCndCompliantFlag(), item.getEmailPrefHdCa(), item.getEmailPrefGardenClub(),
				item.getEmailPrefPro(), item.getPostalCode(), item.getCustomerNbr(), item.getPhonePtcFlag(), item.getDnclSuppresion(),
				item.getPhoneNumber(), item.getFirstName(), item.getLastName(), item.getBusinessName(), item.getIndustryCode(),
				item.getCity(), item.getProvince(), item.getHdCaProSrcId());
	}


	@Override
	public int purgeCitiSuppresionTable()
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION);
	}

	/**
	 * This method is used to connect with the database and truncate a passtrougths table
	 */
	@Override
	public void truncateCompliantTable()
	{
		jdbcTemplate.execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
	}
}

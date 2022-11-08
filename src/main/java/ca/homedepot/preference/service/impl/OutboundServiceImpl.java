package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class OutboundServiceImpl implements OutboundService
{
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
	public int programCompliant(InternalOutboundDto item)
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_PROGRAM_COMPLIANT, item.getEmailAddr(),
				item.getCanPtcEffectiveDate(), item.getCanPtcSourceId(), item.getEmailStatus(), item.getCanPtcGlag(),
				item.getLanguagePreference(), item.getEarlyOptInIDate(), item.getCndCompliantFlag(), item.getHdCaFlag(),
				item.getHdCaGardenClubFlag(), item.getHdCaNewMoverFlag(), item.getHdCaNewMoverEffDate(), item.getHdCaProFlag(),
				item.getPhonePtcFlag(), item.getFirstName(), item.getLastName(), item.getPostalCode(), item.getProvince(),
				item.getCity(), item.getPhoneNumber(), item.getBussinessName(), item.getIndustryCode(), item.getDwellingType(),
				item.getMoveDate());
	}


	@Override
	public int purgeCitiSuppresionTable()
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION);
	}

	@Override
	public void purgeSalesforceExtractTable()
	{
		jdbcTemplate.execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_SALESFORCE_EXTRACT);
	}

	@Override
	public void createFile(String repository, String folder, String fileNameFormat, String headers) throws IOException
	{
		/* Creating File */
		//TODO make formatter class variable
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));

		/* Inserting Headers */
		String file = headers;
		FileOutputStream writer = new FileOutputStream(repository + folder + fileName, false);


		try
		{
			byte[] toFile = file.getBytes();
			writer.write(toFile);
			writer.flush();
		}
		catch (Exception ex)
		{ //TODO is there any specific exception and what should happen in case of exception.
			log.error("File creation error" + ex.getMessage());
		}
		finally
		{
			writer.close();
		}

	}




	/**
	 * This method is used to connect with the database and truncate a passtrougths table
	 */
	@Override
	public void truncateCompliantTable()
	{
		jdbcTemplate.execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
	}

	@Override
	public int purgeProgramCompliant()
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_PROGRAM_COMPLIANT);
	}

	@Override
	public int purgeLoyaltyComplaintTable()
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_LOYALTY_COMPLIANT_TABLE);
	}


}

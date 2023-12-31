package ca.homedepot.preference.repositories.entities.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j


public class OutboundServiceImpl implements OutboundService
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final Format formatter = new SimpleDateFormat("yyyyMMdd");
	private final Format dateTimeFormatter = new SimpleDateFormat("yyyyMMdd HHmmss");

	/**
	 * This methos is used to make a connection with DB and execute a query to get necessary data
	 * 
	 * @param item
	 */
	@Override
	public void preferenceOutbound(PreferenceOutboundDto item)
	{
		jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_CRM_OUTBOUND, item.getEmail(),
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
	public void createFile(String fileNameFormat, String headers) throws IOException
	{
		/* Creating File */
		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));

		/* Inserting Headers */
		String file = headers;

		try (FileOutputStream writer = new FileOutputStream(fileName, false))
		{
			byte[] toFile = file.getBytes();
			writer.write(toFile);
			writer.flush();
		}
		catch (IOException ex)
		{
			log.error(" PREFERENCE BATCH ERROR - File {} creation error : {}", fileName, ex.getMessage());
			throw ex;
		}

	}

	public void createFlexAttributesFile(String repository, String folder, String fileNameFormat, final String headers)
			throws IOException
	{
		// Creat file and insert the headers
		String stamp = dateTimeFormatter.format(Calendar.getInstance().getTime());
		String fileName = fileNameFormat.replace("YYYYMMDDTHHMISS", stamp.replace(" ", "T"));

		File tempFile = FileUtil.createTempFile(CloudStorageUtils.generatePath(folder, fileName));
		try (FileOutputStream writer = new FileOutputStream(tempFile, false))
		{
			byte[] buffer = headers.getBytes();
			writer.write(buffer);
			writer.flush();
		}
		catch (IOException ex)
		{ // TODO is there any specific exception and what should happen in case of exception.
		  // Make the batch status failed
			log.error("PREFERENCE BATCH ERROR - File {} creation error : {}", fileName, ex);
		}

	}

	/**
	 * @param item
	 */
	@Override
	public void internalFlexAttributes(InternalFlexOutboundDTO item)
	{
		jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_FLEX_ATTRIBUTE, item.getFileId(), item.getSequenceNbr(),
				item.getEmailAddr(), item.getHdHhId(), item.getHdIndId(), item.getCustomerNbr(), item.getStoreNbr(),
				item.getOrgName(), item.getCompanyCd(), item.getCustTypeCd(), item.getSourceId(), item.getEffectiveDate(),
				item.getLastUpdateDate(), item.getIndustryCode(), item.getCompanyName(), item.getContactFirstName(),
				item.getContactLastName(), item.getContactRole());
	}

	@Override
	public void createFileGCS(String repository, String folder, String fileNameFormat, String headers) throws IOException
	{
		/* Creating File */
		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));

		/* Inserting Headers */
		String file = headers;

		File tempFile = FileUtil.createTempFile(CloudStorageUtils.generatePath(folder, fileName));
		try (FileOutputStream writer = new FileOutputStream(tempFile, false))
		{
			byte[] toFile = file.getBytes();
			writer.write(toFile);
			writer.flush();
		}
		catch (IOException ex)
		{
			log.error("PREFERENCE BATCH ERROR - File {} creation error : {}", fileName, ex.getMessage());
			throw ex;
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

	@Override
	public int purgeFlexAttributesTable()
	{
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_FLEX_ATTRIBUTE);
	}

}

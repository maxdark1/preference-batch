package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class OutboundServiceImpl implements OutboundService
{
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	/**
	 * This methos is used to make a connection with DB and execute a query to get necessary data
	 * 
	 * @param item
	 */
	@Override
	public void preferenceOutbound(PreferenceOutboundDto item)
	{
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
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
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION);
	}

	@Override
	public void createFile(String repository, String folder, String fileNameFormat)  {
		/*Creating File*/
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));

		/*Inserting Headers*/
		String file = PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS;

		try {
			FileOutputStream writer = new FileOutputStream(repository + folder + fileName, false);
			byte toFile[] = file.getBytes();
			writer.write(toFile);
			writer.close();
		}
		catch (Exception ex){
			log.error(ex.getMessage());
		}

	}



	/**
	 * This method is used to connect with the database and truncate a passtrougths table
	 */
	@Override
	public void truncateCompliantTable()
	{
		jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
	}


}

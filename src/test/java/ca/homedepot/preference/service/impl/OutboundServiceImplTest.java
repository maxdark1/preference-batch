package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OutboundServiceImplTest
{

	@Mock
	DataSource dataSource;
	@Mock
	JdbcTemplate jdbcTemplate;
	@InjectMocks
	@Spy
	OutboundServiceImpl outboundService;

	File directory;

	@BeforeEach
	void setup() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		directory = new File("OUTBOUND");
		directory.mkdirs();
	}

	@AfterAll
	static void ontesttermination() throws IOException
	{
		File directory = new File("OUTBOUND");

		FileUtils.deleteDirectory(directory);
	}

	@Test
	void preferenceOutbound()
	{
		int records = 1;
		PreferenceOutboundDto item = Mockito.mock(PreferenceOutboundDto.class);

		Mockito.when(jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND, item.getEmail(),
				item.getEffectiveDate(), item.getSourceId(), item.getEmailStatus(), item.getEmailPermission(), item.getLanguagePref(),
				item.getEarlyOptInDate(), item.getCndCompliantFlag(), item.getEmailPrefHdCa(), item.getEmailPrefGardenClub(),
				item.getEmailPrefPro(), item.getPostalCode(), item.getCustomerNbr(), item.getPhonePtcFlag(), item.getDnclSuppresion(),
				item.getPhoneNumber(), item.getFirstName(), item.getLastName(), item.getBusinessName(), item.getIndustryCode(),
				item.getCity(), item.getProvince(), item.getHdCaProSrcId())).thenReturn(records);
		outboundService.preferenceOutbound(item);
		Mockito.verify(outboundService).preferenceOutbound(item);

	}

	@Test
	void programCompliant()
	{
		int records = 1;
		InternalOutboundDto item = Mockito.mock(InternalOutboundDto.class);
		Mockito.when(jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_PROGRAM_COMPLIANT, item.getEmailAddr(),
				item.getCanPtcEffectiveDate(), item.getCanPtcSourceId(), item.getEmailStatus(), item.getCanPtcGlag(),
				item.getLanguagePreference(), item.getEarlyOptInIDate(), item.getCndCompliantFlag(), item.getHdCaFlag(),
				item.getHdCaGardenClubFlag(), item.getHdCaNewMoverFlag(), item.getHdCaNewMoverEffDate(), item.getHdCaProFlag(),
				item.getPhonePtcFlag(), item.getFirstName(), item.getLastName(), item.getPostalCode(), item.getProvince(),
				item.getCity(), item.getPhoneNumber(), item.getBussinessName(), item.getIndustryCode(), item.getDwellingType(),
				item.getMoveDate())).thenReturn(records);
		outboundService.programCompliant(item);
		Mockito.verify(outboundService).programCompliant(item);
	}

	@Test
	void purgeCitiSuppresionTable()
	{
		int recordsDeleted = 1;

		Mockito.when(jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION)).thenReturn(recordsDeleted);

		int deletedRecords = outboundService.purgeCitiSuppresionTable();
		assertEquals(recordsDeleted, deletedRecords);
	}

	@Test
	void truncateCompliantTable()
	{

		Mockito.doNothing().when(jdbcTemplate).execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
		outboundService.truncateCompliantTable();
		Mockito.verify(outboundService).truncateCompliantTable();
	}

	@Test
	void purgeProgramCompliant()
	{
		int recordsDeleted = 1;

		Mockito.when(jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_PROGRAM_COMPLIANT)).thenReturn(recordsDeleted);
		outboundService.purgeProgramCompliant();
		Mockito.verify(outboundService).purgeProgramCompliant();
	}

	@Test
	void createFileTest() throws IOException
	{
		String repository = "", folder = "OUTBOUND/", fileNameFormat = "ANYTHING_YYYYMMDD.txt";

		outboundService.createFile(repository, folder, fileNameFormat,
				PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS);
		Mockito.verify(outboundService).createFile(repository, folder, fileNameFormat,
				PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS);
	}

}
package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import com.github.javafaker.Faker;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
		verify(outboundService).preferenceOutbound(item);

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
		verify(outboundService).programCompliant(item);
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
		verify(outboundService).truncateCompliantTable();
	}

	@Test
	void purgeProgramCompliant()
	{
		int recordsDeleted = 1;

		Mockito.when(jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_TRUNCATE_PROGRAM_COMPLIANT)).thenReturn(recordsDeleted);
		outboundService.purgeProgramCompliant();
		verify(outboundService).purgeProgramCompliant();
	}

	@Test
	void createFileTest() throws IOException
	{
		String repository = "", folder = "OUTBOUND/", fileNameFormat = "ANYTHING_YYYYMMDD.txt";

		outboundService.createFile(repository, folder, fileNameFormat,
				PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS);
		verify(outboundService).createFile(repository, folder, fileNameFormat,
				PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS);
	}

	@Test
	void testIinternalFlexAttributes()
	{
		Faker faker = new Faker();
		InternalFlexOutboundDTO instance = InternalFlexOutboundDTO.builder()
				.fileId(BigDecimal.valueOf(abs(faker.number().randomNumber()))).sequenceNbr(faker.number().digits(8))
				.emailAddr(faker.internet().emailAddress()).hdHhId(BigDecimal.valueOf(faker.number().randomNumber()))
				.hdIndId(BigDecimal.valueOf(faker.number().randomNumber())).customerNbr(faker.number().digits(7))
				.storeNbr(faker.number().digits(4)).orgName(faker.company().name()).companyCd(faker.number().digits(3))
				.custTypeCd(faker.number().digits(3)).sourceId(faker.number().randomNumber())
				.effectiveDate(Calendar.getInstance().getTime()).lastUpdateDate(Calendar.getInstance().getTime())
				.industryCode(faker.number().digits(4)).companyName(faker.company().name()).contactFirstName(faker.name().firstName())
				.contactLastName(faker.name().lastName()).contactRole(faker.company().profession()).build();
		outboundService.internalFlexAttributes(instance);
		verify(outboundService).internalFlexAttributes(instance);
	}

	@Test
	void testCreateFlexAttributesFile()
	{
		//		/* Creating File */
		//		String fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
		//
		//		/* Inserting Headers */
		//		String file = headers;
		//
		//
		//
		//		try (FileOutputStream writer = new FileOutputStream(repository + folder + fileName, false))
		//		{
		//			byte[] toFile = file.getBytes();
		//			writer.write(toFile);
		//			writer.flush();
		//		}
		//		catch (IOException ex)
		//		{ //TODO is there any specific exception and what should happen in case of exception.
		//			// Make the batch status failed
		//			log.error("File creation error" + ex.getMessage());
		//		}
	}
}
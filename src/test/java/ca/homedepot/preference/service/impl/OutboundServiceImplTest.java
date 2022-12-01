package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
		PreferenceOutboundDto item = mock(PreferenceOutboundDto.class);

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
		InternalOutboundDto item = mock(InternalOutboundDto.class);
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
				"PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS");
		verify(outboundService).createFile(repository, folder, fileNameFormat,
				"PreferenceBatchConstants.PREFERENCE_OUTBOUND_COMPLIANT_HEADERS");
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
				.custTypeCd(faker.number().digits(3)).sourceId(faker.number().randomNumber()).effectiveDate(LocalDateTime.now())
				.lastUpdateDate(Calendar.getInstance().getTime()).industryCode(faker.number().digits(4))
				.companyName(faker.company().name()).contactFirstName(faker.name().firstName())
				.contactLastName(faker.name().lastName()).contactRole(faker.company().profession()).build();
		outboundService.internalFlexAttributes(instance);
		verify(outboundService).internalFlexAttributes(instance);
	}

	@Test
	void testCreateFlexAttributesFile()
	{

		try
		{
			outboundService.createFlexAttributesFile("/batchFiles/", "/OUTBOUND/", "FLXEMCDADYYYYMMDDTHHMISS.CEACI.ZZAX",
					PreferenceBatchConstants.FLEX_INTERNAL_HEADERS);

		}
		catch (Exception ex)
		{

			System.out.println(ex.getMessage());
		}

	}

	@Test
	void testCreateFileGCS() throws IOException
	{
		// Given
		String folder = "/OUTBOUND/";
		String fileName = "OPTIN_STANDARD_FLEX.txt";
		String fileNameFormat = "OPTIN_STANDARD_FLEX_YYYYMMDD_HHMISS.XYZ";
		String headers = "Language_Preference|AsOfDate|Email_Address|Email_Permission|Phone_Permission|Phone_Number|"
				+ "Phone_Extension|Title|First_Name|Last_Name|Address_1|Address_2|City|Province|Postal_Code|Mail_Permission|"
				+ "EmailPrefHDCA|GardenClub|EmailPrefPRO|NewMover|For_Future_Use|Source_ID|SMS_Flag|Fax_Number|Fax_Extension|"
				+ "Content_1|Value_1|Content_2|Value_2|Content_3|Value_3|Content_4|Value_4|Content_5|Value_5|Content_6|Value_6|"
				+ "Content_7|Value_7|Content_8|Value_8|Content_9|Value_9|Content_10|Value_10|Content_11|Value_11|Content_12|"
				+ "Value_12|Content_13|Value_13|Content_14|Value_14|Content_15|Value_15|Content_16|Value_16|Content_17|"
				+ "Value_17|Content_18|Value_18|Content_19|Value_19|Content_20|Value_20";
		try (MockedStatic<CloudStorageUtils> mockCloudStorageUtils = mockStatic(CloudStorageUtils.class))
		{
			mockCloudStorageUtils.when(() -> CloudStorageUtils.generatePath(folder, fileName)).thenReturn("SomeFileName");
		}
		File mockFile = mock(File.class);
		try (MockedStatic<FileUtil> mockFileUtil = mockStatic(FileUtil.class))
		{
			mockFileUtil.when(() -> FileUtil.createTempFile(CloudStorageUtils.generatePath(folder, fileName))).thenReturn(mockFile);
		}
		FileOutputStream writer = mock(FileOutputStream.class);
		doNothing().when(writer).write(any(byte[].class));
		doNothing().when(writer).flush();
		// When
		outboundService.createFileGCS("", folder, fileNameFormat, headers);
		// Then
		verify(outboundService).createFileGCS("", folder, fileNameFormat, headers);
	}
}
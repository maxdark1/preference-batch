package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalesforceExtractOutboundMapperTest
{

	@Mock
	SalesforceExtractOutboundDTO salesforceExtractOutboundDTO;

	@Mock
	ResultSet rs;

	int rowNum = 1;

	@InjectMocks
	@Spy
	SalesforceExtractOutboundMapper salesforceExtractOutboundMapper;

	String emailAddress = "test@hotmail.com", sourceId = "1", emailStatus = "1", emailPtc = "1", languagePreference = "E",
			hdCanadaEmailCompliantFlag = "Y", hdCanadaFlag = "1", gardenClubFlag = "1", newMoverClubFlug = "1", proFlag = "1",
			phonePtcFlag = "1", firstName = "firstName", lastName = "lastName", postalCode = "0000", province = "US",
			city = "Houston", phoneNumber = "6162930", businessName = "THD", businessType = "products", dwellingType = "Home";

	LocalDateTime asOfDate = LocalDateTime.now(), earliestOptInDate = LocalDateTime.now();

	LocalDate moveDate = LocalDate.now();

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);

		salesforceExtractOutboundDTO = new SalesforceExtractOutboundDTO(emailAddress, asOfDate, sourceId, emailStatus, emailPtc,
				languagePreference, earliestOptInDate, hdCanadaEmailCompliantFlag, hdCanadaFlag, gardenClubFlag, newMoverClubFlug,
				proFlag, phonePtcFlag, firstName, lastName, postalCode, province, city, phoneNumber, businessName, businessType,
				moveDate, dwellingType);
	}

	@Test
	void mapRow() throws SQLException
	{
		Mockito.when(rs.getString("email_address")).thenReturn(emailAddress);
		Mockito.when(rs.getTimestamp("as_of_date")).thenReturn(Timestamp.valueOf(asOfDate));
		Mockito.when(rs.getString("source_id")).thenReturn(sourceId);
		Mockito.when(rs.getString("email_status")).thenReturn(emailStatus);
		Mockito.when(rs.getString("email_ptc")).thenReturn(emailPtc);
		Mockito.when(rs.getString("language_preference")).thenReturn(languagePreference);
		Mockito.when(rs.getTimestamp("earliest_opt_in_date")).thenReturn(Timestamp.valueOf(earliestOptInDate));
		Mockito.when(rs.getString("hd_canada_email_compliant_flag")).thenReturn(hdCanadaEmailCompliantFlag);
		Mockito.when(rs.getString("hd_canada_flag")).thenReturn(hdCanadaFlag);
		Mockito.when(rs.getString("garden_club_flag")).thenReturn(gardenClubFlag);
		Mockito.when(rs.getString("new_mover_flag")).thenReturn(newMoverClubFlug);
		Mockito.when(rs.getString("pro_flag")).thenReturn(proFlag);
		Mockito.when(rs.getString("phone_ptc_flag")).thenReturn(phonePtcFlag);
		Mockito.when(rs.getString("first_name")).thenReturn(firstName);
		Mockito.when(rs.getString("last_name")).thenReturn(lastName);
		Mockito.when(rs.getString("postal_code")).thenReturn(postalCode);
		Mockito.when(rs.getString("province")).thenReturn(province);
		Mockito.when(rs.getString("city")).thenReturn(city);
		Mockito.when(rs.getString("phone_number")).thenReturn(phoneNumber);
		Mockito.when(rs.getString("business_name")).thenReturn(businessName);
		Mockito.when(rs.getString("business_type")).thenReturn(businessType);
		Mockito.when(rs.getDate("move_date")).thenReturn(Date.valueOf(moveDate));
		Mockito.when(rs.getString("dwelling_type")).thenReturn(dwellingType);

		SalesforceExtractOutboundDTO salesforceExtract = salesforceExtractOutboundMapper.mapRow(rs, rowNum);
		Assertions.assertNotNull(salesforceExtract);
		assertEquals(salesforceExtractOutboundDTO, salesforceExtract);
	}
}
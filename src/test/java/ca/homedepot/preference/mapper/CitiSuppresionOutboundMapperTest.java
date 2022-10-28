package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class CitiSuppresionOutboundMapperTest
{

	@Mock
	CitiSuppresionOutboundDTO citiSuppresionOutboundDTO;

	@Mock
	ResultSet rs;


	int rowNum = 1;

	@InjectMocks
	@Spy
	CitiSuppresionOutboundMapper citiSuppresionOutboundMapper;

	String firstName = "firstName", middleInitial = "middleInitial", lastName = "lastName", emailAddr = "email@example.com",
			addrLine1 = "addrLine1", addrLine2 = "addrLine2", stateCd = "stateCd", postalCd = "postalCd", city = "city",
			phone = "1234567890", smsPhoneMobile = "3216549870", dmOptOut = "N", emailOptOut = "N", phoneOptOut = "N",
			smsOptOut = "N", businessName = "businessName";

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);

		citiSuppresionOutboundDTO = new CitiSuppresionOutboundDTO(firstName, middleInitial, lastName, addrLine1, addrLine2, city,
				stateCd, postalCd, emailAddr, phone, smsPhoneMobile, businessName, dmOptOut, emailOptOut, phoneOptOut, smsOptOut);

	}

	@Test
	void mapRow() throws SQLException
	{

		Mockito.when(rs.getString("first_name")).thenReturn(firstName);
		Mockito.when(rs.getString("middle_initial")).thenReturn(middleInitial);
		Mockito.when(rs.getString("last_name")).thenReturn(lastName);
		Mockito.when(rs.getString("addr_line_1")).thenReturn(addrLine1);
		Mockito.when(rs.getString("addr_line_2")).thenReturn(addrLine2);
		Mockito.when(rs.getString("city")).thenReturn(city);
		Mockito.when(rs.getString("state_cd")).thenReturn(stateCd);
		Mockito.when(rs.getString("postal_cd")).thenReturn(postalCd);
		Mockito.when(rs.getString("email_addr")).thenReturn(emailAddr);
		Mockito.when(rs.getString("phone")).thenReturn(phone);
		Mockito.when(rs.getString("sms_mobile_phone")).thenReturn(smsPhoneMobile);
		Mockito.when(rs.getString("business_name")).thenReturn(businessName);
		Mockito.when(rs.getString("dm_opt_out")).thenReturn(dmOptOut);
		Mockito.when(rs.getString("email_opt_out")).thenReturn(emailOptOut);
		Mockito.when(rs.getString("phone_opt_out")).thenReturn(phoneOptOut);
		Mockito.when(rs.getString("sms_opt_out")).thenReturn(smsOptOut);

		CitiSuppresionOutboundDTO citiSuppresion = citiSuppresionOutboundMapper.mapRow(rs, rowNum);
		assertNotNull(citiSuppresion);
		assertEquals(citiSuppresionOutboundDTO, citiSuppresion);
	}
}
package ca.homedepot.preference.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CitiSuppresionOutboundDTOTest
{

	CitiSuppresionOutboundDTO citiSuppresionOutboundDTO;

	String firstName = "firstName", middleInitial = "middleInitial", lastName = "lastName", emailAddr = "email@example.com",
			addrLine1 = "addrLine1", addrLine2 = "addrLine2", stateCd = "stateCd", postalCd = "postalCd", city = "city",
			phone = "1234567890", smsPhoneMobile = "3216549870", dmOptOut = "N", emailOptOut = "N", phoneOptOut = "N",
			smsOptOut = "N", businessName = "businessName";

	@BeforeEach
	void setUp()
	{
		citiSuppresionOutboundDTO = new CitiSuppresionOutboundDTO(firstName, middleInitial, lastName, addrLine1, addrLine2, city,
				stateCd, postalCd, emailAddr, phone, smsPhoneMobile, businessName, dmOptOut, emailOptOut, phoneOptOut, smsOptOut);

	}

	@Test
	void getFirstName()
	{
		assertEquals(firstName, citiSuppresionOutboundDTO.getFirstName());
	}

	@Test
	void getMiddleInitial()
	{
		assertEquals(middleInitial, citiSuppresionOutboundDTO.getMiddleInitial());
	}

	@Test
	void getLastName()
	{
		assertEquals(lastName, citiSuppresionOutboundDTO.getLastName());
	}

	@Test
	void getAddrLine1()
	{
		assertEquals(addrLine1, citiSuppresionOutboundDTO.getAddrLine1());
	}

	@Test
	void getAddrLine2()
	{
		assertEquals(addrLine2, citiSuppresionOutboundDTO.getAddrLine2());
	}

	@Test
	void getCity()
	{
		assertEquals(city, citiSuppresionOutboundDTO.getCity());
	}

	@Test
	void getStateCd()
	{
		assertEquals(stateCd, citiSuppresionOutboundDTO.getStateCd());
	}

	@Test
	void getPostalCd()
	{
		assertEquals(postalCd, citiSuppresionOutboundDTO.getPostalCd());
	}

	@Test
	void getEmailAddr()
	{
		assertEquals(emailAddr, citiSuppresionOutboundDTO.getEmailAddr());
	}

	@Test
	void getPhone()
	{
		assertEquals(phone, citiSuppresionOutboundDTO.getPhone());
	}

	@Test
	void getSmsMobilePhone()
	{
		assertEquals(smsPhoneMobile, citiSuppresionOutboundDTO.getSmsMobilePhone());
	}

	@Test
	void getBusinessName()
	{
		assertEquals(businessName, citiSuppresionOutboundDTO.getBusinessName());
	}

	@Test
	void getDmOptOut()
	{
		assertEquals(dmOptOut, citiSuppresionOutboundDTO.getDmOptOut());
	}

	@Test
	void getEmailOptOut()
	{
		assertEquals(emailOptOut, citiSuppresionOutboundDTO.getEmailOptOut());
	}

	@Test
	void getPhoneOptOut()
	{
		assertEquals(phoneOptOut, citiSuppresionOutboundDTO.getPhoneOptOut());
	}

	@Test
	void getSmsOptOut()
	{
		assertEquals(smsOptOut, citiSuppresionOutboundDTO.getSmsOptOut());
	}
}
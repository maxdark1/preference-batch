package ca.homedepot.preference.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ModelsTest
{
	EmailOptOuts emailOptOuts;

	FileInboundStgTable fileInboundStgTable;

	InboundRegistration inboundRegistration;

	@BeforeEach
	public void setUp()
	{
		emailOptOuts = new EmailOptOuts();
		emailOptOuts.setStatus("status");
		emailOptOuts.setEmailAddress("addr");
		emailOptOuts.setDateUnsubscribed("22-11-2022");

		fileInboundStgTable = new FileInboundStgTable.FileInboundStgTableBuilder().fileId(BigDecimal.ONE).fileName("TEST_FILE")
				.status("status").srcDate(new Date()).updatedDate(new Date()).srcEmailAddress("email@gmail.com").phonePref("1")
				.srcPhoneNumber("123456789").srcPhoneExtension("52").srcTitleName("Sr").emailAddressPref("-1")
				.sourceId(BigDecimal.ONE).emailPrefHdCa("1").srcFirstName("Michael").srcLastName("Penia").srcAddress1("address1")
				.srcAddress2("address 2").srcCity("city").srcState("st").srcDate(new Date()).srcPostalCode("123456")
				.mailAddressPref("-1").emailPrefHdCa("-1").emailPrefPro("-1").emailPrefNewMover("-1").customerNbr("1234")
				.orgName("org_name").storeNbr("132").custTypeCd("123").faxNumber("12345").faxExtension("22").value1("value1")
				.content4("content4").value4("value4").content5("content5").value5("value5").content6("content6").value6("value6")
				.content7("content7").value7("value7").content8("content8").value8("value8").content10("content10")
				.content11("content11").value11("value11").content12("content12").value12("value12").content13("content13")
				.value13("value13").content14("content14").value14("value14").content15("content15").value15("value15")
				.content16("content16").value16("value16").content17("content17").value17("value17").content18("content18")
				.value18("value18").content19("content19").value19("value19").content20("content20").value20("value20").build();
	}

	@Test
	void testEmailOptOuts()
	{

		assertNotNull(emailOptOuts);
		assertEquals("status", emailOptOuts.getStatus());
		assertEquals("addr", emailOptOuts.getEmailAddress());
		assertEquals("22-11-2022", emailOptOuts.getDateUnsubscribed());

		emailOptOuts.setStatus("status2");
		emailOptOuts.setEmailAddress("addr2");
		emailOptOuts.setResource(new FileSystemResource("fileName.txt"));
		assertEquals("status2", emailOptOuts.getStatus());
		assertEquals("addr2", emailOptOuts.getEmailAddress());
		assertEquals("fileName.txt", emailOptOuts.getFileName());

	}

	@Test
	void testFileInboundStgTable()
	{
		FileInboundStgTable fileInboundStgTable1 = FileInboundStgTable.builder().build();

		assertNotNull(fileInboundStgTable);
		assertEquals(null, fileInboundStgTable.getContent1());
		assertEquals("status", fileInboundStgTable.getStatus());
		assertEquals(BigDecimal.ONE, fileInboundStgTable.getSourceId());
		assertNotEquals(fileInboundStgTable1, fileInboundStgTable);


		fileInboundStgTable.setContent1("2");
		fileInboundStgTable.setContent3("content3");
		fileInboundStgTable.setValue3("value3");
		fileInboundStgTable.setEmailStatus(new BigDecimal("123"));
		fileInboundStgTable.setEmailPrefGardenClub("0");
		fileInboundStgTable.setCellSmsFlag("0");
		fileInboundStgTable.setSrcLanguagePref("E");


		assertEquals("2", fileInboundStgTable.getContent1());
		assertEquals("content3", fileInboundStgTable.getContent3());
		assertEquals(new BigDecimal("123"), fileInboundStgTable.getEmailStatus());
		assertEquals("0", fileInboundStgTable.getEmailPrefGardenClub());
		assertEquals("0", fileInboundStgTable.getCellSmsFlag());
		assertEquals("E", fileInboundStgTable.getSrcLanguagePref());
		assertNotNull(fileInboundStgTable.getFileId());
		assertNotNull(fileInboundStgTable.getFileName());
		assertNotNull(fileInboundStgTable.getUpdatedDate());
		assertNotNull(fileInboundStgTable.getSrcEmailAddress());
		assertNotNull(fileInboundStgTable.getEmailAddressPref());
		assertNotNull(fileInboundStgTable.getPhonePref());
		assertNotNull(fileInboundStgTable.getSrcPhoneNumber());
		assertNotNull(fileInboundStgTable.getSrcPhoneExtension());
		assertNotNull(fileInboundStgTable.getSrcTitleName());
		assertNotNull(fileInboundStgTable.getSrcFirstName());
		assertEquals("Penia", fileInboundStgTable.getSrcLastName());
		assertTrue(fileInboundStgTable.getSrcAddress1().contains("address"));

	}

	@Test
	void getterFileInboundStgTable()
	{
		assertTrue(fileInboundStgTable.getSrcAddress2().contains("address"));
		assertEquals("city", fileInboundStgTable.getSrcCity());
		assertEquals("st", fileInboundStgTable.getSrcState());
		assertNotNull(fileInboundStgTable.getSrcDate());
		assertEquals("123456", fileInboundStgTable.getSrcPostalCode());
		assertEquals("-1", fileInboundStgTable.getMailAddressPref());
		assertEquals("-1", fileInboundStgTable.getEmailPrefHdCa());
		assertEquals("-1", fileInboundStgTable.getEmailPrefPro());
		assertEquals("-1", fileInboundStgTable.getEmailPrefNewMover());
		assertEquals("1234", fileInboundStgTable.getCustomerNbr());
		assertEquals("org_name", fileInboundStgTable.getOrgName());
		assertEquals("132", fileInboundStgTable.getStoreNbr());
		assertEquals("123", fileInboundStgTable.getCustTypeCd());
		assertEquals("12345", fileInboundStgTable.getFaxNumber());
		assertEquals("22", fileInboundStgTable.getFaxExtension());
		assertEquals("value1", fileInboundStgTable.getValue1());
		assertNull(fileInboundStgTable.getContent2());
		assertNull(fileInboundStgTable.getValue2());
		assertNull(fileInboundStgTable.getContent3());
		assertNull(fileInboundStgTable.getValue3());
		assertNotNull("content4", fileInboundStgTable.getContent4());
		assertNotNull("value4", fileInboundStgTable.getValue4());
		assertNotNull("content5", fileInboundStgTable.getContent5());
		assertNotNull("value5", fileInboundStgTable.getValue5());
		assertNotNull("content6", fileInboundStgTable.getContent6());

	}

	@Test
	void contentValueAssertions()
	{
		assertNotNull("value6", fileInboundStgTable.getValue6());
		assertNotNull("content7", fileInboundStgTable.getContent7());
		assertNotNull("value7", fileInboundStgTable.getValue7());
		assertNotNull("content8", fileInboundStgTable.getContent8());
		assertNotNull("value8", fileInboundStgTable.getValue8());
		assertNotNull("content9", fileInboundStgTable.getContent9());
		assertNotNull("value9", fileInboundStgTable.getValue9());
		assertNotNull("content10", fileInboundStgTable.getContent10());
		assertNotNull("value10", fileInboundStgTable.getValue10());
		assertNotNull("content11", fileInboundStgTable.getContent11());
		assertNotNull("value11", fileInboundStgTable.getValue11());
		assertNotNull("content12", fileInboundStgTable.getContent12());
		assertNotNull("value12", fileInboundStgTable.getValue12());
		assertEquals("content13", fileInboundStgTable.getContent13());
		assertEquals("value13", fileInboundStgTable.getValue13());
		assertNotNull("content14", fileInboundStgTable.getContent14());
		assertNotNull("value14", fileInboundStgTable.getValue14());

	}

	@Test
	void testContetValuePair()
	{
		assertNotNull("content15", fileInboundStgTable.getContent15());
		assertNotNull("value15", fileInboundStgTable.getValue15());
		assertNotNull("content16", fileInboundStgTable.getContent16());
		assertNotNull("value16", fileInboundStgTable.getValue16());
		assertNotNull("content17", fileInboundStgTable.getContent17());
		assertNotNull("value17", fileInboundStgTable.getValue17());
		assertNotNull("content18", fileInboundStgTable.getContent18());
		assertNotNull("value18", fileInboundStgTable.getValue18());
		assertNotNull("content19", fileInboundStgTable.getContent19());
		assertNotNull("value19", fileInboundStgTable.getValue19());
		assertNotNull("content20", fileInboundStgTable.getContent20());
		assertNotNull("value20", fileInboundStgTable.getValue20());
		assertNull(fileInboundStgTable.getInsertedBy());
		assertNull(fileInboundStgTable.getInsertedDate());
	}

	@Test
	void inboundRegistrationSetResourceTest()
	{
		Resource resource = new FileSystemResource("File");
		inboundRegistration = new InboundRegistration();

		inboundRegistration.setResource(resource);

		assertEquals("File", inboundRegistration.getFileName());
	}
}

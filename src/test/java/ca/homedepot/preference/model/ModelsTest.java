package ca.homedepot.preference.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ModelsTest
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

		fileInboundStgTable = new FileInboundStgTable.FileInboundStgTableBuilder().file_id(BigDecimal.ONE).fileName("TEST_FILE").status("status")
				.src_date(new Date()).updated_date(new Date()).src_email_address("email@gmail.com").phone_pref("1").src_phone_number("123456789")
				.src_phone_extension("52").src_title_name("Sr").email_address_pref("-1").source_id(1L).email_pref_hd_ca("1").src_first_name("Michael")
				.src_last_name("Penia").src_address1("address1").src_address2("address 2").src_city("city").src_state("st").src_date(new Date())
				.src_postal_code("123456").mail_address_pref("-1").email_pref_hd_ca("-1").email_pref_pro("-1").email_pref_new_mover("-1")
				.customer_nbr("1234").org_name("org_name").store_nbr("132").cust_type_cd("123").fax_number("12345").fax_extension("22")
				.value1("value1").content4("content4").value4("value4").content5("content5").value5("value5").content6("content6").value6("value6")
				.content7("content7").value7("value7").content8("content8").value8("value8").content10("content10").content11("content11").value11("value11")
				.content12("content12").value12("value12").content13("content13").value13("value13").content14("content14").value14("value14")
				.content15("content15").value15("value15").content16("content16").value16("value16").content17("content17").value17("value17")
				.content18("content18").value18("value18").content19("content19").value19("value19").content20("content20").value20("value20").build();
	}

	@Test
	public void testEmailOptOuts()
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
	public void testFileInboundStgTable()
	{
		FileInboundStgTable fileInboundStgTable1 = FileInboundStgTable.builder().build();

		assertNotNull(fileInboundStgTable);
		assertEquals(null, fileInboundStgTable.getContent1());
		assertEquals("status", fileInboundStgTable.getStatus());
		assertEquals(1L, fileInboundStgTable.getSource_id());
		assertNotEquals(fileInboundStgTable1, fileInboundStgTable);


		fileInboundStgTable.setContent1("2");
		fileInboundStgTable.setContent3("content3");
		fileInboundStgTable.setValue3("value3");
		fileInboundStgTable.setEmail_status(new BigDecimal("123"));
		fileInboundStgTable.setEmail_pref_garden_club("0");
		fileInboundStgTable.setCell_sms_flag("0");
		fileInboundStgTable.setSrc_language_pref("E");


		assertEquals("2", fileInboundStgTable.getContent1());
		assertEquals("content3", fileInboundStgTable.getContent3());
		assertEquals(new BigDecimal("123"), fileInboundStgTable.getEmail_status());
		assertEquals("0", fileInboundStgTable.getEmail_pref_garden_club());
		assertEquals("0", fileInboundStgTable.getCell_sms_flag());
		assertEquals("E", fileInboundStgTable.getSrc_language_pref());
		assertNotNull(fileInboundStgTable.getFile_id());
		assertNotNull(fileInboundStgTable.getFileName());
		assertNotNull(fileInboundStgTable.getUpdated_date());
		assertNotNull(fileInboundStgTable.getSrc_email_address());
		assertNotNull(fileInboundStgTable.getEmail_address_pref());
		assertNotNull(fileInboundStgTable.getPhone_pref());
		assertNotNull(fileInboundStgTable.getSrc_phone_number());
		assertNotNull(fileInboundStgTable.getSrc_phone_extension());
		assertNotNull(fileInboundStgTable.getSrc_title_name());
		assertNotNull(fileInboundStgTable.getSrc_first_name());
		assertEquals("Penia", fileInboundStgTable.getSrc_last_name());
		assertTrue(fileInboundStgTable.getSrc_address1().contains("address"));
		assertTrue(fileInboundStgTable.getSrc_address2().contains("address"));
		assertEquals("city", fileInboundStgTable.getSrc_city());
		assertEquals("st", fileInboundStgTable.getSrc_state());
		assertNotNull(fileInboundStgTable.getSrc_date());
		assertEquals("123456", fileInboundStgTable.getSrc_postal_code());
		assertEquals("-1", fileInboundStgTable.getMail_address_pref());
		assertEquals("-1", fileInboundStgTable.getEmail_pref_hd_ca());
		assertEquals("-1", fileInboundStgTable.getEmail_pref_pro());
		assertEquals("-1", fileInboundStgTable.getEmail_pref_new_mover());
		assertEquals("1234", fileInboundStgTable.getCustomer_nbr());
		assertEquals("org_name", fileInboundStgTable.getOrg_name());
		assertEquals("132", fileInboundStgTable.getStore_nbr());
		assertEquals("123", fileInboundStgTable.getCust_type_cd());
		assertEquals("12345", fileInboundStgTable.getFax_number());
		assertEquals("22", fileInboundStgTable.getFax_extension());
		assertEquals("value1", fileInboundStgTable.getValue1());
		assertNull(fileInboundStgTable.getContent2());
		assertNull(fileInboundStgTable.getValue2());
		assertEquals("content3",fileInboundStgTable.getContent3());
		assertEquals("value3",fileInboundStgTable.getValue3());
		assertNotNull("content4", fileInboundStgTable.getContent4());
		assertNotNull("value4", fileInboundStgTable.getValue4());
		assertNotNull("content5", fileInboundStgTable.getContent5());
		assertNotNull("value5", fileInboundStgTable.getValue5());
		assertNotNull("content6", fileInboundStgTable.getContent6());
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
		assertEquals("content13",fileInboundStgTable.getContent13());
		assertEquals("value13",fileInboundStgTable.getValue13());
		assertNotNull("content14", fileInboundStgTable.getContent14());
		assertNotNull("value14", fileInboundStgTable.getValue14());
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
		assertNull(fileInboundStgTable.getInserted_by());
		assertNull(fileInboundStgTable.getInserted_date());
	}

	@Test
	void inboundRegistrationSetResourceTest(){
		Resource resource = new FileSystemResource("File");
		inboundRegistration = new InboundRegistration();

		inboundRegistration.setResource(resource);

		assertEquals("File", inboundRegistration.getFileName());
	}
}

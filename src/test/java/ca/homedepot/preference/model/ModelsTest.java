package ca.homedepot.preference.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

		fileInboundStgTable = new FileInboundStgTable.FileInboundStgTableBuilder().status("status").src_date(new Date())
				.email_address_1_pref("-1").source_id(1L).email_pref_hd_ca("1").build();
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
		assertEquals("status2", emailOptOuts.getStatus());
		assertEquals("addr2", emailOptOuts.getEmailAddress());

	}

	@Test
	public void testFileInboundStgTable()
	{
		assertNotNull(fileInboundStgTable);
		assertEquals(null, fileInboundStgTable.getContent1());
		assertEquals("status", fileInboundStgTable.getStatus());
		assertEquals(1L, fileInboundStgTable.getSource_id());


		fileInboundStgTable.setContent1("2");
		fileInboundStgTable.setContent3("content3");
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

		FileInboundStgTable fileInboundStgTable1 = new FileInboundStgTable.FileInboundStgTableBuilder().build();
		assertNotEquals(fileInboundStgTable1, fileInboundStgTable);


	}
}

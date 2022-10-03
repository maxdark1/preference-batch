package ca.homedepot.preference.repositories;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ca.homedepot.preference.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import static org.junit.jupiter.api.Assertions.*;

class JobRepoTest {


	Faker faker;
	RegistrationRequest registrationRequest;

	FileDTO fileDTO;

	Job jobDTO;

	Master masterDTO;

	PreferenceItem preferenceItem;

	@BeforeEach
	void setUp() throws ParseException {
		createRegistrationRequestObj();
		createFileDTO();
		createJobDTO();
		createMaster();
	}

	void createMaster()
	{
		masterDTO = new Master(new BigDecimal("1"), new BigDecimal("1"),"SOURCE", "hybris", true);

	}

	void createJobDTO()
	{
		jobDTO = new Job();
		jobDTO.setJob_id(new BigDecimal("12345"));

		jobDTO.setJob_name("JOB NAME");
		jobDTO.setStatus("S");
		jobDTO.setStatus_id(BigDecimal.ONE);
		jobDTO.setStart_time(new Date());
		jobDTO.setEnd_time(new Date());
		jobDTO.setInserted_by("TEST");
		jobDTO.setInserted_date(new Date());
		jobDTO.setUpdated_by("TEST");
	}

	void createFileDTO() throws ParseException
	{
		fileDTO = new FileDTO("TEST");
		fileDTO.setFile_id(BigDecimal.ONE);
		fileDTO.setFile_source_id(new BigDecimal("12345"));
		fileDTO.setJob(new BigDecimal("246810"));
		fileDTO.setInserted_by("TEST");
		fileDTO.setStart_time((new SimpleDateFormat("MM-dd-yyyy")).parse("09-15-2022"));
		fileDTO.setInserted_date((new SimpleDateFormat("MM-dd-yyyy")).parse("09-15-2022"));
		fileDTO.setStatus("C");
		fileDTO.setUpdated_by("TEST");
		fileDTO.setUpdated_date((new SimpleDateFormat("MM-dd-yyyy")).parse("09-15-2022"));
		fileDTO.setFile_name("TEST_FILE");
		fileDTO.setEnd_time((new SimpleDateFormat("MM-dd-yyyy")).parse("09-15-2022"));
	}

	void createRegistrationRequestObj()
	{
		registrationRequest = new RegistrationRequest();
		registrationRequest.setFileId(new BigDecimal("12345"));
		registrationRequest.setStatus(true);
		registrationRequest.setSequenceNbr("246810");
		registrationRequest.setSourceId(1L);
		registrationRequest.setLanguagePreference("E");
		registrationRequest.setSrcTitleName("Sr");
		registrationRequest.setSrcFirstName("Michael");
		registrationRequest.setSrcLastName("Lawson");
		registrationRequest.setEmailStatus(1);
		registrationRequest.setEmailAddressPref(1);
		registrationRequest.setSrcDate("09-15-2022");
		registrationRequest.setCellSmsFlag(1);
		registrationRequest.setSrcPhoneNumber("1234567890");
		registrationRequest.setSrcPhoneExtension("+52");
		registrationRequest.setPhonePref(1);
		registrationRequest.setFaxNumber("1234");
		registrationRequest.setFaxExtension("12");

		Address address = new Address();
		address.setSrcState("Sinaloa");
		address.setSrcCity("Culiacan");
		address.setSrcAddress1("address1");
		address.setSrcAddress2("address2");
		address.setSrcPostalCode("82000");
		registrationRequest.setSrcAddress(address);
		registrationRequest.setMailAddresspref(1);
		registrationRequest.setEmailPrefHDCa(1);
		registrationRequest.setEmailPrefGardenClub(1);
		registrationRequest.setEmailPrefPro(1);
		registrationRequest.setEmailPrefNewMover(1);

		Map<String, String> contentValue = new HashMap<>();
		contentValue.put("content1", "value1");
		contentValue.put("content2", "value2");
		registrationRequest.setContentValue(contentValue);
		registrationRequest.setUpdatedDate("09-26-2022");
		registrationRequest.setInsertedDate("09-26-2022");
		registrationRequest.setInsertedBy("TEST");
		registrationRequest.setUpdatedBy("TEST");
	}

	private String getFakeEmail()
	{
		FakeValuesService fakeValuesService = new FakeValuesService(new Locale("EN"), new RandomService());
		return fakeValuesService.bothify("????##@gmail.com");
	}

	@AfterEach
	void tearDown()
	{
	}

	@Test
	void testDTORegistrationRequest()
	{
		Address address = new Address();
		address.setSrcState("Sinaloa");
		address.setSrcCity("Culiacan");
		address.setSrcAddress1("address1");
		address.setSrcAddress2("address2");
		address.setSrcPostalCode("82000");
		assertNotNull(registrationRequest);
		registrationRequest.setFileId(new BigDecimal("12345"));
		assertEquals(new BigDecimal("12345"), registrationRequest.getFileId());
		assertTrue(registrationRequest.getStatus());
		assertEquals("246810", registrationRequest.getSequenceNbr());
		assertEquals(Long.valueOf(1), registrationRequest.getSourceId());
		assertEquals("E", registrationRequest.getLanguagePreference());
		assertEquals("Sr", registrationRequest.getSrcTitleName());
		assertEquals("Michael", registrationRequest.getSrcFirstName());
		assertNotEquals("Escobedo", registrationRequest.getSrcLastName());
		assertNull(registrationRequest.getSrcEmailAddress());
		assertNotNull(registrationRequest.getEmailStatus());
		assertNotNull(registrationRequest.getEmailAddressPref());
		assertEquals("09-15-2022", registrationRequest.getSrcDate());
		assertEquals(Integer.valueOf(1), registrationRequest.getCellSmsFlag());
		assertEquals(address, registrationRequest.getSrcAddress());
		assertEquals("1234567890", registrationRequest.getSrcPhoneNumber());
		assertEquals("+52", registrationRequest.getSrcPhoneExtension());
		assertEquals(Integer.valueOf(1), registrationRequest.getPhonePref());
		assertNotNull(registrationRequest.getFaxNumber());
		assertNotNull(registrationRequest.getFaxExtension());
		assertEquals(Integer.valueOf(1), registrationRequest.getMailAddresspref());
		assertEquals(Integer.valueOf(1), registrationRequest.getEmailPrefGardenClub());
		assertEquals(Integer.valueOf(1), registrationRequest.getEmailPrefNewMover());
		assertNotNull(registrationRequest.getEmailPrefPro());
		assertNotNull(registrationRequest.getContentValue());
		assertEquals(Integer.valueOf(1), registrationRequest.getEmailPrefHDCa());
		assertTrue(registrationRequest.getUpdatedDate().contains("9"));
		assertNotNull(registrationRequest.getUpdatedBy());
		assertNotNull(registrationRequest.getInsertedDate());
		assertNotNull(registrationRequest.getInsertedBy());
	}

	@Test
	void testDTOAddress()
	{
		assertNotNull(registrationRequest.getSrcAddress());
		assertEquals("Culiacan", registrationRequest.getSrcAddress().getSrcCity());
		assertEquals("Sinaloa", registrationRequest.getSrcAddress().getSrcState());
	}

	@Test
	void testFileDTO() throws ParseException
	{
		assertNotNull(fileDTO);
		assertEquals("TEST_FILE", fileDTO.getFile_name());
		assertNotNull(fileDTO.getJob());
		assertNotNull(fileDTO.getFile_id());
		assertEquals(new BigDecimal("12345"), fileDTO.getFile_source_id());
		assertNotNull(fileDTO.getStatus());
		assertEquals((new SimpleDateFormat("MM-dd-yyyy")).parse("09-15-2022"), fileDTO.getStart_time());
		assertNotNull(fileDTO.getUpdated_date());
		assertNotNull(fileDTO.getInserted_date());
		assertNotNull(fileDTO.getEnd_time());
		assertEquals("TEST", fileDTO.getInserted_by());
		assertEquals("TEST", fileDTO.getUpdated_by());

	}

	@Test
	void testJobDTO()
	{
		assertNotNull(jobDTO);
		assertEquals(BigDecimal.ONE, jobDTO.getStatus_id());
		assertEquals(new BigDecimal("12345"), jobDTO.getJob_id());
		assertNotEquals("NAME", jobDTO.getJob_name());
		assertEquals("S", jobDTO.getStatus());
		assertNotNull(jobDTO.getStart_time());
		assertNotNull(jobDTO.getInserted_date());
		assertNotNull(jobDTO.getEnd_time());
		assertNull(jobDTO.getUpdated_date());
		assertEquals("TEST", jobDTO.getInserted_by());
		assertEquals("TEST", jobDTO.getUpdated_by());
	}

	@Test
	void testMasterDTO()
	{
		Master master2 = new Master();
		assertNotNull(masterDTO);
		assertEquals("SOURCE", masterDTO.getKey_value());
		assertEquals(BigDecimal.ONE, masterDTO.getKey_id());
		assertNotNull(master2);
		assertNotEquals(master2, masterDTO);
		assertEquals("hybris", masterDTO.getValue_val());
		assertEquals(true, masterDTO.getActive());
	}

	@Test
	void testPreferenceItemDTO()
	{
		preferenceItem = new PreferenceItem("1", "TEST", "TEST1");
		PreferenceItem preferenceItem1 = new PreferenceItem();
		assertNotNull(preferenceItem);
		assertNotNull(preferenceItem);
		assertNotEquals(preferenceItem1, preferenceItem);
		assertEquals("1", preferenceItem.getId());
		assertEquals("TEST", preferenceItem.getType());
		assertEquals("TEST1", preferenceItem.getValue());
	}

	@Test
	void testPreferenceListDTO()
	{
		PreferenceItemList preferenceItemList = new PreferenceItemList();
		ArrayList<PreferenceItem> items = new ArrayList<>();
		items.add(new PreferenceItem());
		assertNotNull(preferenceItemList);
		assertNotNull(preferenceItemList.getItems());

		preferenceItemList.setItems(items);
		assertEquals(items, preferenceItemList.getItems());
		assertEquals(items.size(), preferenceItemList.getItems().size());
	}

	@Test
	void testregistrationResponseDTO()
	{
		RegistrationResponse registrationResponse = new RegistrationResponse();
		assertNotNull(registrationResponse);
		assertNull(registrationResponse.getRegistration());

		registrationResponse.setRegistration(new ArrayList<>());
		assertNotNull(registrationResponse.getRegistration());
	}

	@Test
	void testResponseDTO()
	{
		Response response = new Response();
		Response response1 = new Response();
		response.setId("1");
		response.setStatus("ACTIVE");
		response.setDetails("DETAILS");
		assertNotNull(response);
		assertNotEquals(response1, response);
		assertEquals("1", response.getId());
		assertEquals("ACTIVE", response.getStatus());
		assertEquals("DETAILS", response.getDetails());
	}
}
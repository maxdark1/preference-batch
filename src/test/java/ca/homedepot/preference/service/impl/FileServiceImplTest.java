package ca.homedepot.preference.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
class FileServiceImplTest
{

	@InjectMocks
	FileServiceImpl fileService;
	@Mock
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp()
	{
		jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		fileService = new FileServiceImpl();
		fileService.setJdbcTemplate(jdbcTemplate);
	}

	@Test
	void testGetJdbcTemplate()
	{
		assertNotNull(fileService.getJdbcTemplate());
	}

	@Test
	void insert()
	{
		String status = "G";
		BigDecimal sourceId = new BigDecimal("12345678"), job_id = new BigDecimal("1234567890");
		Date startTime = new Date(), insertedDate = new Date(), endTiem = new Date();
		String file_name = "fileName", inserted_by = "test";
		BigDecimal statusId = BigDecimal.ONE;
		FileDTO file = new FileDTO(null, file_name, job_id,sourceId, status, statusId, startTime, endTiem, inserted_by, insertedDate, null, null);

		int value = 1;

		when(jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_FILE, file.getFile_name(), file.getJob(), file.getFile_source_id(), file.getStatus(), file.getStart_time(),
				file.getInserted_by(), file.getInserted_date(), file.getStatus_id(), file.getEnd_time())).thenReturn(value);
		when(fileService.insert(file))
				.thenReturn(value);

		int result = fileService.insert(file);

		assertEquals(value, result);

	}

	@Test
	void getJobId()
	{
		BigDecimal jobId = new BigDecimal("1234567890");
		String job_name = "registrationInbound";

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(jobId);
		when(fileService.getJobId(job_name)).thenReturn(jobId);

		BigDecimal resultJobId = fileService.getJobId(job_name);
		assertEquals(jobId, resultJobId);
	}

	@Test
	void getFile()
	{
		String file_name = "fileNameTest";
		BigDecimal jobId = new BigDecimal("1234567890"), fileId = new BigDecimal("2468101214");

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(fileId);
		when(fileService.getFile(file_name, jobId)).thenReturn(fileId);

		BigDecimal resultFileID = fileService.getFile(file_name, jobId);
		assertEquals(fileId, resultFileID);
	}

	@Test
	void getLasFile()
	{
		BigDecimal fileId = new BigDecimal("2468101214");


		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(fileId);
		when(fileService.getLasFile()).thenReturn(fileId);
		fileService.setJdbcTemplate(jdbcTemplate);

		BigDecimal resultFileID = fileService.getLasFile();
		assertEquals(fileId, resultFileID);

	}

	@Test
	void getSourceId()
	{
		String keyVal = "SOURCE", valueVal = "hybris";
		BigDecimal masterId = new BigDecimal("123456");

		when(jdbcTemplate.queryForObject(anyString(), eq(new Object[]
		{ keyVal, valueVal }), any(RowMapper.class))).thenReturn(masterId);
		when(fileService.getSourceId(keyVal, valueVal)).thenReturn(masterId);
		BigDecimal currentMasterId = fileService.getSourceId(keyVal, valueVal);

		assertEquals(masterId, currentMasterId);
	}


	@Test
	void updateInboundStgTableStatus()
	{
		String insertedBy = "BATCH", status = "IP", oldStatus = "NS";
		BigDecimal fileId = BigDecimal.ONE;
		Date updatedDate = new Date();

		int updatedRecords = 1;

		when(jdbcTemplate.update(anyString(), eq(status), any(Date.class), anyString(), anyString(), eq(fileId), eq(fileId)))
				.thenReturn(updatedRecords);
		when(fileService.updateInboundStgTableStatus(fileId, status, oldStatus)).thenReturn(updatedRecords);

		int currentUpdatedRecords = fileService.updateInboundStgTableStatus(fileId, status, oldStatus);
		assertEquals(updatedRecords, currentUpdatedRecords);

	}

	@Test
	void updateFileEndTime()
	{
		String updatedBy = "BATCH", status = "IP";
		BigDecimal fileId = BigDecimal.ONE;
		Date updatedDate = new Date(), endTime = new Date();
		Master statusMaster = new Master();

		int updatedRecords = 1;

		when(jdbcTemplate.update(anyString(), eq(endTime), eq(updatedDate), eq(updatedBy), eq(fileId),
				eq(statusMaster.getMaster_id()), eq(statusMaster.getValue_val()))).thenReturn(updatedRecords);
		when(fileService.updateFileEndTime(fileId, updatedDate, updatedBy, endTime, statusMaster)).thenReturn(updatedRecords);

		int currentUpdatedRecords = fileService.updateFileEndTime(fileId, updatedDate, updatedBy, endTime, statusMaster);
		assertEquals(updatedRecords, currentUpdatedRecords);
	}

	@Test
	void getFilesToMove()
	{
		List<FileDTO> files = new ArrayList<>();
		files.add(new FileDTO());

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(files);
		when(fileService.getFilesToMove()).thenReturn(files);

		List<FileDTO> currentFiles = fileService.getFilesToMove();
		assertEquals(files, currentFiles);
	}

	@Test
	void insertInboundStgError()
	{
		FileInboundStgTable stgTable = FileInboundStgTable.builder().build();
		int updated = 1;

		when(jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_ERROR, stgTable.getFile_id(), stgTable.getStatus(),
				stgTable.getSource_id(), stgTable.getSrc_phone_number(), stgTable.getSrc_first_name(), stgTable.getSrc_last_name(),
				stgTable.getSrc_address1(), stgTable.getSrc_address2(), stgTable.getSrc_city(), stgTable.getSrc_state(),
				stgTable.getSrc_postal_code(), stgTable.getSrc_language_pref(), stgTable.getSrc_email_address(),
				stgTable.getSrc_title_name(), stgTable.getPhone_pref(), stgTable.getEmail_address_pref(),
				stgTable.getMail_address_pref(), stgTable.getSrc_date(), stgTable.getEmail_status(),
				stgTable.getSrc_phone_extension(), stgTable.getEmail_pref_hd_ca(), stgTable.getEmail_pref_garden_club(),
				stgTable.getEmail_pref_pro(), stgTable.getEmail_pref_new_mover(), stgTable.getCell_sms_flag(),
				stgTable.getBusiness_name(), stgTable.getCustomer_nbr(), stgTable.getOrg_name(), stgTable.getStore_nbr(),
				stgTable.getCust_type_cd(), stgTable.getContent1(), stgTable.getValue1(), stgTable.getContent2(),
				stgTable.getValue2(), stgTable.getContent3(), stgTable.getValue3(), stgTable.getContent4(), stgTable.getValue4(),
				stgTable.getContent5(), stgTable.getValue5(), stgTable.getContent6(), stgTable.getValue6(), stgTable.getContent7(),
				stgTable.getValue7(), stgTable.getContent8(), stgTable.getValue8(), stgTable.getContent9(), stgTable.getValue9(),
				stgTable.getContent10(), stgTable.getValue10(), stgTable.getContent11(), stgTable.getValue11(),
				stgTable.getContent12(), stgTable.getValue12(), stgTable.getContent13(), stgTable.getValue13(),
				stgTable.getContent14(), stgTable.getValue14(), stgTable.getContent15(), stgTable.getValue15(),
				stgTable.getContent16(), stgTable.getValue16(), stgTable.getContent17(), stgTable.getValue17(),
				stgTable.getContent18(), stgTable.getValue18(), stgTable.getContent19(), stgTable.getValue19(),
				stgTable.getContent20(), stgTable.getValue20(), stgTable.getInserted_by(), stgTable.getInserted_date()))
						.thenReturn(updated);
		when(fileService.insertInboundStgError(stgTable)).thenReturn(updated);

		int currentUpdated = fileService.insertInboundStgError(stgTable);
		assertEquals(updated, currentUpdated);

	}
}
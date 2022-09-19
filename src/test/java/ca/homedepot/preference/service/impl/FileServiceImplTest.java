package ca.homedepot.preference.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

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

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	FileServiceImpl fileService;

	@BeforeEach
	public void setUp()
	{
		jdbcTemplate = Mockito.mock(JdbcTemplate.class);
		fileService = new FileServiceImpl();
		fileService.setJdbcTemplate(jdbcTemplate);
	}

	@Test
	void testGetJdbcTemplate(){
		assertNotNull(fileService.getJdbcTemplate());
	}

	@Test
	void insert()
	{
		String status = "G";
		BigDecimal sourceId = new BigDecimal("12345678"), job_id = new BigDecimal("1234567890");
		Date startTime = new Date(), insertedDate = new Date();
		String file_name = "fileName", inserted_by = "test";
		int value = 1;

		when(jdbcTemplate.update(anyString(), eq(file_name), eq(status), eq(sourceId), eq(startTime), eq(job_id), eq(insertedDate),
				eq(inserted_by))).thenReturn(value);
		when(fileService.insert(file_name, status, sourceId, startTime, job_id, insertedDate, inserted_by)).thenReturn(value);

		int result = fileService.insert(file_name, status, sourceId, startTime, job_id, insertedDate, inserted_by);

		assertEquals(value, result);

	}

	@Test
	void getJobId()
	{
		String job_name = "registrationInbound";
		BigDecimal jobId = new BigDecimal("1234567890");

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
		String keyVal = "SOURCE",  valueVal = "hybris";
		BigDecimal masterId = new BigDecimal("123456");

		when(jdbcTemplate.queryForObject( anyString(), eq(new Object[]{ keyVal, valueVal }),  any(RowMapper.class) )).thenReturn(masterId);
		when(fileService.getSourceId(keyVal, valueVal)).thenReturn(masterId);
		BigDecimal currentMasterId = fileService.getSourceId(keyVal, valueVal);

		assertEquals(masterId, currentMasterId);
	}

	@Test
	void updateFileStatus()
	{
		String fileName = "TEST_FILE",  status = "S",  newStatus = "P";
		Date updatedDate = new Date();
		int rowAffected = 1;

		when(jdbcTemplate.update( anyString(), eq(newStatus), eq(updatedDate), eq(fileName), eq(status) )).thenReturn(rowAffected);

		int currentRowAffected = fileService.updateFileStatus(fileName, updatedDate, status, newStatus);

		assertEquals(rowAffected, currentRowAffected);
	}
}
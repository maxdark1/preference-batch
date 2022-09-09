package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileServiceImplTest {

    @Mock
    FileService fileService;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp(){
        fileService = Mockito.mock(FileServiceImpl.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        ReflectionTestUtils.setField(fileService, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void insert() {
        String status = "G";
        BigDecimal sourceId = new BigDecimal("12345678"), job_id = new BigDecimal("1234567890");
        Date startTime = new Date(), insertedDate = new Date();
        String file_name = "fileName", inserted_by = "test";
        int value = 1;

        when(jdbcTemplate.update(anyString(), eq(file_name), eq(status), eq(sourceId), eq(startTime),
                eq(job_id), eq(insertedDate), eq(inserted_by))).thenReturn(value);
        when(fileService.insert(file_name, status, sourceId, startTime, job_id, insertedDate, inserted_by)).thenReturn(value);

        int result = fileService.insert(file_name, status, sourceId, startTime, job_id, insertedDate, inserted_by);

        assertEquals(value, result);

    }

    @Test
    void getJobId() {
        String job_name="registrationInbound";
        BigDecimal jobId = new BigDecimal("1234567890");

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(jobId);
        when(fileService.getJobId(job_name)).thenReturn(jobId);

        BigDecimal resultJobId = fileService.getJobId(job_name);
        assertEquals(jobId, resultJobId);
    }

    @Test
    void getFile() {
        String file_name = "fileNameTest";
        BigDecimal jobId = new BigDecimal("1234567890"), fileId = new BigDecimal("2468101214");

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(fileId);
        when(fileService.getFile(file_name, jobId)).thenReturn(fileId);

        BigDecimal resultFileID = fileService.getFile(file_name, jobId);
        assertEquals(fileId, resultFileID);
    }

    @Test
    void getLasFile() {
       BigDecimal fileId = new BigDecimal("2468101214");


       when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class))).thenReturn(fileId);
       when(fileService.getLasFile()).thenReturn(fileId);

        BigDecimal resultFileID = fileService.getLasFile();
        verify(fileService).getLasFile();

        assertEquals(fileId, resultFileID);

    }
}
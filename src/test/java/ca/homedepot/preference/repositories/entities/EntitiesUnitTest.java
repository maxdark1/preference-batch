package ca.homedepot.preference.repositories.entities;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntitiesUnitTest
{

	FileEntity fileEntity;

	JobEntity jobEntity;

	@BeforeEach
	void setUp()
	{
		createJobEntity();
		createFileEntity();
	}

	private void createJobEntity()
	{
		jobEntity = new JobEntity();
		jobEntity.setJobId(new BigDecimal("1"));
		jobEntity.setJobName("job name");
		jobEntity.setInsertedBy("TEST");
		jobEntity.setStartTime(new Date());
		jobEntity.setInsertedDate(new Date());
		jobEntity.setEndTime(new Date());
	}

	void createFileEntity()
	{
		fileEntity = new FileEntity();
		fileEntity.setFileId(1L);
		fileEntity.setFileName("TEST");
		fileEntity.setJob(jobEntity);
		fileEntity.setStartTime(new Date());
		fileEntity.setEndTime(new Date());
		fileEntity.setInsertedBy("TEST");
		fileEntity.setInsertedDate(new Date());
	}

	@Test
	void testJobEntity()
	{
		assertNotNull(jobEntity);
		assertEquals(new BigDecimal("1"), jobEntity.getJobId());
		assertEquals("job name", jobEntity.getJobName());
		assertNull(jobEntity.getUpdatedBy());
		assertNull(jobEntity.getUpdatedDate());
		assertNotNull(jobEntity.getStartTime());
		assertNotNull(jobEntity.getEndTime());
		assertNotNull(jobEntity.getInsertedDate());
		assertEquals("TEST", jobEntity.getInsertedBy());
	}

	@Test
	void testFileEntity()
	{

		assertNotNull(fileEntity);
		assertEquals(Long.valueOf(1), fileEntity.getFileId());
		assertEquals("TEST", fileEntity.getFileName());
		assertEquals(jobEntity, fileEntity.getJob());
		assertNotNull(fileEntity.getStartTime());
		assertNotNull(fileEntity.getInsertedDate());
		assertNotNull(fileEntity.getEndTime());
		assertEquals("TEST", fileEntity.getInsertedBy());
		assertNull(fileEntity.getUpdatedBy());
		assertNull(fileEntity.getUpdatedDate());
	}
}
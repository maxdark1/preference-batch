package ca.homedepot.preference.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public interface FileService
{

	JdbcTemplate getJdbcTemplate();

	void setJdbcTemplate(JdbcTemplate jdbcTemplate);

	int insert(String file_name, String status, BigDecimal source_id, Date start_time, BigDecimal job_id, Date inserted_date,
			String inserted_by, BigDecimal status_id, Date endTime);

	BigDecimal getJobId(String job_name);

	BigDecimal getFile(String fileRegistration, BigDecimal job_id);

	BigDecimal getLasFile();

	/*
	 * Table: pcam.hdpc_master
	 */
	BigDecimal getSourceId(String keyVal, String valueVal);

	int updateFileStatus(String fileName, Date updatedDate, String status, String newStatus);

    int updateInboundStgTableStatus(BigDecimal fileId, String status);

	int updateFileEndTime(BigDecimal fileId, Date updatedDate, String updatedBy ,Date endTime);
}

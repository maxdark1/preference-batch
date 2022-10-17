package ca.homedepot.preference.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public interface FileService
{
	/**
	 * Gets JDBC template
	 * 
	 * @return
	 */
	JdbcTemplate getJdbcTemplate();

	/**
	 * Sets JDBC template
	 * 
	 * @param jdbcTemplate
	 */
	void setJdbcTemplate(JdbcTemplate jdbcTemplate);

	/**
	 * Inserts a record on staging table on persistence
	 * 
	 * @param file_name
	 * @param status
	 * @param source_id
	 * @param start_time
	 * @param job_id
	 * @param inserted_date
	 * @param inserted_by
	 * @param status_id
	 * @param endTime
	 * @return how many records have been inserted
	 */
	int insert(String file_name, String status, BigDecimal source_id, Date start_time, BigDecimal job_id, Date inserted_date,
			String inserted_by, BigDecimal status_id, Date endTime);

	/**
	 * Gets job id
	 * 
	 * @param job_name
	 * @return Job id
	 */
	BigDecimal getJobId(String job_name);

	/**
	 * Gets file id
	 * 
	 * @param fileRegistration
	 * @param job_id
	 * @return file id
	 */
	BigDecimal getFile(String fileRegistration, BigDecimal job_id);

	/**
	 * Gets last file inserted id
	 * 
	 * @return file id
	 */
	BigDecimal getLasFile();

	/**
	 * Gets source id from master Table: pcam.hdpc_master
	 */
	BigDecimal getSourceId(String keyVal, String valueVal);

	/**
	 * Updates file's status
	 * 
	 * @param fileName
	 * @param updatedDate
	 * @param status
	 * @param newStatus
	 * @param jobId
	 * @param endTime
	 * @param updatedBy
	 * @param statusId
	 * @return records updated
	 */
	int updateFileStatus(String fileName, Date updatedDate, String status, String newStatus, BigDecimal jobId, Date endTime,
			String updatedBy, BigDecimal statusId);

	/**
	 * Updates file inbound staging's status
	 * 
	 * @param sequenceNbr
	 * @param status
	 * @param oldStatus
	 * @return records updated
	 */
	int updateInboundStgTableStatus(BigDecimal sequenceNbr, String status, String oldStatus);

	/**
	 * Updates specific file's end time
	 * 
	 * @param fileId
	 * @param updatedDate
	 * @param updatedBy
	 * @param endTime
	 * @param status
	 * @return records updated
	 */
	int updateFileEndTime(BigDecimal fileId, Date updatedDate, String updatedBy, Date endTime, Master status);

	/**
	 * Gets files to move to PROCESSED folder
	 * 
	 * @return files to move
	 */
	List<FileDTO> getFilesToMove();

	/**
	 * Inserts failed records on persistence
	 * 
	 * @param fileInboundStgTable
	 * @return inserted records
	 */
	int insertInboundStgError(FileInboundStgTable fileInboundStgTable);
}

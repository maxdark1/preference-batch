package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
	 * @param file
	 * @return how many records have been inserted
	 */
	int insert(FileDTO file);
	int insertOldId(FileDTO file);

	/**
	 * Gets job id
	 * 
	 * @param jobName
	 *           job name of the job that we are looking for
	 * @param statusId
	 *           status from the job we are looking for
	 * @return Job id
	 */
	BigDecimal getJobId(String jobName, BigDecimal statusId);

	/**
	 * Gets file id
	 * 
	 * @param fileRegistration
	 * @param jobId
	 * @return file id
	 */
	BigDecimal getFile(String fileRegistration, BigDecimal jobId);

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

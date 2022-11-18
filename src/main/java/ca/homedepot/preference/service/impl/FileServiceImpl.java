package ca.homedepot.preference.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.service.FileService;

import static ca.homedepot.preference.constants.PreferenceBatchConstants.FILE_ID;


@Service
public class FileServiceImpl implements FileService
{
	/**
	 * The JDBC template
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Gets JDBC template
	 *
	 * @return JDBC template
	 */
	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	/**
	 * Sets JDBC template
	 *
	 * @param jdbcTemplate
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Inserts file on persistence
	 *
	 * @param file
	 * @return inserted records
	 */
	@Override
	@Transactional
	public int insert(FileDTO file)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_FILE, file.getFileName(), file.getJob(),
				file.getSourceType(), file.getStatus(), file.getStartTime(), file.getInsertedBy(), file.getInsertedDate(),
				file.getStatusId(), file.getEndTime());
	}

	/**
	 * Gets job id
	 *
	 * @param jobName
	 * @return job id
	 */
	public BigDecimal getJobId(String jobName)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_JOB_W_NAME,
				(rs, rowNum) -> rs.getBigDecimal("job_id"), jobName);
	}
	/**
	 * Gets last inserted file according to file name and job id
	 *
	 * @param fileName
	 * @param jobId
	 * @return file id
	 */
	@Override
	public BigDecimal getFile(String fileName, BigDecimal jobId)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE_INSERT, new Object[]
		{ fileName, jobId }, (rs, rowNum) -> rs.getBigDecimal(FILE_ID));
	}

	/**
	 * Gets last file
	 *
	 * @return file id
	 */
	@Override
	public BigDecimal getLasFile()
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE, (rs, rowNum) -> rs.getBigDecimal(FILE_ID));
	}

	/**
	 * Gets source id from master
	 *
	 * @param keyVal
	 * @param valueVal
	 * @return Master id
	 */
	@Override
	public BigDecimal getSourceId(String keyVal, String valueVal)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_MASTER_ID, new Object[]
		{ keyVal, valueVal }, (rs, rowNum) -> rs.getBigDecimal("master_id"));
	}


	/**
	 * Updates file inbound staging's status
	 *
	 * @param sequenceNbr
	 * @param status
	 * @param oldStatus
	 * @return records updated
	 */
	@Override
	public int updateInboundStgTableStatus(BigDecimal sequenceNbr, String status, String oldStatus)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STATUS_INBOUND, status, new Date(), "BATCH", oldStatus,
				sequenceNbr, sequenceNbr);
	}

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
	@Override
	public int updateFileEndTime(BigDecimal fileId, Date updatedDate, String updatedBy, Date endTime, Master status)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_ENDTIME_FILE, endTime, updatedDate, updatedBy,
				status.getValueVal(), status.getMasterId(), fileId);
	}

	/**
	 * Gets files to move to PROCESSED folder
	 *
	 * @return files to move
	 */
	@Override
	public List<FileDTO> getFilesToMove()
	{
		return jdbcTemplate.query(SqlQueriesConstants.SQL_GET_FILES_TO_MOVE, (rs, numRow) -> {
			FileDTO fileDTO = new FileDTO();
			fileDTO.setFileId(rs.getBigDecimal(FILE_ID));
			fileDTO.setFileName(rs.getString("file_name"));
			fileDTO.setSourceType(rs.getBigDecimal("source_type"));
			return fileDTO;
		});
	}

	/**
	 * Inserts failed records on persistence
	 *
	 * @param stgTable
	 * @return inserted records
	 */
	@Override
	public int insertInboundStgError(FileInboundStgTable stgTable)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_ERROR, stgTable.getFileId(),
				stgTable.getStatus(), stgTable.getSourceId(), stgTable.getSrcPhoneNumber(), stgTable.getSrcFirstName(),
				stgTable.getSrcLastName(), stgTable.getSrcAddress1(), stgTable.getSrcAddress2(), stgTable.getSrcCity(),
				stgTable.getSrcState(), stgTable.getSrcPostalCode(), stgTable.getSrcLanguagePref(), stgTable.getSrcEmailAddress(),
				stgTable.getSrcTitleName(), stgTable.getPhonePref(), stgTable.getEmailAddressPref(), stgTable.getMailAddressPref(),
				stgTable.getSrcDate(), stgTable.getEmailStatus(), stgTable.getSrcPhoneExtension(), stgTable.getEmailPrefHdCa(),
				stgTable.getEmailPrefGardenClub(), stgTable.getEmailPrefPro(), stgTable.getEmailPrefNewMover(),
				stgTable.getCellSmsFlag(), stgTable.getBusinessName(), stgTable.getCustomerNbr(), stgTable.getOrgName(),
				stgTable.getStoreNbr(), stgTable.getCustTypeCd(), stgTable.getContent1(), stgTable.getValue1(),
				stgTable.getContent2(), stgTable.getValue2(), stgTable.getContent3(), stgTable.getValue3(), stgTable.getContent4(),
				stgTable.getValue4(), stgTable.getContent5(), stgTable.getValue5(), stgTable.getContent6(), stgTable.getValue6(),
				stgTable.getContent7(), stgTable.getValue7(), stgTable.getContent8(), stgTable.getValue8(), stgTable.getContent9(),
				stgTable.getValue9(), stgTable.getContent10(), stgTable.getValue10(), stgTable.getContent11(), stgTable.getValue11(),
				stgTable.getContent12(), stgTable.getValue12(), stgTable.getContent13(), stgTable.getValue13(),
				stgTable.getContent14(), stgTable.getValue14(), stgTable.getContent15(), stgTable.getValue15(),
				stgTable.getContent16(), stgTable.getValue16(), stgTable.getContent17(), stgTable.getValue17(),
				stgTable.getContent18(), stgTable.getValue18(), stgTable.getContent19(), stgTable.getValue19(),
				stgTable.getContent20(), stgTable.getValue20(), stgTable.getInsertedBy(), stgTable.getInsertedDate());
	}


}

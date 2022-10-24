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
	 * @param file_name
	 * @param status
	 * @param source_id
	 * @param start_time
	 * @param job_id
	 * @param inserted_date
	 * @param inserted_by
	 * @param status_id
	 * @param endTime
	 * @return inserted records
	 */
	@Override
	@Transactional
	public int insert(String file_name, String status, BigDecimal source_id, Date start_time, BigDecimal job_id,
			Date inserted_date, String inserted_by, BigDecimal status_id, Date endTime)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_FILE, file_name, job_id, source_id, status, start_time,
				inserted_by, inserted_date, status_id, endTime);
	}

	/**
	 * Gets job id
	 *
	 * @param job_name
	 * @return job id
	 */
	public BigDecimal getJobId(String job_name)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_JOB_W_NAME,
				(rs, RowNum) -> rs.getBigDecimal("job_id"), job_name);
	}

	/**
	 * Gets last inserted file according to file name and job id
	 *
	 * @param file_name
	 * @param job_id
	 * @return file id
	 */
	@Override
	public BigDecimal getFile(String file_name, BigDecimal job_id)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE_INSERT, new Object[]
		{ file_name, job_id }, (rs, RowNum) -> rs.getBigDecimal(FILE_ID));
	}

	/**
	 * Gets last file
	 *
	 * @return file id
	 */
	@Override
	public BigDecimal getLasFile()
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE, (rs, RowNum) -> rs.getBigDecimal(FILE_ID));
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
		{ keyVal, valueVal }, (rs, RowNum) -> rs.getBigDecimal("master_id"));
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
				status.getValue_val(), status.getMaster_id(), fileId);
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
			fileDTO.setFile_id(rs.getBigDecimal(FILE_ID));
			fileDTO.setFile_name(rs.getString("file_name"));
			fileDTO.setFile_source_id(rs.getBigDecimal("source_type"));
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
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_FILE_INBOUND_STG_ERROR, stgTable.getFile_id(),
				stgTable.getStatus(), stgTable.getSource_id(), stgTable.getSrc_phone_number(), stgTable.getSrc_first_name(),
				stgTable.getSrc_last_name(), stgTable.getSrc_address1(), stgTable.getSrc_address2(), stgTable.getSrc_city(),
				stgTable.getSrc_state(), stgTable.getSrc_postal_code(), stgTable.getSrc_language_pref(),
				stgTable.getSrc_email_address(), stgTable.getSrc_title_name(), stgTable.getPhone_pref(),
				stgTable.getEmail_address_pref(), stgTable.getMail_address_pref(), stgTable.getSrc_date(), stgTable.getEmail_status(),
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
				stgTable.getContent20(), stgTable.getValue20(), stgTable.getInserted_by(), stgTable.getInserted_date());
	}


}

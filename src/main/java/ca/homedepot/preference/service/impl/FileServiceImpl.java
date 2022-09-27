package ca.homedepot.preference.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.service.FileService;


@Service
public class FileServiceImpl implements FileService
{

	private JdbcTemplate jdbcTemplate;


	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public int insert(String file_name, String status, BigDecimal source_id, Date start_time, BigDecimal job_id,
			Date inserted_date, String inserted_by, BigDecimal status_id)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_FILE, file_name, job_id, source_id, status, start_time,
				inserted_by, inserted_date, status_id);
	}


	public BigDecimal getJobId(String job_name)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_JOB_W_NAME,
				(rs, RowNum) -> rs.getBigDecimal("job_id"), job_name);
	}

	@Override
	public BigDecimal getFile(String file_name, BigDecimal job_id)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE_INSERT, new Object[]
		{ file_name, job_id }, (rs, RowNum) -> rs.getBigDecimal("file_id"));
	}

	@Override
	public BigDecimal getLasFile()
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE, (rs, RowNum) -> rs.getBigDecimal("file_id"));
	}

	@Override
	public BigDecimal getSourceId(String keyVal, String valueVal)
	{
		return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_MASTER_ID, new Object[]
		{ keyVal, valueVal }, (rs, RowNum) -> rs.getBigDecimal("master_id"));
	}

	@Override
	public int updateFileStatus(String fileName, Date updatedDate, String status, String newStatus)
	{
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STAUTS_FILE, newStatus, updatedDate, fileName, status);
	}


	@Override
	public int updateInboundStgTableStatus(BigDecimal fileId, String status){
		return jdbcTemplate.update(SqlQueriesConstants.SQL_UPDATE_STATUS_INBOUND, status, new Date(), "BATCH",fileId);
	}


}

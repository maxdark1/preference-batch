package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.repositories.FileRepo;
import ca.homedepot.preference.repositories.entities.FileEntity;
import ca.homedepot.preference.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;


@Service
public class FileServiceImpl implements FileService {

    private JdbcTemplate jdbcTemplate;

    private FileRepo fileRepo;


    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
    @Autowired
    private void setFileRepo(FileRepo fileRepo){
        this.fileRepo = fileRepo;
    }

    @Override
    public FileEntity saveFile(FileDTO file) {

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getFile_name());
        fileEntity.setStatus(file.getStatus());
        fileEntity.setFileSourceId(file.getFile_source_id());
        fileEntity.setStartTime(file.getStart_time());
        fileEntity.setJob(fileEntity.getJob());
        fileEntity.setInsertedDate(file.getInserted_date());
        fileEntity.setInsertedBy(file.getInserted_by());
       fileRepo.insert(file);
       // fileRepo.save(fileEntity);
        return fileEntity;
    }

    @Override
    @Transactional
    public int insert(String file_name, String status, BigDecimal source_id, Date start_time, BigDecimal job_id, Date inserted_date, String inserted_by){
        return jdbcTemplate.update(SqlQueriesConstants.SQL_INSERT_HDPC_FILE,
                        file_name,job_id, source_id, status,  start_time,
                        inserted_by, inserted_date );
    }


    public BigDecimal getJobId(String job_name){
        String sql = SqlQueriesConstants.SQL_SELECT_LAST_JOB_W_NAME.replace("%param%", "'"+job_name+ "'");
        return jdbcTemplate.queryForObject(sql, (rs, RowNum)-> rs.getBigDecimal("job_id"));
    }

    @Override
    public BigDecimal getFile(String file_name, BigDecimal job_id) {
        return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE_INSERT,
                new Object[]{file_name, job_id}, (rs, RowNum) -> rs.getBigDecimal("file_id"));
    }

    @Override
    public BigDecimal getLasFile() {
        return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_LAST_FILE,
                (rs, RowNum) -> rs.getBigDecimal("file_id"));
    }

    @Override
    public BigDecimal getSourceId(String keyVal, String valueVal) {
        return jdbcTemplate.queryForObject(SqlQueriesConstants.SQL_SELECT_MASTER_ID,
                new Object[]{keyVal, valueVal},
                (rs, RowNum)-> rs.getBigDecimal("master_id"));
    }


}

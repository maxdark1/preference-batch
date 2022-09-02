package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.repositories.entities.FileEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;


@Service
public interface FileService {

    FileEntity saveFile(FileDTO file);

    int insert(String file_name, Boolean status, BigDecimal source_id, Date start_time, BigDecimal job_id, Date inserted_date, String inserted_by);

    BigDecimal getJobId(String job_name);

    BigDecimal getLastFile(String fileRegistration, BigDecimal job_id);
}

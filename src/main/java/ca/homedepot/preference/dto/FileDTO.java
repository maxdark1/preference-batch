package ca.homedepot.preference.dto;

import ca.homedepot.preference.repositories.entities.JobEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FileDTO {
    private Long file_id;
    private String file_name;
    private BigDecimal job;
    private BigDecimal file_source_id;
    private Boolean status;
    private Date start_time;
    private Date end_time;
    private String inserted_by;
    private Date inserted_date;
    private String updated_by;
    private Date updated_date;

}

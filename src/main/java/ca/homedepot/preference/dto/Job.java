package ca.homedepot.preference.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Job {

    private BigDecimal job_id;

    /**
     * The article id.
     */
    private String job_name;

    /**
     * The email Id.
     */
    private Boolean status;

    /**
     * The Phone number.
     */
    private Date start_time;

    /**
     * The notification type.
     */
    private Date end_time;

    private String inserted_by;

    private Date inserted_date;

    private String updated_by;

    private Date updated_date;
}

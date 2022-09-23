package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class Job
{

	private BigDecimal job_id;

	/**
	 * The Job Name
	 */
	private String job_name;

	/**
	 * The status
	 */
	private String status;

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

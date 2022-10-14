package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class Job
{
	/**
	 * The job_id
	 */
	private BigDecimal job_id;



	/**
	 * The Job Name
	 */
	private String job_name;


	/**
	 * The status id
	 */

	private BigDecimal status_id;

	/**
	 * The status
	 */
	private String status;

	/**
	 * The start time.
	 */
	private Date start_time;

	/**
	 * The end time.
	 */
	private Date end_time;

	/**
	 * The inserted by
	 */

	private String inserted_by;

	/**
	 * The inserted date
	 */
	private Date inserted_date;
	/**
	 * The updated by
	 */
	private String updated_by;
	/**
	 * The updated date
	 */
	private Date updated_date;
}

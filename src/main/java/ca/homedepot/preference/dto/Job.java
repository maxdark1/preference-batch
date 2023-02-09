package ca.homedepot.preference.dto;

import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Generated
public class Job
{
	/**
	 * The job_id
	 */
	private BigDecimal jobId;



	/**
	 * The Job Name
	 */
	private String jobName;


	/**
	 * The status id
	 */

	private BigDecimal statusId;

	/**
	 * The status
	 */
	private String status;

	/**
	 * The start time.
	 */
	private Date startTime;

	/**
	 * The end time.
	 */
	private Date endTime;

	/**
	 * The inserted by
	 */

	private String insertedBy;

	/**
	 * The inserted date
	 */
	private Date insertedDate;
	/**
	 * The updated by
	 */
	private String updatedBy;
	/**
	 * The updated date
	 */
	private Date updatedDate;
}

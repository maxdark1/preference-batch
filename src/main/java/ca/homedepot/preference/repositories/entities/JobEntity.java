package ca.homedepot.preference.repositories.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "hdpc_job")
public class JobEntity
{
	/**
	 * The job id.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private BigDecimal jobId;

	/**
	 * The job name
	 */
	@Column(name = "job_name")
	private String jobName;


	/**
	 * The start time
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * The end time.
	 */
	@Column(name = "end_time")
	private Date endTime;
	/**
	 * The inserted by
	 */
	@Column(name = "inserted_by")
	private String insertedBy;
	/**
	 * The inserted date
	 */
	@Column(name = "inserted_date")
	private Date insertedDate;
	/**
	 * The updated by
	 */
	@Column(name = "updated_by")
	private String updatedBy;
	/**
	 * The updated date
	 */
	@Column(name = "updated_date")
	private Date updatedDate;
}
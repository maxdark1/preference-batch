package ca.homedepot.preference.repositories.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "hdpc_job")
public class JobEntity
{

	@Id
	@Column(name = "job_id")
	private Long jobId;

	/**
	 * The article id.
	 */
	@Column(name = "job_name")
	private String jobName;

	/**
	 * The email Id.
	 */
	@Column(name = "status")
	private Boolean status;

	/**
	 * The Phone number.
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * The notification type.
	 */
	@Column(name = "end_time")
	private Date endTime;
}
package ca.homedepot.preference.repositories.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "hdpc_job")
public class JobEntity
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private BigDecimal jobId;

	/**
	 * The article id.
	 */
	@Column(name = "job_name")
	private String jobName;

	/**
	 * The email Id.
	 */
	@Column(name = "status")
	private String status;

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

	@Column(name = "inserted_by")
	private String insertedBy;

	@Column(name = "inserted_date")
	private Date inserted_date;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
}
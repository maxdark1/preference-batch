package ca.homedepot.preference.repositories.entities;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hdpc_file")
public class FileEntity
{
	/**
	 * The file id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long fileId;

	/**
	 * The file name.
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * The job
	 */
	@ManyToOne
	@JoinColumn(name = "job_id")
	private JobEntity job;

	/**
	 * The start time.
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * The end tiem.
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
	 * The updated date.
	 */
	@Column(name = "updated_date")
	private Date updatedDate;
}

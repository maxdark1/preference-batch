package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDTO
{
	/**
	 * The file_id
	 */
	private BigDecimal file_id;
	/**
	 * The file_name
	 */
	private String file_name;
	/**
	 * The job
	 */
	private BigDecimal job;
	/**
	 * The file_source_id
	 */
	private BigDecimal file_source_id;
	/**
	 * The status
	 */
	private String status;
	/**
	 * The start_time
	 */
	private Date start_time;
	/**
	 * The end_time
	 */
	private Date end_time;
	/**
	 * The inserted_by
	 */
	private String inserted_by;
	/**
	 * The inserted_date
	 */
	private Date inserted_date;
	/**
	 * The updated_by
	 */
	private String updated_by;
	/**
	 * The updated_date
	 */
	private Date updated_date;

	/**
	 * Constructor for FileDTO
	 * 
	 * @param file_name
	 */
	public FileDTO(String file_name)
	{
		this.file_name = file_name;
	}
}

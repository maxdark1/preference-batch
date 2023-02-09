package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO
{
	/**
	 * The file_id
	 */
	private BigDecimal fileId;
	/**
	 * The file_name
	 */
	private String fileName;
	/**
	 * The job
	 */
	private BigDecimal job;
	/**
	 * The file_source_id
	 */
	private BigDecimal sourceType;
	/**
	 * The status
	 */
	private String status;

	/**
	 * The status ID
	 */
	private BigDecimal statusId;
	/**
	 * The start_time
	 */
	private Date startTime;
	/**
	 * The end_time
	 */
	private Date endTime;
	/**
	 * The inserted_by
	 */
	private String insertedBy;
	/**
	 * The inserted_date
	 */
	private Date insertedDate;
	/**
	 * The updated_by
	 */
	private String updatedBy;
	/**
	 * The updated_date
	 */
	private Date updatedDate;

	/**
	 * Constructor for FileDTO
	 * 
	 * @param fileName
	 */
	public FileDTO(String fileName)
	{
		this.fileName = fileName;

	}
}

package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class FileDTO
{
	private Long file_id;
	private String file_name;
	private BigDecimal job;
	private BigDecimal file_source_id;
	private String status;
	private Date start_time;
	private Date end_time;
	private String inserted_by;
	private Date inserted_date;
	private String updated_by;
	private Date updated_date;

}

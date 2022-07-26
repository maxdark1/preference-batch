package ca.homedepot.preference.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class EmailAnalytics
{

	private String regId;
	private String articleId;
	private Integer inventory;
	private String emailId;
	private String emailType;
	private Timestamp createdts;

}

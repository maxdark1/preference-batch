package ca.homedepot.preference.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Registration
{

	private String regId;
	private String articleId;
	private String actionType;
	private String emailId;
	private Timestamp createdts;

}

package ca.homedepot.preference.dto.enums;

public enum JobStatusEnum
{
	IN_PROGRESS("IN PROGRESS"), COMPLETED("COMPLETED"), STARTED("STARTED"), ERROR("ERROR");

	private String status;

	JobStatusEnum(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return status;
	}

}

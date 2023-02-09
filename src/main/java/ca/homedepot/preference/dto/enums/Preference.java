package ca.homedepot.preference.dto.enums;

import lombok.Generated;

@Generated
public enum Preference
{
	NUMBER_1("1"),

	NUMBER_0("0"),

	NUMBER_MINUS_1("-1");

	private String value;

	Preference(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}

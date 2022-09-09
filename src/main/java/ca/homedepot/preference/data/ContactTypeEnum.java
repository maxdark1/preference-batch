package ca.homedepot.preference.data;

/**
 * 
 * type of email Enum
 *
 */
public enum ContactTypeEnum
{
	/**
	 * Contact type email.
	 */
	EMAIL("1"),
	/**
	 * Contact type direct mail.
	 */
	MAIL("2"),
	/**
	 * Contact Type Phone
	 */
	PHONE("3"),
	/**
	 * Contact Type Mobile.
	 */
	MOBILE("4");

	/**
	 * The Value.
	 */
	private String value;

	ContactTypeEnum(String value)
	{
		this.value = value;
	}

	/**
	 * From value email type enum.
	 *
	 * @param value
	 *           the value
	 * @return the email type enum
	 */
	public static ContactTypeEnum fromValue(String value)
	{
		for (ContactTypeEnum b : ContactTypeEnum.values())
		{
			if (b.value.equals(value))
			{
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}

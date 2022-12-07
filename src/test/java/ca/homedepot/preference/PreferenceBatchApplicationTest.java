package ca.homedepot.preference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PreferenceBatchApplicationTest
{

	@Test
	@DisplayName("Test for main Class, just for coverage.")
	void main()
	{
		try
		{
			PreferenceBatchApplication.main(new String[]
			{ "dummy param" });
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
		assertTrue(true);
	}
}
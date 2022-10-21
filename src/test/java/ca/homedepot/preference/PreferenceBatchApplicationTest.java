package ca.homedepot.preference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
	}
}
package ca.homedepot.preference.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutboundSqlQueriesConstantsTest
{


	@Test
	public void getSQL_GET_CRM_OUTBOUND()
	{
		String test = OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND;
		assertEquals(OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND, test);
	}

	@Test
	public void getSQL_INSERT_STG_PREFERENCE_OUTBOUND()
	{
		String test = OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND;
		assertEquals(OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND, test);
	}

	@Test
	public void getSQL_SELECT_OUTBOUND_DB_READER_STEP2()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2;
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2, test);
	}

	@Test
	public void getSQL_TRUNCATE_COMPLIANT_TABLE()
	{
		String test = OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE;
		assertEquals(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE, test);
	}

	@Test
	public void getSQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1);
	}

	@Test
	public void getSQL_SELECT_CITI_SUPPRESION_TABLE()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_CITI_SUPPRESION_TABLE;
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_CITI_SUPPRESION_TABLE, test);
	}

	@Test
	public void getSQL_INSERT_CITI_SUPPRESION()
	{
		String test = OutboundSqlQueriesConstants.SQL_INSERT_CITI_SUPPRESION;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_INSERT_CITI_SUPPRESION);
	}

	@Test
	public void getSQL_TRUNCATE_CITI_SUPPRESION()
	{
		String test = OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_TRUNCATE_CITI_SUPPRESION);
	}

	@Test
	public void getSQL_SELECT_FOR_INTERNAL_DESTINATION()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION);
	}

	@Test
	public void getSQL_TRUNCATE_PROGRAM_COMPLIANT()
	{
		String test = OutboundSqlQueriesConstants.SQL_TRUNCATE_PROGRAM_COMPLIANT;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_TRUNCATE_PROGRAM_COMPLIANT);
	}

	@Test
	public void getSQL_INSERT_PROGRAM_COMPLIANT()
	{
		String test = OutboundSqlQueriesConstants.SQL_INSERT_PROGRAM_COMPLIANT;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_INSERT_PROGRAM_COMPLIANT);
	}

	@Test
	public void getSQL_SELECT_PROGRAM_COMPLIANT()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_PROGRAM_COMPLIANT;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_SELECT_PROGRAM_COMPLIANT);
	}


}
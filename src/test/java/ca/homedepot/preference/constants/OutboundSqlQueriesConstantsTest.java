package ca.homedepot.preference.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutboundSqlQueriesConstantsTest
{


	@Test
	public void getSQL_GET_CRM_OUTBOUND()
	{
		String test = OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND);
	}

	@Test
	public void getSQL_INSERT_STG_PREFERENCE_OUTBOUND()
	{
		String test = OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND);
	}

	@Test
	public void getSQL_SELECT_OUTBOUND_DB_READER_STEP2()
	{
		String test = OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2);
	}

	@Test
	public void getSQL_TRUNCATE_COMPLIANT_TABLE()
	{
		String test = OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE;
		assertEquals(test, OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
	}

}
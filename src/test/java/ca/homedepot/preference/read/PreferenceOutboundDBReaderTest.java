package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreferenceOutboundDBReaderTest
{
	@Mock
	DataSource dataSource;

	@InjectMocks
	@Spy
	PreferenceOutboundDBReader preferenceOutboundDBReader;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void outboundDBReader()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> test = preferenceOutboundDBReader.outboundDBReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2, test.getSql());
	}

	@Test
	void outboundInternalDBReader()
	{
		JdbcCursorItemReader<InternalOutboundDto> test = preferenceOutboundDBReader.outboundInternalDbReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_PROGRAM_COMPLIANT, test.getSql());
	}

	@Test
	void citiSuppressionDBTableReader()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> test = preferenceOutboundDBReader.citiSuppressionDBTableReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_CITI_SUPPRESION_TABLE, test.getSql());
	}

	@Test
	void loyaltyComplaintDBTableReader()
	{
		JdbcCursorItemReader<LoyaltyCompliantDTO> reader = preferenceOutboundDBReader.loyaltyComplaintDBTableReader();
		assertNotNull(reader);
	}

	@Test
	void salesforceExtractDBTableReader()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> reader = preferenceOutboundDBReader.salesforceExtractDBTableReader();
		assertNotNull(reader);
	}

}
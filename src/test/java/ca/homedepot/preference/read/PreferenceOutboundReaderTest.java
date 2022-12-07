package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;
import ca.homedepot.preference.service.impl.OutboundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreferenceOutboundReaderTest
{

	@Mock
	DataSource dataSource;

	@Mock
	PreferenceOutboundMapper mapper;

	@Mock
	OutboundServiceImpl outboundService;

	@InjectMocks
	@Spy
	private PreferenceOutboundReader preferenceOutboundReader;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getOutboundService()
	{
		assertNotNull(outboundService);
	}

	@Test
	void testOutboundDBReader()
	{
		JdbcCursorItemReader<PreferenceOutboundDto> test = preferenceOutboundReader.outboundDBReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND, test.getSql());
	}

	@Test
	void testOutboundCitiSuppresionDBReader()
	{
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> test = preferenceOutboundReader.outboundCitiSuppresionDBReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1, test.getSql());
	}

	@Test
	void testSalesforceExtractOutboundDBReader()
	{
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> test = preferenceOutboundReader.salesforceExtractOutboundDBReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_GET_EMAIL_PREFERENCES_OUTBOUND, test.getSql());
	}

	@Test
	void testPurgeCitiSuppresionTable()
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeCitiSuppresionTable();

		preferenceOutboundReader.purgeCitiSuppresionTable();
		Mockito.verify(preferenceOutboundReader).purgeCitiSuppresionTable();
	}

	@Test
	void testOutboundInternalDBReader()
	{
		JdbcCursorItemReader<InternalOutboundDto> test = (JdbcCursorItemReader<InternalOutboundDto>) preferenceOutboundReader
				.outboundInternalDBReader();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION, test.getSql());
	}

	@Test
	void testPurgeProgramCompliant()
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeProgramCompliant();

		preferenceOutboundReader.purgeProgramCompliant();
		Mockito.verify(preferenceOutboundReader).purgeProgramCompliant();
	}

	@Test
	void outboundLoyaltyComplaintWeekly()
	{
		JdbcCursorItemReader<InternalOutboundDto> test = preferenceOutboundReader.outboundLoyaltyComplaintWeekly();
		assertEquals(OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION, test.getSql());
	}


}
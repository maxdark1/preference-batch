package ca.homedepot.preference.read;

import ca.homedepot.preference.service.impl.OutboundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;

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
		assertNotNull(preferenceOutboundReader.outboundDBReader());
	}

	@Test
	void testOutboundCitiSuppresionDBReader()
	{
		assertNotNull(preferenceOutboundReader.outboundCitiSuppresionDBReader());
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
		assertNotNull(preferenceOutboundReader.outboundInternalDBReader());
	}

	@Test
	void testPurgeProgramCompliant()
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeProgramCompliant();

		preferenceOutboundReader.purgeProgramCompliant();
		Mockito.verify(preferenceOutboundReader).purgeProgramCompliant();
	}


}
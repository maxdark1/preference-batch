package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;
import ca.homedepot.preference.service.impl.OutboundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.ItemReader;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreferenceOutboundReaderTest
{

	@Mock
	DataSource dataSource;

	@Mock
	PreferenceOutboundMapper mapper;

	@Mock
	OutboundServiceImpl outboundService;

	@Mock
	PagingAttributes pagingAttributes;
	@InjectMocks
	@Spy
	private PreferenceOutboundReader preferenceOutboundReader;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);

		preferenceOutboundReader.setOutboundService(outboundService);
		preferenceOutboundReader.setDataSource(dataSource);
		pagingAttributes = new PagingAttributes();
		preferenceOutboundReader.setPagingAttributes(pagingAttributes);
		pagingAttributes.setPageSizeFlexAttributes(1);
		pagingAttributes.setPageSizeCiti(1);
		pagingAttributes.setPageSizeCRM(1);
		pagingAttributes.setPageSizeSFMC(1);
		pagingAttributes.setPageSizeInternal(1);
		pagingAttributes.setPageSizeLoyalty(1);
		pagingAttributes.setFetchSizeFlexAttributes(1);
		pagingAttributes.setFetchSizeCRM(1);
		pagingAttributes.setFetchSizeCiti(1);
		pagingAttributes.setFetchSizeSFMC(1);
		pagingAttributes.setFetchSizeInternal(1);
		pagingAttributes.setFetchSizeLoyalty(1);
	}

	@Test
	void getOutboundService()
	{
		assertNotNull(outboundService);
	}

	@Test
	void testOutboundDBReader() throws Exception
	{
		ItemReader<PreferenceOutboundDto> test = preferenceOutboundReader.outboundDBReader();
		assertNotNull(test);
	}

	@Test
	void testOutboundCitiSuppresionDBReader() throws Exception
	{
		ItemReader<CitiSuppresionOutboundDTO> test = preferenceOutboundReader.outboundCitiSuppresionDBReader();
		assertNotNull(test);
	}

	@Test
	void testSalesforceExtractOutboundDBReader() throws Exception
	{
		ItemReader<SalesforceExtractOutboundDTO> test = preferenceOutboundReader.salesforceExtractOutboundDBReader();
		assertNotNull(test);
	}

	@Test
	void testPurgeSalesForceExtract() throws Exception
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeSalesforceExtractTable();

		preferenceOutboundReader.purgeSalesforceExtractTable();
		Mockito.verify(preferenceOutboundReader).purgeSalesforceExtractTable();
	}

	@Test
	void testPurgeCitiSuppresionTable() throws Exception
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeCitiSuppresionTable();

		preferenceOutboundReader.purgeCitiSuppresionTable();
		Mockito.verify(preferenceOutboundReader).purgeCitiSuppresionTable();
	}

	@Test
	void testOutboundInternalDBReader() throws Exception
	{
		ItemReader<InternalOutboundDto> test = (ItemReader<InternalOutboundDto>) preferenceOutboundReader
				.outboundInternalDBReader();
		assertNotNull(test);
	}

	@Test
	void testPurgeProgramCompliant() throws Exception
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeProgramCompliant();

		preferenceOutboundReader.purgeProgramCompliant();
		Mockito.verify(preferenceOutboundReader).purgeProgramCompliant();
	}

	@Test
	void purgeLoyaltyComplaint() throws Exception
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeLoyaltyComplaint();

		preferenceOutboundReader.purgeLoyaltyComplaint();
		Mockito.verify(preferenceOutboundReader).purgeLoyaltyComplaint();
	}

	@Test
	void purgeFlexAttributes() throws Exception
	{
		Mockito.doNothing().when(preferenceOutboundReader).purgeFlexAttributes();

		preferenceOutboundReader.purgeFlexAttributes();
		Mockito.verify(preferenceOutboundReader).purgeFlexAttributes();
	}

	@Test
	void outboundLoyaltyComplaintWeekly() throws Exception
	{
		ItemReader<InternalOutboundDto> test = preferenceOutboundReader.outboundLoyaltyComplaintWeekly();
		assertNotNull(test);
	}

	@Test
	void outboundInternalFlexDBReader() throws Exception
	{
		ItemReader<InternalFlexOutboundDTO> test = preferenceOutboundReader.outboundInternalFlexDBReader();
		assertNotNull(test);
	}

	@Test
	void testdailyCountReportStep1() throws Exception
	{
		assertNotNull(preferenceOutboundReader.dailyCountReportStep1());
	}

	@Test
	void testdailyCountReportStep2() throws Exception
	{
		assertNotNull(preferenceOutboundReader.dailyCountReportStep2());
	}

}
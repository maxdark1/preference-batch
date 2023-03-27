package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.batch.item.ItemReader;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreferenceOutboundDBReaderTest
{
	@Mock
	DataSource dataSource;


	PagingAttributes pagingAttributes;

	@InjectMocks
	@Spy
	PreferenceOutboundDBReader preferenceOutboundDBReader;


	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		pagingAttributes = new PagingAttributes();
		preferenceOutboundDBReader.setPagingAttributes(pagingAttributes);
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
	void outboundDBReader() throws Exception
	{
		ItemReader<PreferenceOutboundDto> test = preferenceOutboundDBReader.outboundDBCRMReader();
		assertNotNull(test);
	}

	@Test
	void outboundInternalDBReader() throws Exception
	{
		ItemReader<InternalOutboundDto> test = preferenceOutboundDBReader.outboundInternalDbReader();
		assertNotNull(test);
	}

	@Test
	void citiSuppressionDBTableReader() throws Exception
	{
		ItemReader<CitiSuppresionOutboundDTO> test = preferenceOutboundDBReader.citiSuppressionDBTableReader();
		assertNotNull(test);
	}

	@Test
	void loyaltyComplaintDBTableReader() throws Exception
	{
		ItemReader<LoyaltyCompliantDTO> reader = preferenceOutboundDBReader.loyaltyComplaintDBTableReader();
		assertNotNull(reader);
	}

	@Test
	void salesforceExtractDBTableReader() throws Exception
	{
		ItemReader<SalesforceExtractOutboundDTO> reader = preferenceOutboundDBReader.salesforceExtractDBTableReader();
		assertNotNull(reader);
	}

	@Test
	void outboundInternalFlexDbReader() throws Exception
	{
		assertNotNull(preferenceOutboundDBReader.outboundInternalFlexDbReader());
	}

}
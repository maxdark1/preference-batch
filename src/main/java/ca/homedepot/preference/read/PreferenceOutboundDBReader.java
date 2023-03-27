package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.mapper.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
@Setter
public class PreferenceOutboundDBReader
{
	@Autowired
	private DataSource dataSource;

	@Autowired
	private PagingAttributes pagingAttributes;

	/**
	 * Method used to get the necessary data from DB in Step 2
	 * 
	 * @return
	 */
	public ItemReader<PreferenceOutboundDto> outboundDBCRMReader() throws Exception
	{
		log.info(" Preference Outbound : Preference Outbound Step 2 Reader Starter :" + new Date());
		JdbcPagingItemReader<PreferenceOutboundDto> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeCRM());
		reader.setPageSize(pagingAttributes.getPageSizeCRM());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("customer_nbr", OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_CRM_STEP2));
		reader.setRowMapper(new PreferenceOutboundMapperStep2());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Preference Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	/**
	 * This method is used for get the data from the temporary table
	 *
	 * @return
	 */
	public ItemReader<InternalOutboundDto> outboundInternalDbReader() throws Exception
	{
		log.info(" Preference Outbound : Internal Outbound Step 2 Reader Starter :" + new Date());
		JdbcPagingItemReader<InternalOutboundDto> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeInternal());
		reader.setPageSize(pagingAttributes.getPageSizeInternal());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("email_addr", OutboundSqlQueriesConstants.SQL_SELECT_PROGRAM_COMPLIANT));
		reader.setRowMapper(new InternalOutboundStep2Mapper());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Internal Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public ItemReader<InternalFlexOutboundDTO> outboundInternalFlexDbReader() throws Exception
	{
		log.info(" Preference Outbound : Flex Attributes Internal Outbound Step 2 Reader Starter :" + new Date());
		JdbcPagingItemReader<InternalFlexOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeFlexAttributes());
		reader.setPageSize(pagingAttributes.getPageSizeFlexAttributes());
		reader.setQueryProvider(pagingAttributes.queryProvider("file_id", OutboundSqlQueriesConstants.SQL_SELECT_FLEX_ATTRIBUTES));
		reader.setRowMapper(new InternalFlexOutboundStep2Mapper());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Flex Attributes Internal Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public ItemReader<CitiSuppresionOutboundDTO> citiSuppressionDBTableReader() throws Exception
	{
		log.info(" Preference Outbound : Preference Citi Suppresion Outbound Step 2 Reader Starter :" + new Date());
		JdbcPagingItemReader<CitiSuppresionOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeCiti());
		reader.setPageSize(pagingAttributes.getPageSizeCiti());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("first_name", OutboundSqlQueriesConstants.SQL_SELECT_CITI_SUPPRESION_TABLE));
		reader.setRowMapper(new CitiSuppresionOutboundMapper());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Preference Citi Suppresion Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public ItemReader<LoyaltyCompliantDTO> loyaltyComplaintDBTableReader() throws Exception
	{
		log.info(" Preference Loyalty Complaint Outbound : Preference Loyalty Complaint Outbound Step 2 Reader Starter :"
				+ new Date());
		JdbcPagingItemReader<LoyaltyCompliantDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeLoyalty());
		reader.setPageSize(pagingAttributes.getPageSizeLoyalty());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("email_addr", OutboundSqlQueriesConstants.SQL_SELECT_LOYALTY_COMPLAINT));
		reader.setRowMapper(new LoyaltyComplaintWeeklyMapper());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Preference Loyalty Complaint Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public ItemReader<SalesforceExtractOutboundDTO> salesforceExtractDBTableReader() throws Exception
	{
		log.info(" Preference Outbound : Preference Salesforce Extract Outbound Step 2 Reader Starter :" + new Date());
		JdbcPagingItemReader<SalesforceExtractOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeSFMC());
		reader.setPageSize(pagingAttributes.getPageSizeSFMC());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("email_address", OutboundSqlQueriesConstants.SQL_SELECT_SALESFORCE_EXTRACT_TABLE));
		reader.setRowMapper(new SalesforceExtractOutboundMapper());
		reader.afterPropertiesSet();

		log.info(" Preference Outbound : Salesforce Extract Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

}

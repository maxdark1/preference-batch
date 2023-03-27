package ca.homedepot.preference.read;


import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.constants.ReportsQueries;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.mapper.*;
import ca.homedepot.preference.service.OutboundService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
@Setter
public class PreferenceOutboundReader
{
	@Autowired
	private DataSource dataSource;

	@Autowired
	private OutboundService outboundService;

	@Autowired
	private PagingAttributes pagingAttributes;

	/**
	 * This method is used to read the necessary data from DB
	 * 
	 * @return
	 */
	public JdbcPagingItemReader<PreferenceOutboundDto> outboundDBReader() throws Exception
	{
		truncateTable();
		log.info(" Preference Outbound : Preference Outbound Reader Starter :" + new Date());
		JdbcPagingItemReader<PreferenceOutboundDto> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeCRM());
		reader.setPageSize(pagingAttributes.getPageSizeCRM());
		reader.setQueryProvider(pagingAttributes.queryProvider("customer_id", OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND));
		reader.setRowMapper(new PreferenceOutboundMapper());
		reader.afterPropertiesSet();
		log.info(" Preference Outbound : Preference Outbound Reader End :" + new Date());
		return reader;
	}

	public JdbcPagingItemReader<CitiSuppresionOutboundDTO> outboundCitiSuppresionDBReader() throws Exception
	{
		purgeCitiSuppresionTable();
		log.info(" Preference Outbound : Preference Outbound Reader for Citi Suppresion Started at:" + new Date());
		JdbcPagingItemReader<CitiSuppresionOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeCiti());
		reader.setPageSize(pagingAttributes.getPageSizeCiti());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("First_name", OutboundSqlQueriesConstants.SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1));
		reader.setRowMapper(new CitiSuppresionOutboundMapper());
		reader.afterPropertiesSet();

		return reader;
	}

	public JdbcPagingItemReader<SalesforceExtractOutboundDTO> salesforceExtractOutboundDBReader() throws Exception
	{
		purgeSalesforceExtractTable();
		log.info(" Preference Outbound : Preference Outbound Reader for Salesforce Extract Started at:" + new Date());
		JdbcPagingItemReader<SalesforceExtractOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeSFMC());
		reader.setPageSize(pagingAttributes.getPageSizeSFMC());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("first_name", OutboundSqlQueriesConstants.SQL_GET_EMAIL_PREFERENCES_OUTBOUND));
		reader.setRowMapper(new SalesforceExtractOutboundMapper());
		reader.afterPropertiesSet();
		return reader;
	}

	/**
	 * Method for read the data needed from DB
	 *
	 * @return
	 */
	public JdbcPagingItemReader<InternalOutboundDto> outboundInternalDBReader() throws Exception
	{
		purgeProgramCompliant();
		log.info(" Internal Program Outbound : Internal Program Outbound Reader Starter :" + new Date());
		JdbcPagingItemReader<InternalOutboundDto> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeInternal());
		reader.setPageSize(pagingAttributes.getPageSizeInternal());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("email", OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION));
		reader.setRowMapper(new InternalOutboundStep1Mapper());
		reader.afterPropertiesSet();

		log.info(" Internal Program Outbound : Internal Program Outbound Reader End :" + new Date());
		return reader;
	}

	public JdbcPagingItemReader<InternalFlexOutboundDTO> outboundInternalFlexDBReader() throws Exception
	{
		purgeFlexAttributes();
		log.info(" Internal Flex Outbound : Internal Flex Attributes Outbound Reader Starter :" + new Date());
		JdbcPagingItemReader<InternalFlexOutboundDTO> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeFlexAttributes());
		reader.setPageSize(pagingAttributes.getPageSizeFlexAttributes());
		reader.setQueryProvider(pagingAttributes.queryProvider("file_id",
				OutboundSqlQueriesConstants.SQL_SELECT_FOR_FLEX_ATTRIBUTES_INTERNAL_DESTINATION));
		reader.setRowMapper(new InternalFlexOutboundStep1Mapper());
		reader.afterPropertiesSet();

		log.info(" Internal flex Outbound : Internal Flex Attributes Outbound Reader End :" + new Date());
		return reader;
	}

	public JdbcPagingItemReader<InternalOutboundDto> outboundLoyaltyComplaintWeekly() throws Exception
	{
		purgeLoyaltyComplaint();
		log.info(" Loyalty Complaint Outbound : Loyalty Complaint Outbound Reader Starter :" + new Date());
		JdbcPagingItemReader<InternalOutboundDto> reader = new JdbcPagingItemReader<>();

		reader.setDataSource(dataSource);
		reader.setFetchSize(pagingAttributes.getFetchSizeLoyalty());
		reader.setPageSize(pagingAttributes.getPageSizeLoyalty());
		reader.setQueryProvider(
				pagingAttributes.queryProvider("email", OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION));
		reader.setRowMapper(new InternalOutboundStep1Mapper());
		reader.afterPropertiesSet();

		log.info(" Loyalty Complaint Outbound : Loyalty Complaint Outbound Reader End :" + new Date());
		return reader;
	}

	public ItemReader<DailyCountReportDTOStep1> dailyCountReportStep1()
	{
		JdbcCursorItemReader<DailyCountReportDTOStep1> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(ReportsQueries.DAILY_COUNT_REPORT_EMAIL_PREFERENCES_QUERY);
		reader.setRowMapper(new DailyCountReportPreferencesEmail());

		return reader;
	}

	public JdbcCursorItemReader<DailyCountReportStep2> dailyCountReportStep2()
	{
		JdbcCursorItemReader<DailyCountReportStep2> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(ReportsQueries.DAILY_COUNT_REPORT_OVERALL_PREFERENCES);
		reader.setRowMapper(new DailyCountReportPreferencesOverallRowMapper());

		return reader;
	}

	/**
	 * Method used to clear the passthroughs table in every execution
	 */
	private void truncateTable()
	{
		log.info("Deleting hdpc_out_daily_compliant Table :" + new Date());
		outboundService.truncateCompliantTable();
	}

	public void purgeCitiSuppresionTable()
	{
		outboundService.purgeCitiSuppresionTable();
		log.info("Deleting hdpc_out_citi_suppression records at: {}", new Date());
	}

	public void purgeSalesforceExtractTable()
	{
		outboundService.purgeSalesforceExtractTable();
		log.info("Deleting hdpc_out_salesforce_extract records at: {}", new Date());
	}

	public void purgeProgramCompliant()
	{
		outboundService.purgeProgramCompliant();
		log.info("Deleting hdpc_out_program_compliant records at: {}", new Date());
	}

	public void purgeLoyaltyComplaint()
	{
		outboundService.purgeLoyaltyComplaintTable();
		log.info("Deleting hdpc_out_loyalty_compliant records at: {}", new Date());
	}

	public void purgeFlexAttributes()
	{
		outboundService.purgeFlexAttributesTable();
		log.info("Deleting hdpc_out_program_compliant records at: {}", new Date());
	}


	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
}

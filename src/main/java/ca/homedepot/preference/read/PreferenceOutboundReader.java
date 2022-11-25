package ca.homedepot.preference.read;



import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.mapper.*;
import ca.homedepot.preference.service.OutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
public class PreferenceOutboundReader
{
	@Autowired
	private DataSource dataSource;

	@Autowired
	private OutboundService outboundService;

	/**
	 * This method is used to read the necessary data from DB
	 * 
	 * @return
	 */
	public JdbcCursorItemReader<PreferenceOutboundDto> outboundDBReader()
	{
		truncateTable();
		log.info(" Preference Outbound : Preference Outbound Reader Starter :" + new Date());
		JdbcCursorItemReader<PreferenceOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_GET_CRM_OUTBOUND);
		reader.setRowMapper(new PreferenceOutboundMapper());

		log.info(" Preference Outbound : Preference Outbound Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<CitiSuppresionOutboundDTO> outboundCitiSuppresionDBReader()
	{
		purgeCitiSuppresionTable();
		log.info(" Preference Outbound : Preference Outbound Reader for Citi Suppresion Started at:" + new Date());
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_PREFERENCES_FOR_CITI_SUP_STEP_1);
		reader.setRowMapper(new CitiSuppresionOutboundMapper());

		return reader;
	}

	public JdbcCursorItemReader<SalesforceExtractOutboundDTO> salesforceExtractOutboundDBReader()
	{
		purgeSalesforceExtractTable();
		log.info(" Preference Outbound : Preference Outbound Reader for Salesforce Extract Started at:" + new Date());
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_GET_EMAIL_PREFERENCES_OUTBOUND);
		reader.setRowMapper(new SalesforceExtractOutboundMapper());

		return reader;
	}

	/**
	 * Method for read the data needed from DB
	 *
	 * @return
	 */
	public ItemReader<InternalOutboundDto> outboundInternalDBReader()
	{
		purgeProgramCompliant();
		log.info(" Internal Program Outbound : Internal Program Outbound Reader Starter :" + new Date());
		JdbcCursorItemReader<InternalOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION);
		reader.setRowMapper(new InternalOutboundStep1Mapper());

		log.info(" Internal Program Outbound : Internal Program Outbound Reader End :" + new Date());
		return reader;
	}

	public ItemReader<InternalFlexOutboundDTO> outboundInternalFlexDBReader()
	{
		purgeProgramCompliant();
		log.info(" Internal Flex Outbound : Internal Flex Attributes Outbound Reader Starter :" + new Date());
		JdbcCursorItemReader<InternalFlexOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_FOR_FLEX_ATTRIBUTES_INTERNAL_DESTINATION);
		reader.setRowMapper(new InternalFlexOutboundStep1Mapper());

		log.info(" Internal flex Outbound : Internal Flex Attributes Outbound Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<InternalOutboundDto> outboundLoyaltyComplaintWeekly()
	{
		purgeLoyaltyComplaint();
		log.info(" Loyalty Complaint Outbound : Loyalty Complaint Outbound Reader Starter :" + new Date());
		JdbcCursorItemReader<InternalOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_FOR_INTERNAL_DESTINATION);
		reader.setRowMapper(new InternalOutboundStep1Mapper());

		log.info(" Loyalty Complaint Outbound : Loyalty Complaint Outbound Reader End :" + new Date());
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

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
}

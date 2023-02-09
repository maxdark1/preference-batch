package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.*;
import ca.homedepot.preference.mapper.*;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
@Generated

public class PreferenceOutboundDBReader
{
	@Autowired
	private DataSource dataSource;

	/**
	 * Method used to get the necessary data from DB in Step 2
	 * 
	 * @return
	 */
	public JdbcCursorItemReader<PreferenceOutboundDto> outboundDBReader()
	{
		log.info(" Preference Outbound : Preference Outbound Step 2 Reader Starter :" + new Date());
		JdbcCursorItemReader<PreferenceOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_OUTBOUND_DB_READER_STEP2);
		reader.setRowMapper(new PreferenceOutboundMapperStep2());

		log.info(" Preference Outbound : Preference Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	/**
	 * This method is used for get the data from the temporary table
	 *
	 * @return
	 */
	public JdbcCursorItemReader<InternalOutboundDto> outboundInternalDbReader()
	{
		log.info(" Preference Outbound : Internal Outbound Step 2 Reader Starter :" + new Date());
		JdbcCursorItemReader<InternalOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_PROGRAM_COMPLIANT);
		reader.setRowMapper(new InternalOutboundStep2Mapper());

		log.info(" Preference Outbound : Internal Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<InternalFlexOutboundDTO> outboundInternalFlexDbReader()
	{
		log.info(" Preference Outbound : Flex Attributes Internal Outbound Step 2 Reader Starter :" + new Date());
		JdbcCursorItemReader<InternalFlexOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_FLEX_ATTRIBUTES);
		reader.setRowMapper(new InternalFlexOutboundStep2Mapper());

		log.info(" Preference Outbound : Flex Attributes Internal Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<CitiSuppresionOutboundDTO> citiSuppressionDBTableReader()
	{
		log.info(" Preference Outbound : Preference Citi Suppresion Outbound Step 2 Reader Starter :" + new Date());
		JdbcCursorItemReader<CitiSuppresionOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_CITI_SUPPRESION_TABLE);
		reader.setRowMapper(new CitiSuppresionOutboundMapper());

		log.info(" Preference Outbound : Preference Citi Suppresion Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<LoyaltyCompliantDTO> loyaltyComplaintDBTableReader()
	{
		log.info(" Preference Loyalty Complaint Outbound : Preference Loyalty Complaint Outbound Step 2 Reader Starter :"
				+ new Date());
		JdbcCursorItemReader<LoyaltyCompliantDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_LOYALTY_COMPLAINT);
		reader.setRowMapper(new LoyaltyComplaintWeeklyMapper());

		log.info(" Preference Outbound : Preference Citi Suppression Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public JdbcCursorItemReader<SalesforceExtractOutboundDTO> salesforceExtractDBTableReader()
	{
		log.info(" Preference Outbound : Preference Salesforce Extract Outbound Step 2 Reader Starter :" + new Date());
		JdbcCursorItemReader<SalesforceExtractOutboundDTO> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(OutboundSqlQueriesConstants.SQL_SELECT_SALESFORCE_EXTRACT_TABLE);
		reader.setRowMapper(new SalesforceExtractOutboundMapper());

		log.info(" Preference Outbound : Salesforce Extract Outbound Step 2 Reader End :" + new Date());
		return reader;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
}

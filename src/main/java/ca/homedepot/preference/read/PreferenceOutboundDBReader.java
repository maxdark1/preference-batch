package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.mapper.CitiSuppresionOutboundMapper;
import ca.homedepot.preference.mapper.InternalOutboundStep2Mapper;
import ca.homedepot.preference.mapper.PreferenceOutboundMapperStep2;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
@Data
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
}

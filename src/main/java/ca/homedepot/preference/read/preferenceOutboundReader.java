package ca.homedepot.preference.read;



import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.mapper.CitiSuppresionOutboundMapper;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;
import ca.homedepot.preference.service.OutboundService;
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
public class preferenceOutboundReader
{
	@Autowired
	private DataSource dataSource;

	@Autowired
	private OutboundService outboundService;

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

	public void truncateTable()
	{
		log.info("Deleting hdpc_out_daily_compliant Table :" + new Date());
		outboundService.truncateCompliantTable();
	}

	public void purgeCitiSuppresionTable()
	{

		outboundService.purgeCitiSuppresionTable();
		log.info("Deleting hdpc_out_citi_suppression records at: {}", new Date());
	}
}

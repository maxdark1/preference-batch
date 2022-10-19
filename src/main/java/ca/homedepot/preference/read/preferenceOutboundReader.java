package ca.homedepot.preference.read;



import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
@Data
public class preferenceOutboundReader
{
	private DataSource dataSource;

	public JdbcCursorItemReader<PreferenceOutboundDto> outboundDBReader()
	{
		log.info(" Preference Outbound : Preference Outbound Reader Starter :" + new Date());
		JdbcCursorItemReader<PreferenceOutboundDto> reader = new JdbcCursorItemReader<>();

		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstants.SQL_GET_CRM_OUTBOUND);
		reader.setRowMapper(new PreferenceOutboundMapper());

		log.info(" Preference Outbound : Preference Outbound Reader End :" + new Date());
		return reader;
	}
}

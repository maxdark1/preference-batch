package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.PreferenceService;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Data
public class PreferenceOutboundWriter implements ItemWriter<PreferenceOutboundDto>
{
	@Autowired
	private DataSource dataSource;
	private PreferenceService preferenceService = new PreferenceServiceImpl();

	@Override
	public void write(List<? extends PreferenceOutboundDto> list) throws Exception
	{
		log.info(" Preference Outbound : Preference Outbound Writer Starter :" + new Date());
		for (PreferenceOutboundDto preference : list)
		{
			preferenceService.preferenceOutbound(preference, dataSource);
		}
		log.info(" Preference Outbound : Preference Outbound Writer END :" + new Date());
	}
}

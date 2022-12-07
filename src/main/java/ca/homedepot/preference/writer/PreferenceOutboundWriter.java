package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class PreferenceOutboundWriter implements ItemWriter<PreferenceOutboundDto>
{
	@Autowired
	private OutboundService outboundService;

	/**
	 * This method is used for get the data read by reader and generate a plain text file
	 * 
	 * @param list
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends PreferenceOutboundDto> list) throws Exception
	{
		log.info(" Preference Outbound : Preference Outbound Writer Starter :" + new Date());
		for (PreferenceOutboundDto preference : list)
		{
			outboundService.preferenceOutbound(preference);
		}
		log.info(" Preference Outbound : Preference Outbound Writer END :" + new Date());
	}
}

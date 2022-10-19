package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class preferenceOutboundProcessor implements ItemProcessor<PreferenceOutboundDto, PreferenceOutboundDto>
{

	@Override
	public PreferenceOutboundDto process(PreferenceOutboundDto preferenceOutboundDto) throws Exception
	{
		log.info("Nothing to DO on Preference Outbound Processor");
		return preferenceOutboundDto;
	}

}

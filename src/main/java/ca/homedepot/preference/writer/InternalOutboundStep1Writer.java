package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.InternalOutboundDto;

import ca.homedepot.preference.service.OutboundService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Data
public class InternalOutboundStep1Writer implements ItemWriter<InternalOutboundDto>
{

	@Autowired
	private OutboundService outboundService;

	@Override
	public void write(List<? extends InternalOutboundDto> items) throws Exception
	{
		log.info(" Preference Outbound : Preference Outbound Writer Starter :" + new Date());
		for (InternalOutboundDto internalOutboundDto : items)
		{
			outboundService.programCompliant(internalOutboundDto);
		}
		log.info(" Preference Outbound : Preference Outbound Writer END :" + new Date());


	}
}

package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.service.OutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class InternalFlexOutboundStep1Writer implements ItemWriter<InternalFlexOutboundDTO>
{

	@Autowired
	private OutboundService outboundService;

	/**
	 * This method is used for temporary save of data into DB
	 *
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends InternalFlexOutboundDTO> items) throws Exception
	{
		log.info(" Preference Outbound : Preference Outbound Writer Starter :" + new Date());
		for (InternalFlexOutboundDTO internalOutboundDto : items)
		{
			outboundService.internalFlexAttributes(internalOutboundDto);
		}
		log.info(" Preference Outbound : Preference Outbound Writer END :" + new Date());


	}
}
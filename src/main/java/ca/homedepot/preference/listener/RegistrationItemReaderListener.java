package ca.homedepot.preference.listener;

import java.math.BigDecimal;

import ca.homedepot.preference.model.InboundRegistration;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.FileService;
import lombok.Data;

//TODO
@Component
@Data
//@AllArgsConstructor
public class RegistrationItemReaderListener implements ItemReadListener<InboundRegistration>
{

	private final FileService fileService;

	private String fileName;
	private String jobName;

	private BigDecimal fileID;
	private Master master;

	@Override
	public void beforeRead()
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		BigDecimal masterId = master.getMasterId();

		//fileService.insert(fileName, "G", masterId, new Date(), jobId, new Date(), "BATCH");
	}

	@Override
	public void afterRead(InboundRegistration item)
	{

	}

	@Override
	public void onReadError(Exception ex)
	{

	}
}

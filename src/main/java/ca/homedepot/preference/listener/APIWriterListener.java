package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INPROGRESS;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.SUCCESS;

@Component
@Slf4j
public class APIWriterListener implements ItemWriteListener<RegistrationRequest>
{

	private FileService fileService;

	@Autowired
	public void setFileService(FileService fileService)
	{
		this.fileService = fileService;
	}

	@Override
	public void beforeWrite(List<? extends RegistrationRequest> items)
	{
		log.info(" Items gonna be send to Preference Centre API.");
	}

	@Override
	public void afterWrite(List<? extends RegistrationRequest> items)
	{
		List<BigDecimal> filesId = getMapFileNameFileId(items);

		filesId.forEach(fileId -> fileService.updateInboundStgTableStatus(fileId, SUCCESS, INPROGRESS));
	}

	public List<BigDecimal> getMapFileNameFileId(List<? extends RegistrationRequest> items)
	{
		return items.stream().map(RegistrationRequest::getFileId).distinct().collect(Collectors.toList());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends RegistrationRequest> items)
	{
		log.error("Error occurs while sending to API: {}", exception.getMessage());
	}
}

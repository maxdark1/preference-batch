package ca.homedepot.preference.listener.skipers;

import ca.homedepot.preference.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SkipFileService
{
	@Autowired
	FileService fileService;

	public BigDecimal getFromTableFileID(String fileName, String jobName)
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		return fileService.getFile(fileName, jobId);
	}
}

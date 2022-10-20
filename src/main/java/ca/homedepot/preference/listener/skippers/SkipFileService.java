package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Used as a parent class to make the skippers inherit the Service
 */
@Component
public class SkipFileService
{
	/**
	 * The file Service
	 */
	@Autowired
	FileService fileService;


	/**
	 * Gets file Id of a certain file, determinated by its Job id and file name
	 * 
	 * @param fileName
	 * @param jobName
	 * @return File id from a certain file
	 */
	public BigDecimal getFromTableFileID(String fileName, String jobName)
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		return fileService.getFile(fileName, jobId);
	}
}

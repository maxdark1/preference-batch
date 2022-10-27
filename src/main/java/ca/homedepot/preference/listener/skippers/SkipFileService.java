package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.processor.MasterProcessor;
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

	/**
	 * Gets the email status depending if email was valid or not
	 * 
	 * @param t
	 * @return Email status
	 */
	public BigDecimal getEmailStatus(Throwable t)
	{
		String isValid = (isEmailInvalid(t)) ? "Invalid" : "Valid";

		return MasterProcessor.getSourceID("EMAIL_STATUS", isValid + " Email Addresses").getMasterId();
	}

	/**
	 * Gets email status if is it invalid depending on throwable message
	 * 
	 * @param t
	 * @return true if email was valid, false the other case
	 */
	public boolean isEmailInvalid(Throwable t)
	{
		return t.getMessage().contains("email address");
	}
}

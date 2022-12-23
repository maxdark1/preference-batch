package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

import static ca.homedepot.preference.util.constants.StorageConstants.SLASH;

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
		BigDecimal jobId = fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId());
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
		String isValid = isEmailInvalid(t) ? "Invalid" : "Valid";

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

	public boolean isDateInvalid(Throwable t)
	{
		return t.getMessage().contains("date format");
	}

	public String getFileName(String filename)
	{
		int index = filename.lastIndexOf(SLASH) + 1;
		return filename.substring(index);
	}

	/**
	 * Returns if the exception can be skipped
	 * 
	 * @param t
	 * @return can be skipped
	 */
	public boolean shouldSkip(Throwable t)
	{
		return !(t instanceof IOException);
	}
}

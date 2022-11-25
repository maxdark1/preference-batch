package ca.homedepot.preference.listener;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.constants.StorageConstants;
import com.google.cloud.storage.StorageException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ca.homedepot.preference.util.FileUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;

@Slf4j
@Component
@Setter
@Getter
public class InvalidFileListener implements StepExecutionListener
{

	private String directory;

	private String source;

	private String jobName;

	private String process;

	private Map<String, List<Resource>> resources;

	private List<Resource> invalidFiles;
	/**
	 * The file service
	 */
	private FileService fileService;


	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		if (checkExecution(stepExecution.getStepName()))
		{
			resources = getResources(directory + source, process);
			this.invalidFiles = resources.get("INVALID");

			if (this.invalidFiles != null && this.invalidFiles.size() > 0)
			{
				this.invalidFiles.forEach(file -> {
					String blobToCopy = file.getFilename(),
							filename = blobToCopy.substring(blobToCopy.lastIndexOf(StorageConstants.SLASH));
					String blobWhereToCopy = blobToCopy.replace(FileUtil.getInbound(), FileUtil.getError());
					writeFile(filename, false);
					try
					{
						StorageApplicationGCS.moveObject(filename, blobToCopy, blobWhereToCopy);
						log.error("PREFERENCE BATCH ERROR - Invalid Format Name File {} for source {} Moved to ERROR Folder",
								file.getFilename(), source);
					}
					catch (StorageException e)
					{
						log.error("PREFERENCE BATCH ERROR - An exception has occurred moving file: {} to ERROR folder",
								file.getFilename());
					}

				});

			}
			else
			{
				log.info("Nothing to do on InvalidFileListener");
			}
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		return null;
	}

	protected Boolean checkExecution(String stepName)
	{
		switch (stepName)
		{
			case "readInboundCSVFileStep":
			case "readSFMCOptOutsStep1":
			case "readInboundCSVFileCRMStep":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Write file into file table
	 *
	 * @param fileName
	 * @param status
	 */
	protected void writeFile(String fileName, Boolean status)
	{
		BigDecimal jobId = fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID("STATUS", Boolean.TRUE.equals(status) ? VALID : "INVALID");
		BigDecimal masterId = MasterProcessor
				.getSourceID("SOURCE", source.equals(SourceDelimitersConstants.FB_SFMC) ? SourceDelimitersConstants.SFMC : source)
				.getMasterId();
		Date endTime = new Date();
		/**
		 * If status is valid do not need end_time
		 */
		if (Boolean.TRUE.equals(status))
			endTime = null;
		FileDTO file = new FileDTO(null, fileName, jobId, masterId, fileStatus.getValueVal(), fileStatus.getMasterId(), new Date(),
				endTime, "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

	protected Map<String, List<Resource>> getResources(String folder, String source)
	{
		return FileUtil.getFilesOnFolder(folder, source);
	}
}

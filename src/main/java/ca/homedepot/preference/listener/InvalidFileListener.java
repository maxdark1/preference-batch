package ca.homedepot.preference.listener;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.constants.StorageConstants;
import com.google.cloud.storage.StorageException;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.JOB_STATUS;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;
import static ca.homedepot.preference.dto.enums.JobStatusEnum.IN_PROGRESS;

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
	@Autowired
	private FileService fileService;


	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		if (checkExecution(stepExecution.getStepName()))
		{
			resources = getResources(process, directory + source);
			this.invalidFiles = resources.get("INVALID");

			if (this.invalidFiles != null && !this.invalidFiles.isEmpty())
			{
				this.invalidFiles.forEach(file -> {
					String blobToCopy = file.getFilename();
					String filename = blobToCopy.substring(blobToCopy.lastIndexOf(StorageConstants.SLASH) + 1);
					String blobWhereToCopy = blobToCopy.replace(FileUtil.getInbound(), FileUtil.getError());
					writeFile(filename, false);
					try
					{
						StorageApplicationGCS.moveObject(filename, blobToCopy, blobWhereToCopy);
						log.error("PREFERENCE BATCH ERROR - Invalid Format Name File {} for source {} Moved to ERROR Folder", filename,
								process);
					}
					catch (StorageException e)
					{
						log.error("PREFERENCE BATCH ERROR - An exception has occurred moving file: {} to ERROR folder on source {}",
								filename, process);
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
		BigDecimal inprogress = MasterProcessor.getSourceID(JOB_STATUS, IN_PROGRESS.getStatus()).getMasterId();
		BigDecimal jobId = fileService.getJobId(jobName, inprogress);
		Master fileStatus = MasterProcessor.getSourceID("STATUS", Boolean.TRUE.equals(status) ? VALID : "INVALID");
		String sourceDefinitive = this.process.equals(SourceDelimitersConstants.FB_SFMC) ? SourceDelimitersConstants.SFMC
				: this.process;
		BigDecimal masterId = MasterProcessor.getSourceID("SOURCE", sourceDefinitive).getMasterId();
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

	protected Map<String, List<Resource>> getResources(String source, String folder)
	{
		return StorageApplicationGCS.getsGCPResourceMap(source, folder);
	}
}

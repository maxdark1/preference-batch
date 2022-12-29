package ca.homedepot.preference.listener;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import com.google.cloud.storage.StorageException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Slf4j
@Component
public class StepErrorLoggingListener implements StepExecutionListener
{

	/**
	 * The FileService
	 */
	@Autowired
	FileService fileService;

	JobListener jobListener;

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}

	/**
	 * @param stepExecution
	 *           instance of {@link StepExecution}.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		log.info(" Job in process: {} , Step in process: {} ", stepExecution.getStepName(),
				stepExecution.getJobExecution().getJobConfigurationName());
	}

	/**
	 * @param stepExecution
	 *           {@link StepExecution} instance.
	 * @return
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		/**
		 * Gets all exceptions found in StepExecution
		 */
		List<Throwable> exceptions = stepExecution.getFailureExceptions();

		if (exceptions.isEmpty())
		{
			/**
			 * If everything is fine, means that we can move the file to PROCESSED
			 */
			moveFileGCS();
			return ExitStatus.COMPLETED;
		}

		/**
		 * Print the exceptions founds
		 */
		log.info(" The step: {} has {} erros. ", stepExecution.getStepName(), exceptions.size());
		exceptions.forEach(ex -> log.info(" Exception has ocurred:  " + ex.getMessage()));

		return ExitStatus.FAILED;
	}

	@SneakyThrows
	public void moveFileGCS()
	{
		/**
		 * Gets all files that don't haave end time
		 */
		List<FileDTO> filesToMove = fileService.getFilesToMove();
		log.info("Step Error Line 85: " + filesToMove.toString());
		if (filesToMove != null && !filesToMove.isEmpty())
		{
			StringBuilder files = new StringBuilder();

			filesToMove.forEach(file -> {
				files.append(file.getFileName()).append(",");
				log.info("StepError Line 89 File Item: " + file);
				StorageException storageException = null;
				boolean status = true;
				String source = MasterProcessor.getValueVal(file.getSourceType());
				log.info("StepError Line 93 get File Name: " + file.getFileName());
				source = source.equals("SFMC") && file.getFileName().contains(FileValidation.getFbSFMCBaseName()) ? "FB_SFMC"
						: source;
				String basename = FileUtil.getPath(source);
				String blobToCopy = basename + FileUtil.getInbound() + file.getFileName();
				String blobWhereToCopy = blobToCopy.replace(FileUtil.getInbound(), FileUtil.getProcessed());
				try
				{
					StorageApplicationGCS.moveObject(file.getFileName(), blobToCopy, blobWhereToCopy);
				}
				catch (StorageException e)
				{
					status = false;
					log.error(" PREFERENCE BATCH ERROR - Error has occurred trying to move file {} : {}", file.getFileName(),
							e.getMessage());
					storageException = e;
				}

				Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, status ? VALID : INVALID);
				fileService.updateFileEndTime(file.getFileId(), new Date(), INSERTEDBY, new Date(), fileStatus);
				if (storageException != null)
					throw storageException;
			});
			jobListener.setFiles(files.substring(0, files.toString().length() - 2));
		}
	}

	/**
	 * Move file to another location on specific folder
	 */
	public void moveFile()
	{
		/**
		 * Gets all files that don't haave end time
		 */
		List<FileDTO> filesToMove = fileService.getFilesToMove();

		/**
		 * If is it there AnyFile To move, it will move it
		 */
		if (filesToMove != null && !filesToMove.isEmpty())
		{
			filesToMove.forEach(file -> {
				boolean status = true;
				try
				{
					FileUtil.moveFile(file.getFileName(), true, MasterProcessor.getValueVal(file.getSourceType()));
				}
				catch (IOException e)
				{
					status = false;
					log.error("PREFERENCE BATCH ERROR - An exception occurs while trying to move the file {}", file.getFileName());
				}
				Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, status ? VALID : INVALID);
				fileService.updateFileEndTime(file.getFileId(), new Date(), INSERTEDBY, new Date(), fileStatus);
			});

		}
	}
}

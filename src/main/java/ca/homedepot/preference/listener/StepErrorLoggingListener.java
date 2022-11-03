package ca.homedepot.preference.listener;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class StepErrorLoggingListener implements StepExecutionListener
{

	/**
	 * The FileService
	 */
	@Autowired
	FileService fileService;

	/**
	 *
	 * @param stepExecution
	 *           instance of {@link StepExecution}.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		// Nothing to do in here
	}

	/**
	 *
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
			moveFile();
			return ExitStatus.COMPLETED;
		}

		/**
		 * Print the exceptions founds
		 */
		log.info(" The step: {} has {} erros. ", stepExecution.getStepName(), exceptions.size());
		exceptions.forEach(ex -> log.info(" Exception has ocurred:  " + ex.getMessage()));

		return ExitStatus.FAILED;
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
					FileUtil.moveFile(file.getFile_name(), true, MasterProcessor.getValueVal(file.getFile_source_id()));
				}
				catch (Exception e)
				{
					status = false;
					log.error("An exception occurs while trying to move the file " + file.getFile_name());
				}
				Master fileStatus = MasterProcessor.getSourceID("STATUS", status ? "VALID" : "INVALID");
				fileService.updateFileEndTime(file.getFile_id(), new Date(), "BATCH", new Date(), fileStatus);
			});

		}
	}
}

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

	@Autowired
	FileService fileService;

	@Override
	public void beforeStep(StepExecution stepExecution)
	{

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		List<Throwable> exceptions = stepExecution.getFailureExceptions();

		if (exceptions.isEmpty())
		{
			moveFile();
			return ExitStatus.COMPLETED;
		}
		log.info(" The step: {} has {} erros. ", stepExecution.getStepName(), exceptions.size());
		exceptions.forEach(ex -> log.info(" Exception has ocurred:  " + ex.getMessage()));



		return ExitStatus.FAILED;
	}

	public void moveFile()
	{
		List<FileDTO> filesToMove = fileService.getFilesToMove();

		if (filesToMove != null && !filesToMove.isEmpty())
		{
			filesToMove.forEach(file -> {
				Boolean status = true;
				try
				{
					FileUtil.moveFile(file.getFile_name(), true, MasterProcessor.getValueVal(file.getFile_source_id()));
				}
				catch (IOException e)
				{
					status = false;
					log.error("An exception occurs while trying to move the file " + file.getFile_name());
				}
				Master fileStatus = MasterProcessor.getSourceId("STATUS", status ? "VALID" : "INVALID");
				fileService.updateFileEndTime(file.getFile_id(), new Date(), "BATCH", new Date(), fileStatus);
			});

		}
	}
}

package ca.homedepot.preference.listener;


import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.FileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INPROGRESS;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.NOTSTARTED;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class RegistrationItemWriterListener implements ItemWriteListener<FileInboundStgTable>
{
	/**
	 * The File Service
	 */
	@Autowired
	private FileService fileService;

	/**
	 * The job name
	 */
	private String jobName;
	/**
	 * The file Id
	 */
	private BigDecimal fileID;

	/**
	 * Before write, sets items file_id according to fileName
	 * 
	 * @param items
	 *           to be written
	 */

	@Override
	public void beforeWrite(List<? extends FileInboundStgTable> items)
	{

		Map<String, BigDecimal> files = getMapFileNameFileId(items);
		List<String> filesNames = new ArrayList<>(files.keySet());
		Collections.sort(filesNames);
		filesNames.forEach(key -> {
			fileID = getFromTableFileID(key);
			files.put(key, fileID);
		});

		items.forEach(item -> item.setFileId(files.get(item.getFileName())));
	}

	/**
	 * Gets a map of file name and file id
	 * 
	 * @param items
	 * @return a Map of File name and file id
	 */

	public Map<String, BigDecimal> getMapFileNameFileId(List<? extends FileInboundStgTable> items)
	{
		return items.stream().map(item -> {
			FileDTO file = new FileDTO();
			file.setFileId(item.getFileId());
			file.setFileName(item.getFileName());
			return file;
		}).distinct().collect(
				Collectors.toMap(FileDTO::getFileName, value -> (value.getFileId() == null) ? BigDecimal.ZERO : value.getFileId()));
	}

	/**
	 * Gets from table the file_id
	 * 
	 * @param fileName
	 * @return file id
	 */
	public BigDecimal getFromTableFileID(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(jobName, JobListener.status(BatchStatus.STARTED).getMasterId());
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		return fileService.getFile(fileName, jobId);
	}

	/**
	 * After write Change status of NS (NOt Started) to (IP) In progress
	 * 
	 * @param items
	 *           written items
	 */
	@Override
	public void afterWrite(List<? extends FileInboundStgTable> items)
	{
		Map<String, BigDecimal> files = getMapFileNameFileId(items);

		files.forEach((fileName, fileId) -> fileService.updateInboundStgTableStatus(fileId, INPROGRESS, NOTSTARTED));
	}

	/**
	 *
	 * @param exception
	 *           thrown from {{Item Writer}
	 * @param items
	 *           attempted to be written.
	 */
	@Override
	public void onWriteError(Exception exception, List<? extends FileInboundStgTable> items)
	{
		log.error(" PREFERENCE BATCH ERROR - An exception has occurred while writing the items into file_inbound_stg_table: {} ",
				exception.getMessage());
	}
}

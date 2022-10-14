package ca.homedepot.preference.listener;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.service.FileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

		items.forEach(item -> {
			item.setFile_id(files.get(item.getFileName()));
		});
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
			file.setFile_id(item.getFile_id());
			file.setFile_name(item.getFileName());
			return file;
		}).distinct().collect(Collectors.toMap(key -> key.getFile_name(),
				value -> (value.getFile_id() == null) ? BigDecimal.ZERO : value.getFile_id()));
	}

	/**
	 * Gets from table the file_id
	 * 
	 * @param fileName
	 * @return file id
	 */
	public BigDecimal getFromTableFileID(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(jobName);
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

		files.forEach((fileName, fileId) -> {
			fileService.updateInboundStgTableStatus(fileId, "IP", "NS");
		});


	}

	/**
	 *
	 * @param exception
	 *           thrown from {@link ItemWriter}
	 * @param items
	 *           attempted to be written.
	 */
	@Override
	public void onWriteError(Exception exception, List<? extends FileInboundStgTable> items)
	{

	}
}

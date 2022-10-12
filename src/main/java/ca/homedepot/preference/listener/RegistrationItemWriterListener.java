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
	@Autowired
	private FileService fileService;

	private String jobName;
	private BigDecimal fileID;



	@Override
	public void beforeWrite(List<? extends FileInboundStgTable> items)
	{

		Map<String, BigDecimal> files = getMapFileNameFileId(items);
		List<String> filesNames = new ArrayList<>(files.keySet());
		Collections.sort(filesNames);
		filesNames.forEach(key ->
		{
			fileID = getFromTableFileID(key);
			files.put(key, fileID);
		});

		items.forEach(item -> {
			item.setFile_id(files.get(item.getFileName()));
		});
	}

	public Map<String, BigDecimal> getMapFileNameFileId(List<? extends FileInboundStgTable> items){
		return items.stream().map(item -> {
			FileDTO file = new FileDTO();
			file.setFile_id(item.getFile_id());
			file.setFile_name(item.getFileName());
			return file;
		}).distinct().collect(Collectors.toMap(key->key.getFile_name(), value-> (value.getFile_id()==null)?BigDecimal.ZERO: value.getFile_id()));
	}

	public BigDecimal getFromTableFileID(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		return fileService.getFile(fileName, jobId);
	}


	@Override
	public void afterWrite(List<? extends FileInboundStgTable> items)
	{
		Map<String, BigDecimal> files = getMapFileNameFileId(items);

		files.forEach((fileName, fileId)->{
			fileService.updateInboundStgTableStatus(fileId,"IP", "NS");
		});


	}

	@Override
	public void onWriteError(Exception exception, List<? extends FileInboundStgTable> items)
	{

	}
}

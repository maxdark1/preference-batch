package ca.homedepot.preference.listener;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.util.FileUtil;
import org.springframework.batch.core.ItemWriteListener;
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
public class RegistrationItemWriterListener implements ItemWriteListener<FileInboundStgTable>
{
	@Autowired
	private FileService fileService;

	private String fileName;
	private String jobName;
	private BigDecimal fileID;

	private Master master;

	private Master sourceIDMasterObj;


	@Override
	public void beforeWrite(List<? extends FileInboundStgTable> items)
	{
		fileID = writeFile();

		items.forEach(item -> item.setFile_id(fileID));
	}

	public BigDecimal writeFile()
	{
		BigDecimal jobId = fileService.getJobId(jobName);
		Master fileStatus = MasterProcessor.getSourceId("STATUS","VALID");
		BigDecimal masterId = sourceIDMasterObj.getMaster_id();

		fileService.insert(fileName, fileStatus.getValue_val(), masterId, new Date(), jobId, new Date(), "BATCH", fileStatus.getMaster_id());
		return fileService.getFile(fileName, jobId);
	}

	@Override
	public void afterWrite(List<? extends FileInboundStgTable> items)
	{
		//fileService.updateFileStatus(fileName, new Date(), "G", "C");
		fileService.updateInboundStgTableStatus(items.get(0).getFile_id(),"IP");

		try {
			FileUtil.moveFile(fileName, "PROCESSED");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onWriteError(Exception exception, List<? extends FileInboundStgTable> items)
	{

	}
}

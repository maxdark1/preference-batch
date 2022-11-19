package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;

import java.math.BigDecimal;
import java.util.*;

/*
*   MultiResourceItemReader
*  To can manage properly the exceptions
* */
@Slf4j
public class MultiResourceItemReaderInbound<T> extends MultiResourceItemReader<T>
{


	/**
	 * The file service
	 */
	private FileService fileService;

	/**
	 * The job name
	 */
	private String jobName;

	/**
	 * Source: where the file comes from hybris, SFMC, FB_SFMC, citi...
	 */
	private String source;

	/**
	 * Map of file name and boolean to know if the file has been written before
	 */
	private Map<String, Boolean> canResourceBeWriting;



	/**
	 * Constructor to assign Source
	 */
	public MultiResourceItemReaderInbound(String source)
	{
		this.source = source;
	}

	/**
	 * Set resource
	 *
	 * @param source
	 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/**
	 * Sets file service
	 *
	 * @param fileService
	 */
	@Autowired
	public void setFileService(FileService fileService)
	{
		this.fileService = fileService;
	}

	/**
	 * Sets resources and writes INVALID files with INVALID filesNames on persitence
	 *
	 * @param resources
	 */
	public void setResources(Map<String, List<Resource>> resources)
	{
		Resource[] resourcesArray = new Resource[resources.get(VALID).size()];
		resources.get(VALID).toArray(resourcesArray);
		this.setResources(resourcesArray);
	}

	/**
	 * Set jobName
	 */
	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * Set resources
	 *
	 * @param resources
	 *           input resources
	 */
	@Override
	public void setResources(Resource[] resources)
	{
		super.setResources(resources);
		canResourceBeWriting = new HashMap<>();
		/**
		 * Initialize Map with values
		 */
		for (Resource resource : resources)
		{
			canResourceBeWriting.put(resource.getFilename(), true);
		}
	}

	/**
	 * Read item from file
	 */
	@Override
	public T read() throws Exception
	{
		T itemRead = null;

		Resource resource = null;
		Boolean status = true;
		try
		{
			itemRead = super.read();
			resource = getCurrentResource();
		}
		catch (Exception e)
		{
			resource = getCurrentResource();
			status = !status;
			if (resource != null)
			{
				FileUtil.moveFile(resource.getFilename(), status, source);
				log.error(" An exception has ocurred reading file: " + resource.getFilename() + "\n " + e.getCause().getMessage());
			}
		}
		/**
		 * Validates that a resources is not null and can be writing
		 */
		if (resource != null && canResourceBeWriting.get(resource.getFilename()))
		{
			writeFile(resource.getFilename(), status);
			canResourceBeWriting.put(resource.getFilename(), false);
		}


		return itemRead;
	}

	/**
	 * Write file into file table
	 *
	 * @param fileName
	 * @param status
	 */
	public void writeFile(String fileName, Boolean status)
	{
		BigDecimal jobId = fileService.getJobId(jobName);
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

}

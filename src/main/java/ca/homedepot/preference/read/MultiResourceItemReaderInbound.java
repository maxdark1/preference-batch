package ca.homedepot.preference.read;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.constants.StorageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.*;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;

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

	private List<Counters> counters;



	/**
	 * Constructor to assign Source
	 */
	public MultiResourceItemReaderInbound(String source)
	{
		this.source = source;
		counters = new ArrayList<>();
	}

	/**
	 * Set Counters
	 * 
	 * @param counters
	 */
	public void setCounters(List<Counters> counters)
	{
		this.counters = counters;
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
		log.info("Resources Founded in GCP Bucket: " + resources.toString());
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
		if (resources.length == 0)
		{
			log.error(" PREFERENCE BATCH ERROR - No resources to read {} folder is empty.", source.toLowerCase());
		}
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
			status = false;
			if (resource != null && resource.getFilename() != null)
			{
				String filename = resource.getFilename();

				if (filename != null)
				{
					String blobToCopy = filename;
					String blobWhereToCopy = blobToCopy.replace(FileUtil.getInbound(), FileUtil.getError());
					filename = filename.substring(filename.lastIndexOf(StorageConstants.SLASH) + 1);
					StorageApplicationGCS.moveObject(filename, blobToCopy, blobWhereToCopy);
					log.error("PREFERENCE BATCH ERROR - An exception has occurred reading file: {} \n {}", resource.getFilename(),
							e.getCause().getMessage());
					String cause = e.getCause().getMessage().toUpperCase().contains("HEADER") ? "bad header" : "bad structure";
					String message = filename + " FAILED because of " + cause + " on source ";
					Counters counter = new Counters(0, 0, 0);
					counter.fileName = message;
					counters.add(counter);
				}
			}
		}

		/*
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
		fileName = fileName.replace(FileUtil.getPath(source) + FileUtil.getInbound(), "");
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

}

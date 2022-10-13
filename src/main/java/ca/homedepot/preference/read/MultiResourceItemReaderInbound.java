package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.*;

/*
*   MultiResourceItemReader
*  To can manage properly the exceptions
* */
@Slf4j
public class MultiResourceItemReaderInbound<T> extends MultiResourceItemReader<T> {


    private FileService fileService;

    private String jobName;

    /*
    * Source: where the file comes from
    * hybris, SFMC, FB_SFMC, citi...
    * */
    private String source;

    private Map<String, Boolean> canResourceBeWriting;

    /*
    * Constructor to assign Source
    * */
    public MultiResourceItemReaderInbound(String source)
    {
        this.source = source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    @Autowired
    public void setFileService(FileService fileService)
    {
        this.fileService = fileService;
    }

    public void setResources(Map<String, List<Resource>> resources) {
        Resource[] resourcesArray = new Resource[resources.get("VALID").size()];
        resources.get("VALID").toArray(resourcesArray);
        System.out.println(resourcesArray);
        resources.get("INVALID").forEach(fileName ->
        {
            writeFile(fileName.getFilename(), false);
        });
        this.setResources(resourcesArray);
    }

    /*
    * Set jobName
    * */
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    @Override
    public void setResources(Resource[] resources) {
        super.setResources(resources);
        canResourceBeWriting = new HashMap<>();
        for (Resource resource : resources) {
            canResourceBeWriting.put(resource.getFilename(), true);
        }
    }

    /*
    *
    * */
    @Override
    public T read() throws Exception
    {
        T itemRead = null;

        Resource resource = null;
        Boolean status = true;
        try{
            itemRead = super.read();
            resource = getCurrentResource();
        }catch(Exception e){
            resource = getCurrentResource();
            status = !status;
            FileUtil.moveFile(resource.getFilename(), status, source);
            log.error(" An exception has ocurred reading file: " + getCurrentResource().getFilename() + "\n " + e.getCause().getMessage() );
        }

        if(resource != null && canResourceBeWriting.get(resource.getFilename()))
        {
            writeFile(resource.getFilename(), status);
            canResourceBeWriting.put(resource.getFilename(), false);
        }


        return itemRead;
    }

    /*
    * Write file into file table
    * */
    public void writeFile(String fileName, Boolean status)
    {
        BigDecimal jobId = fileService.getJobId(jobName);
        Master fileStatus = MasterProcessor.getSourceId("STATUS",status?"VALID":"INVALID");
        BigDecimal masterId = MasterProcessor.getSourceId("SOURCE", source).getMaster_id();
        Date endTime = new Date();
        if(status)
            endTime = null;

        fileService.insert(fileName, fileStatus.getValue_val(), masterId, new Date(), jobId, new Date(), "BATCH", fileStatus.getMaster_id(), endTime);
    }

}

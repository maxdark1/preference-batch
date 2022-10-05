package ca.homedepot.preference.read;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.math.BigDecimal;
import java.util.Date;

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

    /*
    * Set jobName
    * */
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
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
            status = !status;
            resource = getCurrentResource();
            FileUtil.moveFile(resource.getFilename(), status, source);
            log.error(" An exception has ocurred reading file: " + getCurrentResource().getFilename() + "\n " + e.getCause().getMessage() );
        }

        if(resource != null)
        {
            writeFile(resource.getFilename(), status);
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

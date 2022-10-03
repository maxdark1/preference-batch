package ca.homedepot.preference.read;

import ca.homedepot.preference.util.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.Resource;

import java.util.Arrays;

/*
*   MultiResourceItemReader
*  To can manage properly the exceptions
* */
@Slf4j
public class MultiResourceItemReaderInbound<T> extends MultiResourceItemReader<T> {


    /*
    * Source: where the file comes from
    * hybris, SFMC, FB_SFMC, citi...
    * */
    private String source;

    /*
    * Constructor to assign Source
    * */
    public MultiResourceItemReaderInbound(String source) {
        this.source = source;
    }

    /*
    *
    * */
    @Override
    public T read() throws Exception {

        T itemRead = null;

        try{
            itemRead = super.read();
        }catch(Exception e){
            Resource resource = getCurrentResource();
            FileUtil.moveFile(resource.getFilename(), false, source);
            log.error(" An exception has ocurred reading file: " + getCurrentResource().getFilename() + "\n " + e.getCause().getMessage() );
        }
        return itemRead;
    }
}

package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.BlobInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.core.io.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static ca.homedepot.preference.config.StorageApplicationGCS.*;

@Getter
@Setter
@Slf4j
public class GSFileWriterOutbound<T> extends FileWriterOutBound<T>{


    private Resource resource;

    private OutputStream os;

    /**
     * Set the filename and delete if exists in the current bucket
     */
    @Override
    public void setResource() {
        setfilename();
        StorageApplicationGCS.deleteObject(getFolderSource(), getFileName());
        setResource(new FileSystemResource( getFileName()));
    }

    /**
     * Gets the outputStream to generate the file,
     * and it creates it in the current
     * bucket
     * @param executionContext current step's {@link org.springframework.batch.item.ExecutionContext}.  Will be the
     *                            executionContext from the last run of the step on a restart.
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try{
            os = ((WritableResource) resource).getOutputStream();
            String bucket = StorageApplicationGCS.getBucketName();
            BlobInfo info = BlobInfo.newBuilder(bucket, CloudStorageUtils.generatePath(getFolderSource(), getFileName())).build();
            storage().create(info);
        } catch (IOException e) {
            log.error("OPEN METHOD: {}", e.getMessage());
        }
    }

    /**
     * Writes each item on the file
     * @param items items to be written
     * @throws Exception
     */
    @Override
    public void write(List<? extends T> items) throws Exception {
        saveFileRecord();
        StringBuilder lines = new StringBuilder();

        for (T item: items) {
            lines.append(lineAggregator.aggregate(item)).append(lineSeparator);
        }
        byte[] bytes = lines.toString().getBytes();

        try{
            os.write(bytes);
        }catch (IOException e){
            log.error(" WRITE METHOD: {}", e.getMessage());
        }
    }

    /**
     * Close the stream to write file
     */
    @Override
    public void close() {
        super.close();

        try{
            os.close();
        }catch (IOException e){
            log.error("CLOSE SOURCE: {}", e.getMessage());
        }
    }
}

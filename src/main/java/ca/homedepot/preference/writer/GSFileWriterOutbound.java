package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.constants.StorageConstants;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.core.io.*;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static ca.homedepot.preference.config.StorageApplicationGCS.*;

@Getter
@Setter
@Slf4j
public class GSFileWriterOutbound<T> extends FileWriterOutBound<T>{


    private StringBuilder lines;
    private Resource resource;

    private File tempFile;
    private OutputStream os;

    private StringBuilder stringBuilder;
    /**
     * Set the filename and delete if exists in the current bucket
     */
    @Override
    public void setResource() {
        stringBuilder = new StringBuilder(getHeader()).append("\n");
        setfilename();
        StorageApplicationGCS.deleteObject(getFolderSource(), getFileName());
        tempFile = FileUtil.createTempFile(getFileName());

        super.setResource(new FileSystemResource(tempFile));
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        super.write(items);

        String line = super.doWrite(items);
        if(!stringBuilder.toString().contains(line));
            stringBuilder.append(line);

    }

    /**
     * After de writer is close, it generate the file on the
     * Google Cloud Storage
     */
    @Override
    public void close() {
        super.close();

        byte[] content = stringBuilder.toString().getBytes();
        createFileOnGCS(CloudStorageUtils.generatePath(getFolderSource(), getFileName()), content);
    }

    /**
     * Method to create file on the Google cloud Storage
     * @param filename
     * @param content
     */

    public static void createFileOnGCS(String filename, byte[] content)
    {
        String bucket = StorageApplicationGCS.getBucketName();
        BlobInfo file = BlobInfo.newBuilder(bucket, filename).build();
        storage().create(file, content);
    }
}

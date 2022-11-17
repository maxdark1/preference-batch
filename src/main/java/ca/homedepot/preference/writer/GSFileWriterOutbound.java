package ca.homedepot.preference.writer;

import ca.homedepot.preference.config.StorageApplicationGCS;
import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.constants.StorageConstants;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
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

    /**
     * Set the filename and delete if exists in the current bucket
     */
    @Override
    public void setResource() {
        setfilename();
        StorageApplicationGCS.deleteObject(getFolderSource(), getFileName());
        String bucket = StorageApplicationGCS.getBucketName();
        BlobInfo info = BlobInfo.newBuilder(bucket, CloudStorageUtils.generatePath(getFolderSource(), getFileName())).build();
        Blob blob =  storage().create(info);

        super.setResource(new GoogleStorageResource(storage(), CloudStorageUtils.generatePath("gs://commsvc-preference-centre-feeds/",getFolderSource(), getFileName()) ));
    }


}

package ca.homedepot.preference.writer.resources;

import com.google.cloud.storage.Storage;
import lombok.Setter;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;

@Setter
public class GoogleStorageResourceImpl extends GoogleStorageResource {

    private String folder;

    private String source;

    public GoogleStorageResourceImpl(Storage storage, String location, boolean autoCreateFiles) {
        super(storage, location, autoCreateFiles);
    }

    public GoogleStorageResourceImpl(Storage storage, String location, String folder, String source) {
        super(storage, location);
        setFolder(folder);
        setSource(source);
    }


}

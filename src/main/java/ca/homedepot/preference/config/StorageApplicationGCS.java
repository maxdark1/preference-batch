package ca.homedepot.preference.config;

import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.constants.StorageConstants;
import ca.homedepot.preference.util.validation.FileValidation;
import lombok.experimental.UtilityClass;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INVALID;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;

@UtilityClass
public class StorageApplicationGCS {
   private static Storage storage;

   private static CloudStorageUtils cloudStorageUtils;

    /**
     * Create the storage if it doesn't exist
     * @return Storage
     */
   public static Storage storage()
   {
       if(storage == null){
           storage = StorageOptions.getDefaultInstance().getService();
       }
       return storage;
   }

    /**
     * Gets the bucket anme
     * @return bucket name
     */
   public static String getBucketName(){
       return cloudStorageUtils.getBucketName();
   }

    /**
     * Calls the cloud storage utils to delete an specific file
     * @param folder
     * @param filename
     * @return
     */
   public static boolean deleteObject(String folder, String filename){
       return cloudStorageUtils.deleteObject(folder, filename);
   }

    /**
     * Sets the cloud storage utils
     * @param cloudStorageUtils
     */
    public static void setCloudStorageUtils(CloudStorageUtils cloudStorageUtils) {
        StorageApplicationGCS.cloudStorageUtils = cloudStorageUtils;
    }

    /**
     * Gets the files in folder
     * @param source
     * @param folder
     * @return
     */
    public static List<GoogleStorageResource> getGCPResource(String source, String folder) {
        return cloudStorageUtils.listObjectInBucket(FileUtil.getPath(source), folder).stream()
                .map(path -> new GoogleStorageResource(storage(), buildBlobURL(path))).collect(Collectors.toList());
    }

    public Map<String, List<Resource>> getsGCPResourceMap(String source, String folder){
        
        Map<String, List<Resource>> resources = new HashMap<>();
        List<GoogleStorageResource> resourcesGCS = getGCPResource(source, folder);
        List<Resource> validResources = new ArrayList<>();
        List<Resource> invalidResources = new ArrayList<>();
        if(resourcesGCS != null){
            List<String> files = resourcesGCS.stream().map(GoogleStorageResource::getBlobName).collect(Collectors.toList());

            int i = 0;
            for (String file: files) {
                file = file.replace(CloudStorageUtils.generatePath(source, folder), "");

                if(FileValidation.validateFileName(file, source)){
                    validResources.add(resourcesGCS.get(i));
                }else{
                    invalidResources.add(resourcesGCS.get(i));
                    cloudStorageUtils.moveObject(FileUtil.getPath(source), folder, FileUtil.getError(), file);
                }
                i++;
            }
        }

        resources.put(VALID, validResources);
        resources.put(INVALID, invalidResources);
         return resources;
    }

    /**
     *
     */


    /**
     * Generate Google cloud storage file URL.
     * @param bucketPath
     * @return The blob url for the files in bucket
     */
    public static String buildBlobURL(String bucketPath) {
        return CloudStorageUtils.generatePath("gs://",cloudStorageUtils.getBucketName(), StorageConstants.SLASH, bucketPath);
    }

}

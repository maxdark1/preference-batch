package ca.homedepot.preference.config;

import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


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

}

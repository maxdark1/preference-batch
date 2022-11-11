package ca.homedepot.preference.config;

import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


public class StorageApplicationGCS {
   private static Storage storage;

   private static CloudStorageUtils cloudStorageUtils;
   public static Storage storage()
   {
       if(storage == null){
           storage = StorageOptions.getDefaultInstance().getService();
       }
       return storage;
   }

   public static String getBucketName(){
       return cloudStorageUtils.getBucketName();
   }
   public static boolean deleteObject(String folder, String filename){
       return cloudStorageUtils.deleteObject(folder, filename);
   }

    public static void setCloudStorageUtils(CloudStorageUtils cloudStorageUtils) {
        StorageApplicationGCS.cloudStorageUtils = cloudStorageUtils;
    }

}

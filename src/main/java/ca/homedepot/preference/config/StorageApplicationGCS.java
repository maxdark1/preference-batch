package ca.homedepot.preference.config;

import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.constants.StorageConstants;
import ca.homedepot.preference.util.validation.FileValidation;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INVALID;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.VALID;
import org.mozilla.universalchardet.UniversalDetector;

@UtilityClass
@Slf4j
public class StorageApplicationGCS
{
	private static Storage storage;

	private static CloudStorageUtils cloudStorageUtils;

	protected static String Encoding;

	/**
	 * Create the storage if it doesn't exist
	 * 
	 * @return Storage
	 */
	public static Storage storage()
	{
		if (storage == null)
		{
			storage = StorageOptions.getDefaultInstance().getService();
		}
		return storage;
	}

	public static void setStorage(Storage storage)
	{
		StorageApplicationGCS.storage = storage;
	}

	/**
	 * Gets the bucket name
	 * 
	 * @return bucket name
	 */
	public static String getBucketName()
	{
		return cloudStorageUtils.getBucketName();
	}

	/**
	 * Calls the cloud storage utils to delete an specific file
	 * 
	 * @param folder
	 * @param filename
	 * @return
	 */
	public static boolean deleteObject(String folder, String filename)
	{
		return cloudStorageUtils.deleteObject(folder, filename);
	}

	/**
	 * Sets the cloud storage utils
	 * 
	 * @param cloudStorageUtils
	 */
	public static void setCloudStorageUtils(CloudStorageUtils cloudStorageUtils)
	{
		StorageApplicationGCS.cloudStorageUtils = cloudStorageUtils;
	}

	/**
	 * Gets the files in folder
	 * 
	 * @param directory
	 * @return
	 */
	public static List<GoogleStorageResource> getGCPResource(String directory)
	{

		List<String> resources = cloudStorageUtils.listObjectInBucket(directory);
		if (!resources.isEmpty())
			resources.remove(0);
		return resources.stream().map(path -> new GoogleStorageResource(storage(), buildBlobURL(path)))
				.collect(Collectors.toList());
	}

	public List<GoogleStorageResource> validateEncoding(List<GoogleStorageResource> resourcesGCS) throws IOException
	{
		try
		{
			String encoding = getEncoding(resourcesGCS.get(0));
			log.error("FILE-ENCODING: " + encoding);
			for (int i = 0; i < resourcesGCS.size(); i++)
			{
				if (!encoding.equals(getEncoding(resourcesGCS.get(i))))
				{
					resourcesGCS.remove(i);
				}
			}
			Encoding = encoding;
		}
		catch (Exception ex)
		{
			log.error("PREFERENCE-BATCH-ERROR: Error During Encoding Validation " + ex.getMessage());
		}
		return resourcesGCS;
	}

	public String getEncoding(GoogleStorageResource resource)
	{
		String encoding = "";
		try
		{
			byte[] buff = new byte[4096];
			Blob blob = resource.getBlob();
			ReadChannel reader = blob.reader();

			java.io.InputStream fis = Channels.newInputStream(reader);
			UniversalDetector detector = new UniversalDetector();

			int nread;
			while ((nread = fis.read(buff)) > 0 && !detector.isDone())
			{
				detector.handleData(buff, 0, nread);
			}

			detector.dataEnd();

			encoding = detector.getDetectedCharset();
			fis.close();
			reader.close();
		}
		catch (Exception ex)
		{
			log.error("PREFERENCE-BATCH-ERROR: Error During Get File Encoding" + ex.getMessage());
		}
		return encoding;
	}

	public Map<String, List<Resource>> getsGCPResourceMap(String source, String folder)
	{

		Map<String, List<Resource>> resources = new HashMap<>();
		List<GoogleStorageResource> resourcesGCS = getGCPResource(folder);
		try
		{
			resourcesGCS = validateEncoding(resourcesGCS);
		}
		catch (Exception ex)
		{
			log.error("PREFERENCE-BATCH-ERROR: Encoding Validation" + ex.getMessage());
		}

		List<Resource> validResources = new ArrayList<>();
		List<Resource> invalidResources = new ArrayList<>();
		if (resourcesGCS != null)
		{
			List<String> files = resourcesGCS.stream().map(GoogleStorageResource::getBlobName).collect(Collectors.toList());

			int i = 0;
			for (String file : files)
			{
				file = file.replace(folder, "");

				if (FileValidation.validateFileName(file, source))
				{
					validResources.add(resourcesGCS.get(i));
				}
				else
				{
					invalidResources.add(resourcesGCS.get(i));
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
	 * 
	 * @param bucketPath
	 * @return The blob url for the files in bucket
	 */
	public static String buildBlobURL(String bucketPath)
	{

		return "gs://" + cloudStorageUtils.getBucketName() + StorageConstants.SLASH + bucketPath;
	}

	public static void moveObject(String filename, String blobTobeMove, String blobWhereToCopy) throws StorageException
	{
		cloudStorageUtils.moveObject(filename, blobTobeMove, blobWhereToCopy);
	}

}

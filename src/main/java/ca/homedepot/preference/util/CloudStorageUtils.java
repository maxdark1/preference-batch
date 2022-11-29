package ca.homedepot.preference.util;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static ca.homedepot.preference.config.StorageApplicationGCS.storage;

@Slf4j
@Getter
@Setter
@Service
public class CloudStorageUtils
{

	@Value("{gcp.project-id}")
	private String projectId;

	@Value("${gcp.bucket.name}")
	private String bucketName;

	/**
	 * Move object in GCP to other file
	 * 
	 * @param filename
	 *           the file name fo the file
	 * @param blobTobeMove
	 *           folder/filename
	 * @param blobWhereToCopy
	 *           folder where is the object being move / filename
	 */

	public void moveObject(String filename, String blobTobeMove, String blobWhereToCopy) throws StorageException
	{
		String fileRename = FileUtil.renameFile(filename);
		BlobId firstLocation = BlobId.of(bucketName, blobTobeMove);
		BlobId secondLocation = BlobId.of(bucketName, blobWhereToCopy.replace(filename, fileRename));


		Storage.BlobTargetOption precondition = Storage.BlobTargetOption.doesNotExist();
		storage().copy(Storage.CopyRequest.newBuilder().setSource(firstLocation).setTarget(secondLocation, precondition).build());
		storage().get(firstLocation).delete();
		log.info(" Moved file {} from {} to {} ", filename, blobTobeMove.replace(filename, ""),
				blobWhereToCopy.replace(fileRename, ""));

	}

	/**
	 * prefix is folder source (example: hybris, sfmc, crm....) and folder (example: /inbound/)
	 *
	 * @param prefix
	 * @return
	 */
	public List<String> listObjectInBucket(String... path)
	{
		List<String> listObjetInBucket = new ArrayList<>();
		Page<Blob> blobPage = storage().list(this.bucketName, Storage.BlobListOption.prefix(generatePath(path)),
				Storage.BlobListOption.currentDirectory());

		blobPage.getValues().forEach(blob -> listObjetInBucket.add(blob.getName()));
		return listObjetInBucket;
	}

	/**
	 * Delete object from bucket folder
	 * 
	 * @param folder
	 * @param filename
	 * @return if the object has been deleted
	 */
	public boolean deleteObject(String folder, String filename)
	{
		BlobId b = BlobId.of(bucketName, generatePath(folder, filename));
		return storage().delete(b);
	}

	/**
	 * Generates the path for a specific file
	 * 
	 * @param path
	 * @return the path
	 */

	public static String generatePath(String... path)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(path).forEach(sb::append);
		return sb.toString();
	}
}

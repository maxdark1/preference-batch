package ca.homedepot.preference.config;

import ca.homedepot.preference.util.CloudStorageUtils;
import ca.homedepot.preference.util.FileUtil;
import ca.homedepot.preference.util.validation.FileValidation;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class StorageApplicationGCSTest
{

	@Mock
	CloudStorageUtils cloudStorageUtils;

	@Mock
	Storage storage;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		cloudStorageUtils.setProjectId("ProjectId");
		cloudStorageUtils.setBucketName("BucketName");
		StorageApplicationGCS.setStorage(storage);
		StorageApplicationGCS.setCloudStorageUtils(cloudStorageUtils);
	}

	@Test
	void storage()
	{
		StorageApplicationGCS.setStorage(null);
		try (MockedStatic<StorageOptions> storageOptions = Mockito.mockStatic(StorageOptions.class))
		{
			StorageOptions storageOptionsMock = Mockito.mock(StorageOptions.class);
			storageOptions.when(StorageOptions::getDefaultInstance).thenReturn(storageOptionsMock);

		}
		assertNotNull(StorageApplicationGCS.storage());
	}

	@Test
	void getBucketName()
	{
		assertNull(StorageApplicationGCS.getBucketName());
	}

	@Test
	void deleteObject()
	{
		boolean delete = true;
		Mockito.when(cloudStorageUtils.deleteObject(anyString(), anyString())).thenReturn(delete);

		boolean afterMethodExecution = StorageApplicationGCS.deleteObject("folder/", "filename.txt");
		assertTrue(afterMethodExecution);
	}

	@Test
	void getsGCPResourceMap()
	{
		FileValidation.setHybrisBaseName("file_");
		FileValidation.setExtensionRegex(".txt||.TXT");
		FileUtil.setInbound("inbound/");
		FileUtil.setError("error/");
		FileUtil.setProcessed("processed/");
		FileUtil.setHybrisPath("hybris/");
		List<String> stringList = new ArrayList<>(
				List.of("hybris/", "hybris/inbound/file_20221122.txt", "hybris/inbound/test.txt"));
		Mockito.when(cloudStorageUtils.listObjectInBucket("hybris/inbound/")).thenReturn(stringList);
		Mockito.doNothing().when(cloudStorageUtils).moveObject(anyString(), anyString(), anyString());

		assertNotNull(StorageApplicationGCS.getsGCPResourceMap("hybris", "hybris/inbound/"));
	}

	@Test
	void buildBlobURL()
	{
		String path = "folder/file.txt";
		String result = StorageApplicationGCS.buildBlobURL(path);

		assertNotEquals(path, result);
	}

	@Test
	void moveObject()
	{
		String filename = "filename.txt", blobToMove = "blob/filename.txt", blobToCopy = "blob2/filename.txt";
		Mockito.doNothing().when(cloudStorageUtils).moveObject(anyString(), anyString(), anyString());

		StorageApplicationGCS.moveObject(filename, blobToMove, blobToCopy);
		Mockito.verify(cloudStorageUtils).moveObject(filename, blobToMove, blobToCopy);
	}
}
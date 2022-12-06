package ca.homedepot.preference.util;

import ca.homedepot.preference.config.StorageApplicationGCS;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CloudStorageUtilsTest
{

	@Mock
	Storage storage;

	@Mock
	Page<Blob> blobPage;

	@InjectMocks
	@Spy
	CloudStorageUtils cloudStorageUtils;

	List<String> listObjects = new ArrayList<>(List.of("path/file_YYYYMMDD.txt", "path/test.txt"));

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		cloudStorageUtils.setBucketName("BucketName");
		cloudStorageUtils.setProjectId("ProjectId");

		StorageApplicationGCS.setStorage(storage);
	}

	@Test
	void moveObject()
	{
		String filename = "test.txt", blobToMove = "folder/test.txt", blobWhereToCopy = "folder2/test.txt";
		Blob blob = Mockito.mock(Blob.class);
		CopyWriter copyWriter = Mockito.mock(CopyWriter.class);
		Boolean delete = true;
		Mockito.when(storage.copy(any(Storage.CopyRequest.class))).thenReturn(copyWriter);
		Mockito.when(storage.get(any(BlobId.class))).thenReturn(blob);
		Mockito.when(blob.delete()).thenReturn(delete);

		cloudStorageUtils.moveObject(filename, blobToMove, blobWhereToCopy);
		Mockito.verify(cloudStorageUtils).moveObject(filename, blobToMove, blobWhereToCopy);
	}

	@Test
	void deleteObject()
	{
		boolean delete = true;
		Mockito.when(storage.delete(any(BlobId.class))).thenReturn(delete);
		assertTrue(cloudStorageUtils.deleteObject("path/", "filename.txt"));
	}

	@Test
	void listObjectInBucket()
	{
		Mockito.when(storage.list("BucketName", Storage.BlobListOption.prefix("path/"), Storage.BlobListOption.currentDirectory()))
				.thenReturn(blobPage);
		assertNotNull(cloudStorageUtils.listObjectInBucket("path/"));
	}

	@Test
	void generatePath()
	{
		String result = "path/folder";
		assertEquals(result, CloudStorageUtils.generatePath("path/", "folder"));
	}

	@Test
	void getProjectId()
	{
		assertEquals("ProjectId", cloudStorageUtils.getProjectId());
	}

	@Test
	void getBucketName()
	{
		assertEquals("BucketName", cloudStorageUtils.getBucketName());
	}
}
package ca.homedepot.preference.util;

import ca.homedepot.preference.util.validation.FileValidation;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * The type File util.
 */
@Slf4j
@UtilityClass
public final class FileUtil
{
	private static String registrationFile;
	private static String fileExtTargetEmail;
	private static String hybrisPath;
	private static String crmPath;
	private static String fbSfmcPath;

	private static String sfmcPath;

	private static String inbound;

	private static String error;

	private static String processed;

	/**
	 * Gets registration file.
	 *
	 * @return the registration file
	 */
	public static String getRegistrationFile()
	{
		return registrationFile;
	}

	/**
	 * Sets registration file.
	 *
	 * @param registrationFile
	 *           the registration file
	 */
	public static void setRegistrationFile(String registrationFile)
	{
		FileUtil.registrationFile = registrationFile;
	}

	/**
	 * Gets emailanalytics file.
	 *
	 * @return the emailanalytics file
	 */
	public static String getFileExtTargetEmail()
	{
		return fileExtTargetEmail;
	}

	/**
	 * Sets emailanalytics file.
	 *
	 * @param fileExtTargetEmail
	 *           the emailanalytics file
	 */
	public static void setFileExtTargetEmail(String fileExtTargetEmail)
	{
		FileUtil.fileExtTargetEmail = fileExtTargetEmail;
	}

	/**
	 * Sets hyrbis path
	 *
	 * @param hybrisPath
	 */
	public static void setHybrisPath(String hybrisPath)
	{
		FileUtil.hybrisPath = hybrisPath;
	}

	/**
	 * get CrmPath
	 *
	 * @return
	 */
	public static String getCrmPath()
	{
		return crmPath;
	}

	/**
	 * Sets CRM path
	 *
	 * @param crmPath
	 */
	public static void setCrmPath(String crmPath)
	{
		FileUtil.crmPath = crmPath;
	}

	/**
	 * Gets inbound folder
	 *
	 * @return inbound folder
	 */
	public static String getInbound()
	{
		return inbound;
	}

	/**
	 * Sets inbound folder
	 *
	 * @param inbound
	 */
	public static void setInbound(String inbound)
	{
		FileUtil.inbound = inbound;
	}

	/**
	 * Gets error folder location
	 *
	 * @return error folder location
	 */
	public static String getError()
	{
		return error;
	}

	/**
	 * Sets error folder location
	 *
	 * @param error
	 */
	public static void setError(String error)
	{
		FileUtil.error = error;
	}

	/**
	 * Gets processed folder
	 *
	 * @return
	 */
	public static String getProcessed()
	{
		return processed;
	}

	/**
	 * Sets processed
	 *
	 * @param processed
	 */
	public static void setProcessed(String processed)
	{
		FileUtil.processed = processed;
	}

	/**
	 * Gets sfmc path
	 *
	 * @return sfmc path
	 */
	public static String getSfmcPath()
	{
		return sfmcPath;
	}

	/**
	 * Sets SFMC path
	 *
	 * @param sfmcPath
	 */
	public static void setSfmcPath(String sfmcPath)
	{
		FileUtil.sfmcPath = sfmcPath;
	}

	/**
	 * Gets FB SFMC path
	 *
	 * @returnfb sfcm path
	 */
	public static String getFbsfmcPath()
	{
		return fbSfmcPath;
	}

	/**
	 * Sets fbsfmc path
	 *
	 * @param fbsfmcPath
	 */
	public static void setFbsfmcPath(String fbsfmcPath)
	{
		FileUtil.fbSfmcPath = fbsfmcPath;
	}

	/**
	 * Moves the current file that has being read to ERROR or PROCESSED folder
	 *
	 * @param file
	 * @param status
	 * @param valueVal
	 * @throws IOException
	 */
	public static void moveFile(String file, boolean status, String valueVal) throws IOException
	{
		String folder = status ? processed : error;
		String fileName = FileValidation.getFileName(file);
		String source = isFBSFMC(fileName, valueVal) ? "FB_SFMC" : valueVal;

		String path = getPath(source);
		String newFile = renameFile(file);
		File file1 = new File(path + inbound + file);

		/**
		 * If the files was renamed then... move file to Processed or Error depending on the process result
		 */
		if (file1.renameTo(new File(path + inbound + newFile)))
		{

			Path temp = Files.move(Paths.get(path + inbound + newFile), Paths.get(path + folder + "\\" + newFile));

			if (temp != null)
			{
				log.info(" File {} moved successfully to folder: {} ", file, folder);
			}
			else
			{
				log.info(" Failed to move the file {} ", file);
			}
		}
	}

	/**
	 * To validate that the file being read is FB SFMC
	 *
	 * @param fileName
	 * @return
	 */

	public static boolean isFBSFMC(String fileName, String source)
	{
		return fileName.contains(FileValidation.getFbSFMCBaseName()) && fbSfmcPath.equals(source);
	}

	/**
	 * To rename the file
	 *
	 * @param file
	 *           's name
	 * @return new file name
	 */
	public static String renameFile(String file)
	{
		String baseName = "";
		String extension = "";
		String[] splittedName = file.split("\\.");
		if (splittedName.length > 0)
		{
			for (int i = 0; i < (splittedName.length - 1); i++)
			{
				baseName += splittedName[i] + ".";
			}
			baseName = baseName.substring(0, baseName.length() - 1);
			extension = splittedName[splittedName.length - 1];
		}
		else
		{
			baseName = file;
		}

		return baseName + "_" + (new SimpleDateFormat("yyyyMMSSHHmmssSSSS")).format(new Date()) + "." + extension;
	}

	/**
	 * Gets path
	 */
	public static String getPath(String valueVal)
	{
		switch (valueVal)
		{
			case "hybris":
				return hybrisPath;
			case "CRM":
				return crmPath;
			case "SFMC":
				return sfmcPath;
			default:
				return fbSfmcPath;
		}

	}

	/**
	 * Gets the files that are in certaing folder
	 *
	 * @param path
	 * @param source
	 * @return files in a folder
	 */
	public static Map<String, List<Resource>> getFilesOnFolder(String path, String source)
	{
		File folder = new File(path);
		String[] files = folder.list();

		List<Resource> validFilesNames = new ArrayList<>();
		List<Resource> invalidFileNames = new ArrayList<>();
		if (files != null)
			for (String fileName : files)
			{
				/**
				 * Validate files names that are contain in that folder
				 */
				if (FileValidation.validateFileName(fileName, source))
				{
					/**
					 * It saves all the files that are valid and can be processed
					 */
					validFilesNames.add(new FileSystemResource(path + fileName));
				}
				else
				{
					/**
					 * Saves all the files that are not valid and cannot be processed
					 */
					invalidFileNames.add(new FileSystemResource(fileName));
					log.error(" File name invalid: " + fileName);
					try
					{
						/**
						 * Moves the file, with error name to ERROR folder
						 */
						moveFile(fileName, false, source);
					}
					catch (IOException e)
					{
						//TODO what should happen in case of exception
						// Make the Job status failed
						log.error(" Exception occurs moving file {}: {}", fileName, e.getMessage());
					}
				}
			}
		/**
		 * Returns all the files found on the folder
		 */
		Map<String, List<Resource>> filesNames = new HashMap<>();
		filesNames.put("VALID", validFilesNames);
		filesNames.put("INVALID", invalidFileNames);
		return filesNames;
	}



	public static File createTempFile(String filename){
		File file = null;
		try
		{
			String suffix = getSuffix(filename);
			file = File.createTempFile(getPrefix(suffix, filename), suffix);
		}catch(IOException ex){
			log.error(ex.getMessage());
		}
		return file;
	}

	public static String getSuffix(String file)
	{
		return file.substring(getLastPointIndex(file));
	}
	public static String getPrefix(String suffix, String file)
	{
		return file.replace(suffix, "");
	}
	public static int getLastPointIndex(String file){
		int point = file.indexOf(".");
		String[] fileArray = file.split("\\.");
		if(fileArray[1].matches("\\d+")){
			return file.length() - fileArray[0].length();
		}
		return point;
	}

}

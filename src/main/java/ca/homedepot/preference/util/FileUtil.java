package ca.homedepot.preference.util;

import ca.homedepot.preference.config.SchedulerConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * The type File util.
 */
@Slf4j
@UtilityClass
public final class FileUtil
{
    private static String registrationFile;
    private static String fileExtTargetEmail;
    private static String path;

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
     * @param fileExtTargetEmail
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

    public static void setPath(String path)
    {
        FileUtil.path = path;
    }

    public static void moveFile(String file, String folder) throws IOException
    {
        Path temp = Files.move(
                Paths.get(path+"\\INBOUND\\"+file),
                Paths.get(path+"\\"+folder+"\\"+file)
        );

        if(temp != null){
            log.info(" File moved successfully to folder: {} ", folder);
        }else{
            log.info("Failed to move the file");
        }
    }
}

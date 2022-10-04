package ca.homedepot.preference.util;

import ca.homedepot.preference.util.validation.FileValidation;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    public static void setHybrisPath(String hybrisPath)
    {
        FileUtil.hybrisPath = hybrisPath;
    }

    public static String getCrmPath() {
        return crmPath;
    }

    public static void setCrmPath(String crmPath) {
        FileUtil.crmPath = crmPath;
    }

    public static String getInbound() {
        return inbound;
    }

    public static void setInbound(String inbound) {
        FileUtil.inbound = inbound;
    }

    public static String getError() {
        return error;
    }

    public static void setError(String error) {
        FileUtil.error = error;
    }

    public static String getProcessed() {
        return processed;
    }

    public static void setProcessed(String processed) {
        FileUtil.processed = processed;
    }

    public static void moveFile(String file, boolean status, String value_val) throws IOException
    {
        String folder = ((status)?processed:error);
        String path = getPath(value_val);
        Path temp = Files.move(
                Paths.get(path +inbound+file),
                Paths.get(path +folder+"\\"+file)
        );

        if(temp != null){
            log.info(" File moved successfully to folder: {} ", folder);
        }else{
            log.info("Failed to move the file");
        }
    }

    /*
    * Get path
    * */

    public static String getPath(String value_val)
    {
        switch (value_val){
            case "hybris":
                return hybrisPath;
            case "CRM":
                return crmPath;
        }
        return null;
    }

    /*
        List of files in certain folder
    * */
    public static List<String> getFilesOnFolder(String path, String baseName, String source)
    {
        File folder = new File(path);
        List<String> listOfFiles = new ArrayList<>();
        String[] files = folder.list();
        System.out.println(path);

        if(files != null)
            for (String fileName: files) {

                if(FileValidation.validateFileName(fileName, baseName)){
                    listOfFiles.add(path+fileName);
                }else{
                    log.error( " File name invalid: " + fileName);
                    try {
                        moveFile(fileName, false, source);
                    } catch (IOException e) {
                        log.error(" Exception occurs moving file {}: {}", fileName, e.getMessage());
                    }
                }
            }

        return listOfFiles;
    }


}

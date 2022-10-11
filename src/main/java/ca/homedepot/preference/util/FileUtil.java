package ca.homedepot.preference.util;

import ca.homedepot.preference.util.validation.FileValidation;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    public static String getSfmcPath() {
        return sfmcPath;
    }

    public static void setSfmcPath(String sfmcPath) {
        FileUtil.sfmcPath = sfmcPath;
    }

    public static void moveFile(String file, boolean status, String value_val) throws IOException
    {
        String folder = ((status)?processed:error);
        String path = getPath(value_val);
        String newFile = renameFile(file);

        File file1 = new File(path +inbound+file);
        file1.renameTo(new File(path +inbound+newFile ));


        Path temp = Files.move(
                Paths.get(path +inbound+newFile),
                Paths.get(path +folder+"\\"+newFile)
        );

        if(temp != null){
            log.info(" File {} moved successfully to folder: {} ", temp.getFileName(), folder);
        }else{
            log.info(" Failed to move the file {} ", temp.getFileName());
        }
    }

    public static String renameFile(String file){
        String baseName = FileValidation.getFileName(file);
        String extension = FileValidation.getExtension(file, baseName);
        return baseName+"_"+(new SimpleDateFormat("YYYYMMSSHHmmssSSSS")).format(new Date()) +extension;
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
            case "SFMC":
                return sfmcPath;
        }
        return null;
    }

    /*
        List of files in certain folder
    * */
    public static List<String> getFilesOnFolder(String path, String source)
    {
        File folder = new File(path);
        List<String> listOfFiles = new ArrayList<>();
        String[] files = folder.list();

        if(files != null)
            for (String fileName: files) {

                if(FileValidation.validateFileName(fileName, source)){
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

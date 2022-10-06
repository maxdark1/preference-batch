package ca.homedepot.preference.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@Slf4j
public class FileValidation {

    private static String hybrisBaseName;

    private static String fbSFMCBaseName;

    private static String sfmcBaseName;


    public static String getHybrisBaseName() {
        return hybrisBaseName;
    }

    public static void setHybrisBaseName(String hybrisBaseName) {
        FileValidation.hybrisBaseName = hybrisBaseName;
    }

    public static String getFbSFMCBaseName() {
        return fbSFMCBaseName;
    }

    public static void setFbSFMCBaseName(String fbSFMCBaseName) {
        FileValidation.fbSFMCBaseName = fbSFMCBaseName;
    }

    public static String getSfmcBaseName() {
        return sfmcBaseName;
    }

    public static void setSfmcBaseName(String sfmcBaseName) {
        FileValidation.sfmcBaseName = sfmcBaseName;
    }

    public static LineCallbackHandler lineCallbackHandler(String[] headerFile, String separator)
    {
        return line -> {
            String[] header = line.split(separator);
            if (!Arrays.equals(header, headerFile))
                throw  new ValidationException(" Invalid header {}: " + Arrays.toString(header));
        };
    }
    public static boolean validateFileName(String fileName, String source){
        String formatDate = "yyyyMMdd";
        int start = getBaseName(source).length(), end = start+formatDate.length();
        if(end > fileName.length())
        {
            return false;
        }
        return validateSimpleFileDateFormat(fileName.substring(start, end), formatDate);
    }
    public static String getFileName(String fileName){
        return fileName.replaceAll(".txt.AXOSTD|.TXT.THD.txt.gpg|.pgp|.txt|.TXT", "");
    }

    public static String getExtension(String fileName, String baseName){
        return  fileName.replaceAll(baseName, "");
    }

    public static String getBaseName(String source){

        switch (source){
            case "FB_SFMC":
                return fbSFMCBaseName;
            case "SFMC":
                return sfmcBaseName;
        }
        return hybrisBaseName;
    }
    public static boolean validateSimpleFileDateFormat(String date, String formatDate){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
            simpleDateFormat.parse(date);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

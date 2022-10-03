package ca.homedepot.preference.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@Slf4j
public class FileValidation {

    public static LineCallbackHandler lineCallbackHandler( String[] headerFile, String separator)
    {
        return line -> {
            String[] header = line.split(separator);
            if (!Arrays.equals(header, headerFile))
                throw  new ValidationException(" Invalid header {}: " + Arrays.toString(header));
        };
    }
    public static boolean validateFileName(String fileName, String baseName){
        String formatDate = "yyyyMMdd";
        int start = baseName.length(), end = start+formatDate.length();
        if(end > fileName.length())
        {
            return false;
        }
        return validateSimpleFileDateFormat(fileName.substring(start, end), formatDate);
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

package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileValidationTest {

    @BeforeAll
    static void setup(){
        FileValidation.setFbSFMCBaseName("OPTIN_STANDARD_FLEX_GCFB_");
        FileValidation.setHybrisBaseName("OPTIN_STANDARD_FLEX_");
        FileValidation.setSfmcBaseName("ET.CAN.");
    }
    @Test
    void lineCallbackHandler() {
        LineCallbackHandler lineCallbackHandler = FileValidation.lineCallbackHandler(new String[]{"STUFF1", "STUFF2"}, "\\|");

        ValidationException exception = assertThrows(ValidationException.class, ()-> lineCallbackHandler.handleLine("a|a"));
        assertTrue(exception.getMessage().contains("Invalid"));
    }

    @Test
    void validateFileName() {
        String source ="hybris";
        String fileNameRightOne = "OPTIN_STANDARD_FLEX_20221202", fileNameWrongOne = "EXAMPLE";

        boolean fileNameCorrect = FileValidation.validateFileName(fileNameRightOne, source),
        fileNameWrong = FileValidation.validateFileName(fileNameWrongOne, source);

        assertTrue(fileNameCorrect);
        assertTrue(!fileNameWrong);
    }

    @Test
    void validateSimpleFileDateFormat() {
        String format = "yyyyMMdd";
        String date = "2/2/2022";

        assertTrue(!FileValidation.validateSimpleFileDateFormat(date, format));
    }

    @Test
    void getFileExtension(){
        String baseName = "OPTIN_STANDARD_FLEX_YYYYMMDD";
        String fileName = "OPTIN_STANDARD_FLEX_YYYYMMDD.txt.AXOSTD";


       assertEquals(".txt.AXOSTD", FileValidation.getExtension(fileName, baseName));
    }

    @Test
    void getFileName(){
        String file = "ET.CAN.YYYYMMDD.TXT";
        System.out.println( FileValidation.getFileName(file));
        assertEquals("ET.CAN.YYYYMMDD", FileValidation.getFileName(file));
    }
}
package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileValidationTest {

    @Test
    void lineCallbackHandler() {
        LineCallbackHandler lineCallbackHandler = FileValidation.lineCallbackHandler(new String[]{"STUFF1", "STUFF2"}, "\\|");

        ValidationException exception = assertThrows(ValidationException.class, ()-> lineCallbackHandler.handleLine("a|a"));
        assertTrue(exception.getMessage().contains("Invalid"));
    }

    @Test
    void validateFileName() {
        String baseName ="EXAMPLE_";
        String fileNameRightOne = "EXAMPLE_20221202", fileNameWrongOne = "EXAMPLE";

        boolean fileNameCorrect = FileValidation.validateFileName(fileNameRightOne, baseName),
        fileNameWrong = FileValidation.validateFileName(fileNameWrongOne, baseName);

        assertTrue(fileNameCorrect);
        assertTrue(!fileNameWrong);
    }

    @Test
    void validateSimpleFileDateFormat() {
        String format = "yyyyMMdd";
        String date = "2/2/2022";

        assertTrue(!FileValidation.validateSimpleFileDateFormat(date, format));
    }
}
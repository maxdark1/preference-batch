package ca.homedepot.preference.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @Test
    void getRegistrationFile() {
        FileUtil.setRegistrationFile("registrationFile");
        assertEquals("registrationFile", FileUtil.getRegistrationFile());

    }

    @Test
    void getFileExtTargetEmail() {

        FileUtil.setFileExtTargetEmail("fileExtTargetEmail");
        assertEquals("fileExtTargetEmail", FileUtil.getFileExtTargetEmail());
    }
}
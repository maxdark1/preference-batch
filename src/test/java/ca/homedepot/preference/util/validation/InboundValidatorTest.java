package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InboundValidatorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void lineCallbackHandler() {
    }

    @Test
    void validateMaxLengthNotReq() {
    }

    @Test
    void validateMaxLength() {
    }

    @Test
    void validateLanguagePref() {
    }

    @Test
    void validateEmailFormat() {
    }

    @Test
    void validateDateFormat() {
        String dateValida = "02-02-2022 5:55:60";

        InboundValidator.validateDateFormat(dateValida);

        String invalido = "2-2-2022 54:54:65";

        InboundValidator.validateDateFormat(invalido);
    }

    @Test
    void validateNumberFormat() {
    }

    @Test
    void validateIsNumber() {
    }

    @Test
    void validValue_Number() {
    }

    @Test
    void validateIsRequired() {
    }

    @Test
    void validateRequired() {
    }
}
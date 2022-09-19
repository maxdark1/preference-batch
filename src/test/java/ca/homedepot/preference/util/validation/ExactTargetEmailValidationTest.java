package ca.homedepot.preference.util.validation;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.validator.ValidationException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ExactTargetEmailValidationTest {

    // TODO this might change in the future, these are ACXIOM values
    @Test
    void getExactTargetStatus() {
        String statusUnsubscribed = "unsubscribed", statusHeld = "held";
        String validationUnsubscribed = ExactTargetEmailValidation.getExactTargetStatus(statusUnsubscribed);
        String validationHeld = ExactTargetEmailValidation.getExactTargetStatus(statusHeld);
        assertEquals("98", validationUnsubscribed);
        assertEquals("50", validationHeld);
    }

    @Test
    void validateStatusEmail() {
        String validEmailStatus = "Unsubscribed";
        String invalidEmailStatus = "e.com";

        ValidationException exception = assertThrows(ValidationException.class,()->{ExactTargetEmailValidation.validateStatusEmail(invalidEmailStatus);});
        assertDoesNotThrow(()->ExactTargetEmailValidation.validateStatusEmail(validEmailStatus));
        assertTrue(exception.getMessage().contains(invalidEmailStatus));
    }

    // TODO this might change in the future, these are ACXIOM values
    @Test
    void getSourceId() {
        String reasonAOL = "SOME TEXT AOL";
        String reasonSCAM = "REASON WHY I DONT WANT THIS OR NOT SCAMCOP";
        String reasonSPAMCOPREPORT = "SPAM COP REPORT";

        String sourceIDAOL = ExactTargetEmailValidation.getSourceId(reasonAOL);
        String sourceIDSCAMCOMP = ExactTargetEmailValidation.getSourceId(reasonSCAM);
        String sourceSPAMCOPREPORT = ExactTargetEmailValidation.getSourceId(reasonSPAMCOPREPORT);
        String sourceIDWhatEver = ExactTargetEmailValidation.getSourceId(null);
        String sourceIDReason = ExactTargetEmailValidation.getSourceId("email");

        assertEquals("189", sourceIDAOL);
        assertEquals("190", sourceIDSCAMCOMP);
        assertEquals("190", sourceIDSCAMCOMP);
        assertEquals("188", sourceIDWhatEver);
        assertEquals("188", sourceIDReason);
    }

    @Test
    void validateDateFormat() {
        String dateFormat1 = "09/19/2022 7 :49",
        dateFormat2 = "09/19/2022 16:22",
        dateFormat3 = "9/19/2022 15:2",
        dateFormat4 = "09/10/2022 1 :2",
        invalidDateFormat = "2-2-2";

        ValidationException exception =assertThrows(ValidationException.class,()-> {ExactTargetEmailValidation.validateDateFormat(invalidDateFormat);});

        assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat1));
        assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat2));
        assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat3));
        assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat4));
        assertTrue(exception.getMessage().contains(invalidDateFormat));
    }
}
package ca.homedepot.preference.util.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

class ExactTargetEmailValidationTest
{

	@Test
	void lineCallbackHandler()
	{
		LineCallbackHandler lineCallbackHandler = ExactTargetEmailValidation.lineCallbackHandler();

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			lineCallbackHandler.handleLine(
					"Language_Preference|AsOfDate|Email_Address|Email_Permission|Phone_Permission|Phone_Number|Phone_Extension|Title|First_Name|Last_Name|Address_1|Address_2|City|Province|Postal_Code|Mail_Permission|EmailPrefHDCA|GardenClub|EmailPrefPRO|NewMover|For_Future_Use|Source_ID|SMS_Flag|Fax_Number|Fax_Extension|Content_1|Value_1|Content_2|Value_2|Content_3|Value_3|Content_4|Value_4|Content_5|Value_5|Content_6|Value_6|Content_7|Value_7|Content_8|Value_8|Content_9|Value_9|Content_10|Value_10|Content_11|Value_11|Content_12|Value_12|Content_13|Value_13|Content_14|Value_14|Content_15|Value_15|Content_16|Value_16|Content_17|Value_17|Content_18|Value_18|Content_19|Value_19|Content_20");
		});

		assertTrue(exception.getMessage().contains("Invalid header"));
		assertNotNull(lineCallbackHandler);
	}

	// TODO this might change in the future, these are ACXIOM values
	@Test
	void getExactTargetStatus()
	{
		String statusUnsubscribed = "unsubscribed", statusHeld = "held";
		String validationUnsubscribed = ExactTargetEmailValidation.getExactTargetStatus(statusUnsubscribed);
		String validationHeld = ExactTargetEmailValidation.getExactTargetStatus(statusHeld);
		assertEquals("98", validationUnsubscribed);
		assertEquals("50", validationHeld);
	}

	@Test
	void validateStatusEmail()
	{
		String validEmailStatus = "Unsubscribed";
		String invalidEmailStatus = "e.com";

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			ExactTargetEmailValidation.validateStatusEmail(invalidEmailStatus);
		});
		assertDoesNotThrow(() -> ExactTargetEmailValidation.validateStatusEmail(validEmailStatus));
		assertTrue(exception.getMessage().contains(invalidEmailStatus));
	}

	// TODO this might change in the future, these are ACXIOM values
	@Test
	void getSourceId()
	{
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
	void validateDateFormat()
	{
		String dateFormat1 = "09/19/2022 7 :49", dateFormat2 = "09/19/2022 16:22", dateFormat3 = "9/19/2022 15:2",
				dateFormat4 = "09/10/2022 1 :2", invalidDateFormat = "2-2-2";

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			ExactTargetEmailValidation.validateDateFormat(invalidDateFormat);
		});

		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat1));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat2));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat3));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat4));
		assertTrue(exception.getMessage().contains(invalidDateFormat));
	}
}
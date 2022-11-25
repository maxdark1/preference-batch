package ca.homedepot.preference.util.validation;

import static org.junit.jupiter.api.Assertions.*;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.util.*;

class ExactTargetEmailValidationTest
{
	@BeforeEach
	void setup()
	{
		List<Master> masterList = new ArrayList<>();

		masterList.add(new Master(new BigDecimal("1"), BigDecimal.ONE, "SOURCE", "CRM", true, null));
		masterList.add(new Master(new BigDecimal("2"), BigDecimal.ONE, "SOURCE", "hybris", true, null));
		masterList.add(new Master(new BigDecimal("3"), BigDecimal.ONE, "SOURCE", "manual_update", true, null));
		masterList.add(new Master(new BigDecimal("4"), BigDecimal.ONE, "SOURCE", "citi_bank", true, null));
		masterList.add(new Master(new BigDecimal("5"), BigDecimal.ONE, "SOURCE", "SFMC", true, null));
		masterList.add(new Master(new BigDecimal("21"), BigDecimal.ONE, "SOURCE_ID", "EXACT TARGET OPT OUT -CAN", true,
				new BigDecimal("188")));
		masterList.add(new Master(new BigDecimal("22"), BigDecimal.ONE, "SOURCE_ID", "EXACT TARGET AOL OPT OUT -CAN", true,
				new BigDecimal("189")));
		masterList.add(new Master(new BigDecimal("23"), BigDecimal.ONE, "SOURCE", "EXACT TARGET OPT OUT OTH-CAN", true, null));
		masterList
				.add(new Master(new BigDecimal("24"), BigDecimal.TEN, "EMAIL_STATUS", "Hard Bounces", true, new BigDecimal("50")));
		masterList
				.add(new Master(new BigDecimal("25"), BigDecimal.TEN, "EMAIL_STATUS", "ET SPAM List", true, new BigDecimal("98")));
		MasterProcessor.setMasterList(masterList);
	}

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

	@Test
	void getExactTargetStatus()
	{
		String statusUnsubscribed = "unsubscribed", statusHeld = "held";
		String validationUnsubscribed = ExactTargetEmailValidation.getExactTargetStatus(statusUnsubscribed).toPlainString();
		String validationHeld = ExactTargetEmailValidation.getExactTargetStatus(statusHeld).toPlainString();
		assertEquals("25", validationUnsubscribed);
		assertEquals("24", validationHeld);
	}

	@Test
	void validateStatusEmail()
	{
		StringBuilder error = new StringBuilder();
		String validEmailStatus = "Unsubscribed";
		String invalidEmailStatus = "e.com";
		ExactTargetEmailValidation.validateStatusEmail(invalidEmailStatus, error);
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});
		StringBuilder notError = new StringBuilder();
		ExactTargetEmailValidation.validateStatusEmail(validEmailStatus, notError);
		assertDoesNotThrow(() -> InboundValidator.isValidationsErros(notError));
		assertTrue(exception.getMessage().contains(invalidEmailStatus));
	}

	@Test
	void getSourceId()
	{
		String reasonAOL = "SOME TEXT AOL";
		String reasonSCAM = "REASON WHY I DONT WANT THIS OR NOT SCAMCOP";
		String reasonSPAMCOPREPORT = "SPAM COP REPORT";

		BigDecimal sourceIDAOL = ExactTargetEmailValidation.getSourceId(reasonAOL);
		BigDecimal sourceIDSCAMCOMP = ExactTargetEmailValidation.getSourceId(reasonSCAM);
		BigDecimal sourceSPAMCOPREPORT = ExactTargetEmailValidation.getSourceId(reasonSPAMCOPREPORT);
		BigDecimal sourceIDWhatEver = ExactTargetEmailValidation.getSourceId(null);
		BigDecimal sourceIDReason = ExactTargetEmailValidation.getSourceId("email");

		assertEquals(new BigDecimal("22"), sourceIDAOL);
		assertEquals(new BigDecimal("23"), sourceSPAMCOPREPORT);
		assertEquals(new BigDecimal("23"), sourceIDSCAMCOMP);
		assertEquals(new BigDecimal("21"), sourceIDWhatEver);
		assertEquals(new BigDecimal("21"), sourceIDReason);
	}

	@Test
	void validateDateFormat()
	{
		String dateFormat1 = "09/19/2022 7 :49", dateFormat2 = "09/19/2022 16:22", dateFormat3 = "9/19/2022 15:2",
				dateFormat4 = "09/10/2022 1 :2", invalidDateFormat = "2-2-2";

		StringBuilder error = new StringBuilder();
		ExactTargetEmailValidation.validateDateFormat(invalidDateFormat, error);

		ValidationException exception = assertThrows(ValidationException.class, () -> {
			InboundValidator.isValidationsErros(error);
		});

		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat1, error));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat2, error));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat3, error));
		assertNotNull(ExactTargetEmailValidation.validateDateFormat(dateFormat4, error));
		assertTrue(exception.getMessage().contains(invalidDateFormat));
	}
}
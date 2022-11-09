package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.util.Date;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INSERTEDBY;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.NOTSTARTED;
import static ca.homedepot.preference.dto.enums.Preference.*;

@Slf4j
public class ExactTargetEmailProcessor implements ItemProcessor<EmailOptOuts, FileInboundStgTable>
{

	/**
	 * Process the item from LayoutB (SFMC)
	 * 
	 * @param item
	 *           to be processed
	 * @return File Inbound Staging table object ready to insert on persistence
	 */
	@Override
	public FileInboundStgTable process(EmailOptOuts item)
	{

		FileInboundStgTable.FileInboundStgTableBuilder builder = FileInboundStgTable.builder();

		log.info(" Item in process: {}", item.toString());

		/**
		 * This saves all Validation's error messages If there are any
		 */
		StringBuilder error = new StringBuilder();
		Date srcDate = null;
		try
		{
			InboundValidator.validateRequired(item.getEmailAddress(), "email address", error);
			InboundValidator.validateRequired(item.getStatus(), "status", error);
			InboundValidator.validateRequired(item.getDateUnsubscribed(), "Date Unsubscribed", error);
			InboundValidator.validateMaxLength("Email Address", item.getEmailAddress(), 150, error);
			ExactTargetEmailValidation.validateStatusEmail(item.getStatus(), error);
			srcDate = ExactTargetEmailValidation.validateDateFormat(item.getDateUnsubscribed(), error);

			InboundValidator.validateEmailFormat(item.getEmailAddress(), error);

			/**
			 * Throws an exception if it finds any Error message on StringBuilder container
			 */
			InboundValidator.isValidationsErros(error);
		}
		catch (ValidationException e)
		{
			log.error(" Validation error: {} ", e.getMessage());
			/**
			 * Throws the exception again after is being log This is catch on the Skipper of LayoutB
			 */
			throw e;
		}

		/**
		 * Create the object if every field is valid
		 */
		return builder.srcEmailAddress(item.getEmailAddress()).fileName(item.getFileName())
				.sourceId(ExactTargetEmailValidation.getSourceId(item.getReason()))
				.emailStatus(ExactTargetEmailValidation.getExactTargetStatus(item.getStatus())).status(NOTSTARTED).srcDate(srcDate)
				.emailAddressPref(NUMBER_0.getValue()).emailPrefHdCa(NUMBER_0.getValue())
				.emailPrefGardenClub(NUMBER_MINUS_1.getValue()).emailPrefPro(NUMBER_MINUS_1.getValue())
				.emailPrefNewMover(NUMBER_MINUS_1.getValue()).insertedBy(INSERTEDBY).insertedDate(new Date()).build();
	}
}

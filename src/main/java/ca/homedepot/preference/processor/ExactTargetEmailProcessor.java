package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.constants.StorageConstants;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.util.Date;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INSERTEDBY;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.NOTSTARTED;
import static ca.homedepot.preference.dto.enums.Preference.NUMBER_0;
import static ca.homedepot.preference.dto.enums.Preference.NUMBER_MINUS_1;

@Slf4j
public class ExactTargetEmailProcessor implements ItemProcessor<EmailOptOuts, FileInboundStgTable>
{

	private int count = 0;

	private String fileName = "";

	public void setCount(int count)
	{
		this.count = count;
	}

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
		if (fileName.equals(""))
		{
			fileName = item.getFileName();
		}
		else if (!fileName.equals(item.getFileName()))
		{
			count = 0;
		}

		count++;
		FileInboundStgTable.FileInboundStgTableBuilder builder = FileInboundStgTable.builder();


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
			log.error(
					" PREFERENCE BATCH VALIDATION ERROR - The record # {} has the above fields with validation error on file {}: {} ",
					count, item.getFileName().substring(item.getFileName().lastIndexOf(StorageConstants.SLASH) + 1), e.getMessage());
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
				.emailStatus(ExactTargetEmailValidation.getExactTargetStatus(item.getStatus())).status(NOTSTARTED)
				.srcDate(srcDate.toString()).emailAddressPref(NUMBER_0.getValue()).emailPrefHdCa(NUMBER_0.getValue())
				.emailPrefGardenClub(NUMBER_MINUS_1.getValue()).emailPrefPro(NUMBER_MINUS_1.getValue())
				.emailPrefNewMover(NUMBER_MINUS_1.getValue()).insertedBy(INSERTEDBY).insertedDate(new Date()).build();
	}
}

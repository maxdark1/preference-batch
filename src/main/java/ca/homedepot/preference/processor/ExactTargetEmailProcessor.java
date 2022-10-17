package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.util.Date;

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
		return builder.src_email_address(item.getEmailAddress()).fileName(item.getFileName())
				.source_id(ExactTargetEmailValidation.getSourceId(item.getReason()))
				.email_status(new BigDecimal(ExactTargetEmailValidation.getExactTargetStatus(item.getStatus()))).status("NS")
				.src_date(srcDate).email_address_pref("0").email_pref_hd_ca("0").email_pref_garden_club("-1").email_pref_pro("-1")
				.email_pref_new_mover("-1").inserted_by("BATCH").inserted_date(new Date()).build();
	}
}

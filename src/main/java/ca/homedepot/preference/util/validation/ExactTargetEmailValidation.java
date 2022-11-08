package ca.homedepot.preference.util.validation;

import ca.homedepot.preference.processor.MasterProcessor;
import io.micrometer.core.lang.Nullable;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ExactTargetEmailValidation
{
	public static final String[] FIELD_NAMES_SFMC_OPTOUTS = new String[]
	{ "Email Address", "Status", "Reason", "Date Unsubscribed" };

	private ExactTargetEmailValidation()
	{
	}

	/**
	 * Gets SFMC status value
	 * 
	 * @param status
	 * @return status value
	 */
	public static BigDecimal getExactTargetStatus(String status)
	{
		/**
		 * In case of unsubscribe... old_id was '98'
		 */
		if (status.equalsIgnoreCase("unsubscribed"))
			return MasterProcessor.getSourceID("EMAIL_STATUS", "ET SPAM List").getMasterId();
		/**
		 * In case is held because... old_id was '50'
		 */
		return MasterProcessor.getSourceID("EMAIL_STATUS", "Hard Bounces").getMasterId();

	}

	/**
	 * Validate status email
	 * 
	 * @param status
	 * @param error
	 */
	public static void validateStatusEmail(String status, StringBuilder error)
	{
		if (!status.trim().equalsIgnoreCase("Unsubscribed") && !status.trim().equalsIgnoreCase("held"))
			error.append(" Email status: ").append(status).append(" is not equals to Unsubscribed or held");
	}

	/**
	 * Gets source ID
	 * 
	 * @param reason
	 * @return source Id
	 */
	public static BigDecimal getSourceId(@Nullable String reason)
	{
		//TODO ids can not be hardcoded, read from master table
		if (reason == null)
			return MasterProcessor.getSourceID("188");
		String reasonUp = reason.toUpperCase();
		if (reasonUp.contains("AOL"))
			return MasterProcessor.getSourceID("189");
		if (reasonUp.contains("SCAMCOP") || reasonUp.contains("SPAM COP REPORT"))
			return MasterProcessor.getSourceID("SOURCE", "EXACT TARGET OPT OUT OTH-CAN").getMasterId();

		return MasterProcessor.getSourceID("188");
	}

	/**
	 * Validates date format
	 * 
	 * @param date
	 * @param error
	 * @return a valid date
	 */
	public static Date validateDateFormat(String date, StringBuilder error)
	{
		Date asOfDate = null;
		//TODO make it class variable as its a constant
		SimpleDateFormat[] simpleDateFormatArray =
		{ new SimpleDateFormat("MM/dd/yyyy H :mm"), new SimpleDateFormat("MM/dd/yyyy HH:mm"),
				new SimpleDateFormat("MM/dd/yyyy HH:m"), new SimpleDateFormat("MM/dd/yyyy H :m"), };

		for (SimpleDateFormat simpleDateFormat : simpleDateFormatArray)
		{
			try
			{
				/**
				 * Validates date format
				 */
				asOfDate = simpleDateFormat.parse(date);
				/**
				 * Validates that day and month are valid
				 */
				InboundValidator.validateDayMonth(date, "/", error);
				return asOfDate;
			}
			catch (ParseException ex)
			{
				asOfDate = null;
			}
		}
		/**
		 * If it doesn't returns before the date, means that the date format is not valid
		 */
		error.append("invalid date format ").append(date).append("\n");
		return asOfDate;
	}

	/**
	 * Validates headers values
	 * 
	 * @return line call back handler
	 */
	public static LineCallbackHandler lineCallbackHandler()
	{
		//TODO  read the split character from the DElimeter class
		return line -> {
			String[] header = line.split("\\t");
			if (!Arrays.equals(header, FIELD_NAMES_SFMC_OPTOUTS))
				throw new ValidationException(" Invalid header {}: " + Arrays.toString(header));
		};
	}
}

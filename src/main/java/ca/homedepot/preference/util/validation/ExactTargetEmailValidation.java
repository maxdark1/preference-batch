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

	// TODO status number may change
	public static String getExactTargetStatus(String status)
	{

		if (status.equalsIgnoreCase("unsubscribed"))
			return "98";
		// In case is 'held'
		return "50";

	}

	public static void validateStatusEmail(String status, StringBuilder error)
	{
		if (!status.trim().equalsIgnoreCase("Unsubscribed") && !status.trim().equalsIgnoreCase("held"))
			error.append(" Email status: ").append(status).append(" is not equals to Unsubscribed or held");
	}

	public static BigDecimal getSourceId(@Nullable String reason)
	{
		if (reason == null)
			return MasterProcessor.getSourceId("SOURCE", "EXACT TARGET OPT OUT-CAN").getMaster_id();
		String reasonUp = reason.toUpperCase();
		if (reasonUp.contains("AOL"))
			return MasterProcessor.getSourceId("SOURCE", "EXACT TARGET OPT OUT AOL-CAN").getMaster_id();
		if (reasonUp.contains("SCAMCOP") || reasonUp.contains("SPAM COP REPORT"))
			return MasterProcessor.getSourceId("SOURCE", "EXACT TARGET OPT OUT OTH-CAN").getMaster_id();

		return MasterProcessor.getSourceId("SOURCE", "EXACT TARGET OPT OUT-CAN").getMaster_id();
	}

	public static Date validateDateFormat(String date, StringBuilder error)
	{
		Date asOfDate = null;

		SimpleDateFormat simpleDateFormatArray[] =
		{ new SimpleDateFormat("MM/dd/yyyy H :mm"), new SimpleDateFormat("MM/dd/yyyy HH:mm"),
				new SimpleDateFormat("MM/dd/yyyy HH:m"), new SimpleDateFormat("MM/dd/yyyy H :m"), };

		for (SimpleDateFormat simpleDateFormat : simpleDateFormatArray)
		{
			try
			{
				asOfDate = simpleDateFormat.parse(date);
				InboundValidator.validateDayMonth(date, "/", error);
				return asOfDate;
			}
			catch (ParseException ex)
			{
				// Nothing to do in here
			}
		}
		error.append("invalid date format ").append(date).append("\n");
		return asOfDate;
	}


	public static LineCallbackHandler lineCallbackHandler()
	{

		return line -> {
			String[] header = line.split("\\t");
			if (!Arrays.equals(header, FIELD_NAMES_SFMC_OPTOUTS))
				throw new ValidationException(" Invalid header {}: " + Arrays.toString(header));
		};
	}
}

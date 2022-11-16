package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;
import static ca.homedepot.preference.dto.enums.Preference.*;

@Component
@JobScope
@Setter
@Getter
@Slf4j
public class SkipListenerLayoutB extends SkipFileService implements SkipListener<EmailOptOuts, FileInboundStgTable>
{


	/**
	 * The job name that is currently being executed
	 */
	@Value("#{jobParameters['job_name']}")
	private String jobName;

	/**
	 * Skips on read when it throws a ValidationException
	 * 
	 * @param t
	 *           cause of the failure
	 */
	@SneakyThrows
	@Override
	public void onSkipInRead(Throwable t)
	{
		if (!shouldSkip(t))
		{
			log.error(" Something went wrong trying to read the file: ", t.getMessage());
			throw new IOException(t.getMessage());
		}
	}

	/**
	 * Skips on write item when it throws a ValidationException
	 * 
	 * @param item
	 *           the failed item
	 * @param t
	 *           the cause of the failure
	 */
	@Override
	public void onSkipInWrite(FileInboundStgTable item, Throwable t)
	{
		// Nothing to do inhere
	}

	/**
	 * Skips on process item when it throws a ValidationException
	 * 
	 * @param item
	 *           the failed item
	 * @param t
	 *           the cause of the failure
	 */
	@Override
	public void onSkipInProcess(EmailOptOuts item, Throwable t)
	{
		BigDecimal emailStatus = ExactTargetEmailValidation.getExactTargetStatus(item.getStatus());

		Boolean isEmailInvalid = isEmailInvalid(t);

		/**
		 * Creating the File inbound statging table record
		 */
		FileInboundStgTable fileInboundStgTable = FileInboundStgTable.builder()
				.fileId(getFromTableFileID(item.getFileName(), jobName)).status(ERROR).srcEmailAddress(item.getEmailAddress())
				.fileName(item.getFileName()).emailStatus(Boolean.TRUE.equals(isEmailInvalid) ? getEmailStatus(t) : emailStatus)
				.emailAddressPref(NUMBER_0.getValue()).emailPrefHdCa(NUMBER_0.getValue())
				.emailPrefGardenClub(NUMBER_MINUS_1.getValue()).emailPrefPro(NUMBER_MINUS_1.getValue())
				.emailPrefNewMover(NUMBER_MINUS_1.getValue()).insertedBy(INSERTEDBY).insertedDate(new Date()).build();

		/**
		 * Inserting the failed item to Staging error
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}


}

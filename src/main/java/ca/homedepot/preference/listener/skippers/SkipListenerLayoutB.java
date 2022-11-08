package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import lombok.Data;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@JobScope
@Data
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
	@Override
	public void onSkipInRead(Throwable t)
	{
		//Nothing to do in here
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
				//TODO no status field
				.file_id(getFromTableFileID(item.getFileName(), jobName)).src_email_address(item.getEmailAddress())
				//TODO no hardcoding language preference and default value will be "UNK"
				.fileName(item.getFileName()).email_status(Boolean.TRUE.equals(isEmailInvalid) ? getEmailStatus(t) : emailStatus)
				.status("E").email_address_pref("0").email_pref_hd_ca("0").email_pref_garden_club("-1").email_pref_pro("-1")
				//TODO read "test_batch" from constant file and rename to "batch".
				.email_pref_new_mover("-1").inserted_by("tested_batch").inserted_date(new Date()).build();

		/**
		 * Inserting the failed item to Staging error
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}


}

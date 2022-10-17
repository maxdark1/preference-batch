package ca.homedepot.preference.listener.skipers;

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

		/**
		 * Creating the File inbound statging table record
		 */
		FileInboundStgTable fileInboundStgTable = FileInboundStgTable.builder()
				.file_id(getFromTableFileID(item.getFileName(), jobName)).src_email_address(item.getEmailAddress())
				.fileName(item.getFileName())
				.email_status(new BigDecimal(ExactTargetEmailValidation.getExactTargetStatus(item.getStatus()))).status("E")
				.email_address_pref("0").email_pref_hd_ca("0").email_pref_garden_club("-1").email_pref_pro("-1")
				.email_pref_new_mover("-1").inserted_by("tested_batch").inserted_date(new Date()).build();

		/**
		 * Inserting the failed item to Staging error
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}


}

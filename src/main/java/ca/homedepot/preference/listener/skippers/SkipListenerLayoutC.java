package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@JobScope
@Setter
@Getter
public class SkipListenerLayoutC extends SkipFileService implements SkipListener<InboundRegistration, FileInboundStgTable>
{

	/**
	 * The job name that is currently being executed
	 */
	@Value("#{jobParameters['job_name']}")
	private String jobName;

	/**
	 * +
	 *
	 * @param t
	 *           cause of the failure
	 */
	@Override
	public void onSkipInRead(Throwable t)
	{
		// Nothing to do in here
	}

	/**
	 *
	 * @param item
	 *           the failed item
	 * @param t
	 *           the cause of the failure
	 */
	@Override
	public void onSkipInWrite(FileInboundStgTable item, Throwable t)
	{
		// Nothing to do in here
	}

	/**
	 * Skip in process item when it throws a ValidationException
	 * 
	 * @param item
	 *           the failed item
	 * @param t
	 *           the cause of the failure
	 */
	@Override
	public void onSkipInProcess(InboundRegistration item, Throwable t)
	{

		FileInboundStgTable fileInboundStgTable = FileInboundStgTable.builder()
				//TODO no hardcoding status read from Enum
				.file_id(getFromTableFileID(item.getFileName(), jobName)).status("E").fileName(item.getFileName())
				.src_language_pref(item.getLanguage_Preference().trim().toUpperCase()).updated_date(new Date())
				.email_status(getEmailStatus(t)).src_email_address(item.getEmail_Address())
				.email_address_pref(item.getEmail_Permission()).phone_pref(item.getPhone_Permission())
				.src_phone_number(item.getPhone_Number()).src_phone_extension(item.getPhone_Extension())
				.src_title_name(item.getTitle()).src_first_name(item.getFirst_Name()).src_last_name(item.getLast_Name())
				.src_address1(item.getAddress_1()).src_address2(item.getAddress_2()).src_city(item.getCity())
				.src_state(item.getProvince()).src_postal_code(item.getPostal_Code()).mail_address_pref(item.getMail_Permission())
				.email_pref_hd_ca(item.getEmailPrefHDCA()).email_pref_garden_club(item.getGardenClub())
				.email_pref_pro(item.getEmailPrefPRO()).email_pref_new_mover(item.getNewMover()).cell_sms_flag(item.getSMS_Flag())
				.customer_nbr(item.getContent_1()).fax_number(item.getFax_Number()).fax_extension(item.getFax_Extension())
				.content1(item.getContent_1()).value1(item.getValue_1()).content2(item.getContent_2()).store_nbr(item.getContent_2())
				.value2(item.getValue_2()).content3(item.getContent_3()).org_name(item.getContent_3()).value3(item.getValue_3())
				.content4(item.getContent_4()).value4(item.getValue_4()).content5(item.getContent_5())
				.cust_type_cd(item.getContent_5()).value5(item.getValue_5()).content6(item.getContent_6()).value6(item.getValue_6())
				.content7(item.getContent_7()).value7(item.getValue_7()).content8(item.getValue_8()).value8(item.getValue_8())
				.content9(item.getContent_9()).value9(item.getValue_9()).content10(item.getContent_10()).value10(item.getValue_10())
				.content11(item.getContent_11()).value11(item.getValue_11()).content12(item.getContent_12())
				.value12(item.getValue_12()).content13(item.getContent_13()).value13(item.getValue_13())
				.content14(item.getContent_14()).value14(item.getValue_14()).content15(item.getContent_15())
				.value15(item.getValue_15()).content16(item.getContent_16()).value16(item.getValue_16())
				.content17(item.getContent_17()).value17(item.getValue_17()).content18(item.getContent_18())
				.value18(item.getValue_18()).content19(item.getContent_19()).value19(item.getValue_19())
				//TODO read "test_batch" from constant file and rename to "batch".
				.content20(item.getContent_20()).value20(item.getValue_20()).inserted_by("test_batch").inserted_date(new Date())
				.build();

		/**
		 * Insertion to staging error on failed item
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}
}

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
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

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
				.fileId(getFromTableFileID(item.getFileName(), jobName)).status(ERROR).fileName(item.getFileName())
				.srcLanguagePref(item.getLanguage_Preference().trim().toUpperCase()).updatedDate(new Date())
				.emailStatus(getEmailStatus(t)).srcEmailAddress(item.getEmail_Address()).emailAddressPref(item.getEmail_Permission())
				.phonePref(item.getPhone_Permission()).srcPhoneNumber(item.getPhone_Number())
				.srcPhoneExtension(item.getPhone_Extension()).srcTitleName(item.getTitle()).srcFirstName(item.getFirst_Name())
				.srcLastName(item.getLast_Name()).srcAddress1(item.getAddress_1()).srcAddress2(item.getAddress_2())
				.srcCity(item.getCity()).srcState(item.getProvince()).srcPostalCode(item.getPostal_Code())
				.mailAddressPref(item.getMail_Permission()).emailPrefHdCa(item.getEmailPrefHDCA())
				.emailPrefGardenClub(item.getGardenClub()).emailPrefPro(item.getEmailPrefPRO()).emailPrefNewMover(item.getNewMover())
				.cellSmsFlag(item.getSMS_Flag()).customerNbr(item.getContent_1()).faxNumber(item.getFax_Number())
				.faxExtension(item.getFax_Extension()).content1(item.getContent_1()).value1(item.getValue_1())
				.content2(item.getContent_2()).storeNbr(item.getContent_2()).value2(item.getValue_2()).content3(item.getContent_3())
				.orgName(item.getContent_3()).value3(item.getValue_3()).content4(item.getContent_4()).value4(item.getValue_4())
				.content5(item.getContent_5()).custTypeCd(item.getContent_5()).value5(item.getValue_5()).content6(item.getContent_6())
				.value6(item.getValue_6()).content7(item.getContent_7()).value7(item.getValue_7()).content8(item.getValue_8())
				.value8(item.getValue_8()).content9(item.getContent_9()).value9(item.getValue_9()).content10(item.getContent_10())
				.value10(item.getValue_10()).content11(item.getContent_11()).value11(item.getValue_11())
				.content12(item.getContent_12()).value12(item.getValue_12()).content13(item.getContent_13())
				.value13(item.getValue_13()).content14(item.getContent_14()).value14(item.getValue_14())
				.content15(item.getContent_15()).value15(item.getValue_15()).content16(item.getContent_16())
				.value16(item.getValue_16()).content17(item.getContent_17()).value17(item.getValue_17())
				.content18(item.getContent_18()).value18(item.getValue_18()).content19(item.getContent_19())
				.value19(item.getValue_19()).content20(item.getContent_20()).value20(item.getValue_20()).insertedBy(INSERTEDBY)
				.insertedDate(new Date()).build();

		/**
		 * Insertion to staging error on failed item
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}
}

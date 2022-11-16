package ca.homedepot.preference.listener.skippers;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;

@Component
@JobScope
@Setter
@Getter
@Slf4j
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
	@SneakyThrows
	@Override
	public void onSkipInRead(Throwable t)
	{
		if (!shouldSkip(t))
		{
			log.error(t.getMessage());
			throw new IOException(t.getMessage());
		}
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
	@SneakyThrows
	@Override
	public void onSkipInProcess(InboundRegistration item, Throwable t)
	{

		FileInboundStgTable fileInboundStgTable = FileInboundStgTable.builder()
				.fileId(getFromTableFileID(item.getFileName(), jobName)).status(ERROR).fileName(item.getFileName())
				.srcLanguagePref(item.getLanguagePreference().trim().toUpperCase()).updatedDate(new Date())
				.emailStatus(getEmailStatus(t)).srcEmailAddress(item.getEmailAddress()).emailAddressPref(item.getEmailPermission())
				.phonePref(item.getPhonePermission()).srcPhoneNumber(item.getPhoneNumber())
				.srcPhoneExtension(item.getPhoneExtension()).srcTitleName(item.getTitle()).srcFirstName(item.getFirstName())
				.srcLastName(item.getLastName()).srcAddress1(item.getAddress1()).srcAddress2(item.getAddress2())
				.srcCity(item.getCity()).srcState(item.getProvince()).srcPostalCode(item.getPostalCode())
				.mailAddressPref(item.getMailPermission()).emailPrefHdCa(item.getEmailPrefHDCA())
				.emailPrefGardenClub(item.getGardenClub()).emailPrefPro(item.getEmailPrefPRO()).emailPrefNewMover(item.getNewMover())
				.cellSmsFlag(item.getSmsFlag()).customerNbr(item.getContent1()).faxNumber(item.getFaxNumber())
				.faxExtension(item.getFaxExtension()).content1(item.getContent1()).value1(item.getValue1())
				.content2(item.getContent2()).storeNbr(item.getContent2()).value2(item.getValue2()).content3(item.getContent3())
				.orgName(item.getContent3()).value3(item.getValue3()).content4(item.getContent4()).value4(item.getValue4())
				.content5(item.getContent5()).custTypeCd(item.getContent5()).value5(item.getValue5()).content6(item.getContent6())
				.value6(item.getValue6()).content7(item.getContent7()).value7(item.getValue7()).content8(item.getValue8())
				.value8(item.getValue8()).content9(item.getContent9()).value9(item.getValue9()).content10(item.getContent10())
				.value10(item.getValue10()).content11(item.getContent11()).value11(item.getValue11()).content12(item.getContent12())
				.value12(item.getValue12()).content13(item.getContent13()).value13(item.getValue13()).content14(item.getContent14())
				.value14(item.getValue14()).content15(item.getContent15()).value15(item.getValue15()).content16(item.getContent16())
				.value16(item.getValue16()).content17(item.getContent17()).value17(item.getValue17()).content18(item.getContent18())
				.value18(item.getValue18()).content19(item.getContent19()).value19(item.getValue19()).content20(item.getContent20())
				.value20(item.getValue20()).insertedBy(INSERTEDBY).insertedDate(new Date()).build();

		/**
		 * Insertion to staging error on failed item
		 */
		fileService.insertInboundStgError(fileInboundStgTable);
	}
}

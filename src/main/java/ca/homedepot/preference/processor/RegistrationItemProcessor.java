package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.util.constants.StorageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.util.Date;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;
import static ca.homedepot.preference.util.validation.InboundValidator.*;

@Slf4j
public class RegistrationItemProcessor implements ItemProcessor<InboundRegistration, FileInboundStgTable>
{
	/**
	 * Source value Where item comes from
	 */
	private String source;

	private int count = 0;

	private String fileName = "";

	public void setCount(int count)
	{
		this.count = count;
	}

	/**
	 * Constructor with resource
	 *
	 * @param source
	 */
	public RegistrationItemProcessor(String source)
	{
		this.source = source;
	}

	/**
	 * Process item
	 *
	 * @param item
	 *           to be processed
	 * @return Item to be writing on persistence
	 * @throws Exception
	 */
	@Override
	public FileInboundStgTable process(InboundRegistration item) throws Exception
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
		Date asOfDate = null;
		BigDecimal sourceId = null;
		String asOfDateStr = item.getAsOfDate();
		try
		{
			StringBuilder error = validate(item);
			asOfDate = validateDateFormat(asOfDateStr, error);
			sourceId = validateSourceID(item.getSourceID(), source, error);

			/**
			 * Throws an exception if it finds any Error message on StringBuilder container
			 */
			isValidationsErros(error);
		}
		catch (ValidationException e)
		{
			log.error(
					" PREFERENCE BATCH VALIDATION ERROR - The record # {} has the above fields with validation error on file {}: {} ",
				count, item.getFileName().substring(item.getFileName().lastIndexOf(StorageConstants.SLASH) + 1), e.getMessage());
			/**
			 * Throws the exception again after has been logged This is catch on the LayoutC's skippers
			 */
			throw e;
		}

		builder.status(NOTSTARTED).fileName(item.getFileName()).srcLanguagePref(item.getLanguagePreference().trim().toUpperCase())
				.updatedDate(new Date()).srcDate(asOfDate.toString()).srcEmailAddress(item.getEmailAddress())
				.emailStatus(
						item.getEmailAddress() == null ? null : MasterProcessor.getSourceID(EMAIL_STATUS, VALID_EMAIL).getMasterId())
				.emailAddressPref(item.getEmailPermission()).phonePref(item.getPhonePermission())
				.srcPhoneNumber(item.getPhoneNumber()).sourceId(sourceId).srcPhoneExtension(item.getPhoneExtension())
				.srcTitleName(item.getTitle()).srcFirstName(item.getFirstName()).srcLastName(item.getLastName())
				.srcAddress1(item.getAddress1()).srcAddress2(item.getAddress2()).srcCity(item.getCity()).srcState(item.getProvince())
				.srcPostalCode(item.getPostalCode()).mailAddressPref(item.getMailPermission()).emailPrefHdCa(item.getEmailPrefHDCA())
				.emailPrefGardenClub(item.getGardenClub()).emailPrefPro(item.getEmailPrefPRO()).emailPrefNewMover(item.getNewMover())
				.cellSmsFlag(item.getSmsFlag()).customerNbr(item.getValue1()).faxNumber(item.getFaxNumber())
				.faxExtension(item.getFaxExtension()).content1(item.getContent1()).value1(item.getValue1())
				.content2(item.getContent2()).storeNbr(item.getContent2()).value2(item.getValue2()).content3(item.getContent3())
				.orgName(item.getContent3()).value3(item.getValue3()).content4(item.getContent4()).value4(item.getValue4())
				.content5(item.getContent5()).custTypeCd(item.getContent5())
				.value5(MasterProcessor.getCustTypeCD(item.getValue5()).toPlainString()).content6(item.getContent6())
				.value6(item.getValue6()).content7(item.getContent7()).value7(item.getValue7()).content8(item.getValue8())
				.value8(item.getValue8()).content9(item.getContent9()).value9(item.getValue9()).content10(item.getContent10())
				.value10(item.getValue10()).content11(item.getContent11()).value11(item.getValue11()).content12(item.getContent12())
				.value12(item.getValue12()).content13(item.getContent13()).value13(item.getValue13()).content14(item.getContent14())
				.value14(item.getValue14()).content15(item.getContent15()).value15(item.getValue15()).content16(item.getContent16())
				.value16(item.getValue16()).content17(item.getContent17()).value17(item.getValue17()).content18(item.getContent18())
				.value18(item.getValue18()).content19(item.getContent19()).value19(item.getValue19()).content20(item.getContent20())
				.value20(item.getValue20()).insertedBy(INSERTEDBY).insertedDate(new Date());


		return builder.build();
	}

	/**
	 * Validate item's values
	 *
	 * @param item
	 * @return
	 */
	private StringBuilder validate(final InboundRegistration item)
	{
		StringBuilder error = new StringBuilder();
		validateIsRequired(item, error);
		if (item != null)
		{
			validateLengthNoRequired(item, error);
			validateMaxLengthReqField(item, error);
			validateNumberFormat(item, error);
			validateEmailFormat(item.getEmailAddress(), error);
			validateLanguagePref(item, error);
		}

		return error;
	}

	/**
	 * Validate Max Length of required fields
	 *
	 * @param item
	 * @param error
	 */
	private void validateMaxLengthReqField(InboundRegistration item, StringBuilder error)
	{
		item.setLanguagePreference(validateMaxLength("language_pref", item.getLanguagePreference(), 2, error));
		item.setAsOfDate(validateMaxLength("as_of_date", item.getAsOfDate(), 19, error));
		item.setEmailPermission(validateMaxLength("email_permission", item.getEmailPermission(), 2, error));
		item.setMailPermission(validateMaxLength("mail_permission", item.getMailPermission(), 2, error));
		item.setEmailPrefHDCA(validateMaxLength("email_pref_hd_ca", item.getEmailPrefHDCA(), 2, error));
		item.setGardenClub(validateMaxLength("email_pref_garden_club", item.getGardenClub(), 2, error));
		item.setEmailPrefPRO(validateMaxLength("email_pref_pro", item.getEmailPrefPRO(), 2, error));
		item.setNewMover(validateMaxLength("email_pref_new_mover", item.getNewMover(), 2, error));
		item.setContent1(validateMaxLength("content1", item.getContent1(), 30, error));
		item.setContent2(validateMaxLength("content2", item.getContent2(), 30, error));
		item.setContent3(validateMaxLength("content3", item.getContent3(), 30, error));
		item.setContent5(validateMaxLength("content5", item.getContent5(), 30, error));
		item.setContent6(validateMaxLength("content6", item.getContent6(), 30, error));

	}

	/**
	 * Validate Max Length of not required fields
	 *
	 * @param item,
	 *           error
	 *
	 * @return
	 */
	private void validateLengthNoRequired(InboundRegistration item, StringBuilder error)
	{
		item.setEmailAddress(validateMaxLengthNotReq("email_addr", item.getEmailAddress(), 72, error));
		item.setPhonePermission(validateMaxLengthNotReq("phone_permission", item.getPhonePermission(), 2, error));
		item.setPhoneNumber(validateMaxLengthNotReq("phone_num", item.getPhoneNumber(), 10, error));
		item.setPhoneExtension(validateMaxLengthNotReq("phone_ext", item.getPhoneExtension(), 6, error));
		item.setTitle(validateMaxLengthNotReq("title", item.getTitle(), 20, error));
		item.setFirstName(validateMaxLengthNotReq("first_name", item.getFirstName(), 40, error));
		item.setLastName(validateMaxLengthNotReq("last_name", item.getLastName(), 60, error));
		item.setAddress1(validateMaxLengthNotReq("addr1", item.getAddress1(), 100, error));
		item.setAddress2(validateMaxLengthNotReq("addr2", item.getAddress2(), 60, error));
		item.setCity(validateMaxLengthNotReq("city", item.getCity(), 60, error));
		item.setProvince(validateMaxLengthNotReq("province", item.getProvince(), 2, error));
		item.setPostalCode(validateMaxLengthNotReq("postal_code", item.getPostalCode(), 7, error));
		item.setSmsFlag(validateMaxLengthNotReq("sms_flag", item.getSmsFlag(), 2, error));
		item.setFaxNumber(validateMaxLengthNotReq("fax_number", item.getFaxNumber(), 30, error));
		item.setFaxExtension(validateMaxLengthNotReq("fax_extension", item.getFaxExtension(), 6, error));
		item.setContent4(validateMaxLengthNotReq("content4", item.getContent4(), 30, error));
		item.setContent7(validateMaxLengthNotReq("content7", item.getContent7(), 30, error));
		item.setContent8(validateMaxLengthNotReq("content8", item.getContent8(), 30, error));
		item.setContent9(validateMaxLengthNotReq("content9", item.getContent9(), 30, error));
		item.setContent10(validateMaxLengthNotReq("content10", item.getContent10(), 30, error));
		item.setContent11(validateMaxLengthNotReq("content11", item.getContent11(), 30, error));
		item.setContent12(validateMaxLengthNotReq("content12", item.getContent12(), 30, error));
		item.setContent13(validateMaxLengthNotReq("content13", item.getContent13(), 30, error));
		item.setContent14(validateMaxLengthNotReq("content14", item.getContent14(), 30, error));
		item.setContent15(validateMaxLengthNotReq("content15", item.getContent15(), 30, error));
		item.setContent16(validateMaxLengthNotReq("content16", item.getContent16(), 30, error));
		item.setContent17(validateMaxLengthNotReq("content17", item.getContent17(), 30, error));
		item.setContent18(validateMaxLengthNotReq("content18", item.getContent18(), 30, error));
		item.setContent19(validateMaxLengthNotReq("content19", item.getContent19(), 30, error));
		item.setContent20(validateMaxLengthNotReq("content20", item.getContent20(), 30, error));
	}



}

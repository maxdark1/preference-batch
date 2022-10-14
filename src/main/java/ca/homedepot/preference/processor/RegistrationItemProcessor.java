package ca.homedepot.preference.processor;

import static ca.homedepot.preference.util.validation.InboundValidator.*;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.util.validation.InboundValidator;

public class RegistrationItemProcessor implements ItemProcessor<InboundRegistration, FileInboundStgTable>
{

	private final Logger LOG = LoggerFactory.getLogger(RegistrationItemProcessor.class);

	/**
	 * Source value Where item comes from
	 *
	 */
	private String source;

	/**
	 * Constructor with source
	 *
	 * @param source
	 *
	 *
	 */
	public RegistrationItemProcessor(String source)
	{
		this.source = source;
	}

	/**
	 * Process items
	 *
	 * @param item
	 *
	 * @return FileInboundStgTable
	 *
	 */
	@Override
	public FileInboundStgTable process(InboundRegistration item) throws Exception
	{
		FileInboundStgTable.FileInboundStgTableBuilder builder = FileInboundStgTable.builder();
		Date asOfDate = null;
		String asOfDateStr = item.getAsOfDate();
		try
		{
			StringBuilder error = validate(item);
			asOfDate = validateDateFormat(asOfDateStr, error);
			InboundValidator.isValidationsErros(error);
		}
		catch (ValidationException e)
		{
			LOG.error(" Validation error: {} ", e.getMessage());
			throw e;
		}
		LOG.info(" Processing inbound item {}: ", item);
		builder.status("NS").fileName(item.getFileName()).src_language_pref(item.getLanguage_Preference().trim().toUpperCase())
				.updated_date(new Date()).src_date(asOfDate).src_email_address(item.getEmail_Address())
				.email_address_pref(item.getEmail_Permission()).phone_pref(item.getPhone_Permission())
				.src_phone_number(item.getPhone_Number()).source_id(InboundValidator.getSourceID(source))
				.src_phone_extension(item.getPhone_Extension()).src_title_name(item.getTitle()).src_first_name(item.getFirst_Name())
				.src_last_name(item.getLast_Name()).src_address1(item.getAddress_1()).src_address2(item.getAddress_2())
				.src_city(item.getCity()).src_state(item.getProvince()).src_postal_code(item.getPostal_Code())
				.mail_address_pref(item.getMail_Permission()).email_pref_hd_ca(item.getEmailPrefHDCA())
				.email_pref_garden_club(item.getGardenClub()).email_pref_pro(item.getEmailPrefPRO())
				.email_pref_new_mover(item.getNewMover()).cell_sms_flag(item.getSMS_Flag()).customer_nbr(item.getContent_1())
				.fax_number(item.getFax_Number()).fax_extension(item.getFax_Extension()).content1(item.getContent_1())
				.value1(item.getValue_1()).content2(item.getContent_2()).store_nbr(item.getContent_2()).value2(item.getValue_2())
				.content3(item.getContent_3()).org_name(item.getContent_3()).value3(item.getValue_3()).content4(item.getContent_4())
				.value4(item.getValue_4()).content5(item.getContent_5()).cust_type_cd(item.getContent_5()).value5(item.getValue_5())
				.content6(item.getContent_6()).value6(item.getValue_6()).content7(item.getContent_7()).value7(item.getValue_7())
				.content8(item.getValue_8()).value8(item.getValue_8()).content9(item.getContent_9()).value9(item.getValue_9())
				.content10(item.getContent_10()).value10(item.getValue_10()).content11(item.getContent_11())
				.value11(item.getValue_11()).content12(item.getContent_12()).value12(item.getValue_12())
				.content13(item.getContent_13()).value13(item.getValue_13()).content14(item.getContent_14())
				.value14(item.getValue_14()).content15(item.getContent_15()).value15(item.getValue_15())
				.content16(item.getContent_16()).value16(item.getValue_16()).content17(item.getContent_17())
				.value17(item.getValue_17()).content18(item.getContent_18()).value18(item.getValue_18())
				.content19(item.getContent_19()).value19(item.getValue_19()).content20(item.getContent_20())
				.value20(item.getValue_20()).inserted_by("BATCH").inserted_date(new Date());


		return builder.build();
	}

	/**
	 * Validate item's values
	 *
	 * @param item
	 *
	 * @return StringBuilder Returns error message
	 */
	private StringBuilder validate(final InboundRegistration item)
	{
		StringBuilder error = new StringBuilder();
		validateIsRequired(item, error);
		if (item != null)
		{
			validateMaxLength(item, error);
			validateMaxLengthReqField(item, error);
			validateNumberFormat(item, error);
			validateEmailFormat(item.getEmail_Address(), error);
			validateLanguagePref(item, error);
		}

		return error;
	}

	/**
	 * Validate Max Length of required fields
	 *
	 * @param item,
	 *           error
	 *
	 * @return
	 */
	private void validateMaxLengthReqField(InboundRegistration item, StringBuilder error)
	{
		item.setLanguage_Preference(InboundValidator.validateMaxLength("language_pref", item.getLanguage_Preference(), 2, error));
		item.setAsOfDate(InboundValidator.validateMaxLength("as_of_date", item.getAsOfDate(), 19, error));
		item.setEmail_Permission(InboundValidator.validateMaxLength("email_permission", item.getEmail_Permission(), 2, error));
		item.setMail_Permission(InboundValidator.validateMaxLength("mail_permission", item.getMail_Permission(), 2, error));
		item.setEmailPrefHDCA(InboundValidator.validateMaxLength("email_pref_hd_ca", item.getEmailPrefHDCA(), 2, error));
		item.setGardenClub(InboundValidator.validateMaxLength("email_pref_garden_club", item.getGardenClub(), 2, error));
		item.setEmailPrefPRO(InboundValidator.validateMaxLength("email_pref_pro", item.getEmailPrefPRO(), 2, error));
		item.setNewMover(InboundValidator.validateMaxLength("email_pref_new_mover", item.getNewMover(), 2, error));
		item.setContent_1(InboundValidator.validateMaxLength("content1", item.getContent_1(), 30, error));
		item.setContent_2(InboundValidator.validateMaxLength("content2", item.getContent_2(), 30, error));
		item.setContent_3(InboundValidator.validateMaxLength("content3", item.getContent_3(), 30, error));
		item.setContent_5(InboundValidator.validateMaxLength("content5", item.getContent_5(), 30, error));
		item.setContent_6(InboundValidator.validateMaxLength("content6", item.getContent_6(), 30, error));

	}

	/**
	 * Validate Max Length of not required fields
	 *
	 * @param item,
	 *           error
	 *
	 * @return
	 */
	private void validateMaxLength(InboundRegistration item, StringBuilder error)
	{
		item.setEmail_Address(validateMaxLengthNotReq("email_addr", item.getEmail_Address(), 72, error));
		item.setPhone_Permission(validateMaxLengthNotReq("phone_permission", item.getPhone_Permission(), 2, error));
		item.setPhone_Number(validateMaxLengthNotReq("phone_num", item.getPhone_Number(), 10, error));
		item.setPhone_Extension(validateMaxLengthNotReq("phone_ext", item.getPhone_Extension(), 6, error));
		item.setTitle(validateMaxLengthNotReq("title", item.getTitle(), 20, error));
		item.setFirst_Name(validateMaxLengthNotReq("first_name", item.getFirst_Name(), 40, error));
		item.setLast_Name(validateMaxLengthNotReq("last_name", item.getLast_Name(), 60, error));
		item.setAddress_1(validateMaxLengthNotReq("addr1", item.getAddress_1(), 100, error));
		item.setAddress_2(validateMaxLengthNotReq("addr2", item.getAddress_2(), 60, error));
		item.setCity(validateMaxLengthNotReq("city", item.getCity(), 60, error));
		item.setProvince(validateMaxLengthNotReq("province", item.getProvince(), 2, error));
		item.setPostal_Code(validateMaxLengthNotReq("postal_code", item.getPostal_Code(), 7, error));
		item.setSMS_Flag(validateMaxLengthNotReq("sms_flag", item.getSMS_Flag(), 2, error));
		item.setFax_Number(validateMaxLengthNotReq("fax_number", item.getFax_Number(), 30, error));
		item.setFax_Extension(validateMaxLengthNotReq("fax_extension", item.getFax_Extension(), 6, error));
		item.setContent_4(validateMaxLengthNotReq("content4", item.getContent_4(), 30, error));
		item.setContent_7(validateMaxLengthNotReq("content7", item.getContent_7(), 30, error));
		item.setContent_8(validateMaxLengthNotReq("content8", item.getContent_8(), 30, error));
		item.setContent_9(validateMaxLengthNotReq("content9", item.getContent_9(), 30, error));
		item.setContent_10(validateMaxLengthNotReq("content10", item.getContent_10(), 30, error));
		item.setContent_11(validateMaxLengthNotReq("content11", item.getContent_11(), 30, error));
		item.setContent_12(validateMaxLengthNotReq("content12", item.getContent_12(), 30, error));
		item.setContent_13(validateMaxLengthNotReq("content13", item.getContent_13(), 30, error));
		item.setContent_14(validateMaxLengthNotReq("content14", item.getContent_14(), 30, error));
		item.setContent_15(validateMaxLengthNotReq("content15", item.getContent_15(), 30, error));
		item.setContent_16(validateMaxLengthNotReq("content16", item.getContent_16(), 30, error));
		item.setContent_17(validateMaxLengthNotReq("content17", item.getContent_17(), 30, error));
		item.setContent_18(validateMaxLengthNotReq("content18", item.getContent_18(), 30, error));
		item.setContent_19(validateMaxLengthNotReq("content19", item.getContent_19(), 30, error));
		item.setContent_20(validateMaxLengthNotReq("content20", item.getContent_20(), 30, error));
	}



}

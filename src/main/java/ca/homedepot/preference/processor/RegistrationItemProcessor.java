package ca.homedepot.preference.processor;

import ca.homedepot.preference.util.validation.InboundValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import org.springframework.batch.item.validator.ValidationException;

import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.model.OutboundRegistration;
import static ca.homedepot.preference.util.validation.InboundValidator.*;

import java.util.Date;

public class RegistrationItemProcessor implements ItemProcessor<InboundRegistration, OutboundRegistration> {

    private final Logger LOG = LoggerFactory.getLogger(RegistrationItemProcessor.class);


    @Override
    public OutboundRegistration process(InboundRegistration item) throws Exception {
        System.out.println(" IN PROCESS");

        OutboundRegistration.OutboundRegistrationBuilder builder = OutboundRegistration.builder();

        LOG.info("item in process{} :" + item.toString());
        Date asOfDate = null;
        try{
            validate(item, builder);
            asOfDate= validateDateFormat(item.getAsOfDate());

        }catch (ValidationException e){
            LOG.debug(" Validation error {}: ", e.getMessage());
        }
        LOG.debug(" Processing inbound item {}: " , item);
        builder
                .credit_language_cd(item.getLanguage_Preference().trim())
                .updated_date(asOfDate)
                .src_email_address(item.getEmail_Address())
                .email_address_1_pref(item.getEmail_Permission())
                .phone_1_pref(item.getPhone_Permission())
                .src_phone_number(item.getPhone_Number())
                .src_phone_extension(item.getPhone_Extension())
                .src_title_name(item.getTitle())
                .src_first_name(item.getFirst_Name())
                .src_last_name(item.getLast_Name())
                .src_address1(item.getAddress_1())
                .src_address2(item.getAddress_2())
                .src_city(item.getCity())
                .src_postal_code(item.getPostal_Code())
                .mail_address_1_pref(item.getMail_Permission())
                .email_pref_hd_ca(item.getEmailPrefHDCA())
                .email_pref_garden_club(item.getGardenClub())
                .email_pref_pro(item.getEmailPrefPRO())
                .email_pref_new_mover(item.getNewMover())
                .cell_sms_flag(item.getSMS_Flag())
                .customer_nbr(item.getContent_1())
                .content1(item.getContent_1())
                .value1(item.getValue_1())
                .content2(item.getContent_2())
                .store_nbr(item.getContent_2())
                .value2(item.getValue_2())
                .content3(item.getContent_3())
                .org_name(item.getContent_3())
                .value3(item.getValue_3())
                .content4(item.getContent_4())
                .value4(item.getValue_4())
                .content5(item.getContent_5())
                .cust_type_cd(item.getContent_5())
                .value5(item.getValue_5())
                .content6(item.getContent_6())
                .value6(item.getValue_6())
                .content7(item.getContent_7())
                .value7(item.getValue_7())
                .content8(item.getValue_8())
                .value8(item.getValue_8())
                .content9(item.getContent_9())
                .value9(item.getValue_9())
                .content10(item.getContent_10())
                .value10(item.getValue_10())
                .content11(item.getContent_11())
                .value11(item.getValue_11())
                .content12(item.getContent_12())
                .value12(item.getValue_12())
                .content13(item.getContent_13())
                .value13(item.getValue_13())
                .content14(item.getContent_14())
                .value14(item.getValue_14())
                .content15(item.getContent_15())
                .value15(item.getValue_15())
                .content16(item.getContent_16())
                .value16(item.getValue_16())
                .content17(item.getContent_17())
                .value17(item.getValue_17())
                .content18(item.getContent_18())
                .value18(item.getValue_18())
                .content19(item.getContent_19())
                .value19(item.getValue_19())
                .content20(item.getContent_20())
                .value20(item.getValue_20())
                .inserted_by("test_batch")
                .inserted_date(new Date());


        return builder.build();
    }

    private void validate(final InboundRegistration item, final OutboundRegistration.OutboundRegistrationBuilder builder) {
        validateIsRequired(item);
        validateMaxLength(item);
        validateMaxLengthReqField(item);
        validateNumberFormat(item, builder);
        validateEmailFormat(item);
        validateLanguagePref(item);


    }

    private void validateMaxLengthReqField(InboundRegistration item) {

        InboundValidator.validateMaxLength("language_pref", item.getLanguage_Preference(), 2);
        InboundValidator.validateMaxLength("as_of_date", item.getAsOfDate(), 19);
        InboundValidator.validateMaxLength("email_permission", item.getEmail_Permission(), 2);
        InboundValidator.validateMaxLength("mail_permission", item.getMail_Permission(), 2);
        InboundValidator.validateMaxLength("email_pref_hd_ca", item.getEmailPrefHDCA(), 2);
        InboundValidator.validateMaxLength("email_pref_garden_club", item.getGardenClub(), 2);
        InboundValidator.validateMaxLength("email_pref_pro", item.getEmailPrefPRO(), 2);
        InboundValidator.validateMaxLength("email_pref_new_mover", item.getNewMover(), 2);
        InboundValidator.validateMaxLength("content1", item.getContent_1(), 30);
        InboundValidator.validateMaxLength("content2", item.getContent_2(), 30);
        InboundValidator.validateMaxLength("content3", item.getContent_3(), 30);
        InboundValidator.validateMaxLength("content5", item.getContent_5(), 30);
        InboundValidator.validateMaxLength("content6", item.getContent_6(), 30);
    }

    private void validateMaxLength(InboundRegistration item) {
        validateMaxLengthNotReq("email_addr", item.getEmail_Address(), 72);
        validateMaxLengthNotReq("phone_permission", item.getPhone_Permission(), 2);
        validateMaxLengthNotReq("phone_num", item.getPhone_Number(), 10);
        validateMaxLengthNotReq("phone_ext", item.getPhone_Extension(), 6);
        validateMaxLengthNotReq("title", item.getTitle(), 20);
        validateMaxLengthNotReq("first_name", item.getFirst_Name(), 40);
        validateMaxLengthNotReq("last_name", item.getLast_Name(), 60);
        validateMaxLengthNotReq("addr1", item.getAddress_1(), 100);
        validateMaxLengthNotReq("addr2", item.getAddress_2(), 60);
        validateMaxLengthNotReq("city", item.getCity(), 60);
        validateMaxLengthNotReq("province", item.getProvince(), 2);
        validateMaxLengthNotReq("postal_code", item.getPostal_Code(), 7);
        validateMaxLengthNotReq("sms_flag", item.getSMS_Flag(), 2);
        validateMaxLengthNotReq("fax_number", item.getFax_Number(), 30);
        validateMaxLengthNotReq("fax_extension", item.getFax_Extension(), 6);
        validateMaxLengthNotReq("content4", item.getContent_4(), 30);
        validateMaxLengthNotReq("content7", item.getContent_7(), 30);
        validateMaxLengthNotReq("content8", item.getContent_8(), 30);
        validateMaxLengthNotReq("content9", item.getContent_9(), 30);
        validateMaxLengthNotReq("content10", item.getContent_10(), 30);
        validateMaxLengthNotReq("content11", item.getContent_11(), 30);
        validateMaxLengthNotReq("content12", item.getContent_12(), 30);
        validateMaxLengthNotReq("content13", item.getContent_13(), 30);
        validateMaxLengthNotReq("content14", item.getContent_14(), 30);
        validateMaxLengthNotReq("content15", item.getContent_15(), 30);
        validateMaxLengthNotReq("content16", item.getContent_16(), 30);
        validateMaxLengthNotReq("content17", item.getContent_17(), 30);
        validateMaxLengthNotReq("content18", item.getContent_18(), 30);
        validateMaxLengthNotReq("content19", item.getContent_19(), 30);
        validateMaxLengthNotReq("content20", item.getContent_20(), 30);


    }



}

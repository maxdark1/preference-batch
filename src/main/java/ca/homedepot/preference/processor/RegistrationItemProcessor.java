package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.OutboundRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import ca.homedepot.preference.model.InboundRegistration;
import org.springframework.batch.item.validator.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class RegistrationItemProcessor implements ItemProcessor<InboundRegistration, OutboundRegistration> {

    private final Logger LOG = LoggerFactory.getLogger(RegistrationItemProcessor.class);
    private final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public OutboundRegistration process(InboundRegistration item) throws Exception {
        System.out.println(" IN PROCESS");

        OutboundRegistration.OutboundRegistrationBuilder builder = OutboundRegistration.builder();

        LOG.info("item in process{} :" + item.toString());
        try{
            validate(item, builder);
        }catch (ValidationException e){
            LOG.debug(" Validation error {}: " + e.getMessage());
        }
        LOG.debug(" Processing inbound item {}: " + item);

        builder
                .credit_language_cd(item.getLanguage_Preference().trim())
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
        validateDateFormat(item, builder);
        validateEmailFormat(item);
        validateLanguagePref(item);


    }

    private void validateMaxLengthReqField(InboundRegistration item) {

        validateMaxLength("language_pref", item.getLanguage_Preference(), 2);
        validateMaxLength("as_of_date", item.getAsOfDate(), 19);
        validateMaxLength("email_permission", item.getEmail_Permission(), 2);
        validateMaxLength("mail_permission", item.getMail_Permission(), 2);
        validateMaxLength("email_pref_hd_ca", item.getEmailPrefHDCA(), 2);
        validateMaxLength("email_pref_garden_club", item.getGardenClub(), 2);
        validateMaxLength("email_pref_pro", item.getEmailPrefPRO(), 2);
        validateMaxLength("email_pref_new_mover", item.getNewMover(), 2);
        validateMaxLength("content1", item.getContent_1(), 30);
        validateMaxLength("content2", item.getContent_2(), 30);
        validateMaxLength("content3", item.getContent_3(), 30);
        validateMaxLength("content5", item.getContent_5(), 30);
        validateMaxLength("content6", item.getContent_6(), 30);
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

    private void validateMaxLengthNotReq(String field, String value, int maxLength) {
        if (value != null)
            validateMaxLength(field, value, maxLength);
    }

    private void validateMaxLength(String field, String value, int maxLength) {
        if (value.length() > maxLength)
            throw new ValidationException(String.format("The length of %s field  must be %d caracters or fewer.", field, maxLength));


    }

    private void validateLanguagePref(InboundRegistration item) {

        if (!item.getLanguage_Preference().trim().matches("e|E|f|F|fr|FR|en|EN"))
            throw new ValidationException("invalid value for language_pref {}: " + item.getLanguage_Preference() + " not matches with: E, EN, F, FR");
    }

    private void validateEmailFormat(InboundRegistration item) {

        if (item.getEmail_Address() != null)
            if (!item.getEmail_Address().matches(VALID_EMAIL_PATTERN))
                throw new ValidationException(" email address does not have a valid format {}: " + item.getEmail_Address());

    }

    private void validateDateFormat(InboundRegistration item, OutboundRegistration.OutboundRegistrationBuilder builder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        try {
            Date asOfDate = simpleDateFormat.parse(item.getAsOfDate());
            builder.updated_date(asOfDate);
        } catch (ParseException ex) {
            throw new ValidationException("invalid date format");
        }


    }

    private void validateNumberFormat(InboundRegistration item, OutboundRegistration.OutboundRegistrationBuilder builder) {
        Integer value = null;
        value = validateIsNumber(item.getEmail_Permission());
        validValue_Number(value, "email_permission");

        if (item.getPhone_Permission() != null) {
            value = validateIsNumber(item.getPhone_Permission());
            validValue_Number(value, "phone_permission");
        }

        value = validateIsNumber(item.getMail_Permission());
        validValue_Number(value, "mail_permission");

        value = validateIsNumber(item.getEmailPrefHDCA());
        validValue_Number(value, "email_pref_hd_ca");

        value = validateIsNumber(item.getGardenClub());
        validValue_Number(value, "email_pref_garden_club");

        value = validateIsNumber(item.getEmailPrefPRO());
        validValue_Number(value, "email_pref_pro");

        value = validateIsNumber(item.getNewMover());
        validValue_Number(value, "email_pref_new_mover");

        if (item.getValue_5() != null) {
            value = validateIsNumber(item.getValue_5());
            if (value != 1 && value != 2 && value != 5)
                throw new ValidationException("invalid value for field {}: value5");
        }

        if(item.getSource_ID() != null && !item.getSource_ID().isBlank()){
            value = validateIsNumber(item.getSource_ID().trim());
            builder.source_id(value != null ? Long.valueOf(value): 0L);
        }else{
            builder.source_id(0L);
        }
    }

    private Integer validateIsNumber(String number) {
        Integer value = null;
        try {
            value = Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            throw new ValidationException("invalid number format");
        }

        return value;
    }

    private void validValue_Number(Integer value, String field) {
        if (value < -1 || value > 1)
            throw new ValidationException("invalid value for field {}: " + field);
    }

    private void validateIsRequired(InboundRegistration item) {
        if (item == null) {
            throw new ValidationException(" Item should be present");
        }
        validateRequired(item.getLanguage_Preference(), "language_pref");
        validateRequired(item.getAsOfDate(), "as_of_date");
        validateRequired(item.getEmail_Permission(), "email_permission");
        validateRequired(item.getMail_Permission(), "mail_permission");
        validateRequired(item.getEmailPrefHDCA(), "email_pref_hd_ca");
        validateRequired(item.getGardenClub(), "email_pref_garden_club");
        validateRequired(item.getEmailPrefPRO(), "email_pref_pro");
        validateRequired(item.getNewMover(), "email_pref_new_mover");
        validateRequired(item.getSource_ID(), "source_id");
        validateRequired(item.getContent_1(), "content1");
        validateRequired(item.getContent_2(), "content2");
        validateRequired(item.getContent_3(), "content3");
        validateRequired(item.getContent_5(), "content5");
        validateRequired(item.getContent_6(), "content6");
    }

    public void validateRequired(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field + " should be present");
        }
    }

}

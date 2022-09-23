package ca.homedepot.preference.processor;

import ca.homedepot.preference.model.EmailOptOuts;
import ca.homedepot.preference.model.FileInboundStgTable;
import ca.homedepot.preference.util.validation.ExactTargetEmailValidation;
import ca.homedepot.preference.util.validation.InboundValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class ExactTargetEmailProcessor implements ItemProcessor<EmailOptOuts, FileInboundStgTable> {
    @Override
    public FileInboundStgTable process(EmailOptOuts item) throws Exception {

        FileInboundStgTable.FileInboundStgTableBuilder builder = FileInboundStgTable.builder();
        log.info(" Item in process: " + item.toString());
        Date srcDate = null;
        try{
            InboundValidator.validateRequired(item.getEmailAddress(), "email address");
            InboundValidator.validateRequired(item.getStatus(), "status");
            InboundValidator.validateRequired(item.getDateUnsubscribed(), "Date Unsubscribed");
            ExactTargetEmailValidation.validateStatusEmail(item.getStatus());
            srcDate = ExactTargetEmailValidation.validateDateFormat(item.getDateUnsubscribed());
            InboundValidator.validateEmailFormat(item.getEmailAddress());
        }catch (ValidationException e){
            log.error(" Validation error {}: ", e.getMessage());
            return null;
        }

		return builder.src_email_address(item.getEmailAddress())
				.email_status(new BigDecimal(ExactTargetEmailValidation.getExactTargetStatus(item.getStatus()))).status("NS")
				.src_date(srcDate).email_address_pref("0").email_pref_hd_ca("0").email_pref_garden_club("-1").email_pref_pro("-1")
				.email_pref_new_mover("-1").inserted_by("tested_batch").inserted_date(new Date()).build();
	}
}

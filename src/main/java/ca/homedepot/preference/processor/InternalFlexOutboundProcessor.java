package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalFlexOutboundProcessorDTO;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@Generated
public class InternalFlexOutboundProcessor implements ItemProcessor<InternalFlexOutboundDTO, InternalFlexOutboundProcessorDTO>
{

	private final Format formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * This method is used to transform the data coming from the db into the data to write in CSV file
	 *
	 * @param item
	 *           to be processed
	 * @return
	 * @throws Exception
	 */
	@Override
	public InternalFlexOutboundProcessorDTO process(InternalFlexOutboundDTO item) throws Exception
	{

		LocalDateTime itemEffectiveDate = item.getEffectiveDate();
		String effectiveDate = null != itemEffectiveDate ? itemEffectiveDate.format(formatter2) : "";

		String lastUpdateDate = item.getLastUpdateDate() != null ? formatter1.format(item.getLastUpdateDate()) : "";

		return InternalFlexOutboundProcessorDTO.builder().fileId(item.getFileId().toString()).sequenceNbr(item.getSequenceNbr())
				.emailAddr(item.getEmailAddr()).hdHhId(isValueNull(item.getHdHhId())).hdIndId(isValueNull(item.getHdIndId()))
				.customerNbr(item.getCustomerNbr()).storeNbr(item.getStoreNbr()).orgName(item.getOrgName())
				.companyCd(item.getCompanyCd()).custTypeCd(item.getCustTypeCd()).sourceId(item.getSourceId() + "")
				.effectiveDate(effectiveDate).lastUpdateDate(lastUpdateDate).industryCode(item.getIndustryCode())
				.companyName(item.getCompanyName()).contactFirstName(item.getContactFirstName())
				.contactLastName(item.getContactLastName()).contactRole(item.getContactRole()).build();
	}

	private static String isValueNull(BigDecimal value)
	{
		if (value == null)
			return null;
		return value.toPlainString();
	}
}

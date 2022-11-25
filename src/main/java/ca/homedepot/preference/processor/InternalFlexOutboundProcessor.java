package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalFlexOutboundProcessorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class InternalFlexOutboundProcessor implements ItemProcessor<InternalFlexOutboundDTO, InternalFlexOutboundProcessorDTO>
{

	private final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

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

		return InternalFlexOutboundProcessorDTO.builder().fileId(item.getFileId().toString()).sequenceNbr(item.getSequenceNbr())
				.emailAddr(item.getEmailAddr()).hdHhId(item.getHdHhId().toString()).hdIndId(item.getHdIndId().toString())
				.customerNbr(item.getCustomerNbr()).storeNbr(item.getStoreNbr()).orgName(item.getOrgName())
				.companyCd(item.getCompanyCd()).custTypeCd(item.getCustTypeCd()).sourceId(item.getSourceId().toString())
				.effectiveDate(formatter.format(item.getEffectiveDate())).lastUpdateDate(formatter.format(item.getLastUpdateDate()))
				.industryCode(item.getIndustryCode()).companyName(item.getCompanyName()).contactFirstName(item.getContactFirstName())
				.contactLastName(item.getContactLastName()).contactRole(item.getContactRole()).build();
	}
}

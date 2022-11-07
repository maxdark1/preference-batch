package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDtoProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class PreferenceOutboundProcessor implements ItemProcessor<PreferenceOutboundDto, PreferenceOutboundDtoProcessor>
{
	@Override
	public PreferenceOutboundDtoProcessor process(PreferenceOutboundDto preferenceOutboundDto) throws Exception
	{
		String split = SourceDelimitersConstants.SINGLE_DELIMITER_TAB;
		//TODO make formatter as class variable, not to initialize again
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
		PreferenceOutboundDtoProcessor preferenceOutboundDtoProcessor = new PreferenceOutboundDtoProcessor();

		preferenceOutboundDtoProcessor.setEmail(preferenceOutboundDto.getEmail() + split);
		preferenceOutboundDtoProcessor.setEffectiveDate(formatter.format(preferenceOutboundDto.getEffectiveDate()) + split);
		preferenceOutboundDtoProcessor.setSourceId(preferenceOutboundDto.getSourceId().toString() + split);
		preferenceOutboundDtoProcessor.setEmailStatus(preferenceOutboundDto.getEmailStatus() + split);
		preferenceOutboundDtoProcessor.setPhonePtcFlag(preferenceOutboundDto.getPhonePtcFlag().toString() + split);
		preferenceOutboundDtoProcessor.setLanguagePref(preferenceOutboundDto.getLanguagePref() + split);
		preferenceOutboundDtoProcessor.setEarlyOptInDate(formatter.format(preferenceOutboundDto.getEarlyOptInDate()) + split);
		preferenceOutboundDtoProcessor.setCndCompliantFlag(preferenceOutboundDto.getCndCompliantFlag().toString() + split);
		preferenceOutboundDtoProcessor.setEmailPrefHdCa(preferenceOutboundDto.getEmailPrefHdCa().toString() + split);
		preferenceOutboundDtoProcessor.setEmailPrefGardenClub(preferenceOutboundDto.getEmailPrefGardenClub().toString() + split);
		preferenceOutboundDtoProcessor.setEmailPrefPro(preferenceOutboundDto.getEmailPrefPro().toString() + split);
		preferenceOutboundDtoProcessor.setPostalCode(preferenceOutboundDto.getPostalCode() + split);
		preferenceOutboundDtoProcessor.setCustomerNbr(preferenceOutboundDto.getCustomerNbr() + split);
		preferenceOutboundDtoProcessor.setPhonePtcFlag(preferenceOutboundDto.getPhonePtcFlag().toString() + split);
		preferenceOutboundDtoProcessor.setDnclSuppresion(preferenceOutboundDto.getDnclSuppresion().toString() + split);
		preferenceOutboundDtoProcessor.setPhoneNumber(preferenceOutboundDto.getPhoneNumber() + split);
		preferenceOutboundDtoProcessor.setFirstName(preferenceOutboundDto.getFirstName() + split);
		preferenceOutboundDtoProcessor.setLastName(preferenceOutboundDto.getLastName() + split);
		preferenceOutboundDtoProcessor.setBusinessName(preferenceOutboundDto.getBusinessName() + split);
		preferenceOutboundDtoProcessor.setIndustryCode(preferenceOutboundDto.getIndustryCode() + split);
		preferenceOutboundDtoProcessor.setCity(preferenceOutboundDto.getCity() + split);
		preferenceOutboundDtoProcessor.setProvince(preferenceOutboundDto.getProvince() + split);
		preferenceOutboundDtoProcessor.setHdCaProSrcId(preferenceOutboundDto.getHdCaProSrcId() + "\n");

		return preferenceOutboundDtoProcessor;
	}
}

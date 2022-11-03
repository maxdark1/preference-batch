package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InternalOutboundProcessor implements ItemProcessor<InternalOutboundDto, InternalOutboundProcessorDto> {
    @Override
    public InternalOutboundProcessorDto process(InternalOutboundDto internalOutboundDto) throws Exception {

        String split = SourceDelimitersConstants.DELIMITER_COMA;
        InternalOutboundProcessorDto internalOutboundProcessorDto = new InternalOutboundProcessorDto();

        internalOutboundProcessorDto.setEmailAddr(internalOutboundDto.getEmailAddr() + split);
        internalOutboundProcessorDto.setCanPtcEffectiveDate(internalOutboundDto.getCanPtcEffectiveDate() + split);
        internalOutboundProcessorDto.setCanPtcSourceId(internalOutboundDto.getCanPtcSourceId()+ split);
        internalOutboundProcessorDto.setEmailStatus(internalOutboundDto.getEmailStatus()+ split);
        internalOutboundProcessorDto.setCanPtcGlag(internalOutboundDto.getCanPtcGlag()+ split);
        internalOutboundProcessorDto.setLanguagePreference(internalOutboundDto.getLanguagePreference()+ split);
        internalOutboundProcessorDto.setEarlyOptInIDate(internalOutboundDto.getEarlyOptInIDate()+ split);
        internalOutboundProcessorDto.setCndCompliantFlag(internalOutboundDto.getCndCompliantFlag()+ split);
        internalOutboundProcessorDto.setHdCaFlag(internalOutboundDto.getHdCaFlag()+ split);
        internalOutboundProcessorDto.setHdCaGardenClubFlag(internalOutboundDto.getHdCaGardenClubFlag()+ split);
        internalOutboundProcessorDto.setHdCaNewMoverFlag(internalOutboundDto.getHdCaNewMoverFlag()+ split);
        internalOutboundProcessorDto.setHdCaNewMoverEffDate(internalOutboundDto.getHdCaNewMoverEffDate()+ split);
        internalOutboundProcessorDto.setHdCaProFlag(internalOutboundDto.getHdCaProFlag()+ split);
        internalOutboundProcessorDto.setPhonePtcFlag(internalOutboundDto.getPhonePtcFlag()+ split);
        internalOutboundProcessorDto.setFirstName(internalOutboundDto.getFirstName()+ split);
        internalOutboundProcessorDto.setLastName(internalOutboundDto.getLastName()+ split);
        internalOutboundProcessorDto.setPostalCode(internalOutboundDto.getPostalCode()+ split);
        internalOutboundProcessorDto.setProvince(internalOutboundDto.getProvince()+ split);
        internalOutboundProcessorDto.setCity(internalOutboundDto.getCity()+ split);
        internalOutboundProcessorDto.setPhoneNumber(internalOutboundDto.getPhoneNumber()+ split);
        internalOutboundProcessorDto.setBussinessName(internalOutboundDto.getBussinessName()+ split);
        internalOutboundProcessorDto.setIndustryCode(internalOutboundDto.getIndustryCode()+ split);
        internalOutboundProcessorDto.setMoveDate(internalOutboundDto.getMoveDate()+ split);
        internalOutboundProcessorDto.setDwellingType(internalOutboundDto.getDwellingType() + "\n");

        return internalOutboundProcessorDto;
    }
}

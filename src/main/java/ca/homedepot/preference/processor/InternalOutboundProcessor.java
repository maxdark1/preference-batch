package ca.homedepot.preference.processor;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;

@Component
@Slf4j
public class InternalOutboundProcessor implements ItemProcessor<InternalOutboundDto, InternalOutboundProcessorDto>
{

	private final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

	/**
	 * This method is used to transform the data coming from the db into the data to write in CSV file
	 * 
	 * @param internalOutboundDto
	 *           to be processed
	 * @return
	 * @throws Exception
	 */
	@Override
	public InternalOutboundProcessorDto process(InternalOutboundDto internalOutboundDto) throws Exception
	{

		String split = SourceDelimitersConstants.DELIMITER_COMA;
		InternalOutboundProcessorDto internalOutboundProcessorDto = new InternalOutboundProcessorDto();

		internalOutboundProcessorDto.setEmailAddr(internalOutboundDto.getEmailAddr() + split);
		internalOutboundProcessorDto.setCanPtcEffectiveDate(formatter.format(internalOutboundDto.getCanPtcEffectiveDate()) + split);
		internalOutboundProcessorDto.setCanPtcSourceId(internalOutboundDto.getCanPtcSourceId() + split);
		internalOutboundProcessorDto.setEmailStatus((internalOutboundDto.getEmailStatus() == BigDecimal.ZERO ? "00" : internalOutboundDto.getEmailStatus() ) + split);
		internalOutboundProcessorDto.setCanPtcFlag(internalOutboundDto.getCanPtcGlag() + split);
		internalOutboundProcessorDto.setLanguagePreference(internalOutboundDto.getLanguagePreference() + split);
		internalOutboundProcessorDto.setEarlyOptInDate(internalOutboundDto.getEarlyOptInIDate() + split);
		internalOutboundProcessorDto.setCndCompliantFlag(internalOutboundDto.getCndCompliantFlag() + split);
		internalOutboundProcessorDto.setHdCaFlag(internalOutboundDto.getHdCaFlag() + split);
		internalOutboundProcessorDto.setHdCaGardenClubFlag(internalOutboundDto.getHdCaGardenClubFlag() + split);
		internalOutboundProcessorDto.setHdCaNewMoverFlag(internalOutboundDto.getHdCaNewMoverFlag() + split);
		internalOutboundProcessorDto.setHdCaNewMoverEffDate(internalOutboundDto.getHdCaNewMoverEffDate() + split);
		internalOutboundProcessorDto.setHdCaProFlag(internalOutboundDto.getHdCaProFlag() + split);
		internalOutboundProcessorDto.setPhonePtcFlag(internalOutboundDto.getPhonePtcFlag() + split);
		internalOutboundProcessorDto.setFirstName(internalOutboundDto.getFirstName() + split);
		internalOutboundProcessorDto.setLastName(internalOutboundDto.getLastName() + split);
		internalOutboundProcessorDto.setPostalCode(internalOutboundDto.getPostalCode() + split);
		internalOutboundProcessorDto.setProvince(internalOutboundDto.getProvince() + split);
		internalOutboundProcessorDto.setCity(internalOutboundDto.getCity() + split);
		internalOutboundProcessorDto.setPhoneNumber(internalOutboundDto.getPhoneNumber() + split);
		internalOutboundProcessorDto.setBussinessName(internalOutboundDto.getBussinessName() + split);
		internalOutboundProcessorDto.setIndustryCode(internalOutboundDto.getIndustryCode() + split);
		internalOutboundProcessorDto.setMoveDate(internalOutboundDto.getMoveDate() + split);
		internalOutboundProcessorDto.setDwellingType(internalOutboundDto.getDwellingType() + "\n");

		return internalOutboundProcessorDto;
	}
}

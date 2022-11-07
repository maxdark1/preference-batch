package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalOutboundProcessorDto
{
	private String emailAddr;
	private String canPtcEffectiveDate;
	private String canPtcSourceId;
	private String emailStatus;
	private String canPtcFlag;
	private String languagePreference;
	private String earlyOptInDate;
	private String cndCompliantFlag;
	private String hdCaFlag;
	private String hdCaGardenClubFlag;
	private String hdCaNewMoverFlag;
	private String hdCaNewMoverEffDate;
	private String hdCaProFlag;
	private String phonePtcFlag;
	private String firstName;
	private String lastName;
	private String postalCode;
	private String province;
	private String city;
	private String phoneNumber;
	private String bussinessName;
	private String industryCode;
	private String moveDate;
	private String dwellingType;
}

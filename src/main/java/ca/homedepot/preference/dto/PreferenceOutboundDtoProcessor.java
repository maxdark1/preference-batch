package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PreferenceOutboundDtoProcessor
{
	private String email;
	private String effectiveDate;
	private String sourceId;
	private String emailStatus;
	private String emailPermission;
	private String languagePref;
	private String earlyOptInDate;
	private String cndCompliantFlag;
	private String emailPrefHdCa;
	private String emailPrefGardenClub;
	private String emailPrefPro;
	private String postalCode;
	private String customerNbr;
	private String phonePtcFlag;
	private String dnclSuppresion;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String businessName;
	private String industryCode;
	private String city;
	private String province;
	private String hdCaProSrcId;
}

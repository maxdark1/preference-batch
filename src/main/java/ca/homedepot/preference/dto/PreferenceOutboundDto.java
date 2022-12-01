package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceOutboundDto
{
	private String email;
	private Date effectiveDate;
	private BigDecimal sourceId;
	private BigDecimal emailStatus;
	private Character emailPermission;
	private String languagePref;
	private Date earlyOptInDate;
	private Character cndCompliantFlag;
	private Character emailPrefHdCa;
	private Character emailPrefGardenClub;
	private Character emailPrefPro;
	private String postalCode;
	private String customerNbr;
	private Character phonePtcFlag;
	private Character dnclSuppresion;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String businessName;
	private String industryCode;
	private String city;
	private String province;
	private BigDecimal hdCaProSrcId;
}

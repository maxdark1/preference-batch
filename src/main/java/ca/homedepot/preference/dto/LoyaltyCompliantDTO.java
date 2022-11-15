package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyCompliantDTO
{
	private String emailAddr;
	private Timestamp canPtcEffectiveDate;
	private BigDecimal canPtcSourceId;
	private String emailStatus;
	private String canPtcFlag;
	private String firstName;
	private String lastName;
	private String languagePreference;
	private Timestamp earlyOptInDate;
	private String cndCompliantFlag;
	private String hdCaFlag;
	private String hdCaGardenClubFlag;
	private String hdCaProFlag;
	private String postalCd;
	private String city;
	private String customerNbr;
	private String province;
}

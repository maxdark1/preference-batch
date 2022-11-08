package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesforceExtractOutboundDTO
{

	private String emailAddress;
	private LocalDateTime asOfDate;
	private String sourceId;
	private String emailStatus;
	private String emailPtc;
	private String languagePreference;
	private LocalDateTime earliestOptInDate;
	private String hdCanadaEmailCompliantFlag;
	private String hdCanadaFlag;
	private String gardenClubFlag;
	private String newMoverFlag;
	private String proFlag;
	private String phonePtcFlag;
	private String firstName;
	private String lastName;
	private String postalCode;
	private String province;
	private String city;
	private String phoneNumber;
	private String businessName;
	private String businessType;
	private LocalDate moveDate;
	private String dwellingType;

}
package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.SINGLE_DELIMITER_TAB;

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

	public String toString()
	{
		return new StringBuilder().append(emailAddress).append(SINGLE_DELIMITER_TAB).append(asOfDate).append(SINGLE_DELIMITER_TAB)
				.append(sourceId).append(SINGLE_DELIMITER_TAB).append(emailStatus).append(SINGLE_DELIMITER_TAB)
				.append(emailPtc).append(SINGLE_DELIMITER_TAB).append(languagePreference).append(SINGLE_DELIMITER_TAB)
				.append(earliestOptInDate).append(SINGLE_DELIMITER_TAB).append(hdCanadaEmailCompliantFlag).append(SINGLE_DELIMITER_TAB)
				.append(hdCanadaFlag).append(SINGLE_DELIMITER_TAB).append(gardenClubFlag).append(SINGLE_DELIMITER_TAB).append(newMoverFlag).append(SINGLE_DELIMITER_TAB).append(proFlag).append(SINGLE_DELIMITER_TAB)
				.append(phonePtcFlag).append(SINGLE_DELIMITER_TAB).append(firstName).append(SINGLE_DELIMITER_TAB).append(lastName).append(postalCode).append(SINGLE_DELIMITER_TAB)
				.append(province).append(SINGLE_DELIMITER_TAB).append(city).append(SINGLE_DELIMITER_TAB).append(phoneNumber).append(SINGLE_DELIMITER_TAB).append(businessName).append(SINGLE_DELIMITER_TAB).append(businessType).append(SINGLE_DELIMITER_TAB).append(moveDate).append(SINGLE_DELIMITER_TAB).append(dwellingType).toString();
	}

}
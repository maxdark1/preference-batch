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
		return new StringBuilder().append(emptyString(emailAddress)).append(SINGLE_DELIMITER_TAB).append(emptyString(asOfDate))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(sourceId)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(emailStatus)).append(SINGLE_DELIMITER_TAB).append(emptyString(emailPtc))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(languagePreference)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(earliestOptInDate)).append(SINGLE_DELIMITER_TAB).append(emptyString(hdCanadaEmailCompliantFlag))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(hdCanadaFlag)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(gardenClubFlag)).append(SINGLE_DELIMITER_TAB).append(emptyString(newMoverFlag))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(proFlag)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(phonePtcFlag)).append(SINGLE_DELIMITER_TAB).append(emptyString(firstName))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(lastName)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(postalCode)).append(SINGLE_DELIMITER_TAB).append(emptyString(province))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(city)).append(SINGLE_DELIMITER_TAB).append(emptyString(phoneNumber))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(businessName)).append(SINGLE_DELIMITER_TAB)
				.append(emptyString(businessType)).append(SINGLE_DELIMITER_TAB).append(emptyString(moveDate))
				.append(SINGLE_DELIMITER_TAB).append(emptyString(dwellingType)).toString();
	}

	private String emptyString(Object text)
	{
		return text == null ? "" : text.toString();
	}

}
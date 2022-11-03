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
public class InternalOutboundDto {
    private String emailAddr;
    private Date canPtcEffectiveDate;
    private BigDecimal canPtcSourceId;
    private BigDecimal emailStatus;
    private String canPtcGlag;
    private String languagePreference;
    private Date earlyOptInIDate;
    private String cndCompliantFlag;
    private String hdCaFlag;
    private String hdCaGardenClubFlag;
    private String hdCaNewMoverFlag;
    private Date hdCaNewMoverEffDate;
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

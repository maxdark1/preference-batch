package ca.homedepot.preference.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@Builder
public class InternalFlexOutboundDTO
{
	private BigDecimal fileId;
	private String sequenceNbr;
	private String emailAddr;
	private BigDecimal hdHhId;
	private BigDecimal hdIndId;
	private String customerNbr;
	private String storeNbr;
	private String orgName;
	private String companyCd;
	private String custTypeCd;
	private Long sourceId;
	private LocalDateTime effectiveDate;
	private Date lastUpdateDate;
	private String industryCode;
	private String companyName;
	private String contactFirstName;
	private String contactLastName;
	private String contactRole;


}

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
	private Date effectiveDate;
	private Date lastUpdateDate;
	private String industryCode;
	private String companyName;
	private String contactFirstName;
	private String contactLastName;
	private String contactRole;


}

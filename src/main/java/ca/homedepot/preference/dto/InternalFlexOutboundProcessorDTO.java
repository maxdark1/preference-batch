package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalFlexOutboundProcessorDTO
{
	private String fileId;
	private String sequenceNbr;
	private String emailAddr;
	private String hdHhId;
	private String hdIndId;
	private String customerNbr;
	private String storeNbr;
	private String orgName;
	private String companyCd;
	private String custTypeCd;
	private String sourceId;
	private String effectiveDate;
	private String lastUpdateDate;
	private String industryCode;
	private String companyName;
	private String contactFirstName;
	private String contactLastName;
	private String contactRole;
}

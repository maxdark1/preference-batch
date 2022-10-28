package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitiSuppresionOutboundDTO
{
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String stateCd;
	private String postalCd;
	private String emailAddr;
	private String phone;
	private String smsMobilePhone;
	private String businessName;
	private String dmOptOut;
	private String emailOptOut;
	private String phoneOptOut;
	private String smsOptOut;


	public String rowOnFIle(){
		return firstName+","+middleInitial+","+lastName+","+addrLine1
				+","+addrLine2+","+city+","+stateCd+","+postalCd+","+emailAddr+","+phone+","+smsMobilePhone+","+businessName+","+dmOptOut+","+emailOptOut+","+phoneOptOut+","+smsOptOut+"\n";
	}
}

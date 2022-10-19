package ca.homedepot.preference.model;


import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/*
* LayoutC fields
* */
@Data
public class InboundRegistration implements ResourceAware
{
	/*
	 * Field Language_Preference on LayoutC
	 *
	 */
	private String Language_Preference;

	/*
	 * Field AsOfDate on LayoutC MM-dd-yyyy HH:mm:ss
	 */
	private String AsOfDate;

	/*
	 * Field Email_Address on LayoutC
	 *
	 */
	private String Email_Address;

	/*
	 * Field Email_Permission on LayoutC
	 *
	 */
	private String Email_Permission;

	/*
	 * Field Phone_Permission on LayoutC
	 *
	 */
	private String Phone_Permission;


	/*
	 * Field Phone_Number on LayoutC
	 *
	 */
	private String Phone_Number;

	/*
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String Phone_Extension;

	/*
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String Title;

	/*
	 * Field First_Name on LayoutC
	 *
	 */
	private String First_Name;

	/*
	 * Field Last_Name on LayoutC
	 *
	 */
	private String Last_Name;

	/*
	 * Field Address_1 on LayoutC
	 *
	 */
	private String Address_1;

	/*
	 * Field Address_2 on LayoutC
	 *
	 */
	private String Address_2;

	/*
	 * Field City on LayoutC
	 *
	 */
	private String City;

	/*
	 * Field Province on LayoutC
	 *
	 */
	private String Province;

	/*
	 * Field Postal_Code on LayoutC
	 *
	 */
	private String Postal_Code;

	/*
	 * Field Mail_Permission on LayoutC
	 *
	 */
	private String Mail_Permission;

	/*
	 * Field EmailPrefHDCA on LayoutC
	 *
	 */
	private String EmailPrefHDCA;

	/*
	 * Field GardenClub on LayoutC
	 *
	 */
	private String GardenClub;

	/*
	 * Field EmailPrefPRO on LayoutC
	 *
	 */
	private String EmailPrefPRO;

	/*
	 * Field NewMover on LayoutC
	 *
	 */
	private String NewMover;

	/*
	 * Field For_Future_Use on LayoutC
	 *
	 */
	private String For_Future_Use;

	/*
	 * Field Source_ID on LayoutC
	 *
	 */
	private String Source_ID;

	/*
	 * Field SMS_Flag on LayoutC
	 *
	 */
	private String SMS_Flag;

	/*
	 * Field Fax_Number on LayoutC
	 *
	 */
	private String Fax_Number;

	/*
	 * Field Fax_Extension on LayoutC
	 *
	 */
	private String Fax_Extension;

	///----------------------
	// Content_ - Value_ pair
	//-----------------------
	private String Content_1;
	private String Value_1;
	private String Content_2;
	private String Value_2;
	private String Content_3;
	private String Value_3;
	private String Content_4;
	private String Value_4;
	private String Content_5;
	private String Value_5;
	private String Content_6;
	private String Value_6;
	private String Content_7;
	private String Value_7;
	private String Content_8;
	private String Value_8;
	private String Content_9;
	private String Value_9;
	private String Content_10;
	private String Value_10;
	private String Content_11;
	private String Value_11;
	private String Content_12;
	private String Value_12;
	private String Content_13;
	private String Value_13;
	private String Content_14;
	private String Value_14;
	private String Content_15;
	private String Value_15;
	private String Content_16;
	private String Value_16;
	private String Content_17;
	private String Value_17;
	private String Content_18;
	private String Value_18;
	private String Content_19;
	private String Value_19;
	private String Content_20;
	private String Value_20;
	private String fileName;

	@Override
	public void setResource(Resource resource)
	{
		this.fileName = resource.getFilename();
	}
}

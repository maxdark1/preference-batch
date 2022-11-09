package ca.homedepot.preference.model;


import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/**
 * LayoutC fields Resource aware from which one is coming
 */
@Data

//TODO fields can be camelCase.
public class InboundRegistration implements ResourceAware
{
	/**
	 * Field Language_Preference on LayoutC
	 *
	 */
	private String Language_Preference;

	/**
	 * Field AsOfDate on LayoutC MM-dd-yyyy HH:mm:ss
	 */
	private String AsOfDate;

	/**
	 * Field Email_Address on LayoutC
	 *
	 */
	private String Email_Address;

	/**
	 * Field Email_Permission on LayoutC
	 *
	 */
	private String Email_Permission;

	/**
	 * Field Phone_Permission on LayoutC
	 *
	 */
	private String Phone_Permission;


	/**
	 * Field Phone_Number on LayoutC
	 *
	 */
	private String Phone_Number;

	/**
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String Phone_Extension;

	/**
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String Title;

	/**
	 * Field First_Name on LayoutC
	 *
	 */
	private String First_Name;

	/**
	 * Field Last_Name on LayoutC
	 *
	 */
	private String Last_Name;

	/**
	 * Field Address_1 on LayoutC
	 *
	 */
	private String Address_1;

	/**
	 * Field Address_2 on LayoutC
	 *
	 */
	private String Address_2;

	/**
	 * Field City on LayoutC
	 *
	 */
	private String City;

	/**
	 * Field Province on LayoutC
	 *
	 */
	private String Province;

	/**
	 * Field Postal_Code on LayoutC
	 *
	 */
	private String Postal_Code;

	/**
	 * Field Mail_Permission on LayoutC
	 *
	 */
	private String Mail_Permission;

	/**
	 * Field EmailPrefHDCA on LayoutC
	 *
	 */
	private String EmailPrefHDCA;

	/**
	 * Field GardenClub on LayoutC
	 *
	 */
	private String GardenClub;

	/**
	 * Field EmailPrefPRO on LayoutC
	 *
	 */
	private String EmailPrefPRO;

	/**
	 * Field NewMover on LayoutC
	 *
	 */
	private String NewMover;

	/**
	 * Field For_Future_Use on LayoutC
	 *
	 */
	private String For_Future_Use;

	/**
	 * Field Source_ID on LayoutC
	 *
	 */
	private String Source_ID;

	/**
	 * Field SMS_Flag on LayoutC
	 *
	 */
	private String SMS_Flag;

	/**
	 * Field Fax_Number on LayoutC
	 */
	private String Fax_Number;

	/**
	 * Field Fax_Extension on LayoutC
	 */
	private String Fax_Extension;

	/**
	 * Field content_1 on LayoutC
	 */
	private String Content_1;
	/**
	 * Field value_1 on LayoutC
	 */
	private String Value_1;
	/**
	 * Field content_2 on LayoutC
	 */
	private String Content_2;
	/**
	 * Field value_2 on LayoutC
	 */
	private String Value_2;
	/**
	 * Field content_3 on LayoutC
	 */
	private String Content_3;
	/**
	 * Field value_3 on LayoutC
	 */
	private String Value_3;
	/**
	 * Field content_4 on LayoutC
	 */
	private String Content_4;
	/**
	 * Field value_4 on LayoutC
	 */
	private String Value_4;
	/**
	 * Field content_5 on LayoutC
	 */
	private String Content_5;
	/**
	 * Field value_5 on LayoutC
	 */
	private String Value_5;
	/**
	 * Field content_6 on LayoutC
	 */
	private String Content_6;
	/**
	 * Field value_6 on LayoutC
	 */
	private String Value_6;
	/**
	 * Field content_7 on LayoutC
	 */
	private String Content_7;
	/**
	 * Field value_7 on LayoutC
	 */
	private String Value_7;
	/**
	 * Field content_8 on LayoutC
	 */
	private String Content_8;
	/**
	 * Field value_8 on LayoutC
	 */
	private String Value_8;
	/**
	 * Field content_9 on LayoutC
	 */
	private String Content_9;
	/**
	 * Field value_9 on LayoutC
	 */
	private String Value_9;
	/**
	 * Field content_10 on LayoutC
	 */
	private String Content_10;
	/**
	 * Field value_10 on LayoutC
	 */
	private String Value_10;
	/**
	 * Field content_11 on LayoutC
	 */
	private String Content_11;
	/**
	 * Field value_11 on LayoutC
	 */
	private String Value_11;
	/**
	 * Field content_12 on LayoutC
	 */
	private String Content_12;
	/**
	 * Field value_12 on LayoutC
	 */
	private String Value_12;
	/**
	 * Field content_13 on LayoutC
	 */
	private String Content_13;
	/**
	 * Field value_13 on LayoutC
	 */
	private String Value_13;
	/**
	 * Field content_14 on LayoutC
	 */
	private String Content_14;
	/**
	 * Field value_14 on LayoutC
	 */
	private String Value_14;
	/**
	 * Field content_15 on LayoutC
	 */
	private String Content_15;
	/**
	 * Field value_15 on LayoutC
	 */
	private String Value_15;
	/**
	 * Field content_16 on LayoutC
	 */
	private String Content_16;
	/**
	 * Field value_16 on LayoutC
	 */
	private String Value_16;
	/**
	 * Field content_17 on LayoutC
	 */
	private String Content_17;
	/**
	 * Field value_17 on LayoutC
	 */
	private String Value_17;
	/**
	 * Field content_18 on LayoutC
	 */
	private String Content_18;
	/**
	 * Field value_18 on LayoutC
	 */
	private String Value_18;
	/**
	 * Field content_19 on LayoutC
	 */
	private String Content_19;
	/**
	 * Field value_19 on LayoutC
	 */
	private String Value_19;
	/**
	 * Field content_20 on LayoutC
	 */
	private String Content_20;
	/**
	 * Field content_20 on LayoutC
	 */
	private String Value_20;
	/**
	 * File name from is coming
	 */
	private String fileName;

	/**
	 * Sets file name according on the resource that is being read
	 *
	 * @param resource
	 */
	@Override
	public void setResource(Resource resource)
	{
		this.fileName = resource.getFilename();
	}
}
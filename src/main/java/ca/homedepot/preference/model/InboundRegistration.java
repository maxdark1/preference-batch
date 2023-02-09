package ca.homedepot.preference.model;


import lombok.Data;
import lombok.Generated;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/**
 * LayoutC fields Resource aware from which one is coming
 */
@Data
@Generated
public class InboundRegistration implements ResourceAware
{
	/**
	 * Field Language_Preference on LayoutC
	 *
	 */
	private String languagePreference;

	/**
	 * Field AsOfDate on LayoutC MM-dd-yyyy HH:mm:ss
	 */
	private String asOfDate;

	/**
	 * Field Email_Address on LayoutC
	 *
	 */
	private String emailAddress;

	/**
	 * Field Email_Permission on LayoutC
	 *
	 */
	private String emailPermission;

	/**
	 * Field Phone_Permission on LayoutC
	 *
	 */
	private String phonePermission;


	/**
	 * Field Phone_Number on LayoutC
	 *
	 */
	private String phoneNumber;

	/**
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String phoneExtension;

	/**
	 * Field Phone_Extension on LayoutC
	 *
	 */
	private String title;

	/**
	 * Field First_Name on LayoutC
	 *
	 */
	private String firstName;

	/**
	 * Field Last_Name on LayoutC
	 *
	 */
	private String lastName;

	/**
	 * Field Address_1 on LayoutC
	 *
	 */
	private String address1;

	/**
	 * Field Address_2 on LayoutC
	 *
	 */
	private String address2;

	/**
	 * Field City on LayoutC
	 *
	 */
	private String city;

	/**
	 * Field Province on LayoutC
	 *
	 */
	private String province;

	/**
	 * Field Postal_Code on LayoutC
	 *
	 */
	private String postalCode;

	/**
	 * Field Mail_Permission on LayoutC
	 *
	 */
	private String mailPermission;

	/**
	 * Field EmailPrefHDCA on LayoutC
	 *
	 */
	private String emailPrefHDCA;

	/**
	 * Field GardenClub on LayoutC
	 *
	 */
	private String gardenClub;

	/**
	 * Field EmailPrefPRO on LayoutC
	 *
	 */
	private String emailPrefPRO;

	/**
	 * Field NewMover on LayoutC
	 *
	 */
	private String newMover;

	/**
	 * Field For_Future_Use on LayoutC
	 *
	 */
	private String forFutureUse;

	/**
	 * Field Source_ID on LayoutC
	 *
	 */
	private String sourceID;

	/**
	 * Field SMS_Flag on LayoutC
	 *
	 */
	private String smsFlag;

	/**
	 * Field Fax_Number on LayoutC
	 */
	private String faxNumber;

	/**
	 * Field Fax_Extension on LayoutC
	 */
	private String faxExtension;

	/**
	 * Field content_1 on LayoutC
	 */
	private String content1;
	/**
	 * Field value_1 on LayoutC
	 */
	private String value1;
	/**
	 * Field content_2 on LayoutC
	 */
	private String content2;
	/**
	 * Field value_2 on LayoutC
	 */
	private String value2;
	/**
	 * Field content_3 on LayoutC
	 */
	private String content3;
	/**
	 * Field value_3 on LayoutC
	 */
	private String value3;
	/**
	 * Field content_4 on LayoutC
	 */
	private String content4;
	/**
	 * Field value_4 on LayoutC
	 */
	private String value4;
	/**
	 * Field content_5 on LayoutC
	 */
	private String content5;
	/**
	 * Field value_5 on LayoutC
	 */
	private String value5;
	/**
	 * Field content_6 on LayoutC
	 */
	private String content6;
	/**
	 * Field value_6 on LayoutC
	 */
	private String value6;
	/**
	 * Field content_7 on LayoutC
	 */
	private String content7;
	/**
	 * Field value_7 on LayoutC
	 */
	private String value7;
	/**
	 * Field content_8 on LayoutC
	 */
	private String content8;
	/**
	 * Field value_8 on LayoutC
	 */
	private String value8;
	/**
	 * Field content_9 on LayoutC
	 */
	private String content9;
	/**
	 * Field value_9 on LayoutC
	 */
	private String value9;
	/**
	 * Field content_10 on LayoutC
	 */
	private String content10;
	/**
	 * Field value_10 on LayoutC
	 */
	private String value10;
	/**
	 * Field content_11 on LayoutC
	 */
	private String content11;
	/**
	 * Field value_11 on LayoutC
	 */
	private String value11;
	/**
	 * Field content_12 on LayoutC
	 */
	private String content12;
	/**
	 * Field value_12 on LayoutC
	 */
	private String value12;
	/**
	 * Field content_13 on LayoutC
	 */
	private String content13;
	/**
	 * Field value_13 on LayoutC
	 */
	private String value13;
	/**
	 * Field content_14 on LayoutC
	 */
	private String content14;
	/**
	 * Field value_14 on LayoutC
	 */
	private String value14;
	/**
	 * Field content_15 on LayoutC
	 */
	private String content15;
	/**
	 * Field value_15 on LayoutC
	 */
	private String value15;
	/**
	 * Field content_16 on LayoutC
	 */
	private String content16;
	/**
	 * Field value_16 on LayoutC
	 */
	private String value16;
	/**
	 * Field content_17 on LayoutC
	 */
	private String content17;
	/**
	 * Field value_17 on LayoutC
	 */
	private String value17;
	/**
	 * Field content_18 on LayoutC
	 */
	private String content18;
	/**
	 * Field value_18 on LayoutC
	 */
	private String value18;
	/**
	 * Field content_19 on LayoutC
	 */
	private String content19;
	/**
	 * Field value_19 on LayoutC
	 */
	private String value19;
	/**
	 * Field content_20 on LayoutC
	 */
	private String content20;
	/**
	 * Field content_20 on LayoutC
	 */
	private String value20;
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
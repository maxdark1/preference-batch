package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.CUST_TYPE;

/**
 * Master Processor obtains information from Master catalog
 */
@UtilityClass
public class MasterProcessor
{

	/**
	 * The preference Service
	 */
	private static PreferenceService preferenceService;

	/**
	 * The master List
	 */
	private static List<Master> masterList;

	/**
	 * Gets Master information from persistence and save's it as static value
	 */
	public static void getMasterInfo()
	{
		MasterProcessor.setMasterList(preferenceService.getMasterInfo());
	}

	/**
	 * Obtains Master value according to key_val and value_val
	 * 
	 * @param keyVal
	 * @param valueVal
	 * @return the current Master value
	 */
	public static Master getSourceID(String keyVal, String valueVal)
	{
		Optional<Master> optionalMaster = masterList.stream()
				.filter(master -> master.getKeyValue().equals(keyVal) && master.getValueVal().equals(valueVal)).findFirst();
		return optionalMaster.orElse(null);
	}

	/**
	 * Obtains Value_val according to MasterId
	 * 
	 * @param masterId
	 * @return Value_val of a certain Master information
	 */
	public static String getValueVal(BigDecimal masterId)
	{
		return masterList.stream().filter(master -> master.getMasterId().equals(masterId)).map(Master::getValueVal).findFirst()
				.orElse(null);
	}

	/**
	 * Gets Customer Type CD from Master table
	 * 
	 * @param oldId
	 * @return Master value for old id assigned
	 */
	public static BigDecimal getCustTypeCD(String oldId)
	{
		return masterList.stream()
				.filter(master -> master.getKeyValue().equals(CUST_TYPE) && master.getOldID().equals(new BigDecimal(oldId)))
				.map(Master::getMasterId).findFirst().orElse(null);
	}

	/**
	 * Gets the actual MasterID for the current source
	 * 
	 * @param oldId
	 * @return MasterID
	 */
	public static BigDecimal getSourceID(String oldId)
	{

		BigDecimal masterId = new BigDecimal("-400");

		if (oldId == null || oldId.equals("null"))
			return masterId;
		Optional<Master> optional = masterList.stream().filter(master -> master.getOldID() != null
				&& master.getKeyValue().equals("SOURCE_ID") && master.getOldID().equals(new BigDecimal(oldId))).findFirst();
		if (optional.isPresent())
		{

			/**
			 * Gets the MasterID from the Master List
			 */
			return optional.get().getMasterId();
		}
		else
		{

			/**
			 * If it founds any exception return an invalid ID that's a flag for the validation
			 */
			return masterId;
		}
	}

	/**
	 * Gets Master information and saves it in masterList
	 * 
	 * @return
	 */
	public static List<Master> getMasterList()
	{
		return masterList;
	}

	/**
	 * Sets master list content
	 * 
	 * @param masterList
	 */
	public static void setMasterList(List<Master> masterList)
	{
		MasterProcessor.masterList = masterList;
	}


	public static void setPreferenceService(PreferenceService preferenceService)
	{
		MasterProcessor.preferenceService = preferenceService;
	}
}

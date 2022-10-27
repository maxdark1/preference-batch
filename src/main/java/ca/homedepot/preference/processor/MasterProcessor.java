package ca.homedepot.preference.processor;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;

/**
 * Master Processor obtains information from Master catalog
 */
@Component
public class MasterProcessor
{

	/**
	 * The preference Service
	 */
	private PreferenceService preferenceService;

	/**
	 * The master List
	 */
	private static List<Master> masterList;

	/**
	 * Gets Master information from persistence and save's it as static value
	 */
	public void getMasterInfo()
	{
		masterList = preferenceService.getMasterInfo();
	}

	/**
	 * Obtains Master value according to key_val and value_val
	 * 
	 * @param key_val
	 * @param value_val
	 * @return the current Master value
	 */
	public static Master getSourceID(String key_val, String value_val)
	{
		return masterList.stream().filter(master -> master.getKeyValue().equals(key_val) && master.getValueVal().equals(value_val))
				.findFirst().get();
	}

	/**
	 * Obtains Value_val according to MasterId
	 * 
	 * @param masterId
	 * @return Value_val of a certain Master information
	 */
	public static String getValueVal(BigDecimal masterId)
	{
		return masterList.stream().filter(master -> master.getMasterId().equals(masterId)).map(master -> master.getValueVal())
				.findFirst().get();
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

		try
		{
			/**
			 * Gets the MasterID from the Master List
			 */
			return masterList.stream().filter(master -> master.getOldID() != null && master.getKeyValue().equals("SOURCE_ID")
					&& master.getOldID().toPlainString().equals(oldId)).findFirst().get().getMasterId();
		}
		catch (Exception e)
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

	@Autowired
	public void setPreferenceService(PreferenceService preferenceService)
	{
		this.preferenceService = preferenceService;
	}
}

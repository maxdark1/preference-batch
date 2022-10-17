package ca.homedepot.preference.processor;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.Getter;

/**
 * Master Processor obtains information from Master catalog
 */
@Component
@Setter
public class MasterProcessor
{

	/**
	 * The preference Service
	 */
	@Autowired
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
	public static Master getSourceId(String key_val, String value_val)
	{
		return masterList.stream()
				.filter(master -> master.getKey_value().equals(key_val) && master.getValue_val().equals(value_val)).findFirst().get();
	}

	/**
	 * Obtains Value_val according to MasterId
	 * 
	 * @param masterId
	 * @return Value_val of a certain Master information
	 */
	public static String getValueVal(BigDecimal masterId)
	{
		return masterList.stream().filter(master -> master.getMaster_id().equals(masterId)).map(master -> master.getValue_val())
				.findFirst().get();
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
}

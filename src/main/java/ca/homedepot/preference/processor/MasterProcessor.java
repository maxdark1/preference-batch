package ca.homedepot.preference.processor;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.Getter;

@Component
@Setter
public class MasterProcessor
{

	@Autowired
	private PreferenceService preferenceService;

	private static List<Master> masterList;

	/*
	 * Gets Master information from persistence and save's it as static value
	 *
	 * @param
	 *
	 * @return
	 */
	public void getMasterInfo()
	{
		masterList = preferenceService.getMasterInfo();
	}

	/*
	 * Obtains Master value according to key_val and value_val
	 *
	 * @param key_val, value_val
	 *
	 * @return Master
	 */
	public static Master getSourceId(String key_val, String value_val)
	{
		return masterList.stream().filter(master -> master.getKey_value().equals(key_val) && master.getValue_val().equals(value_val))
				.findFirst().get();
	}

	/*
	 * Obtains Value_val according to MasterId
	 *
	 * @param masterId
	 *
	 * @return String
	 */
	public static String getValueVal(BigDecimal masterId){
		return masterList.stream().filter(master -> master.getMaster_id().equals(masterId)).map(master -> master.getValue_val())
				.findFirst().get();
	}

	/*
	 * Gets master's values
	 *
	 * @param
	 *
	 * @return
	 */
	public static List<Master> getMasterList()
	{
		return masterList;
	}

	/*
	 * Sets master's values
	 *
	 * @param masterList
	 *
	 * @return
	 */
	public static void setMasterList(List<Master> masterList)
	{
		MasterProcessor.masterList = masterList;
	}
}

package ca.homedepot.preference.processor;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;

@Component
public class MasterProcessor
{

	@Autowired
	private PreferenceService preferenceService;

	private static List<Master> masterList;

	public void getMasterInfo()
	{
		masterList = preferenceService.getMasterInfo();
	}

	public static Master getSourceId(String key_val, String value_val)
	{
		return masterList.stream().filter(master -> master.getKey_value().equals(key_val) && master.getValue_val().equals(value_val))
				.findFirst().get();
	}
	public static List<Master> getMasterList(){
		return masterList;
	}

	public static void setMasterList(List<Master> masterList) {
		MasterProcessor.masterList = masterList;
	}
}

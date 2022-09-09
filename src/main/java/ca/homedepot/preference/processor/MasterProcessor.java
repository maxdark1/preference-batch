package ca.homedepot.preference.processor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;

@Component
public class MasterProcessor
{

	@Autowired
	private PreferenceService preferenceService;

	private List<Master> masterList;

	public void getMasterInfo()
	{
		masterList = preferenceService.getMasterInfo();
	}

	public Master getSourceId(String key_val, String value_val)
	{
		return masterList.stream().filter(master -> master.getKey_val().equals(key_val) && master.getValue_val().equals(value_val))
				.findFirst().get();
	}
}

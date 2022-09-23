package ca.homedepot.preference.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PreferenceItemList
{
	@JsonProperty("items")
	List<PreferenceItem> items;

	public PreferenceItemList()
	{
		items = new ArrayList<>();
	}
}

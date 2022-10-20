package ca.homedepot.preference.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PreferenceItemList
{
	/**
	 * The preference Item List
	 */
	@JsonProperty("items")
	List<PreferenceItem> items;

	/**
	 * To initialize object
	 */
	public PreferenceItemList()
	{
		items = new ArrayList<>();
	}
}

package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PreferenceItemList {
    @JsonProperty("items")
    List<PreferenceItem> items;
    public PreferenceItemList(){
        items = new ArrayList<>();
    }
}

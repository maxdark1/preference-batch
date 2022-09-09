package ca.homedepot.preference.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    @JsonProperty("src_address1")
    private String srcAddress1;

    @JsonProperty("src_address2")
    private String srcAddress2;

    @JsonProperty("src_city")
    private String srcCity;

    @JsonProperty("src_state")
    private String state;

    @JsonProperty("src_zipcode")
    private String srcZipcode;

    @JsonProperty("src_postal_code")
    private String srcPostalCode;
}

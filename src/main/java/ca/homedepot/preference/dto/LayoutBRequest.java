package ca.homedepot.preference.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayoutBRequest {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("sequence_nbr")
    private String sequenceNbr;

    @JsonProperty("source_id")
    private Long sourceId;

    @JsonProperty("src_email_address")
    private String srcEmailAddress;

    @JsonProperty("email_address_1_pref")
    private Integer emailAddress1Pref;

    @JsonProperty("email_status")
    private Integer email_status;

    @JsonProperty("email_pref_hd_ca")
    private Integer emailPrefHDCa;

    @JsonProperty("email_pref_garden_club")
    private Integer emailPrefGardenClub;

    @JsonProperty("email_pref_pro")
    private Integer emailPrefPro;

    @JsonProperty("email_pref_new_mover")
    private Integer emailPrefNewMover;

    @JsonProperty("inserted_by")
    private String insertedBy;

    @JsonProperty("inserted_date")
    private String insertedDate;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_date")
    private String updatedDate;

}

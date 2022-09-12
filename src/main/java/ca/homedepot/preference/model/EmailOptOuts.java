package ca.homedepot.preference.model;

import lombok.Data;

@Data
public class EmailOptOuts {
    private String emailAddress;
    private String status;
    private String reason;
    private String dateUnsubscribed;

}

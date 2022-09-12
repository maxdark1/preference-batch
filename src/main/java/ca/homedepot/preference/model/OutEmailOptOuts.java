package ca.homedepot.preference.model;

import lombok.Data;

import java.util.Date;

@Data
public class OutEmailOptOuts {
    private String emailAddress;
    private String status;
    private String reason;
    private Date dateUnsubscribed;

}

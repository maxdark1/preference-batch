package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Master {

    private BigDecimal master_id;
    
    private String key_val;

    private String value_val;

    private String active;

    private String inserted_by;

    private Date inserted_date;

    private String updated_by;

    private Date updated_date;

    
}

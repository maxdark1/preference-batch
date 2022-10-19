package ca.homedepot.preference.dto;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferenceOutboundDto {
    private String email;
    private Date effective_date;
    private BigDecimal source_id;
    private BigDecimal email_status;
    private Character email_permission;
    private String language_pref;
    private Date early_opt_in_date;
    private Character cnd_compliant_flag;
    private Character email_pref_hd_ca;
    private Character email_pref_garden_club;
    private Character email_pref_pro;
    private String postal_code;
    private String customer_nbr;
    private Character phone_ptc_flag;
    private Character dncl_suppresion;
    private String phone_number;
    private String first_name;
    private String last_name;
    private String business_name;
    private String industry_code;
    private String city;
    private String province;
    private BigDecimal hd_ca_pro_src_id;
}

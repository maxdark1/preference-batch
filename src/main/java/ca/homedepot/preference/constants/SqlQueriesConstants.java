package ca.homedepot.preference.constants;

public interface SqlQueriesConstants {

    String SQL_INSERT_FILE_INBOUND_STG_REGISTRATION= "INSERT INTO pcam.hdpc_file_inbound_stg " +
            "(source_id, src_phone_number, src_first_name,  src_last_name," +
            "src_address1, src_address2, src_city, src_state, src_postal_code, credit_language_cd," +
            "src_email_address, src_title_name, phone_1_pref, email_address_1_pref, mail_address_1_pref, src_phone_extension," +
            "email_pref_hd_ca, email_pref_garden_club, email_pref_pro, email_pref_new_mover, content1, value1, content2, value2, " +
            "content3, value3, content4, value4, content5, value5, content6, value6, content7, value7, content8, value8, content9, value9," +
            "content10, value10, content11, value11, content12, value12, content13, value13, content14, value14, content15, value15," +
            "content16, value16, content17, value17, content18, value18, content19, value19, content20, value20, updated_date, cell_sms_flag, " +
            "customer_nbr, org_name, store_nbr, cust_type_cd, inserted_by, inserted_date) " +
            "VALUES" +
            "(:source_id, :src_phone_number, :src_first_name, :src_last_name," +
            ":src_address1, :src_address2, :src_city, :src_state, :src_postal_code, :credit_language_cd," +
            ":src_email_address, :src_title_name, :phone_1_pref, :email_address_1_pref, :mail_address_1_pref, :src_phone_extension," +
            ":email_pref_hd_ca, :email_pref_garden_club, :email_pref_pro, :email_pref_new_mover, :content1, :value1, :content2, :value2, " +
            ":content3, :value3, :content4, :value4, :content5, :value5, :content6, :value6, :content7, :value7, :content8, :value8, :content9, :value9," +
            ":content10, :value10, :content11, :value11, :content12, :value12, :content13, :value13, :content14, :value14, :content15, :value15," +
            ":content16, :value16, :content17, :value17, :content18, :value18, :content19, :value19, :content20, :value20, :updated_date, :cell_sms_flag, " +
            ":customer_nbr, :org_name, :store_nbr, :cust_type_cd, :inserted_by, :inserted_date)";
}

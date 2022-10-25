package ca.homedepot.preference.service.impl;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import ca.homedepot.preference.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
@Service
public class OutboundServiceImpl implements OutboundService {
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    /**
     * This methos is used to make a connection with DB and execute a query to get necessary data
     * @param item
     */
    @Override
    public void preferenceOutbound(PreferenceOutboundDto item) {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.update(OutboundSqlQueriesConstants.SQL_INSERT_STG_PREFERENCE_OUTBOUND,
                item.getEmail(),
                item.getEffective_date(),
                item.getSource_id(),
                item.getEmail_status(),
                item.getEmail_permission(),
                item.getLanguage_pref(),
                item.getEarly_opt_in_date(),
                item.getCnd_compliant_flag(),
                item.getEmail_pref_hd_ca(),
                item.getEmail_pref_garden_club(),
                item.getEmail_pref_pro(),
                item.getPostal_code(),
                item.getCustomer_nbr(),
                item.getPhone_ptc_flag(),
                item.getDncl_suppresion(),
                item.getPhone_number(),
                item.getFirst_name(),
                item.getLast_name(),
                item.getBusiness_name(),
                item.getIndustry_code(),
                item.getCity(),
                item.getProvince(),
                item.getHd_ca_pro_src_id());
    }

    /**
     * This method is used to connect with the database and truncate a passtrougths table
     */
    @Override
    public void truncateCompliantTable() {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.execute(OutboundSqlQueriesConstants.SQL_TRUNCATE_COMPLIANT_TABLE);
    }
}

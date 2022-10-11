package ca.homedepot.preference.config;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.RegistrationRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static ca.homedepot.preference.config.RegistrationRowMapper.getIntegerValue;

public class SFMCRowMapper implements RowMapper<RegistrationRequest> {
    @Override
    public RegistrationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegistrationRequest registrationRequest = new RegistrationRequest();


        registrationRequest.setFileId(rs.getBigDecimal(PreferenceBatchConstants.FILE_ID));
        registrationRequest.setSequenceNbr(rs.getString(PreferenceBatchConstants.SEQUENCE_NBR));
        registrationRequest.setSourceId(rs.getLong(PreferenceBatchConstants.SOURCE_ID));
        registrationRequest.setSrcEmailAddress(rs.getString(PreferenceBatchConstants.SRC_EMAIL_ADDRESS));

        Integer emailStatus = getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_STATUS));
        registrationRequest.setEmailStatus(emailStatus == null ? 0 : emailStatus);
        registrationRequest.setEmailAddressPref(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_ADDRESS_PREF)));
        registrationRequest.setSrcDate(rs.getDate(PreferenceBatchConstants.SRC_DATE).toString());


        registrationRequest.setEmailPrefHDCa(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_HD_CA)));
        registrationRequest.setEmailPrefGardenClub(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_GARDEN_CLUB)));
        registrationRequest.setEmailPrefPro(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_PRO)));
        registrationRequest.setEmailPrefNewMover(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_NEW_MOVER)));

        return registrationRequest;



    }

    public static String getDate(Date date){
        if(date != null)
            return date.toString();

        return null;
    }
}

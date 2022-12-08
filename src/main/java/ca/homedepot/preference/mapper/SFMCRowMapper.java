package ca.homedepot.preference.mapper;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.RegistrationRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ca.homedepot.preference.util.validation.FormatUtil.getIntegerValue;

public class SFMCRowMapper implements RowMapper<RegistrationRequest>
{
	/**
	 *
	 * @param rs
	 *           the ResultSet to map (pre-initialized for the current row)
	 * @param rowNum
	 *           the number of the current row
	 * @return
	 * @throws SQLException
	 */
	@Override
	public RegistrationRequest mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		RegistrationRequest registrationRequest = new RegistrationRequest();


		registrationRequest.setFileId(rs.getBigDecimal(PreferenceBatchConstants.FILE_ID));
		registrationRequest.setSequenceNbr(rs.getString(PreferenceBatchConstants.SEQUENCE_NBR));
		registrationRequest.setSourceId(rs.getLong(PreferenceBatchConstants.SOURCE_ID));
		registrationRequest.setSrcEmailAddress(rs.getString(PreferenceBatchConstants.SRC_EMAIL_ADDRESS));

		Integer emailStatus = getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_STATUS));
		registrationRequest.setEmailStatus(emailStatus);
		registrationRequest.setEmailAddressPref(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_ADDRESS_PREF)));
		registrationRequest.setSrcDate(rs.getDate(PreferenceBatchConstants.SRC_DATE).toString());


		registrationRequest.setEmailPrefHDCa(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_HD_CA)));
		registrationRequest.setEmailPrefGardenClub(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_GARDEN_CLUB)));
		registrationRequest.setEmailPrefPro(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_PRO)));
		registrationRequest.setEmailPrefNewMover(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_NEW_MOVER)));

		return registrationRequest;



	}

}

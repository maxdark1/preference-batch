package ca.homedepot.preference.mapper;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.RegistrationRequest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static ca.homedepot.preference.util.validation.FormatUtil.getIntegerValue;

public class SFMCRowMapper implements RowMapper<RegistrationRequest>
{

	String timezone;

	/**
	 *
	 * @param rs
	 *           the ResultSet to map (pre-initialized for the current row)
	 * @param rowNum
	 *           the number of the current row
	 * @return
	 * @throws SQLException
	 */

	public SFMCRowMapper(String timezone)
	{
		this.timezone = timezone;
	}

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

		OffsetDateTime srcDate = rs.getTimestamp(PreferenceBatchConstants.SRC_DATE).toLocalDateTime().atZone(ZoneId.of(timezone))
				.toOffsetDateTime();
		registrationRequest.setSrcDate(srcDate.toString());

		registrationRequest.setEmailPrefHDCa(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_HD_CA)));
		registrationRequest.setEmailPrefGardenClub(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_GARDEN_CLUB)));
		registrationRequest.setEmailPrefPro(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_PRO)));
		registrationRequest.setEmailPrefNewMover(getIntegerValue(rs.getString(PreferenceBatchConstants.EMAIL_PREF_NEW_MOVER)));

		return registrationRequest;



	}

}

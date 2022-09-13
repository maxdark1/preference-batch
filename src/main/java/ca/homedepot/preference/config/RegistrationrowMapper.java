package ca.homedepot.preference.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import ca.homedepot.preference.constants.PreferenceBatchConstants;
import ca.homedepot.preference.dto.Address;
import ca.homedepot.preference.dto.RegistrationRequest;

public class RegistrationrowMapper implements RowMapper<RegistrationRequest>
{

	@Override
	public RegistrationRequest mapRow(ResultSet rs, int rowNum) throws SQLException
	{

		RegistrationRequest registrationRequest = new RegistrationRequest();


		registrationRequest.setFileId("12345");
		registrationRequest.setStatus(rs.getString(PreferenceBatchConstants.STATUS).equals("T"));
		registrationRequest.setSequenceNbr("12345");
		registrationRequest.setSourceId(rs.getLong(PreferenceBatchConstants.SOURCE_ID));
		registrationRequest.setLanguagePreference(rs.getString(PreferenceBatchConstants.CREDIT_LANGUAGE_CD));
		registrationRequest.setSrcTitleName(rs.getString(PreferenceBatchConstants.SRC_TITLE_NAME));
		registrationRequest.setSrcFirstName(rs.getString(PreferenceBatchConstants.SRC_FIRST_NAME));
		registrationRequest.setSrcLastName(rs.getString(PreferenceBatchConstants.SRC_LAST_NAME));
		registrationRequest.setSrcEmailAddress(rs.getString(PreferenceBatchConstants.SRC_EMAIL_ADDRESS));

		String emailStatus = rs.getString(PreferenceBatchConstants.EMAIL_STATUS);
		registrationRequest.setEmailStatus(emailStatus == null ? 0 : Integer.parseInt(emailStatus));
		registrationRequest.setEmailAddressPref(Integer.parseInt(rs.getString(PreferenceBatchConstants.EMAIL_ADDRESS_1_PREF)));
		registrationRequest.setSrcDate(rs.getDate(PreferenceBatchConstants.SRC_DATE).toString());
		registrationRequest.setCellSmsFlag(Integer.parseInt(rs.getString(PreferenceBatchConstants.CELL_SMS_FLAG)));
		registrationRequest.setSrcPhoneNumber(rs.getString(PreferenceBatchConstants.SRC_PHONE_NUMBER));
		registrationRequest.setSrcPhoneExtension(rs.getString(PreferenceBatchConstants.SRC_PHONE_EXTENSION));
		registrationRequest.setPhonePref(Integer.parseInt(rs.getString(PreferenceBatchConstants.PHONE_1_PREF)));

		Address address = new Address();
		address.setSrcAddress1(rs.getString(PreferenceBatchConstants.SRC_ADDRESS1));
		address.setSrcAddress2(rs.getString(PreferenceBatchConstants.SRC_ADDRESS2));
		address.setSrcCity(rs.getString(PreferenceBatchConstants.SRC_CITY));
		address.setSrcState(rs.getString(PreferenceBatchConstants.SRC_STATE));
		address.setSrcPostalCode(rs.getString(PreferenceBatchConstants.SRC_POSTAL_CODE));

		registrationRequest.setSrcAddress(address);
		registrationRequest.setMailAddresspref(Integer.parseInt(rs.getString(PreferenceBatchConstants.MAIL_ADDRESS_1_PREF)));
		registrationRequest.setEmailPrefHDCa(Integer.parseInt(rs.getString(PreferenceBatchConstants.EMAIL_PREF_HD_CA)));
		registrationRequest.setEmailPrefGardenClub(Integer.parseInt(rs.getString(PreferenceBatchConstants.EMAIL_PREF_GARDEN_CLUB)));
		registrationRequest.setEmailPrefPro(Integer.parseInt(rs.getString(PreferenceBatchConstants.EMAIL_PREF_PRO)));
		registrationRequest.setEmailPrefNewMover(Integer.parseInt(rs.getString(PreferenceBatchConstants.EMAIL_PREF_NEW_MOVER)));

		Map<String, String> contentValue = new HashMap<>();

		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT1), rs.getString(PreferenceBatchConstants.VALUE1));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT2), rs.getString(PreferenceBatchConstants.VALUE2));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT3), rs.getString(PreferenceBatchConstants.VALUE3));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT4), rs.getString(PreferenceBatchConstants.VALUE4));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT5), rs.getString(PreferenceBatchConstants.VALUE5));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT6), rs.getString(PreferenceBatchConstants.VALUE6));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT7), rs.getString(PreferenceBatchConstants.VALUE7));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT8), rs.getString(PreferenceBatchConstants.VALUE8));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT9), rs.getString(PreferenceBatchConstants.VALUE9));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT10), rs.getString(PreferenceBatchConstants.VALUE10));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT11), rs.getString(PreferenceBatchConstants.VALUE11));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT12), rs.getString(PreferenceBatchConstants.VALUE12));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT13), rs.getString(PreferenceBatchConstants.VALUE13));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT14), rs.getString(PreferenceBatchConstants.VALUE14));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT15), rs.getString(PreferenceBatchConstants.VALUE15));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT16), rs.getString(PreferenceBatchConstants.VALUE16));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT17), rs.getString(PreferenceBatchConstants.VALUE17));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT18), rs.getString(PreferenceBatchConstants.VALUE18));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT19), rs.getString(PreferenceBatchConstants.VALUE19));
		contentValue.put(rs.getString(PreferenceBatchConstants.CONTENT20), rs.getString(PreferenceBatchConstants.VALUE20));

		/*
		 * Deletes any null value before send it to the service
		 */
		contentValue.forEach((key, value) -> {
			if (key == null || value == null)
				contentValue.remove(key, value);
		});


		registrationRequest.setContentValue(contentValue);



		return registrationRequest;



	}


}

package ca.homedepot.preference.mapper;


import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PreferenceOutboundMapperStep2Test
{

	@Mock
	ResultSet resultSet;

	@InjectMocks
	PreferenceOutboundMapperStep2 PreferenceOutboundMapperStep2;

	@Test
	void mapRow() throws SQLException
	{
		resultSet = Mockito.mock(ResultSet.class);
		final int row = 1;
		PreferenceOutboundMapperStep2 = new PreferenceOutboundMapperStep2();

		Mockito.when(resultSet.getString("email_addr")).thenReturn("juan@lara.com");
		Mockito.when(resultSet.getBigDecimal("can_ptc_source_id")).thenReturn(BigDecimal.ONE);
		Mockito.when(resultSet.getBigDecimal("email_status")).thenReturn(BigDecimal.ONE);
		Mockito.when(resultSet.getString("can_ptc_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("language_preference")).thenReturn("A");
		Mockito.when(resultSet.getString("cnd_compliant_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("hd_ca_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("hd_ca_garden_club_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("hd_ca_pro_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("phone_ptc_flag")).thenReturn("A");
		Mockito.when(resultSet.getString("dncl_suppression_flag")).thenReturn("A");

		PreferenceOutboundDto preferenceOutboundDto = PreferenceOutboundMapperStep2.mapRow(resultSet, row);
		assertNotNull(preferenceOutboundDto);
		assertEquals(preferenceOutboundDto.getSourceId(), BigDecimal.ONE);
		assertEquals(preferenceOutboundDto.getEmail(), "juan@lara.com");

	}
}
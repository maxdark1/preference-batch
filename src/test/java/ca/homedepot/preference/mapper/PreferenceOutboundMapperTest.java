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

class PreferenceOutboundMapperTest {

    @Mock
    ResultSet resultSet;

    @InjectMocks
    PreferenceOutboundMapper preferenceOutboundMapper;

    @Test
    void mapRow() throws SQLException {
        resultSet = Mockito.mock(ResultSet.class);
        final int row = 1;
        preferenceOutboundMapper = new PreferenceOutboundMapper();

        Mockito.when(resultSet.getString("email")).thenReturn("juan@lara.com");
        Mockito.when(resultSet.getBigDecimal("source_id")).thenReturn(BigDecimal.ONE);
        Mockito.when(resultSet.getBigDecimal("email_status")).thenReturn(BigDecimal.ONE);
        Mockito.when(resultSet.getString("email_permission")).thenReturn("A");
        Mockito.when(resultSet.getString("language_preference")).thenReturn("A");
        Mockito.when(resultSet.getString("cnd_compliant_flag")).thenReturn("A");
        Mockito.when(resultSet.getString("email_pref_hd_ca")).thenReturn("A");
        Mockito.when(resultSet.getString("email_pref_garden_club")).thenReturn("A");
        Mockito.when(resultSet.getString("email_pref_pro")).thenReturn("A");
        Mockito.when(resultSet.getString("phone_ptc_flag")).thenReturn("A");
        Mockito.when(resultSet.getString("dncl_suppresion")).thenReturn("A");

        PreferenceOutboundDto preferenceOutboundDto = preferenceOutboundMapper.mapRow(resultSet, row);
        assertNotNull(preferenceOutboundDto);
        assertEquals(preferenceOutboundDto.getEmail(), "juan@lara.com");

    }
}
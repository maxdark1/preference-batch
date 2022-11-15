package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalOutboundDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;

class InternalOutboundStep1MapperTest
{

	@Mock
	ResultSet resultSet;

	@InjectMocks
	InternalOutboundStep1Mapper internalOutboundStep1Mapper;

	@Test
	void mapRow() throws SQLException
	{
		resultSet = Mockito.mock(ResultSet.class);
		final int row = 1;

		internalOutboundStep1Mapper = new InternalOutboundStep1Mapper();
		Mockito.when(resultSet.getString("email")).thenReturn("test@test.com");
		Mockito.when(resultSet.getBigDecimal("source_id")).thenReturn(BigDecimal.TEN);
		Mockito.when(resultSet.getString("cnd_compliant_flag")).thenReturn("Y");
		Mockito.when(resultSet.getString("email_pref_hd_ca")).thenReturn("Y");
		Mockito.when(resultSet.getDate("move_date")).thenReturn(new java.sql.Date((Long.valueOf("1667487046196"))));

		InternalOutboundDto internalOutboundDto = internalOutboundStep1Mapper.mapRow(resultSet, row);
		assertEquals("test@test.com", internalOutboundDto.getEmailAddr());
		assertEquals("Y", internalOutboundDto.getCndCompliantFlag());
		assertEquals(BigDecimal.TEN, internalOutboundDto.getCanPtcSourceId());


	}
}
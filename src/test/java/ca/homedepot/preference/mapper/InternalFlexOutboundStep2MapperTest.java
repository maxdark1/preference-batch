package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InternalFlexOutboundStep2MapperTest
{
	InternalFlexOutboundStep2Mapper internalFlexOutboundStep2Mapper;
	Faker faker;

	@BeforeEach
	void setUp()
	{
		internalFlexOutboundStep2Mapper = new InternalFlexOutboundStep2Mapper();
		faker = new Faker();
	}

	@Test
	void testMapRow() throws SQLException
	{
		ResultSet rs = mock(ResultSet.class);
		when(rs.getBigDecimal(anyString())).thenReturn(BigDecimal.valueOf(faker.number().randomNumber()));
		when(rs.getString(anyString())).thenReturn(faker.number().digits(10));
		when(rs.getString(anyString())).thenReturn(faker.internet().emailAddress());
		when(rs.getBigDecimal(anyString())).thenReturn(BigDecimal.valueOf(faker.number().randomNumber()));
		when(rs.getBigDecimal(anyString())).thenReturn(BigDecimal.valueOf(faker.number().randomNumber()));
		when(rs.getString(anyString())).thenReturn(faker.number().digits(5));
		when(rs.getString(anyString())).thenReturn(faker.number().digits(4));
		when(rs.getString(anyString())).thenReturn(faker.company().name());
		when(rs.getString(anyString())).thenReturn(faker.number().digits(3));
		when(rs.getString(anyString())).thenReturn(faker.number().digits(3));
		when(rs.getLong(anyString())).thenReturn(abs(faker.number().randomNumber()));
		when(rs.getDate(anyString())).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
		when(rs.getDate(anyString())).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
		when(rs.getString(anyString())).thenReturn(faker.number().digits(3));
		when(rs.getString(anyString())).thenReturn(faker.company().name());
		when(rs.getString(anyString())).thenReturn(faker.name().firstName());
		when(rs.getString(anyString())).thenReturn(faker.name().lastName());
		when(rs.getString(anyString())).thenReturn(faker.company().profession());

		InternalFlexOutboundDTO result = internalFlexOutboundStep2Mapper.mapRow(rs, 0);
		assertThat(result).isNotNull();
	}
}

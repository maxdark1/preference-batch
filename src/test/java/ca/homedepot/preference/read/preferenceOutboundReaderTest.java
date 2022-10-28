package ca.homedepot.preference.read;

import ca.homedepot.preference.constants.OutboundSqlQueriesConstants;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import ca.homedepot.preference.mapper.PreferenceOutboundMapper;
import org.mockito.Mockito;
import org.springframework.batch.item.database.JdbcCursorItemReader;

class preferenceOutboundReaderTest
{

	@Mock
	DataSource dataSource = null;

	@Mock
	PreferenceOutboundMapper mapper;
	@InjectMocks
	private preferenceOutboundReader reader = new preferenceOutboundReader();

	@Test
	void getDataSource()
	{
		dataSource = reader.getDataSource();
		assertNull(dataSource);
	}

	@Test
	void setDataSource()
	{
		reader.setDataSource(dataSource);
		assertNull(reader.getDataSource());
	}


	@Test
	void canEqual()
	{
		assertTrue(reader.canEqual(reader));
	}


	@Test
	void testToString()
	{
		assertNotNull(reader.toString());
	}
}
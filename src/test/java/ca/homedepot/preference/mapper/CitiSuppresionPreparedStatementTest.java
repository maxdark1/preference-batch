package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class CitiSuppresionPreparedStatementTest
{


	@Mock
	CitiSuppresionOutboundDTO item;

	@Mock
	PreparedStatement ps;

	@InjectMocks
	@Spy
	CitiSuppresionPreparedStatement citiSuppresionPreparedStatement;


	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void setValues() throws SQLException
	{


		citiSuppresionPreparedStatement.setValues(item, ps);

		Mockito.verify(citiSuppresionPreparedStatement).setValues(item, ps);
	}
}
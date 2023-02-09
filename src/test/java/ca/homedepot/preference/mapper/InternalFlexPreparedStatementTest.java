package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class InternalFlexPreparedStatementTest
{

	InternalFlexPreparedStatement internalPreparedStatement;

	@Mock
	PreparedStatement ps;
	@Mock
	InternalFlexOutboundDTO internalFlexOutboundDTO;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		internalPreparedStatement = new InternalFlexPreparedStatement();
	}

	@Test
	void setValues() throws SQLException
	{
		internalPreparedStatement.setValues(internalFlexOutboundDTO, ps);
		Assertions.assertNotNull(internalFlexOutboundDTO);
	}
}
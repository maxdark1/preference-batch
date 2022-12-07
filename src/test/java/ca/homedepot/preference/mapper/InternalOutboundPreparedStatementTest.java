package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.InternalOutboundDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

class InternalOutboundPreparedStatementTest
{

	@Mock
	PreparedStatement ps;


	InternalOutboundDto item;

	@InjectMocks
	@Spy
	InternalOutboundPreparedStatement internalOutboundPreparedStatement;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		item = new InternalOutboundDto();
		item.setEarlyOptInIDate(new Date());
		item.setCanPtcEffectiveDate(new Date());
		item.setCanPtcSourceId(BigDecimal.ONE);
		item.setEmailStatus(BigDecimal.ZERO);
	}

	@Test
	void setValues() throws SQLException
	{
		internalOutboundPreparedStatement.setValues(item, ps);
		Mockito.verify(internalOutboundPreparedStatement).setValues(item, ps);
	}
}
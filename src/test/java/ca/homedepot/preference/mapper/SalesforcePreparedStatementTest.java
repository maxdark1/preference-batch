package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.SalesforceExtractOutboundDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SalesforcePreparedStatementTest
{
	@Mock
	SalesforceExtractOutboundDTO item;

	@Mock
	PreparedStatement ps;

	@InjectMocks
	@Spy
	SalesforcePreparedStatement salesforcePreparedStatement;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);
		item = new SalesforceExtractOutboundDTO();
		item.setAsOfDate(LocalDateTime.now());
		item.setEarliestOptInDate(LocalDateTime.now());
		item.setMoveDate(LocalDate.now());
	}

	@Test
	void setValues() throws SQLException
	{
		salesforcePreparedStatement.setValues(item, ps);
		Mockito.verify(salesforcePreparedStatement).setValues(item, ps);
	}
}
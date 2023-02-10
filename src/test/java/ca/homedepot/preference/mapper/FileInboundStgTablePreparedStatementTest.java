package ca.homedepot.preference.mapper;

import ca.homedepot.preference.model.FileInboundStgTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


class FileInboundStgTablePreparedStatementTest
{

	FileInboundStgTable item;

	@Mock
	PreparedStatement ps;


	@InjectMocks
	@Spy
	FileInboundStgTablePreparedStatement fileInboundStgTablePreparedStatement;

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.openMocks(this);
		item = FileInboundStgTable.builder().build();
		item.setInsertedDate(new Date());
		item.setSrcDate(new Date().toString());
	}

	@Test
	void setValues() throws SQLException
	{
		fileInboundStgTablePreparedStatement.setValues(item, ps);
		Mockito.verify(fileInboundStgTablePreparedStatement).setValues(item, ps);
	}

	@Test
	void stringConverter() throws SQLException
	{
		fileInboundStgTablePreparedStatement.setValues(item, ps);
		fileInboundStgTablePreparedStatement.toString();
		Mockito.verify(fileInboundStgTablePreparedStatement).setValues(item, ps);
	}
}
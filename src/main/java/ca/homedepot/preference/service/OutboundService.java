package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public interface OutboundService
{
	void preferenceOutbound(PreferenceOutboundDto item);

	int programCompliant(InternalOutboundDto item);

	void truncateCompliantTable();

	int purgeCitiSuppresionTable();

	int purgeProgramCompliant();

	void createFile(String repository, String folder, String fileNameFormat, String file) throws IOException;
}

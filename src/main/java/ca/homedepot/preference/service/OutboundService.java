package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;

@Service
public interface OutboundService
{
	void preferenceOutbound(PreferenceOutboundDto item);

	void truncateCompliantTable();

	int purgeCitiSuppresionTable();

	void createFile(String repository, String folder, String fileNameFormat) throws IOException;
}

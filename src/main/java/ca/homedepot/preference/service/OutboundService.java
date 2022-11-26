package ca.homedepot.preference.service;

import ca.homedepot.preference.dto.InternalFlexOutboundDTO;
import ca.homedepot.preference.dto.InternalOutboundDto;
import ca.homedepot.preference.dto.PreferenceOutboundDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface OutboundService
{
	void preferenceOutbound(PreferenceOutboundDto item);

	int programCompliant(InternalOutboundDto item);

	void truncateCompliantTable();

	int purgeCitiSuppresionTable();

	void purgeSalesforceExtractTable();

	int purgeProgramCompliant();

	int purgeLoyaltyComplaintTable();

	void createFile(String repository, String folder, String fileNameFormat, String file) throws IOException;

	void createFlexAttributesFile(String repository, String folder, String fileNameFormat, String headers) throws IOException;

	int purgeFlexAttributesTable();

	void internalFlexAttributes(InternalFlexOutboundDTO internalFlexOutboundDTO);
}

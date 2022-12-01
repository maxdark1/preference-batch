package ca.homedepot.preference.writer;

import java.math.BigDecimal;
import java.util.List;

import ca.homedepot.preference.service.FileService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INPROGRESS;

@Slf4j
@Component
@Setter
public class RegistrationAPIWriter implements ItemWriter<RegistrationRequest>
{
	/**
	 * The preference service
	 */
	private PreferenceService preferenceService;

	/**
	 * The file service
	 */
	private FileService fileService;

	/**
	 * The Job Name that is currently being executed
	 */
	private String jobName;

	/**
	 * Sends the item to the API via LayoutC endpoint
	 * 
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends RegistrationRequest> items) throws Exception
	{
		RegistrationResponse response;
		try
		{
			response = preferenceService.preferencesRegistration(items);
			/**
			 * Updates status for each record
			 */
			response.getRegistration().forEach(resp -> fileService.updateInboundStgTableStatus(new BigDecimal(resp.getId()),
					resp.getStatus().substring(0, 1), INPROGRESS));
			log.info("Service Response {} :", response);
		}
		catch (Exception e)
		{
			log.error(" PREFERENCE BATCH ERROR - Service not available, ERROR occurs trying to send items throw end point on job {} \n: {}", jobName,
					e.getMessage());
			throw e;
		}



	}
}

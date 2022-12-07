package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.service.PreferenceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static ca.homedepot.preference.constants.SourceDelimitersConstants.INPROGRESS;

@Slf4j
@Component
@Setter
public class RegistrationLayoutBWriter implements ItemWriter<RegistrationRequest>
{
	/**
	 * The prefernce service
	 */
	private PreferenceService preferenceService;
	/**
	 * The File Service
	 */
	private FileService fileService;

	/**
	 * The Job's name that is currently being executed
	 */
	private String jobName;

	/**
	 * Sends the items processed to the API LayoutB end point
	 * 
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends RegistrationRequest> items) throws Exception
	{

		try
		{
			RegistrationResponse response = preferenceService.preferencesSFMCEmailOptOutsLayoutB(items);

			/**
			 * Updates the status of each record
			 */
			response.getRegistration().forEach(resp -> fileService.updateInboundStgTableStatus(new BigDecimal(resp.getId()),
					resp.getStatus().substring(0, 1), INPROGRESS));
			log.info("Service Response: {} ", response);
		}
		catch (Exception e)
		{
			log.error(
					" PREFERENCE BATCH ERROR - Service not available, ERROR occurs trying to send items throw end point on job {} \n: {}",
					jobName, e.getMessage());
			throw e;
		}


	}
}

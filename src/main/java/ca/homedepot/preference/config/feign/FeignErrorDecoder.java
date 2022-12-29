package ca.homedepot.preference.config.feign;


import com.google.api.client.util.Charsets;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import java.io.IOException;


/**
 * The type Feign error decoder.
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder
{

	/***
	 * Customer decoder for all the feign clients
	 *
	 * @param key
	 * @param response
	 * @return
	 */
	@Override
	public Exception decode(final String key, final Response response)
	{
		if (response.status() == HttpStatus.SC_NOT_FOUND)
		{
			String conflict = "";
			try
			{
				conflict = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8.toString());
				log.error(" PREFERENCE BATCH SERVICE ERROR - Service not available got status {}, response from service {}",
						response.status(), conflict);
			}
			catch (IOException e)
			{
				conflict = "{}";
				log.error(" PREFERENCE BATCH ERROR - An error occurs trying to get the body response {}", conflict);
			}

		}
		else
		{
			log.error("PREFERENCE BATCH SERVICE ERROR - Error occurred while calling {} with the status {} and for the cause {}",
					key, response.status(), response.reason());
		}

		return FeignException.errorStatus(key, response);
	}
}

package ca.homedepot.preference.config.feign;


import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;


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
		log.error("Error occurred while calling {} with the status {} and for the cause {}", key, response.status(),
				response.reason());
		return FeignException.errorStatus(key, response);
	}
}

package ca.homedepot.preference.config.feign;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.Jackson2JsonEncoder;


/**
 * The type Product ms feign configuration.
 */
@Configuration
public class RegistrationFeignConfig
{

	/**
	 * The Base server url.
	 */
	@Value("${feign.restapi.baseurl}")
	private String baseServerUrl;

	/**
	 * The Period.
	 */
	@Value("${feign.retry.period}")
	private int period;

	/**
	 * The Max period.
	 */
	@Value("${feign.retry.maxPeriod}")
	private int maxPeriod;

	/**
	 * The Max attempts.
	 */
	@Value("${feign.retry.maxAttempts}")
	private int maxAttempts;

	/**
	 * Product ms client product ms client.
	 *
	 * @return the product ms client
	 */
	@Bean
	public PreferenceRegistrationClient preferenceRegistrationClient()
	{
		return Feign.builder().client(new ApacheHttpClient())
				.encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.contract(new SpringMvcContract()).errorDecoder(new FeignErrorDecoder())
				.logger(new Slf4jLogger(PreferenceRegistrationClient.class))
				.retryer(new Retryer.Default(period, maxPeriod, maxAttempts))
				.target(PreferenceRegistrationClient.class, baseServerUrl);
	}

	@Bean
	public Contract feignContract()
	{
		return new Contract.Default();
	}


	/**
	 * Feign logger level logger . level.
	 *
	 * @return the logger . level
	 */
	@Bean
	Logger.Level feignLoggerLevel()
	{
		return Logger.Level.BASIC;
	}

	/**
	 * Feign error decoder error decoder.
	 *
	 * @return the error decoder
	 */
	@Bean
	@Primary
	public ErrorDecoder feignErrorDecoder()
	{
		return new FeignErrorDecoder();
	}
}



package ca.homedepot.preference.config.feign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationFeignConfigTest
{

	RegistrationFeignConfig registrationFeignConfig;

	@BeforeEach
	void setUp()
	{
		registrationFeignConfig = new RegistrationFeignConfig();
	}

	@Test
	void preferenceRegistrationClient()
	{
		assertAll(() -> {
			assertThrows(NullPointerException.class, () -> {
				registrationFeignConfig.preferenceRegistrationClient();
			});
		});
		assertThat(registrationFeignConfig.feignContract()).isNotNull();
		assertThat(registrationFeignConfig.feignErrorDecoder()).isNotNull();
		assertThat(registrationFeignConfig.feignLoggerLevel()).isNotNull();
	}

}
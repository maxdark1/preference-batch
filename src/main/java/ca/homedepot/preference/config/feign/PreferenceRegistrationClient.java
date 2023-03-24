package ca.homedepot.preference.config.feign;

import ca.homedepot.preference.dto.EmailNotificationRequest;
import ca.homedepot.preference.dto.RegistrationRequest;
import ca.homedepot.preference.dto.RegistrationResponse;
import ca.homedepot.preference.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * The interface defines Preference Registration rest API client.
 */
@FeignClient(name = "preferenceRegistrationClient", configuration = RegistrationFeignConfig.class)
public interface PreferenceRegistrationClient
{

	@RequestMapping(method = RequestMethod.POST, value = "/preferences/registration", consumes = "application/json")
	RegistrationResponse registration(@RequestBody List<? extends RegistrationRequest> registrationRequest);

	@RequestMapping(method = RequestMethod.POST, value = "/preferences/registration/layoutB", consumes = "application/json")
	RegistrationResponse registrationLayoutB(@RequestBody List<? extends RegistrationRequest> registrationRequest);

	@RequestMapping(method = RequestMethod.POST, value = "/preferences/email-notification", consumes = "application/json")
	RegistrationResponse sendEmail(@RequestBody EmailNotificationRequest email);

	@RequestMapping(method = RequestMethod.GET, value = "/preferences/health", consumes = "application/json")
	Response getServiceStatus();
}

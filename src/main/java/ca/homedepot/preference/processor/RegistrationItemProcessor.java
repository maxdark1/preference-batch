package ca.homedepot.preference.processor;

import ca.homedepot.preference.util.DecodeUtil;
import java.security.NoSuchAlgorithmException;
import org.springframework.batch.item.ItemProcessor;

import ca.homedepot.preference.model.Registration;

public class RegistrationItemProcessor implements ItemProcessor<Registration, Registration>
{

	@Override
	public Registration process(Registration registration) throws NoSuchAlgorithmException
	{
		registration.setEmailId(DecodeUtil.convertToHex(registration.getEmailId().toLowerCase()));
		return registration;
	}
}

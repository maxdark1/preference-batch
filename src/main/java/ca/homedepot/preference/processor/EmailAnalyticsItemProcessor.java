package ca.homedepot.preference.processor;

import ca.homedepot.preference.util.DecodeUtil;
import java.security.NoSuchAlgorithmException;
import org.springframework.batch.item.ItemProcessor;

import ca.homedepot.preference.model.EmailAnalytics;

public class EmailAnalyticsItemProcessor implements ItemProcessor<EmailAnalytics, EmailAnalytics>
{

	@Override
	public EmailAnalytics process(EmailAnalytics emailAnalytics) throws NoSuchAlgorithmException
	{
		emailAnalytics.setEmailId(DecodeUtil.convertToHex(emailAnalytics.getEmailId().toLowerCase()));
		return emailAnalytics;
	}
}

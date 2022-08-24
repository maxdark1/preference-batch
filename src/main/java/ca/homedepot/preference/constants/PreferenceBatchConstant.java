package ca.homedepot.preference.constants;

import org.springframework.beans.factory.annotation.Value;

public class PreferenceBatchConstant {

    @Value("${service.preference.baseUrl}")
	public static String baseUrl;
}

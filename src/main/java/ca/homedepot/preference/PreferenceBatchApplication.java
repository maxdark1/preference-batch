package ca.homedepot.preference;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PreferenceBatchApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PreferenceBatchApplication.class, args);
	}

	@Bean
	public Storage createGoogleStorage()
	{
		return StorageOptions.getDefaultInstance().getService();
	}


}

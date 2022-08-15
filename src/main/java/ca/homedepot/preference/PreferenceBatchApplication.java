package ca.homedepot.preference;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(
{ "ca.homedepot.preference.dto", "ca.homedepot.preference.data", "ca.homedepot.preference.listener",
		"ca.homedepot.preference.model", "ca.homedepot.preference.processor", "ca.homedepot.preference.service",
		"ca.homedepot.preference.tasklet", "ca.homedepot.preference.dto", "ca.homedepot.preference.util" })
@EntityScan("ca.homedepot.preference.repositories.entities")
public class PreferenceBatchApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PreferenceBatchApplication.class, args);
	}

	/*
	 * @Bean public Storage createGoogleStorage() { return StorageOptions.getDefaultInstance().getService(); }
	 */


}

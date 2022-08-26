package ca.homedepot.preference;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(value =
{ "ca.homedepot.preference.*", "ca.homedepot.preference.dto", "ca.homedepot.preference.data", "ca.homedepot.preference.listener",
		"ca.homedepot.preference.model", "ca.homedepot.preference.processor", "ca.homedepot.preference.tasklet",
		"ca.homedepot.preference.dto", "ca.homedepot.preference.util", "ca.homedepot.preference.config", })
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

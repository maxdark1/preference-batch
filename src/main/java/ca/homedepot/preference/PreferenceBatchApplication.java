package ca.homedepot.preference;


import ca.homedepot.preference.config.SchedulerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class PreferenceBatchApplication
{
	public static void main(String[] args)
	{
		if(args.length > 0)
		SchedulerConfig.individualJob = args[0];
		SpringApplication.run(PreferenceBatchApplication.class, args);
	}

}

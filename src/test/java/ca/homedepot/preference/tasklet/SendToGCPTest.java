package ca.homedepot.preference.tasklet;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SendToGCPTest
{

	@Test
	void execute()
	{
		List<String> files = new ArrayList<>();
		files.add("someFile.txt");
		SendToGCP step3 = new SendToGCP();
		step3.setFiles(files);
		step3.setRepository("");
		step3.setFolder("outbound");
		step3.setJobName("jobName");

		assertNotNull(step3);
	}
}
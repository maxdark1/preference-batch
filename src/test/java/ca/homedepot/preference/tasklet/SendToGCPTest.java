package ca.homedepot.preference.tasklet;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
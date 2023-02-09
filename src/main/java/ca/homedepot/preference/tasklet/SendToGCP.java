package ca.homedepot.preference.tasklet;


import ca.homedepot.preference.util.CloudStorageUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.homedepot.preference.writer.GSFileWriterOutbound.createFileOnGCS;

@Component
@Setter
@Slf4j
public class SendToGCP implements Tasklet
{
	List<String> files = new ArrayList<>();
	String fileName;
	String Repository;
	String Folder;
	FileInputStream reader;
	String content;
	String JobName;

	private final Format formatter = new SimpleDateFormat("yyyyMMdd");

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception
	{

		for (int i = 0; i < files.size(); i++)
		{
			try
			{
				fileName = files.get(i).replace("YYYYMMDD", formatter.format(new Date()));
				reader = new FileInputStream(fileName);
				content = new String(reader.readAllBytes());
				reader.close();
				createFileOnGCS(CloudStorageUtils.generatePath(Repository + Folder, fileName), JobName, content.getBytes());
			}
			catch (IOException ex)
			{
				log.error(" PREFERENCE BATCH ERROR - Error during the creation of Files in GCP ");
				throw ex;
			}

		}

		files.forEach(file -> {
			try
			{
				String fileTmp = file.replace("YYYYMMDD", formatter.format(new Date()));
				DeleteFile(new File(fileTmp));
			}
			catch (IOException e)
			{
				log.error("PREFERENCE BATCH ERROR - Error during the deletion of Internal Destination Temp Files on Step 3");
			}
		});

		return RepeatStatus.FINISHED;
	}

	protected void DeleteFile(File file) throws IOException
	{
		Files.delete(file.toPath());
	}
}

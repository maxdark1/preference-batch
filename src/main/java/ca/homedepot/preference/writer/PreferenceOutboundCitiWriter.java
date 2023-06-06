package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.CitiSuppresionOutboundDTO;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.listener.JobListener;
import ca.homedepot.preference.model.Counters;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import ca.homedepot.preference.util.CloudStorageUtils;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ca.homedepot.preference.config.SchedulerConfig.JOB_NAME_CITI_SUPPRESION;
import static ca.homedepot.preference.config.StorageApplicationGCS.*;
import static ca.homedepot.preference.constants.SourceDelimitersConstants.*;
import static ca.homedepot.preference.util.validation.FormatUtil.*;

@Slf4j
@Component
public class PreferenceOutboundCitiWriter implements ItemWriter<CitiSuppresionOutboundDTO>, ItemStream
{
	@Value("${folders.citi.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.citi.mastersuppresion}")
	protected String fileNameFormat;

	protected String sourceId;
	@Autowired
	private FileService fileService;
	private final Format formatter = new SimpleDateFormat("yyyyMMdd");

	private int quantityRecords = 0;
	StringBuilder fileBuilder = new StringBuilder();

	private List<Counters> counters;

	private JobListener jobListener;

	public String source;

	private Counters counter = new Counters(0, 0, 0);

	public void setCounters(List<Counters> counters)
	{
		this.counters = counters;
	}

	public Storage storage;
	public BlobId blobId;

	public WritableByteChannel writer;

	public String fileName;

	public void setJobListener(JobListener jobListener)
	{
		this.jobListener = jobListener;
	}

	/**
	 * Method used to generate a plain text file
	 *
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends CitiSuppresionOutboundDTO> items) throws Exception
	{
		if (items.size() > 0)
		{
			for (CitiSuppresionOutboundDTO citi : items)
			{
				fileBuilder.append(String.format("%-30s", citi.getFirstName() != null ? columnTrim(citi.getFirstName()) : ""))
						.append(String.format("%-1s", citi.getMiddleInitial() != null ? columnTrim(citi.getMiddleInitial()) : ""))
						.append(String.format("%-30s", citi.getLastName() != null ? columnTrim(citi.getLastName()) : ""))
						.append(String.format("%-60s", citi.getAddrLine1() != null ? columnTrim(citi.getAddrLine1()) : ""))
						.append(String.format("%-60s", citi.getAddrLine2() != null ? columnTrim(citi.getAddrLine2()) : ""))
						.append(String.format("%-40s", citi.getCity() != null ? columnTrim(citi.getCity()) : ""))
						.append(String.format("%-2s", citi.getStateCd() != null ? columnTrim(citi.getStateCd()) : ""))
						.append(String.format("%-7s", citi.getPostalCd() != null ? columnTrim(citi.getPostalCd()) : ""))
						.append(String.format("%-150s", citi.getEmailAddr() != null ? columnTrim(citi.getEmailAddr()) : ""))
						.append(String.format("%-10s", citi.getPhone() != null ? columnTrim(citi.getPhone()) : ""))
						.append(String.format("%-10s", citi.getSmsMobilePhone() != null ? columnTrim(citi.getSmsMobilePhone()) : ""))
						.append(String.format("%-30s",
								citi.getBusinessName() != null ? businessNameJust30(columnTrim(citi.getBusinessName())) : ""))
						.append(String.format("%-1s", citi.getDmOptOut() != null ? columnTrim(citi.getDmOptOut()) : ""))
						.append(String.format("%-1s", citi.getEmailOptOut() != null ? columnTrim(citi.getEmailOptOut()) : ""))
						.append(String.format("%-1s", citi.getPhoneOptOut() != null ? columnTrim(citi.getPhoneOptOut()) : ""))
						.append(String.format("%-1s", citi.getSmsOptOut() != null ? columnTrim(citi.getSmsOptOut()) : "")).append("\n");
				quantityRecords++;
				writer.write(ByteBuffer.wrap(fileBuilder.toString().getBytes()));
				fileBuilder = new StringBuilder();
				log.info(quantityRecords + citi.toString());
			}
		}
		else
		{
			log.info("Nothing to Write in Citi Supression Outbound File");
		}
	}


	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 *
	 * @param file
	 * @throws IOException
	 */


	/**
	 * This method registry in file table the generated file
	 *
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId(JOB_NAME_CITI_SUPPRESION, JobListener.status(BatchStatus.STARTED).getMasterId());
		Master fileStatus = MasterProcessor.getSourceID(STATUS_STR, VALID);
		String sourceStr = source.equals(CITI_SUP) ? SOURCE_ID_STR : SOURCE_STR;
		BigDecimal sourceId = MasterProcessor.getSourceID(sourceStr, source).getMasterId();
		log.error(" PREFERENCE-BATCH: SOURCE_TYPE master_id: {}", sourceId);
		BigDecimal sourceIdBD = sourceId == null || sourceId.equals(BigDecimal.valueOf(-400L))
				? MasterProcessor.getSourceID("SOURCE_ID", "database").getOldID()
				: sourceId;
		log.error(" PREFERENCE-BATCH: SOURCE_TYPE to insert: {}", sourceIdBD);
		jobListener.setFiles(fileName);
		FileDTO file = new FileDTO(null, fileName, jobId, sourceIdBD, fileStatus.getValueVal(), fileStatus.getMasterId(),
				new Date(), new Date(), INSERTEDBY, new Date(), null, null);

		fileService.insert(file);
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Daily Compliant Writer Start");
		this.storage = StorageOptions.getDefaultInstance().getService();
		fileName = CloudStorageUtils.generatePath(repositorySource, UUID.randomUUID().toString());
		blobId = BlobId.of(getBucketName(), fileName);
		BlobInfo file = BlobInfo.newBuilder(blobId).build();
		writer = storage().create(file).writer();
		log.error("PREFERENCE-BATCH: Temporary File Created: " + fileName);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException
	{
		log.info("Chunck Executed");
	}

	@Override
	public void close() throws ItemStreamException
	{
		try
		{
			writer.close();
			log.info("Writter Closed");
			String fileDestination = repositorySource + fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
			BlobId rename = BlobId.of(getBucketName(), fileDestination);
			storage().copy(Storage.CopyRequest.newBuilder().setSource(blobId).setTarget(rename).build());
			storage().get(blobId).delete();
			log.error("PREFERENCE-BATCH: File Renamed to: " + fileDestination);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);

		}
		log.error(" PREFERENCE-BATCH: file: {} going to be write on DataBase",
				fileNameFormat.replace("YYYYMMDD", formatter.format(new Date())));
		setFileRecord(fileNameFormat.replace("YYYYMMDD", formatter.format(new Date())));
		counter.quantityRecords = quantityRecords;
		counter.fileName = fileNameFormat.replace("YYYYMMDD", formatter.format(new Date()));
		counter.date = new Date().toString();
		counters.add(counter);
		quantityRecords = 0;
	}

}

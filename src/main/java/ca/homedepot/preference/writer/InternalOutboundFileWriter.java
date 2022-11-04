package ca.homedepot.preference.writer;

import ca.homedepot.preference.constants.SourceDelimitersConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.dto.InternalOutboundProcessorDto;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class InternalOutboundFileWriter implements ItemWriter<InternalOutboundProcessorDto>
{
	@Value("${folders.internal.path}")
	protected String repositorySource;
	@Value("${folders.outbound}")
	protected String folderSource;
	@Value("${outbound.files.internalCa}")
	protected String caFileFormat;
	@Value("${outbound.files.internalMover}")
	protected String moverFileFormat;
	@Value("${outbound.files.internalGarden}")
	protected String gardenFileFormat;
	private FileOutputStream writer;


	private String sourceId;
	@Autowired
	private FileService fileService;

	/**
	 * Method used to generate a plain text file
	 * 
	 * @param items
	 *           items to be written
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends InternalOutboundProcessorDto> items) throws Exception
	{
		sourceId = items.get(0).getCanPtcSourceId().replace(",", "");
		String file = "";

		for (InternalOutboundProcessorDto internal : items)
		{
			String line = "";
			line = internal.getEmailAddr();
			line += internal.getCanPtcEffectiveDate();
			line += internal.getCanPtcSourceId();
			line += internal.getEmailStatus();
			line += internal.getCanPtcGlag();
			line += internal.getLanguagePreference();
			line += internal.getEarlyOptInIDate();
			line += internal.getCndCompliantFlag();
			line += internal.getHdCaFlag();
			line += internal.getHdCaGardenClubFlag();
			line += internal.getHdCaNewMoverFlag();
			line += internal.getHdCaNewMoverEffDate();
			line += internal.getHdCaProFlag();
			line += internal.getPhonePtcFlag();
			line += internal.getFirstName();
			line += internal.getLastName();
			line += internal.getPostalCode();
			line += internal.getProvince();
			line += internal.getCity();
			line += internal.getPhoneNumber();
			line += internal.getBussinessName();
			line += internal.getIndustryCode();
			line += internal.getMoveDate();
			line += internal.getDwellingType();
			file += line;

		}

		generateFile(file, caFileFormat);
		generateFile(file, moverFileFormat);
		generateFile(file, gardenFileFormat);

	}

	/**
	 * This Method saves in a plain text file the string that receives as parameter
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void generateFile(String file, String filePath) throws Exception
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String fileName = filePath.replace("YYYYMMDD", formatter.format(new Date()));

		writer = new FileOutputStream(repositorySource + folderSource + fileName, true);
		byte toFile[] = file.getBytes();
		writer.write(toFile);
		writer.flush();
		writer.close();
		setFileRecord(fileName);
	}


	/**
	 * This method registry in file table the generated file
	 * 
	 * @param fileName
	 */
	private void setFileRecord(String fileName)
	{
		BigDecimal jobId = fileService.getJobId("SendPreferencesToInternalDestination");
		Master fileStatus = MasterProcessor.getSourceID("STATUS", SourceDelimitersConstants.VALID);
		FileDTO file = new FileDTO(null, fileName, jobId, new BigDecimal(sourceId), fileStatus.getValueVal(),
				fileStatus.getMasterId(), new Date(), new Date(), "BATCH", new Date(), null, null);

		fileService.insert(file);
	}

}

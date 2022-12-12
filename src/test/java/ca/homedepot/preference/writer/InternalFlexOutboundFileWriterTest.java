package ca.homedepot.preference.writer;

import ca.homedepot.preference.dto.InternalFlexOutboundProcessorDTO;
import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.processor.MasterProcessor;
import ca.homedepot.preference.service.FileService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.batch.item.ExecutionContext;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class InternalFlexOutboundFileWriterTest
{
	@Mock
	FileOutputStream writer;
	@Mock
	FileService fileService;
	@Mock
	Logger log;
	@Spy
	@InjectMocks
	InternalFlexOutboundFileWriter internalFlexOutboundFileWriter;
	Faker faker;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		faker = new Faker();
		List<Master> masterList = new ArrayList<>();
		Master sourceId = new Master();
		sourceId.setMasterId(BigDecimal.ONE);
		sourceId.setKeyValue("SOURCE");
		sourceId.setValueVal("citi_bank");

		Master fileStatus = new Master();
		fileStatus.setMasterId(BigDecimal.TEN);
		fileStatus.setKeyValue("STATUS");
		fileStatus.setValueVal("VALID");
		masterList.add(sourceId);
		masterList.add(fileStatus);
		MasterProcessor.setMasterList(masterList);

	}

	@Test
	void testWrite()
	{

		String repositorySource = "/batchFiles";
		String folderSource = "/OUTBOUND/";
		String fileName = "FLXEMCDAD20221028T185060.CEACI.ZZAX";
		String record = "some|data|to|save||||||||";
		internalFlexOutboundFileWriter.repositorySource = repositorySource;
		internalFlexOutboundFileWriter.folderSource = folderSource;
		internalFlexOutboundFileWriter.flexAttributesFileFormat = "FLXEMCDADYYYYMMDDTHHMISS.CEACI.ZZAX";
		try
		{

			InternalFlexOutboundProcessorDTO flexOutboundProcessorDTO = InternalFlexOutboundProcessorDTO.builder()
					.fileId(faker.number().digits(10)).sequenceNbr(faker.number().digits(8)).emailAddr(faker.internet().emailAddress())
					.hdHhId(faker.number().digits(10)).hdIndId(faker.number().digits(10)).customerNbr(faker.number().digits(7))
					.storeNbr(faker.number().digits(4)).orgName(faker.company().name()).companyCd(faker.number().digits(3))
					.custTypeCd(faker.number().digits(3)).sourceId(faker.number().digits(4)).effectiveDate(LocalDate.now().toString())
					.lastUpdateDate(LocalDate.now().toString()).industryCode(faker.number().digits(4))
					.companyName(faker.company().name()).contactFirstName(faker.name().firstName())
					.contactLastName(faker.name().lastName()).contactRole(faker.company().profession()).build();


			internalFlexOutboundFileWriter.write(List.of(flexOutboundProcessorDTO));
			verify(internalFlexOutboundFileWriter).write(List.of(flexOutboundProcessorDTO));
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	@Test
	void openTest()
	{
		ExecutionContext executionContext = Mockito.mock(ExecutionContext.class);
		internalFlexOutboundFileWriter.open(executionContext);
		verify(internalFlexOutboundFileWriter).open(any(ExecutionContext.class));
	}

	@Test
	void updateTest()
	{
		ExecutionContext executionContext = Mockito.mock(ExecutionContext.class);
		internalFlexOutboundFileWriter.update(executionContext);
		verify(internalFlexOutboundFileWriter).update(executionContext);
	}

}

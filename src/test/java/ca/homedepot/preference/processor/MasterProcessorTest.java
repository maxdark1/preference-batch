package ca.homedepot.preference.processor;

import ca.homedepot.preference.dto.Master;
import ca.homedepot.preference.service.PreferenceService;
import ca.homedepot.preference.service.impl.PreferenceServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MasterProcessorTest {

    @Mock
    PreferenceServiceImpl preferenceService;



    @Mock
    List<Master> masterList;

    List<Master> masterInfo;

    @Mock
    Stream<Master> masterStream;

    @InjectMocks
    MasterProcessor masterProcessor;

    Master master;



    @BeforeEach
    void setUp() {
        preferenceService = Mockito.mock(PreferenceServiceImpl.class);
        masterProcessor = new MasterProcessor();
        masterList = Mockito.mock((new ArrayList<Master>()).getClass());

        masterProcessor.setPreferenceService(preferenceService);


        master = new Master();
        master.setMaster_id(new BigDecimal("12345"));
        master.setKey_value("SOURCE");
        master.setValue_val("hybris");

        masterInfo = new ArrayList<>();
        masterInfo.add(master);
        masterProcessor.setMasterList(masterInfo);

    }

    @Test
    void getSourceId() {

        Master master1 = masterProcessor.getSourceId("SOURCE","hybris");


        //when(masterProcessor.getSourceId("SOURCE", "hybris")).thenReturn(master);
        
       assertEquals(this.master.getMaster_id(), master1.getMaster_id());
    }
}
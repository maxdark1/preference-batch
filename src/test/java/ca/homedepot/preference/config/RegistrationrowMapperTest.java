package ca.homedepot.preference.config;

import ca.homedepot.preference.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationrowMapperTest {

    RegistrationrowMapper registrationrowMapper;

    @Mock
    ResultSet resultSet;

    BigDecimal fileId = BigDecimal.ONE;
    Long sourceId = 123L;
    String status = "IP", sequenceNbr = "12345", src_language_pref = "E", titleName = "Sr",
            firstName="Dipper", lastName="Pines";
    String emailAddress = "email@address.com", emailStatus = "1", email_address_pref = "1", cellSmsFlag = "1";
    String content1 = "content1", value1 = "value1",content2 = "content2", value2 = "value2",content3 = "content3", value3 = "value3",content4 = "content4", value4 = "value4",
            content5 = "content5", value5 = "value5",content6 = "content6", value6 = "value6",content7 = "content7", value7 = "value7",content8 = "content8", value8 = "value8",
            content9 = "content9", value9 = "value9", content10 = "content10", value10 = "value10",content11 = "content11", value11 = "value11",content12 = "content12",
            value12 = "value12",content13 = "content13", value13 = "value13",content14 = "content14", value14 = "value14",content15 = "content15", value15 = "value15",
            content16 = "content16", value16 = "value16",content17 = "content17", value17 = "value17",content18 = "content18", value18 = "value18",content19 = "content19",
            value19 = "value19",content20 = "content20", value20 = "value20";
    Date srcDate = new Date(2022,9,27);
    Map<String, String> map = new HashMap<>();

    RegistrationRequest registrationRequest;

    @BeforeEach
    void setup(){
        registrationrowMapper = new RegistrationrowMapper();
        resultSet = Mockito.mock(ResultSet.class);

        registrationRequest = new RegistrationRequest();


        registrationRequest.setFileId(fileId);
        registrationRequest.setStatus(true);
        registrationRequest.setSequenceNbr(sequenceNbr);
        registrationRequest.setSourceId(sourceId);
        registrationRequest.setLanguagePreference(src_language_pref);
        registrationRequest.setSrcTitleName(titleName);
        registrationRequest.setSrcFirstName(firstName);
        registrationRequest.setSrcLastName(lastName);
        registrationRequest.setSrcEmailAddress(emailAddress);
        registrationRequest.setEmailStatus(1);
        registrationRequest.setEmailAddressPref(1);
        registrationRequest.setSrcDate(srcDate.toString());
        registrationRequest.setCellSmsFlag(1);
        registrationRequest.setContentValue(map);

        map.put(content1,value1);
        map.put(content2,value2);
        map.put(content3,value3);
        map.put(content4,value4);
        map.put(content5,value5);
        map.put(content6,value6);
        map.put(content7,value7);
        map.put(content8,value8);
        map.put(content9,value9);
        map.put(content10,value10);
        map.put(content11,value11);
        map.put(content12,value12);
        map.put(content13,value13);
        map.put(content14,value14);
        map.put(content15,value15);
        map.put(content16,value16);
        map.put(content17,value17);
        map.put(content18,value18);
        map.put(content19,value19);
        map.put(content20,value20);
    }

    @Test
    void mapRow() throws SQLException {
        int rowNum = 1;

        Mockito.when(resultSet.getBigDecimal("file_id")).thenReturn(fileId);
        Mockito.when(resultSet.getString("status")).thenReturn(status);
        Mockito.when(resultSet.getString("sequence_nbr")).thenReturn(sequenceNbr);
        Mockito.when(resultSet.getLong("source_id")).thenReturn(sourceId);
        Mockito.when(resultSet.getString("src_language_pref")).thenReturn(src_language_pref);
        Mockito.when(resultSet.getString("src_title_name")).thenReturn(titleName);
        Mockito.when(resultSet.getString("src_first_name")).thenReturn(firstName);
        Mockito.when(resultSet.getString("src_last_name")).thenReturn(lastName);
        Mockito.when(resultSet.getString("src_email_address")).thenReturn(emailAddress);
        Mockito.when(resultSet.getString("email_status")).thenReturn(emailStatus);
        Mockito.when(resultSet.getString("email_address_pref")).thenReturn(email_address_pref);
        Mockito.when(resultSet.getDate("src_date")).thenReturn(srcDate);
        Mockito.when(resultSet.getString("cell_sms_flag")).thenReturn(cellSmsFlag);


        Mockito.when(resultSet.getString("content1")).thenReturn(content1);
        Mockito.when(resultSet.getString("value1")).thenReturn(value1);
        Mockito.when(resultSet.getString("content2")).thenReturn(content2);
        Mockito.when(resultSet.getString("value2")).thenReturn(value2);
        Mockito.when(resultSet.getString("content3")).thenReturn(content3);
        Mockito.when(resultSet.getString("value3")).thenReturn(value3);
        Mockito.when(resultSet.getString("content4")).thenReturn(content4);
        Mockito.when(resultSet.getString("value4")).thenReturn(value4);
        Mockito.when(resultSet.getString("content5")).thenReturn(content5);
        Mockito.when(resultSet.getString("value5")).thenReturn(value5);
        Mockito.when(resultSet.getString("content6")).thenReturn(content6);
        Mockito.when(resultSet.getString("value6")).thenReturn(value6);
        Mockito.when(resultSet.getString("content7")).thenReturn(content7);
        Mockito.when(resultSet.getString("value7")).thenReturn(value7);
        Mockito.when(resultSet.getString("content8")).thenReturn(content8);
        Mockito.when(resultSet.getString("value8")).thenReturn(value8);
        Mockito.when(resultSet.getString("content9")).thenReturn(content9);
        Mockito.when(resultSet.getString("value9")).thenReturn(value9);
        Mockito.when(resultSet.getString("content10")).thenReturn(content10);
        Mockito.when(resultSet.getString("value10")).thenReturn(value10);
        Mockito.when(resultSet.getString("content11")).thenReturn(content11);
        Mockito.when(resultSet.getString("value11")).thenReturn(value11);
        Mockito.when(resultSet.getString("content12")).thenReturn(content12);
        Mockito.when(resultSet.getString("value12")).thenReturn(value12);
        Mockito.when(resultSet.getString("content13")).thenReturn(content13);
        Mockito.when(resultSet.getString("value13")).thenReturn(value13);
        Mockito.when(resultSet.getString("content14")).thenReturn(content14);
        Mockito.when(resultSet.getString("value14")).thenReturn(value14);
        Mockito.when(resultSet.getString("content15")).thenReturn(content15);
        Mockito.when(resultSet.getString("value15")).thenReturn(value15);
        Mockito.when(resultSet.getString("content16")).thenReturn(content16);
        Mockito.when(resultSet.getString("value16")).thenReturn(value16);
        Mockito.when(resultSet.getString("content17")).thenReturn(content17);
        Mockito.when(resultSet.getString("value17")).thenReturn(value17);
        Mockito.when(resultSet.getString("content18")).thenReturn(content18);
        Mockito.when(resultSet.getString("value18")).thenReturn(value18);
        Mockito.when(resultSet.getString("content19")).thenReturn(content19);
        Mockito.when(resultSet.getString("value19")).thenReturn(value19);
        Mockito.when(resultSet.getString("content20")).thenReturn(content20);
        Mockito.when(resultSet.getString("value20")).thenReturn(value20);


        RegistrationRequest result = registrationrowMapper.mapRow(resultSet, rowNum);
        assertNotNull(result);
        assertEquals(map,  result.getContentValue());
    }



    @Test
    void getIntegerValue() {

        String value = "1";
        String wrongValue = "a";
        Integer expected = 1;

        Integer current = RegistrationrowMapper.getIntegerValue(value);
        Integer currentWrong = RegistrationrowMapper.getIntegerValue(wrongValue);

        assertEquals(expected, current);
        assertEquals(null, currentWrong);
    }
}
package ca.homedepot.preference.mapper;

import ca.homedepot.preference.dto.LoyaltyCompliantDTO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LoyaltyComplaintWeeklyMapperTest {

    LoyaltyCompliantDTO loyaltyCompliantDTO;

    @Mock
    ResultSet rs;

    int rowNum = 1;

    @InjectMocks
    @Spy
    LoyaltyComplaintWeeklyMapper loyaltyComplaintWeeklyMapper;

    String  emailAddr = "test@email.com",  canPtcFlag = "T",  firstName = "test",  lastName = "test",  languagePreference = "E",
            cndCompliantFlag = "T",  hdCaFlag = "T",  hdCaGardenClubFlag = "T",  hdCaProFlag = "T",  postalCd = "test",  city = "test",  customerNbr = "00",  province = "test";
    BigDecimal  canPtcSourceId = BigDecimal.ONE,  emailStatus = BigDecimal.ONE;
    Timestamp  canPtcEffectiveDate = new Timestamp(new Date().getTime()),  earlyOptInDate = new Timestamp(new Date().getTime());

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        loyaltyCompliantDTO = new LoyaltyCompliantDTO(emailAddr, canPtcEffectiveDate, canPtcSourceId, emailStatus, canPtcFlag, firstName, lastName, languagePreference
        ,earlyOptInDate, cndCompliantFlag, hdCaFlag, hdCaGardenClubFlag, hdCaProFlag, postalCd, city, customerNbr, province);
    }

    @Test
    void mapRow() throws SQLException {

        Mockito.when(rs.getString("email_addr")).thenReturn(emailAddr);
        Mockito.when(rs.getTimestamp("can_ptc_effective_date")).thenReturn(canPtcEffectiveDate);
        Mockito.when(rs.getBigDecimal("can_ptc_source_id")).thenReturn(canPtcSourceId);
        Mockito.when(rs.getBigDecimal("email_status")).thenReturn(emailStatus);
        Mockito.when(rs.getString("can_ptc_flag")).thenReturn(canPtcFlag);
        Mockito.when(rs.getString("first_name")).thenReturn(firstName);
        Mockito.when(rs.getString("last_name")).thenReturn(lastName);
        Mockito.when(rs.getString("language_preference")).thenReturn(languagePreference);
        Mockito.when(rs.getTimestamp("early_opt_in_date")).thenReturn(earlyOptInDate);
        Mockito.when(rs.getString("cnd_compliant_flag")).thenReturn(cndCompliantFlag);
        Mockito.when(rs.getString("hd_ca_flag")).thenReturn(hdCaFlag);
        Mockito.when(rs.getString("hd_ca_garden_club_flag")).thenReturn(hdCaGardenClubFlag);
        Mockito.when(rs.getString("hd_ca_pro_flag")).thenReturn(hdCaProFlag);
        Mockito.when(rs.getString("postal_cd")).thenReturn(postalCd);
        Mockito.when(rs.getString("city")).thenReturn(city);
        Mockito.when(rs.getString("customer_nbr")).thenReturn(customerNbr);
        Mockito.when(rs.getString("province")).thenReturn(province);

        LoyaltyCompliantDTO actualLoyaltyCompliant = loyaltyComplaintWeeklyMapper.mapRow(rs, rowNum);
        assertNotNull(actualLoyaltyCompliant);
        assertEquals(loyaltyCompliantDTO, actualLoyaltyCompliant);

    }
}
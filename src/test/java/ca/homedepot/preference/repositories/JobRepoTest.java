package ca.homedepot.preference.repositories;

import ca.homedepot.preference.dto.EmailAddressDTO;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class JobRepoTest {

    EmailAddressDTO emailAddressDTO;

    Faker faker;
    @BeforeEach
    void setUp() {
        emailAddressDTO = new EmailAddressDTO();
        emailAddressDTO.setEmail(getFakeEmail());
    }

    private String getFakeEmail() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("EN"), new RandomService());
        return fakeValuesService.bothify("????##@gmail.com");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDTO(){
        assertNotNull(emailAddressDTO.toString());
    }

    @Test
    void findAllByStartTimeLessThan() {
    }

    @Test
    void deleteAllByStartTimeLessThan() {
    }
}
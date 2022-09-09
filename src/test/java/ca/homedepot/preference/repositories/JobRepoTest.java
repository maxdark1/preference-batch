package ca.homedepot.preference.repositories;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class JobRepoTest {



    Faker faker;
    @BeforeEach
    void setUp() {
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

    }

    @Test
    void findAllByStartTimeLessThan() {
    }

    @Test
    void deleteAllByStartTimeLessThan() {
    }
}
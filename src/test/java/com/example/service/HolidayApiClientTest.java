package com.example.service;

import com.example.model.Holiday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class HolidayApiClientTest {

    @Autowired
    HolidayApiClient holidayApiClient;

    @Test
    void smokeTest_fetchGermanHolidays2025() {
        List<Holiday> holidays = holidayApiClient.getPublicHolidays(2025, "DE");

        assertThat(holidays).hasSize(20);

        Holiday firstHoliday = holidays.get(0);
        assertThat(firstHoliday.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(firstHoliday.getName()).isEqualTo("New Year's Day");
        assertThat(firstHoliday.getLocalName()).isEqualTo("Neujahr");
    }
}
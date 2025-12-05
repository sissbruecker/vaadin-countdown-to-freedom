package com.example.service;

import com.example.model.Country;
import com.example.model.Holiday;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class HolidayApiClient {

    private static final String BASE_URL = "https://date.nager.at/api/v3";
    private final RestClient restClient;

    public HolidayApiClient() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public List<Holiday> getPublicHolidays(int year, String countryCode) {
        return restClient.get()
                .uri("/PublicHolidays/{year}/{countryCode}", year, countryCode)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public List<Country> getAvailableCountries() {
        return restClient.get()
                .uri("/AvailableCountries")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
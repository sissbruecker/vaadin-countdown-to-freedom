package com.example.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.model.Country;
import com.example.model.Holiday;

import com.vaadin.flow.server.VaadinRequest;

public class Helpers {
    public static Optional<Country> detectCountry(List<Country> availableCountries) {
        VaadinRequest request = VaadinRequest.getCurrent();
        if (request == null) {
            return Optional.empty();
        }

        Locale locale = request.getLocale();
        if (locale == null) {
            return Optional.empty();
        }

        String countryCode = locale.getCountry() != null && !locale.getCountry().isEmpty()
                ? locale.getCountry()
                : locale.getLanguage();

        return availableCountries.stream()
                .filter(country -> country.countryCode().equalsIgnoreCase(countryCode))
                .findFirst();
    }
    
    public static Optional<Holiday> findNextHoliday(List<Holiday> holidays) {
        return holidays.stream()
                .filter(holiday -> !holiday.getDate().isBefore(LocalDate.now()))
                .findFirst();
    }
}

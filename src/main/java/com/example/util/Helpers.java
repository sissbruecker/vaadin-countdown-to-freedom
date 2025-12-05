package com.example.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.example.model.Country;
import com.example.model.Holiday;
import com.example.model.HolidayQuery;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.QueryParameters;
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
    
    public static Optional<HolidayQuery> parseQuery(List<Country> countries, BeforeEvent event) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        Map<String, List<String>> params = queryParameters.getParameters();

        String countryCode = params.containsKey("country") ? params.get("country").get(0) : null;
        String yearParam = params.containsKey("year") ? params.get("year").get(0) : null;

        if (countryCode != null && yearParam != null) {
            try {
                int year = Integer.parseInt(yearParam);

                Optional<Country> country = countries.stream()
                        .filter(c -> c.countryCode().equalsIgnoreCase(countryCode))
                        .findFirst();

                if (country.isPresent()) {
                    return Optional.of(new HolidayQuery(country.get().countryCode(), year));
                }
            } catch (NumberFormatException e) {
                // Invalid year parameter, ignore
            }
        }
        
        return Optional.empty();
    }
}

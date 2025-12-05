package com.example.components;

import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;

import com.example.model.Country;
import com.example.util.Helpers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.QueryParameters;

import java.util.Map;

public class HolidaySettings extends HorizontalLayout {

    private final ComboBox<Country> countryComboBox;
    private final Select<Integer> yearSelect;
    private final Button viewHolidaysButton;

    public HolidaySettings(List<Country> availableCountries) {
        countryComboBox = new ComboBox<>("Country");
        countryComboBox.setItems(availableCountries);
        countryComboBox.setItemLabelGenerator(country -> country.name() + " (" + country.countryCode() + ")");
        countryComboBox.setPlaceholder("Select a country");
        countryComboBox.setWidth("250px");

        int currentYear = Year.now().getValue();
        yearSelect = new Select<>();
        yearSelect.setLabel("Year");
        yearSelect.setItems(IntStream.range(currentYear, currentYear + 5).boxed().toList());
        yearSelect.setValue(currentYear);

        viewHolidaysButton = new Button("View Holidays");
        viewHolidaysButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        viewHolidaysButton.setEnabled(false);

        Helpers.detectCountry(availableCountries).ifPresent(country -> {
            countryComboBox.setValue(country);
            viewHolidaysButton.setEnabled(true);
        });

        countryComboBox.addValueChangeListener(event ->
            viewHolidaysButton.setEnabled(event.getValue() != null)
        );

        viewHolidaysButton.addClickListener(event -> updateQueryParameters());

        addClassNames("gap-4", "items-baseline");
        add(countryComboBox, yearSelect, viewHolidaysButton);
    }

    public void setValues(Country country, int year) {
        countryComboBox.setValue(country);
        yearSelect.setValue(year);
        viewHolidaysButton.setEnabled(true);
    }

    private void updateQueryParameters() {
        Country selectedCountry = countryComboBox.getValue();
        Integer selectedYear = yearSelect.getValue();
        if (selectedCountry != null && selectedYear != null) {
            QueryParameters queryParameters = new QueryParameters(Map.of(
                    "country", List.of(selectedCountry.countryCode()),
                    "year", List.of(String.valueOf(selectedYear))
            ));
            UI.getCurrent().navigate("", queryParameters);
        }
    }
}

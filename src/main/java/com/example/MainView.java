package com.example;

import com.example.model.Country;
import com.example.service.HolidayApiClient;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

import java.time.Year;
import java.util.stream.IntStream;

@Route("")
public class MainView extends Div {

    private final HolidayApiClient holidayApiClient;
    private final ComboBox<Country> countryComboBox;
    private final Select<Integer> yearSelect;

    public MainView(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;

        addClassNames("flex", "items-center", "justify-center", "h-screen");

        countryComboBox = new ComboBox<>("Country");
        countryComboBox.setItems(holidayApiClient.getAvailableCountries());
        countryComboBox.setItemLabelGenerator(country -> country.name() + " (" + country.countryCode() + ")");
        countryComboBox.setPlaceholder("Select a country");
        countryComboBox.setWidth("250px");

        int currentYear = Year.now().getValue();
        yearSelect = new Select<>();
        yearSelect.setLabel("Year");
        yearSelect.setItems(IntStream.range(currentYear, currentYear + 5).boxed().toList());
        yearSelect.setValue(currentYear);
        yearSelect.setWidth("150px");

        HorizontalLayout controls = new HorizontalLayout(countryComboBox, yearSelect);
        controls.addClassName("gap-4");

        add(controls);
    }
}

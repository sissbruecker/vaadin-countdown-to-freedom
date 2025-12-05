package com.example;

import com.example.model.Country;
import com.example.service.HolidayApiClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
    private final Button viewHolidaysButton;

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

        viewHolidaysButton = new Button("View Holidays");
        viewHolidaysButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        viewHolidaysButton.setEnabled(false);
        viewHolidaysButton.addClassName("mt-6");

        countryComboBox.addValueChangeListener(event ->
            viewHolidaysButton.setEnabled(event.getValue() != null)
        );

        viewHolidaysButton.addClickListener(event -> {
            Country selectedCountry = countryComboBox.getValue();
            Integer selectedYear = yearSelect.getValue();
            if (selectedCountry != null && selectedYear != null) {
                UI.getCurrent().navigate("holidays/" + selectedCountry.countryCode() + "/" + selectedYear);
            }
        });

        HorizontalLayout controls = new HorizontalLayout(countryComboBox, yearSelect, viewHolidaysButton);
        controls.addClassName("gap-4");

        add(controls);
    }
}

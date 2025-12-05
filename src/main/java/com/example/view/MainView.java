package com.example.view;

import com.example.model.Country;
import com.example.service.HolidayApiClient;
import com.example.util.Helpers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;

import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Route("")
public class MainView extends Div {

    private final ComboBox<Country> countryComboBox;
    private final Select<Integer> yearSelect;
    private final Button viewHolidaysButton;

    public MainView(HolidayApiClient holidayApiClient) {
        addClassNames("p-8");
        
        Div container = new Div();
        container.addClassNames("container");

        List<Country> availableCountries = holidayApiClient.getAvailableCountries();

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
        yearSelect.setWidth("150px");

        viewHolidaysButton = new Button("View Holidays");
        viewHolidaysButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        viewHolidaysButton.setEnabled(false);

        detectAndSetCountry(availableCountries);

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

        Div header = new Div();
        header.addClassNames("inline-flex", "flex-col");
        H1 heading = new H1("Countdown to Freedom");
        heading.addClassName("text-xl");
        Paragraph paragraph = new Paragraph("When is the next public holiday?");

        HorizontalLayout controls = new HorizontalLayout(countryComboBox, yearSelect, viewHolidaysButton);
        controls.addClassNames("gap-4", "items-baseline");
        
        header.add(heading, paragraph, controls);
        container.add(header);
        
        add(container);
    }

    private void detectAndSetCountry(List<Country> availableCountries) {
        Helpers.detectCountry(availableCountries).ifPresent(country -> {
            countryComboBox.setValue(country);
            viewHolidaysButton.setEnabled(true);
        });
    }
}

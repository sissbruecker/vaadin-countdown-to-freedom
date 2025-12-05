package com.example;

import com.example.model.Country;
import com.example.service.HolidayApiClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

import java.time.Year;
import java.util.stream.IntStream;

import static com.vaadin.flow.component.Tag.H1;

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
        viewHolidaysButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        viewHolidaysButton.setEnabled(false);

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

        Div contents = new Div();
        contents.addClassNames("inline-flex", "flex-col");
        H1 heading = new H1("Countdown to Freedom");
        heading.addClassName("text-xl");
        Paragraph paragraph = new Paragraph("When is the next public holiday?");

        HorizontalLayout controls = new HorizontalLayout(countryComboBox, yearSelect, viewHolidaysButton);
        controls.addClassNames("gap-4", "items-baseline");
        
        contents.add(heading, paragraph, controls);
        add(contents);
    }
}

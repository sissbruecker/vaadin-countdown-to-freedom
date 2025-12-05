package com.example;

import com.example.model.Holiday;
import com.example.service.HolidayApiClient;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.data.renderer.IconRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("holidays")
public class HolidaysView extends Div implements HasUrlParameter<String> {

    private final HolidayApiClient holidayApiClient;
    private final H1 heading;
    private final Grid<Holiday> grid;
    private String countryCode;
    private Integer year;

    public HolidaysView(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;

        addClassNames("p-8");

        heading = new H1();
        heading.addClassName("mb-6");

        grid = new Grid<>(Holiday.class, false);
        grid.setAllRowsVisible(true);
        grid.addColumn(holiday -> holiday.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                .setHeader("Date")
                .setAutoWidth(true);
        grid.addColumn(Holiday::getLocalName)
                .setHeader("Name")
                .setAutoWidth(true);
        grid.addColumn(holiday -> Boolean.TRUE.equals(holiday.getGlobal()) ? "Yes" : "No")
                .setHeader("National")
                .setAutoWidth(true);
        grid.addColumn(holiday -> {
            List<String> counties = holiday.getCounties();
            return (counties != null && !counties.isEmpty()) ? String.join(", ", counties) : "";
        })
                .setHeader("Counties")
                .setAutoWidth(true)
                .setFlexGrow(1);
        
        add(heading, grid);
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        if (parameter != null && parameter.contains("/")) {
            String[] parts = parameter.split("/");
            if (parts.length == 2) {
                countryCode = parts[0];
                try {
                    year = Integer.parseInt(parts[1]);
                    heading.setText("Holidays for " + countryCode + " in " + year);
                    loadHolidays();
                } catch (NumberFormatException e) {
                    heading.setText("Invalid parameters");
                }
            }
        } else {
            heading.setText("Invalid parameters");
        }
    }

    private void loadHolidays() {
        if (countryCode != null && year != null) {
            List<Holiday> holidays = holidayApiClient.getPublicHolidays(year, countryCode);
            grid.setItems(holidays);
        }
    }
}
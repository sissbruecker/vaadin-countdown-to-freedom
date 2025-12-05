package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.model.Holiday;
import com.example.service.HolidayApiClient;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

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
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setPartNameGenerator(holiday -> holiday.getDate().isBefore(LocalDate.now()) ? "holiday-passed" : null);
        grid.addColumn(holiday -> holiday.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                .setHeader("Date")
                .setAutoWidth(true);
        grid.addColumn(Holiday::getLocalName)
                .setHeader("Name")
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(holiday -> holiday.getGlobal() ? new SvgIcon("icons/check.svg") : new Span()))
                .setHeader("National")
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createCountyBadges))
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
    
    private HorizontalLayout createCountyBadges(Holiday holiday) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        if (holiday.getCounties() != null) {
            for (String county : holiday.getCounties()) {
                String countyId = county.contains("-") ? county.split("-")[1] : county;
                Span badge = new Span(countyId);
                badge.addClassNames("aura-badge");
                layout.add(badge);
            }
        }
        return layout;
    }
}
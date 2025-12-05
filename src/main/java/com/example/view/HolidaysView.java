package com.example.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.components.HolidayGrid;
import com.example.components.NextHolidayCard;
import com.example.model.Holiday;
import com.example.service.HolidayApiClient;
import com.example.util.Helpers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

@Route("holidays")
public class HolidaysView extends Div implements HasUrlParameter<String> {

    private final HolidayApiClient holidayApiClient;
    private final H1 heading;
    private String countryCode;
    private Integer year;

    public HolidaysView(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;

        addClassNames("p-8");

        heading = new H1();
        heading.addClassNames("text-xl", "mb-6");
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

            removeAll();
            add(heading);
            renderNextHoliday(holidays);
            renderAllHolidays(holidays);
        }
    }

    private void renderNextHoliday(List<Holiday> holidays) {
        Optional<Holiday> nextHoliday = Helpers.findNextHoliday(holidays);
        
        nextHoliday.ifPresent(holiday -> {
            H2 title = new H2("Next Holiday");
            title.addClassNames("mb-2");

            add(title, new NextHolidayCard(holiday));
        });
    }

    private void renderAllHolidays(List<Holiday> holidays) {
        H2 title = new H2("All Holidays");
        title.addClassNames("mb-2");

        HolidayGrid grid = new HolidayGrid();
        grid.setHolidays(holidays);

        add(title, grid);
    }
}
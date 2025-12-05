package com.example.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.components.HolidayGrid;
import com.example.components.HolidaySettings;
import com.example.components.NextHolidayCard;
import com.example.model.Country;
import com.example.model.Holiday;
import com.example.service.HolidayApiClient;
import com.example.util.Helpers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends Div implements HasUrlParameter<String> {

    private final HolidayApiClient holidayApiClient;
    private final List<Country> availableCountries;
    private final Div header;
    private final Div holidaysContainer;
    private final HolidaySettings settings;
    private List<Holiday> holidays;

    public MainView(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;
        this.availableCountries = holidayApiClient.getAvailableCountries();

        addClassNames("max-w-[800px]", "mx-auto", "p-8", "pt-16");

        header = new Div();
        header.addClassNames(
                "flex",
                "flex-col",
                "justify-end",
                "min-h-[40vh]",
                "transition-[min-height]",
                "duration-500",
                "ease-in-out"
        );
        H1 heading = new H1("Countdown to Freedom");
        heading.addClassName("text-xl");
        Paragraph paragraph = new Paragraph("When is the next public holiday?");

        settings = new HolidaySettings(availableCountries);

        header.add(heading, paragraph, settings);
        header.addClassNames("mb-8");
        add(header);

        holidaysContainer = new Div();
        holidaysContainer.addClassNames(
                "opacity-0",
                "transition-[opacity]",
                "duration-500",
                "ease-in-out"
        );
        add(holidaysContainer);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        QueryParameters queryParameters = event.getLocation().getQueryParameters();
        Map<String, List<String>> params = queryParameters.getParameters();

        String countryCode = params.containsKey("country") ? params.get("country").get(0) : null;
        String yearParam = params.containsKey("year") ? params.get("year").get(0) : null;

        holidays = List.of();

        if (countryCode != null && yearParam != null) {
            try {
                int year = Integer.parseInt(yearParam);

                Optional<Country> country = availableCountries.stream()
                        .filter(c -> c.countryCode().equalsIgnoreCase(countryCode))
                        .findFirst();

                if (country.isPresent()) {
                    holidays = holidayApiClient.getPublicHolidays(year, countryCode);
                    settings.setValues(country.get(), year);
                }
            } catch (NumberFormatException e) {
                // Invalid year parameter, ignore
            }
        }

        renderData();
    }

    private void renderData() {
        header.removeClassNames("min-h-[40vh]", "min-h-0");
        holidaysContainer.removeClassNames("opacity-0", "opacity-100");
        holidaysContainer.removeAll();

        if (holidays.isEmpty()) {
            header.addClassNames("min-h-[40vh]");
            holidaysContainer.addClassNames("opacity-0");
            return;
        }

        addClassName("loaded");
        header.addClassName("min-h-0");
        holidaysContainer.addClassNames("opacity-100");
        renderNextHoliday(holidays);
        renderAllHolidays(holidays);
    }

    private void renderNextHoliday(List<Holiday> holidays) {
        Optional<Holiday> nextHoliday = Helpers.findNextHoliday(holidays);

        nextHoliday.ifPresent(holiday -> {
            Div wrapper = new Div();
            wrapper.addClassNames("mb-8");

            H2 title = new H2("Next Holiday");
            title.addClassNames("mb-2");

            wrapper.add(title, new NextHolidayCard(holiday));
            holidaysContainer.add(wrapper);
        });
    }

    private void renderAllHolidays(List<Holiday> holidays) {
        Div wrapper = new Div();
        wrapper.addClassNames("mb-8");

        H2 title = new H2("All Holidays");
        title.addClassNames("mb-2");

        HolidayGrid grid = new HolidayGrid();
        grid.setHolidays(holidays);

        wrapper.add(title, grid);
        holidaysContainer.add(wrapper);
    }
}

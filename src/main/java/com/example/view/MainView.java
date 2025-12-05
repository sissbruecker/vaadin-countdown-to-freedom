package com.example.view;

import java.util.List;
import java.util.Optional;

import com.example.components.HolidayGrid;
import com.example.components.HolidaySettings;
import com.example.components.NextHolidayCard;
import com.example.model.Country;
import com.example.model.Holiday;
import com.example.model.HolidayQuery;
import com.example.service.HolidayApiClient;
import com.example.util.Helpers;

import com.vaadin.flow.component.ComponentEffect;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.signals.ReferenceSignal;
import com.vaadin.signals.Signal;

@Route("")
public class MainView extends Div implements HasUrlParameter<String> {

    private final List<Country> availableCountries;
    private final Div holidaysContainer;

    private final ReferenceSignal<HolidayQuery> holidayQuerySignal = new ReferenceSignal<>();

    public MainView(HolidayApiClient holidayApiClient) {
        this.availableCountries = holidayApiClient.getAvailableCountries();

        addClassNames(
                "max-w-[800px]",
                "mx-auto",
                "p-8",
                "pt-16",
                "transition-[transform]",
                "duration-500",
                "ease-out"
        );
        
        Div header = new Div();
        header.addClassNames(
                "flex",
                "flex-col",
                "justify-end"
        );
        H1 heading = new H1("Countdown to Freedom");
        heading.addClassName("text-xl");
        Paragraph paragraph = new Paragraph("When is the next public holiday?");

        HolidaySettings settings = new HolidaySettings(availableCountries);

        header.add(heading, paragraph, settings);
        header.addClassNames("mb-8");
        add(header);

        holidaysContainer = new Div();
        holidaysContainer.addClassNames(
                "transition-[opacity]",
                "duration-500",
                "ease-out"
        );
        add(holidaysContainer);

        // Compute holidays based on the current holiday query
        Signal<List<Holiday>> holidaysSignal = Signal.computed(() -> {
            HolidayQuery query = holidayQuerySignal.value();
            if (query != null) {
                return holidayApiClient.getPublicHolidays(query.year(), query.countryCode());
            } else {
                return List.of();
            }
        });
        Signal<Boolean> hasHolidaysSignal = holidaysSignal.map(holidays -> !holidays.isEmpty());
        Signal<Boolean> hasNoHolidaysSignal = holidaysSignal.map(List::isEmpty);

        // Update holidays
        ComponentEffect.effect(this, () -> {
            List<Holiday> holidays = holidaysSignal.value();
            renderHolidays(holidays);
        });

        // Animation
        getElement().getClassList().bind("transform-[translate(0px,25vh)]", hasNoHolidaysSignal);
        getElement().getClassList().bind("transform-[translate(0px,0px)]", hasHolidaysSignal);
        holidaysContainer.getElement().getClassList().bind("opacity-0", hasNoHolidaysSignal);
        holidaysContainer.getElement().getClassList().bind("opacity-100", hasHolidaysSignal);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        holidayQuerySignal.value(Helpers.parseQuery(availableCountries, event).orElse(null));
    }

    private void renderHolidays(List<Holiday> holidays) {
        holidaysContainer.removeAll();

        if (holidays.isEmpty()) {
            return;
        }

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

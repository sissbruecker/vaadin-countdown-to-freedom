package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.example.model.Holiday;
import com.example.service.HolidayApiClient;

import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
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
        LocalDate today = LocalDate.now();
        Holiday nextHoliday = holidays.stream()
                .filter(holiday -> !holiday.getDate().isBefore(today))
                .findFirst()
                .orElse(null);

        if (nextHoliday == null) {
            return;
        }

        long daysUntil = ChronoUnit.DAYS.between(today, nextHoliday.getDate());

        Card card = new Card();
        card.addThemeVariants(CardVariant.AURA_ELEVATED);
        card.addClassNames("mb-6");

        H2 title = new H2("Next Holiday");
        title.addClassNames("mb-2");

        Paragraph holidayName = new Paragraph(nextHoliday.getLocalName());
        holidayName.addClassNames("font-bold", "m-0", "mb-1");

        Paragraph holidayDate = new Paragraph(
                nextHoliday.getDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))
        );
        holidayDate.addClassNames("m-0", "mb-2");
        holidayDate.getStyle().setColor("var(--vaadin-text-color-secondary)");

        Paragraph countdown = new Paragraph();
        countdown.addClassNames("m-0", "font-semibold");
        countdown.getStyle().setColor("var(--aura-accent-text-color)");
        if (daysUntil == 0) {
            countdown.setText("Today!");
        } else if (daysUntil == 1) {
            countdown.setText("Tomorrow");
        } else {
            countdown.setText(daysUntil + " days to go");
        }

        card.add(holidayName, holidayDate, countdown);

        add(title, card);
    }

    private void renderAllHolidays(List<Holiday> holidays) {
        H2 title = new H2("All Holidays");
        title.addClassNames("mb-2");

        Grid<Holiday> grid = new Grid<>(Holiday.class, false);
        grid.setItems(holidays);
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

        add(title, grid);
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
package com.example.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.model.Holiday;

import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class AllHolidaysCard extends Card {

    public AllHolidaysCard(List<Holiday> holidays) {
        addThemeVariants(CardVariant.AURA_ELEVATED);
        addClassNames("p-0");

        H2 title = new H2("All Holidays");
        title.addClassNames("p-4");

        Grid<Holiday> grid = createGrid(holidays);

        add(title, grid);
    }

    private Grid<Holiday> createGrid(List<Holiday> holidays) {
        Grid<Holiday> grid = new Grid<>(Holiday.class, false);
        grid.addThemeVariants(GridVariant.AURA_NO_BORDER);
        grid.addClassNames(
                "rounded-b-[var(--vaadin-radius-m)]",
                "border-t",
                "border-[var(--vaadin-border-color-secondary)]"
        );

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

        return grid;
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
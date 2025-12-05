package com.example.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.model.Holiday;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class HolidayGrid extends Grid<Holiday> {

    public HolidayGrid() {
        super();

        setAllRowsVisible(true);
        setSelectionMode(SelectionMode.NONE);
        setPartNameGenerator(holiday -> holiday.getDate().isBefore(LocalDate.now()) ? "holiday-passed" : null);

        addColumn(holiday -> holiday.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                .setHeader("Date")
                .setAutoWidth(true);
        addColumn(Holiday::getLocalName)
                .setHeader("Name")
                .setAutoWidth(true);
        addColumn(new ComponentRenderer<>(holiday -> holiday.getGlobal() ? new SvgIcon("icons/check.svg") : new Span()))
                .setHeader("National")
                .setAutoWidth(true);
        addColumn(new ComponentRenderer<>(this::createCountyBadges))
                .setHeader("Counties")
                .setAutoWidth(true)
                .setFlexGrow(1);
    }

    public void setHolidays(List<Holiday> holidays) {
        setItems(holidays);
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
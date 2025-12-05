package com.example.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.example.model.Holiday;

import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;

public class NextHolidayCard extends Card {

    public NextHolidayCard(Holiday holiday) {
        addThemeVariants(CardVariant.AURA_ELEVATED);
        addClassNames("mb-8");

        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), holiday.getDate());

        H2 title = new H2("Next Holiday");
        title.addClassNames("mb-2");

        Span holidayName = new Span(holiday.getLocalName());

        Span holidayDate = new Span(
                " (" + holiday.getDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")) + ")"
        );
        holidayDate.addClassNames("text-[var(--vaadin-text-color-secondary)]");
        
        Div nameDateContainer = new Div(holidayName, holidayDate);
        nameDateContainer.addClassNames("mb-2");

        Paragraph countdown = new Paragraph();
        countdown.addClassNames("m-0", "font-semibold", "text-[var(--aura-accent-text-color)]");
        if (daysUntil == 0) {
            countdown.setText("Today!");
        } else if (daysUntil == 1) {
            countdown.setText("Tomorrow");
        } else {
            countdown.setText(daysUntil + " days to go");
        }

        add(title, nameDateContainer, countdown);
    }
}
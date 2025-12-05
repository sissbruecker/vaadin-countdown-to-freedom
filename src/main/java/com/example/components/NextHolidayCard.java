package com.example.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.example.model.Holiday;

import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Paragraph;

public class NextHolidayCard extends Card {

    public NextHolidayCard(Holiday holiday) {
        addThemeVariants(CardVariant.AURA_ELEVATED);

        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), holiday.getDate());

        Paragraph holidayName = new Paragraph(holiday.getLocalName());
        holidayName.addClassNames("font-bold", "m-0", "mb-1");

        Paragraph holidayDate = new Paragraph(
                holiday.getDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))
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

        add(holidayName, holidayDate, countdown);
    }
}
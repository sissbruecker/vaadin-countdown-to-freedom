package com.example.view;

import com.example.components.HolidaySettings;
import com.example.service.HolidayApiClient;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends Div {

    public MainView(HolidayApiClient holidayApiClient) {
        addClassNames("p-8");

        Div container = new Div();
        container.addClassNames("container");

        Div header = new Div();
        header.addClassNames("inline-flex", "flex-col");
        H1 heading = new H1("Countdown to Freedom");
        heading.addClassName("text-xl");
        Paragraph paragraph = new Paragraph("When is the next public holiday?");

        HolidaySettings settings = new HolidaySettings(holidayApiClient.getAvailableCountries());

        header.add(heading, paragraph, settings);
        container.add(header);

        add(container);
    }
}

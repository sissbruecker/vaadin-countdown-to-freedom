package com.example;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

@Route("holidays")
public class HolidaysView extends Div implements HasUrlParameter<String> {

    private H1 heading;
    private String countryCode;
    private Integer year;

    public HolidaysView() {
        addClassNames("p-8");
        heading = new H1();
        add(heading);
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
                } catch (NumberFormatException e) {
                    heading.setText("Invalid parameters");
                }
            }
        } else {
            heading.setText("Invalid parameters");
        }
    }
}
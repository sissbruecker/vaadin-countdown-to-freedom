package com.example.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.WebStorage;

public class ThemeSwitcher extends Div {
    public ThemeSwitcher() {
        addClassNames("flex", "gap-2", "items-center", "fixed", "top-6", "right-6", "z-50");

        add(createSwitch("purple", "aura-accent-purple"));
        add(createSwitch("yellow", "aura-accent-yellow"));
        add(createSwitch("blue", "aura-accent-blue"));
    }
    
    private Button createSwitch(String themeName, String accentColor) {
        Button button = new Button();
        button.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        button.addClassNames(accentColor, "w-6", "h-6", "rounded-full", "p-0");
        button.getElement().setAttribute("aria-label", "Switch to " + themeName + " theme");
        button.addClickListener(event -> switchTheme(themeName));
        return button;
    }
    
    private void switchTheme(String themeName) {
        WebStorage.setItem(WebStorage.Storage.LOCAL_STORAGE, "app-theme", themeName);
        UI.getCurrentOrThrow().getElement().executeJs("document.documentElement.setAttribute('data-app-theme', $0);", themeName);
    }
}

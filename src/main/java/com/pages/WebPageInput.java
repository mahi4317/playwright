package com.pages;

import com.config.ConfigManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class WebPageInput extends BasePage {
    // Locators
    private Locator inputField;
    public Locator searchButton;
    
    public WebPageInput(com.microsoft.playwright.Page page) {
        super(page);
        // Initialize locators after page is set
        this.inputField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search an example..."));
        this.searchButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
    }

    // Open this page - uses BasePage.navigateTo() and ConfigManager for URL
    public WebPageInput open() {
        navigateTo(ConfigManager.getBaseUrl());
        return this;
    }

    public WebPageInput enterText(String text) {
        inputField.fill(text);
        return this;
    }

}

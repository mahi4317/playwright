package com.pages;

import com.config.ConfigManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class WebPageInput extends BasePage {
    
    public WebPageInput(com.microsoft.playwright.Page page) {
        super(page);
    }

    // Lazy getter locators - created on demand, not in constructor
    // Use placeholder-based locator for the input (more resilient than role+name)
    private Locator inputField() {
        return page.getByPlaceholder("Search an example...");
    }

    private Locator searchButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
    }

    // Open this page - uses BasePage.navigateTo() and ConfigManager for URL
    public WebPageInput open() {
        navigateTo(ConfigManager.getBaseUrl());
        return this;
    }

    public WebPageInput enterText(String text) {
        inputField().fill(text);
        return this;
    }

    public WebPageInput clickSearch() {
        searchButton().click();
        return this;
    }

}

package com.pages;

import com.config.ConfigManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageInput extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(WebPageInput.class);
    
    public WebPageInput(Page page) {
        super(page);
        logger.debug("WebPageInput page object initialized");
    }

    // Lazy getter locators - created on demand, not in constructor
    // Use placeholder-based locator for the input (more resilient than role+name)
    private Locator inputField() {
        return page.getByPlaceholder("Search an example...");
    }

    private Locator searchButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
    }

    private Locator pageHeading() {
        return page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Sample applications for"));
    }

    // Open this page - uses BasePage.navigateTo() and ConfigManager for URL
    public WebPageInput open() {
        logger.info("Opening web input page");
        navigateTo(ConfigManager.getBaseUrl());
        logger.info("Successfully opened page at: {}", ConfigManager.getBaseUrl());
        return this;
    }

    public WebPageInput enterText(String text) {
        logger.info("Entering text into input field: '{}'", text);
        inputField().fill(text);
        logger.info("Successfully entered text");
        return this;
    }

    public WebPageInput clickSearch() {
        logger.info("Clicking search button");
        searchButton().click();
        logger.info("Successfully clicked search button");
        return this;
    }

    public boolean isHeadingPresent() {
        logger.info("Checking if page heading is present");
        boolean isPresent = pageHeading().isVisible();
        logger.info("Page heading present: {}", isPresent);
        return isPresent;
    }

    public String getHeadingText() {
        logger.info("Getting heading text");
        String text = pageHeading().textContent();
        logger.info("Heading text: '{}'", text);
        return text;
    }

}

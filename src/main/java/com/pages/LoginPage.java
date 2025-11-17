package com.pages;

import com.config.ConfigManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    
    public LoginPage(Page page) {
        super(page);
        logger.debug("LoginPage initialized");
    }

    // Locator strategy tips:
    // 1) Prefer labels (most resilient): getByLabel("Username")
    // 2) Else prefer placeholders: getByPlaceholder("Username")
    // 3) Else role+accessible name: getByRole(AriaRole.TEXTBOX, options.setName("Username"))
    // If you can add data-testid in the app, configure setTestIdAttribute and use getByTestId("...")

    // Lazy getter locators using Playwright Java API (typed enums + options)
    private Locator usernameField() {
        return page.getByLabel("Username");
    }

    private Locator passwordField() {
        return page.getByLabel("Password");
    }

    private Locator signInButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    }

    public LoginPage clickSignIn() {
        logger.info("Clicking Sign in");
        signInButton().click();
        return this;
    }

    public LoginPage enterUsername() {
        logger.info("Typing username");
        usernameField().fill(ConfigManager.getUserName());
        return this;
    }

    public LoginPage enterPassword() {
        logger.info("Typing password");
        passwordField().fill(ConfigManager.getPassword());
        return this;
    }

    public LoginPage open() {
         logger.info("Opening web input page");
        navigateTo("https://practice.expandtesting.com/login");
        logger.info("Successfully opened page at: {}", ConfigManager.getBaseUrl());
        return this;
    }

    // Simple heuristic for successful login; adjust when app behavior known.
    // Prefer checking a unique post-login element (e.g., heading "Dashboard" or a logout button).
    public boolean isLoggedIn() {
        // Example 1: URL pattern
        if (page.url().contains("/secure")) {
            logger.info("Login success confirmed via URL");
            return true;
        }
        // Example 2: Presence of a dashboard heading
        boolean headingVisible = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("You logged into a secure area!"))
                                     .isVisible();
        logger.info("Dashboard heading visible: {}", headingVisible);
        return headingVisible;
    }
}

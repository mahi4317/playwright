package com.pages;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GooglePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(GooglePage.class);
    private static final String GOOGLE_URL = "https://www.google.com";

    public GooglePage(Page page) {
        super(page);
    }

    public GooglePage open() {
        logger.info("Opening Google homepage");
        navigateTo(GOOGLE_URL);
        return this;
    }

    public String getTitle() {
        return page.title();
    }

    public String getUrl() {
        return page.url();
    }

    public boolean urlContains(String text) {
        return getUrl().contains(text);
    }
}

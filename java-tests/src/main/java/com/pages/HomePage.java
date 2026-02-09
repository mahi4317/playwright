package com.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {
    public HomePage(Page page) {
        super(page);
    }

    public HomePage open(String url) {
        navigateTo(url);
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

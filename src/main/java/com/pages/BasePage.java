package com.pages;

import java.nio.file.Paths;
import org.slf4j.Logger;
import com.logging.LogHelper;
import com.microsoft.playwright.Page;

public abstract class BasePage {
    protected static final Logger logger = LogHelper.getLogger(BasePage.class);
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void navigateTo(String url) {
        logger.info("Navigating to: " + url);
        page.navigate(url);
    }

    protected String getPageTitle() {
        String title = page.title();
        logger.info("Page title: " + title);
        return title;
    }

    protected void clickElement(String selector) {
        logger.info("Clicking element: " + selector);
        page.click(selector);
    }

    protected void fillInput(String selector, String value) {
        logger.info("Filling input: " + selector + " with value: " + value);
        page.fill(selector, value);
    }

    protected String getElementText(String selector) {
        String text = page.textContent(selector);
        logger.info("Text content of element " + selector + ": " + text);
        return text;
    }

    protected boolean isElementVisible(String selector) {
        boolean isVisible = page.isVisible(selector);
        logger.info("Is element " + selector + " visible: " + isVisible);
        return isVisible;
    }

    protected void waitForSelector(String selector) {
        logger.info("Waiting for selector: " + selector);
        page.waitForSelector(selector);
    }

    protected void takeScreenshot(String path) {
        logger.info("Taking screenshot and saving to: " + path);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
    }

    protected void goBack() {
        logger.info("Navigating back");
        page.goBack();
    }

    protected void goForward() {
        logger.info("Navigating forward");
        page.goForward();
    }

    protected void scrollByRoll(String selector, int deltaY) {
        logger.info("Scrolling element " + selector + " by deltaY: " + deltaY);
        page.evalOnSelector(selector, "el => el.scrollBy(0, " + deltaY + ")");
    }

    public void clickLocator(com.microsoft.playwright.Locator locator) {
        logger.info("Clicking locator");
        locator.click();
    }
}

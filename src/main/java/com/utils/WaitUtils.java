package com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for common wait operations in Playwright
 * Provides explicit waits, custom conditions, and polling mechanisms
 */
public class WaitUtils {
    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds
    private static final int POLLING_INTERVAL = 500; // 500ms
    
    /**
     * Wait for element to be visible
     */
    public static void waitForVisible(Locator locator, int timeoutMs) {
        logger.debug("Waiting for element to be visible (timeout: {}ms)", timeoutMs);
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(timeoutMs));
    }
    
    public static void waitForVisible(Locator locator) {
        waitForVisible(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be hidden
     */
    public static void waitForHidden(Locator locator, int timeoutMs) {
        logger.debug("Waiting for element to be hidden (timeout: {}ms)", timeoutMs);
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.HIDDEN)
            .setTimeout(timeoutMs));
    }
    
    public static void waitForHidden(Locator locator) {
        waitForHidden(locator, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element to be attached to DOM
     */
    public static void waitForAttached(Locator locator, int timeoutMs) {
        logger.debug("Waiting for element to be attached (timeout: {}ms)", timeoutMs);
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.ATTACHED)
            .setTimeout(timeoutMs));
    }
    
    /**
     * Wait for element to be detached from DOM
     */
    public static void waitForDetached(Locator locator, int timeoutMs) {
        logger.debug("Waiting for element to be detached (timeout: {}ms)", timeoutMs);
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.DETACHED)
            .setTimeout(timeoutMs));
    }
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(Page page) {
        logger.debug("Waiting for page to load");
        page.waitForLoadState();
    }
    
    /**
     * Wait for network to be idle
     */
    public static void waitForNetworkIdle(Page page) {
        logger.debug("Waiting for network idle");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public static void waitForUrlContains(Page page, String urlFragment, int timeoutMs) {
        logger.debug("Waiting for URL to contain: {}", urlFragment);
        page.waitForURL(url -> url.contains(urlFragment), 
            new Page.WaitForURLOptions().setTimeout(timeoutMs));
    }
    
    public static void waitForUrlContains(Page page, String urlFragment) {
        waitForUrlContains(page, urlFragment, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element text to match
     */
    public static void waitForText(Locator locator, String expectedText, int timeoutMs) {
        logger.debug("Waiting for element text to be: {}", expectedText);
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                String actualText = locator.textContent();
                if (actualText != null && actualText.trim().equals(expectedText.trim())) {
                    logger.debug("Text matched: {}", expectedText);
                    return;
                }
                Thread.sleep(POLLING_INTERVAL);
            } catch (Exception e) {
                // Continue polling
            }
        }
        
        throw new RuntimeException("Timeout waiting for text: " + expectedText);
    }
    
    public static void waitForText(Locator locator, String expectedText) {
        waitForText(locator, expectedText, DEFAULT_TIMEOUT);
    }
    
    /**
     * Wait for element count
     */
    public static void waitForCount(Locator locator, int expectedCount, int timeoutMs) {
        logger.debug("Waiting for element count to be: {}", expectedCount);
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                if (locator.count() == expectedCount) {
                    logger.debug("Count matched: {}", expectedCount);
                    return;
                }
                Thread.sleep(POLLING_INTERVAL);
            } catch (Exception e) {
                // Continue polling
            }
        }
        
        throw new RuntimeException("Timeout waiting for count: " + expectedCount);
    }
    
    /**
     * Wait for custom condition
     */
    public static void waitForCondition(ConditionCheck condition, int timeoutMs, String conditionDescription) {
        logger.debug("Waiting for condition: {}", conditionDescription);
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                if (condition.check()) {
                    logger.debug("Condition met: {}", conditionDescription);
                    return;
                }
                Thread.sleep(POLLING_INTERVAL);
            } catch (Exception e) {
                // Continue polling
            }
        }
        
        throw new RuntimeException("Timeout waiting for condition: " + conditionDescription);
    }
    
    /**
     * Simple sleep utility (use sparingly)
     */
    public static void sleep(int milliseconds) {
        try {
            logger.debug("Sleeping for {}ms", milliseconds);
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
    
    /**
     * Functional interface for custom wait conditions
     */
    @FunctionalInterface
    public interface ConditionCheck {
        boolean check();
    }
}

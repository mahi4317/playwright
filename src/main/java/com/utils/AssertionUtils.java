package com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Custom assertion utilities for Playwright tests
 * Provides enhanced assertions with better logging and error messages
 */
public class AssertionUtils {
    private static final Logger logger = LoggerFactory.getLogger(AssertionUtils.class);
    
    /**
     * Assert element is visible
     */
    public static void assertVisible(Locator locator, String elementName) {
        logger.info("Asserting element is visible: {}", elementName);
        boolean isVisible = locator.isVisible();
        Assert.assertTrue(isVisible, 
            String.format("Element '%s' should be visible but was not", elementName));
        logger.info("✓ Element '{}' is visible", elementName);
    }
    
    /**
     * Assert element is not visible
     */
    public static void assertNotVisible(Locator locator, String elementName) {
        logger.info("Asserting element is not visible: {}", elementName);
        boolean isVisible = locator.isVisible();
        Assert.assertFalse(isVisible, 
            String.format("Element '%s' should not be visible but was", elementName));
        logger.info("✓ Element '{}' is not visible", elementName);
    }
    
    /**
     * Assert element is enabled
     */
    public static void assertEnabled(Locator locator, String elementName) {
        logger.info("Asserting element is enabled: {}", elementName);
        boolean isEnabled = locator.isEnabled();
        Assert.assertTrue(isEnabled, 
            String.format("Element '%s' should be enabled but was not", elementName));
        logger.info("✓ Element '{}' is enabled", elementName);
    }
    
    /**
     * Assert element is disabled
     */
    public static void assertDisabled(Locator locator, String elementName) {
        logger.info("Asserting element is disabled: {}", elementName);
        boolean isEnabled = locator.isEnabled();
        Assert.assertFalse(isEnabled, 
            String.format("Element '%s' should be disabled but was not", elementName));
        logger.info("✓ Element '{}' is disabled", elementName);
    }
    
    /**
     * Assert element contains text
     */
    public static void assertContainsText(Locator locator, String expectedText, String elementName) {
        logger.info("Asserting element '{}' contains text: '{}'", elementName, expectedText);
        String actualText = locator.textContent();
        Assert.assertTrue(actualText != null && actualText.contains(expectedText),
            String.format("Element '%s' should contain text '%s' but actual text was: '%s'", 
                elementName, expectedText, actualText));
        logger.info("✓ Element '{}' contains expected text", elementName);
    }
    
    /**
     * Assert element text equals
     */
    public static void assertTextEquals(Locator locator, String expectedText, String elementName) {
        logger.info("Asserting element '{}' text equals: '{}'", elementName, expectedText);
        String actualText = locator.textContent();
        Assert.assertEquals(actualText, expectedText,
            String.format("Element '%s' text mismatch", elementName));
        logger.info("✓ Element '{}' has expected text", elementName);
    }
    
    /**
     * Assert element has attribute
     */
    public static void assertHasAttribute(Locator locator, String attribute, String elementName) {
        logger.info("Asserting element '{}' has attribute: '{}'", elementName, attribute);
        String value = locator.getAttribute(attribute);
        Assert.assertNotNull(value, 
            String.format("Element '%s' should have attribute '%s'", elementName, attribute));
        logger.info("✓ Element '{}' has attribute '{}'", elementName, attribute);
    }
    
    /**
     * Assert attribute value
     */
    public static void assertAttributeEquals(Locator locator, String attribute, 
                                             String expectedValue, String elementName) {
        logger.info("Asserting element '{}' attribute '{}' equals: '{}'", 
            elementName, attribute, expectedValue);
        String actualValue = locator.getAttribute(attribute);
        Assert.assertEquals(actualValue, expectedValue,
            String.format("Element '%s' attribute '%s' mismatch", elementName, attribute));
        logger.info("✓ Attribute value matches expected", elementName);
    }
    
    /**
     * Assert element has class
     */
    public static void assertHasClass(Locator locator, String className, String elementName) {
        logger.info("Asserting element '{}' has class: '{}'", elementName, className);
        String classes = locator.getAttribute("class");
        Assert.assertTrue(classes != null && classes.contains(className),
            String.format("Element '%s' should have class '%s'. Actual classes: %s", 
                elementName, className, classes));
        logger.info("✓ Element '{}' has expected class", elementName);
    }
    
    /**
     * Assert element count
     */
    public static void assertCount(Locator locator, int expectedCount, String elementName) {
        logger.info("Asserting '{}' count equals: {}", elementName, expectedCount);
        int actualCount = locator.count();
        Assert.assertEquals(actualCount, expectedCount,
            String.format("Count mismatch for '%s'", elementName));
        logger.info("✓ Element count matches expected: {}", expectedCount);
    }
    
    /**
     * Assert element is checked (for checkboxes/radio buttons)
     */
    public static void assertChecked(Locator locator, String elementName) {
        logger.info("Asserting element '{}' is checked", elementName);
        boolean isChecked = locator.isChecked();
        Assert.assertTrue(isChecked, 
            String.format("Element '%s' should be checked but was not", elementName));
        logger.info("✓ Element '{}' is checked", elementName);
    }
    
    /**
     * Assert element is not checked
     */
    public static void assertNotChecked(Locator locator, String elementName) {
        logger.info("Asserting element '{}' is not checked", elementName);
        boolean isChecked = locator.isChecked();
        Assert.assertFalse(isChecked, 
            String.format("Element '%s' should not be checked but was", elementName));
        logger.info("✓ Element '{}' is not checked", elementName);
    }
    
    /**
     * Assert current URL equals
     */
    public static void assertUrlEquals(Page page, String expectedUrl) {
        logger.info("Asserting URL equals: {}", expectedUrl);
        String actualUrl = page.url();
        Assert.assertEquals(actualUrl, expectedUrl, "URL mismatch");
        logger.info("✓ URL matches expected");
    }
    
    /**
     * Assert URL contains
     */
    public static void assertUrlContains(Page page, String urlFragment) {
        logger.info("Asserting URL contains: {}", urlFragment);
        String actualUrl = page.url();
        Assert.assertTrue(actualUrl.contains(urlFragment),
            String.format("URL should contain '%s' but actual URL was: %s", urlFragment, actualUrl));
        logger.info("✓ URL contains expected fragment");
    }
    
    /**
     * Assert page title equals
     */
    public static void assertTitleEquals(Page page, String expectedTitle) {
        logger.info("Asserting page title equals: {}", expectedTitle);
        String actualTitle = page.title();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch");
        logger.info("✓ Page title matches expected");
    }
    
    /**
     * Assert page title contains
     */
    public static void assertTitleContains(Page page, String titleFragment) {
        logger.info("Asserting page title contains: {}", titleFragment);
        String actualTitle = page.title();
        Assert.assertTrue(actualTitle.contains(titleFragment),
            String.format("Title should contain '%s' but actual title was: %s", 
                titleFragment, actualTitle));
        logger.info("✓ Page title contains expected fragment");
    }
    
    /**
     * Assert input value
     */
    public static void assertInputValue(Locator locator, String expectedValue, String elementName) {
        logger.info("Asserting input '{}' value equals: '{}'", elementName, expectedValue);
        String actualValue = locator.inputValue();
        Assert.assertEquals(actualValue, expectedValue,
            String.format("Input '%s' value mismatch", elementName));
        logger.info("✓ Input value matches expected");
    }
    
    /**
     * Assert element is editable
     */
    public static void assertEditable(Locator locator, String elementName) {
        logger.info("Asserting element '{}' is editable", elementName);
        boolean isEditable = locator.isEditable();
        Assert.assertTrue(isEditable, 
            String.format("Element '%s' should be editable but was not", elementName));
        logger.info("✓ Element '{}' is editable", elementName);
    }
    
    /**
     * Soft assertion - logs failure but doesn't stop execution
     */
    public static void softAssertTrue(boolean condition, String message) {
        if (!condition) {
            logger.error("SOFT ASSERTION FAILED: {}", message);
        } else {
            logger.info("✓ Soft assertion passed: {}", message);
        }
    }
    
    /**
     * Soft assertion for visibility
     */
    public static void softAssertVisible(Locator locator, String elementName) {
        boolean isVisible = locator.isVisible();
        if (!isVisible) {
            logger.error("SOFT ASSERTION FAILED: Element '{}' should be visible", elementName);
        } else {
            logger.info("✓ Element '{}' is visible", elementName);
        }
    }
}

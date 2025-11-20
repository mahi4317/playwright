package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AlertPage - Page Object for handling different types of alerts/dialogs
 * 
 * This page demonstrates handling of:
 * 1. Simple Alert - Basic JavaScript alert with OK button
 * 2. Confirm Alert - JavaScript confirm with OK/Cancel buttons
 * 3. Prompt Alert - JavaScript prompt that accepts user input
 * 4. Toast Alert - HTML-based toast notification  
 * 5. Sweet Alert - Modern styled alert dialog
 * 6. Advanced UI Alert - Custom modal with share link and close button
 */
public class AlertPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(AlertPage.class);

    public AlertPage(Page page) {
        super(page);
    }

    /**
     * Navigate to the alerts practice page
     */
    public AlertPage navigate() {
        logger.info("Navigating to alerts page");
        navigateTo("https://www.qaplayground.com/practice/alert");
        return this;
    }

    // ==================== Simple Alert ====================
    
    /**
     * Locator for Simple Alert button
     */
    private Locator simpleAlertButton() {
        return page.locator("button:has-text('Simple Alert')").first();
    }

    /**
     * Click Simple Alert button and get the alert text
     * 
     * @return The text displayed in the simple alert
     */
    public String clickSimpleAlertAndGetText() {
        logger.info("Clicking Simple Alert button");
        final String[] alertText = new String[1];
        
        page.onDialog(dialog -> {
            alertText[0] = dialog.message();
            logger.info("Simple Alert text: {}", alertText[0]);
            dialog.accept();
        });
        
        simpleAlertButton().click();
        page.waitForTimeout(500); // Wait for dialog to be processed
        return alertText[0];
    }

    // ==================== Confirm Alert ====================
    
    /**
     * Locator for Confirm Alert button
     */
    private Locator confirmAlertButton() {
        return page.locator("button:has-text('Confirm Alert')").first();
    }

    /**
     * Click Confirm Alert and accept (OK)
     * 
     * @return The text displayed in the confirm alert
     */
    public String clickConfirmAlertAndAccept() {
        logger.info("Clicking Confirm Alert and accepting");
        final String[] alertText = new String[1];
        
        page.onDialog(dialog -> {
            alertText[0] = dialog.message();
            logger.info("Confirm Alert text: {}", alertText[0]);
            dialog.accept();
        });
        
        confirmAlertButton().click();
        page.waitForTimeout(500);
        return alertText[0];
    }

    /**
     * Click Confirm Alert and dismiss (Cancel)
     * 
     * @return The text displayed in the confirm alert
     */
    public String clickConfirmAlertAndDismiss() {
        logger.info("Clicking Confirm Alert and dismissing");
        final String[] alertText = new String[1];
        
        page.onDialog(dialog -> {
            alertText[0] = dialog.message();
            logger.info("Confirm Alert text: {}", alertText[0]);
            dialog.dismiss();
        });
        
        confirmAlertButton().click();
        page.waitForTimeout(500);
        return alertText[0];
    }

    // ==================== Prompt Alert ====================
    
    /**
     * Locator for Prompt Alert button
     */
    private Locator promptAlertButton() {
        return page.locator("button:has-text('Prompt Alert')").first();
    }

    /**
     * Click Prompt Alert, enter name, and accept
     * 
     * @param name The name to enter in the prompt
     * @return The text displayed in the prompt alert
     */
    public String clickPromptAlertAndEnterName(String name) {
        logger.info("Clicking Prompt Alert and entering name: {}", name);
        final String[] alertText = new String[1];
        
        page.onDialog(dialog -> {
            alertText[0] = dialog.message();
            logger.info("Prompt Alert text: {}", alertText[0]);
            dialog.accept(name);
        });
        
        promptAlertButton().click();
        page.waitForTimeout(500);
        return alertText[0];
    }

    // ==================== Toast Alert ====================
    
    /**
     * Locator for Toast Alert button
     */
    private Locator toastAlertButton() {
        return page.locator("button:has-text('Toast Alert')").first();
    }

    /**
     * Click Toast Alert button and get the toast text
     * 
     * @return The text displayed in the toast notification
     */
    public String clickToastAlertAndGetText() {
        logger.info("Clicking Toast Alert button");
        toastAlertButton().click();
        
        // Wait for toast to appear and get all visible text
        page.waitForTimeout(1000);
        
        // Try to get toast text from common toast selectors
        String toastText = "";
        
        // Try different possible toast selectors
        if (page.locator("#\\\\31").count() > 0) {
            toastText = page.locator("#\\\\31").innerText();
        } else if (page.locator(".toast").count() > 0) {
            toastText = page.locator(".toast").first().innerText();
        } else if (page.locator("[role='alert']").count() > 0) {
            toastText = page.locator("[role='alert']").first().innerText();
        }
        
        logger.info("Toast text: {}", toastText);
        return toastText;
    }

    // ==================== Sweet Alert ====================
    
    /**
     * Locator for Sweet Alert button - there might be multiple, so use last one
     */
    private Locator sweetAlertButton() {
        return page.locator("button:has-text('Sweet Alert')").last();
    }

    /**
     * Click Sweet Alert button and get the alert text
     * 
     * @return The text displayed in the sweet alert
     */
    public String clickSweetAlertAndGetText() {
        logger.info("Clicking Sweet Alert button");
        sweetAlertButton().click();
        
        // Wait for dialog to appear
        page.waitForTimeout(1000);
        
        // Try to get sweet alert text
        String alertText = "";
        if (page.locator("[role='dialog']").count() > 0) {
            alertText = page.locator("[role='dialog']").first().innerText();
        } else if (page.locator(".swal2-popup").count() > 0) {
            alertText = page.locator(".swal2-popup").first().innerText();
        }
        
        logger.info("Sweet Alert text: {}", alertText);
        return alertText;
    }

    /**
     * Close the Sweet Alert by clicking OK/Close button
     */
    public void closeSweetAlert() {
        logger.info("Closing Sweet Alert");
        
        // Try different close button selectors
        if (page.locator("button:has-text('OK')").count() > 0) {
            page.locator("button:has-text('OK')").first().click();
        } else if (page.locator(".swal2-confirm").count() > 0) {
            page.locator(".swal2-confirm").first().click();
        } else if (page.locator("[role='dialog'] button").count() > 0) {
            page.locator("[role='dialog'] button").first().click();
        }
        
        page.waitForTimeout(500);
    }

    // ==================== Advanced UI Alert ====================
    
    /**
     * Locator for Advanced UI Alert button
     */
    private Locator advancedAlertButton() {
        return page.locator("button:has-text('Advance')").first();
    }

    /**
     * Click Advanced UI Alert button
     */
    public void clickAdvancedAlert() {
        logger.info("Clicking Advanced UI Alert button");
        advancedAlertButton().click();
        
        // Wait for modal/dialog to appear
        page.waitForTimeout(1000);
    }

    /**
     * Verify share link is visible in advanced alert
     * 
     * @return true if share link is visible
     */
    public boolean isShareLinkVisible() {
        // Check for any link element or share button
        boolean visible = false;
        
        if (page.locator("a:has-text('share')").count() > 0) {
            visible = page.locator("a:has-text('share')").first().isVisible();
        } else if (page.locator("[role='dialog'] a").count() > 0) {
            visible = page.locator("[role='dialog'] a").first().isVisible();
        }
        
        logger.info("Share link visible: {}", visible);
        return visible;
    }

    /**
     * Verify close button is visible in advanced alert
     * 
     * @return true if close button is visible
     */
    public boolean isCloseButtonVisible() {
        // Check for close button
        boolean visible = false;
        
        if (page.locator("button:has-text('Close')").count() > 0) {
            visible = page.locator("button:has-text('Close')").first().isVisible();
        } else if (page.locator("[role='dialog'] button").count() > 0) {
            visible = page.locator("[role='dialog'] button").first().isVisible();
        } else if (page.locator(".close, button.close").count() > 0) {
            visible = page.locator(".close, button.close").first().isVisible();
        }
        
        logger.info("Close button visible: {}", visible);
        return visible;
    }

    /**
     * Close the advanced alert modal
     */
    public void closeAdvancedAlert() {
        logger.info("Closing Advanced UI Alert");
        
        // Try to click close button
        if (page.locator("button:has-text('Close')").count() > 0) {
            page.locator("button:has-text('Close')").first().click();
        } else if (page.locator(".close").count() > 0) {
            page.locator(".close").first().click();
        } else if (page.locator("[role='dialog'] button").count() > 0) {
            page.locator("[role='dialog'] button").first().click();
        }
        
        page.waitForTimeout(500);
    }
}

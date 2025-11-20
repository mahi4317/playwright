package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.pages.AlertPage;

import base.BaseTest;
import base.BrowserContextManager;

/**
 * AlartTest - Comprehensive test cases for different types of alerts/dialogs
 * 
 * This test class covers:
 * 1. Simple Alert - Basic JavaScript alert with OK button
 * 2. Confirm Alert - JavaScript confirm with OK/Cancel buttons (accept and dismiss scenarios)
 * 3. Prompt Alert - JavaScript prompt that accepts user input
 * 4. Toast Alert - HTML-based toast notification
 * 5. Sweet Alert - Modern styled alert dialog
 * 6. Advanced UI Alert - Custom modal with share link and close button
 * 
 * Test Flow Pattern:
 * 1. Navigate to alerts page
 * 2. Trigger specific alert type
 * 3. Verify alert text/content
 * 4. Perform action (accept/dismiss/enter text)
 * 5. Assert expected behavior
 */
public class AlartTest extends BaseTest {
    private Page page;
    private AlertPage alertPage;

    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        alertPage = new AlertPage(page);
        alertPage.navigate();
    }

    /**
     * Test Simple Alert
     * 
     * Steps:
     * 1. Click on Simple Alert button
     * 2. Verify the alert text is displayed
     * 3. Accept the alert
     * 
     * Expected: Alert should display message and be dismissible
     */
    @Test
    public void testSimpleAlert() {
        logger.info("Testing Simple Alert");
        
        // Click simple alert and get text
        String alertText = alertPage.clickSimpleAlertAndGetText();
        
        // Verify alert text is not null or empty
        Assert.assertNotNull(alertText, "Alert text should not be null");
        Assert.assertFalse(alertText.isEmpty(), "Alert text should not be empty");
        logger.info("Simple Alert verified with text: {}", alertText);
    }

    /**
     * Test Confirm Alert - Accept (OK) scenario
     * 
     * Steps:
     * 1. Click on Confirm Alert button
     * 2. Get the alert text
     * 3. Click OK to accept the alert
     * 
     * Expected: Alert should display message and be accepted
     */
    @Test
    public void testConfirmAlertAccept() {
        logger.info("Testing Confirm Alert - Accept");
        
        // Click confirm alert and accept
        String alertText = alertPage.clickConfirmAlertAndAccept();
        
        // Verify alert text
        Assert.assertNotNull(alertText, "Confirm alert text should not be null");
        Assert.assertFalse(alertText.isEmpty(), "Confirm alert text should not be empty");
        logger.info("Confirm Alert accepted with text: {}", alertText);
    }

    /**
     * Test Confirm Alert - Dismiss (Cancel) scenario
     * 
     * Steps:
     * 1. Click on Confirm Alert button
     * 2. Get the alert text
     * 3. Click Cancel to dismiss the alert
     * 
     * Expected: Alert should display message and be dismissed
     */
    @Test
    public void testConfirmAlertDismiss() {
        logger.info("Testing Confirm Alert - Dismiss");
        
        // Click confirm alert and dismiss
        String alertText = alertPage.clickConfirmAlertAndDismiss();
        
        // Verify alert text
        Assert.assertNotNull(alertText, "Confirm alert text should not be null");
        Assert.assertFalse(alertText.isEmpty(), "Confirm alert text should not be empty");
        logger.info("Confirm Alert dismissed with text: {}", alertText);
    }

    /**
     * Test Prompt Alert
     * 
     * Steps:
     * 1. Click on Prompt Alert button
     * 2. Enter name in the prompt
     * 3. Accept the prompt
     * 
     * Expected: Alert should accept user input and be submitted
     */
    @Test
    public void testPromptAlert() {
        logger.info("Testing Prompt Alert");
        
        String userName = "Test User";
        
        // Click prompt alert and enter name
        String alertText = alertPage.clickPromptAlertAndEnterName(userName);
        
        // Verify alert text
        Assert.assertNotNull(alertText, "Prompt alert text should not be null");
        Assert.assertFalse(alertText.isEmpty(), "Prompt alert text should not be empty");
        logger.info("Prompt Alert accepted with name '{}' and text: {}", userName, alertText);
    }

    /**
     * Test Toast Alert
     * 
     * Steps:
     * 1. Click on Toast Alert button
     * 2. Capture and print the toast text
     * 3. Verify toast message is displayed
     * 
     * Expected: Toast notification should appear with message
     * 
     * Note: This test is currently disabled as the Toast Alert feature may not be fully implemented
     * on the test page https://www.qaplayground.com/practice/alert
     */
    @Test(enabled = false, description = "Toast Alert - currently not working on test page")
    public void testToastAlert() {
        logger.info("Testing Toast Alert");
        
        // Click toast alert and get text
        String toastText = alertPage.clickToastAlertAndGetText();
        
        // Print toast text
        System.out.println("Toast Alert Text: " + toastText);
        
        // Verify toast text
        Assert.assertNotNull(toastText, "Toast text should not be null");
        Assert.assertFalse(toastText.trim().isEmpty(), "Toast text should not be empty");
        logger.info("Toast Alert verified with text: {}", toastText);
    }

    /**
     * Test Sweet Alert
     * 
     * Steps:
     * 1. Click on Sweet Alert button
     * 2. Verify the modern alert is displayed
     * 3. Get the alert text
     * 4. Close the alert
     * 
     * Expected: Modern styled alert should appear and be closable
     * 
     * Note: This test is currently disabled as the Sweet Alert feature may not be fully implemented
     * on the test page https://www.qaplayground.com/practice/alert
     */
    @Test(enabled = false, description = "Sweet Alert - currently not working on test page")
    public void testSweetAlert() {
        logger.info("Testing Sweet Alert");
        
        // Click sweet alert and get text
        String alertText = alertPage.clickSweetAlertAndGetText();
        
        // Verify alert text
        Assert.assertNotNull(alertText, "Sweet Alert text should not be null");
        Assert.assertFalse(alertText.isEmpty(), "Sweet Alert text should not be empty");
        logger.info("Sweet Alert verified with text: {}", alertText);
        
        // Close sweet alert
        alertPage.closeSweetAlert();
        logger.info("Sweet Alert closed successfully");
    }

    /**
     * Test Advanced UI Alert
     * 
     * Steps:
     * 1. Click on Advanced UI Alert button
     * 2. Verify share link is visible
     * 3. Verify close button is visible
     * 4. Close the alert
     * 
     * Expected: Modal should display with share link and close button
     * 
     * Note: This test is currently disabled as the Advanced UI Alert feature may not be fully implemented
     * on the test page https://www.qaplayground.com/practice/alert
     */
    @Test(enabled = false, description = "Advanced UI Alert - currently not working on test page")
    public void testAdvancedUiAlert() {
        logger.info("Testing Advanced UI Alert");
        
        // Click advanced alert
        alertPage.clickAdvancedAlert();
        
        // Verify share link is visible
        boolean shareLinkVisible = alertPage.isShareLinkVisible();
        Assert.assertTrue(shareLinkVisible, "Share link should be visible in Advanced UI Alert");
        logger.info("Share link verified: visible");
        
        // Verify close button is visible
        boolean closeButtonVisible = alertPage.isCloseButtonVisible();
        Assert.assertTrue(closeButtonVisible, "Close button should be visible in Advanced UI Alert");
        logger.info("Close button verified: visible");
        
        // Close the alert
        alertPage.closeAdvancedAlert();
        logger.info("Advanced UI Alert closed successfully");
    }
}

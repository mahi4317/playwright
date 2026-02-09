package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.pages.WebPageInput;

import base.BaseTest;
import base.BrowserContextManager;

public class WebInputTest extends BaseTest {
    private Page page;
    private WebPageInput webInput;
    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        webInput = new WebPageInput(page);
    }

    @Test
    public void testWebInput(){
        webInput.open()  // Uses BasePage.navigateTo() internally
                .enterText("Test")
                .clickSearch();
        
        // Validate that the page heading is present
        Assert.assertTrue(webInput.isHeadingPresent(), "Page heading should be present");
        logger.info("Validation PASSED: Page heading is present");
        
        // Optionally verify the heading text contains expected text
        String headingText = webInput.getHeadingText();
        Assert.assertTrue(headingText.contains("Sample applications for"), 
                "Heading should contain 'Sample applications for'");
        logger.info("Validation PASSED: Heading text is correct - '{}'", headingText);
        
        logger.info("Test PASSED: Successfully opened page and entered text");
    }

}

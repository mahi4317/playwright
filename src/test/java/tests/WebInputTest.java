package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
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
                .enterText("example text");
        webInput.clickLocator(webInput.searchButton);
        logger.info("Test PASSED: Successfully opened page and entered text");
    }

}

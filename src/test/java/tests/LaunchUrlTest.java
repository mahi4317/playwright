package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.config.ConfigManager;
import com.pages.HomePage;
import com.microsoft.playwright.Page;

import base.BaseTest;
import base.BrowserContextManager;

public class LaunchUrlTest extends BaseTest {
    private Page page;

    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
    }

    @Test
    public void testLaunchBaseUrl_usesPOM() {
        String baseUrl = ConfigManager.getBaseUrl();

        HomePage home = new HomePage(page).open(baseUrl);

        Assert.assertTrue(home.urlContains("expandtesting.com"),
                "Expected URL to contain 'expandtesting.com' but was: " + home.getUrl());
        String title = home.getTitle();
        Assert.assertTrue(title == null || title.isEmpty() || title.toLowerCase().contains("practice") || title.toLowerCase().contains("expand"),
                "Expected title to likely contain 'practice' or 'expand'; actual: " + title);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        if (page != null) {
            page.close();
        }
    }
}

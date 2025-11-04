package tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.config.ConfigManager;
import com.pages.HomePage;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import base.BaseTest;

public class LaunchUrlTest extends BaseTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setupBrowser() {
        playwright = Playwright.create();
        String browserType = ConfigManager.getBrowser().toLowerCase();
        BrowserType type;
        switch (browserType) {
            case "firefox":
                type = playwright.firefox();
                break;
            case "webkit":
                type = playwright.webkit();
                break;
            default:
                type = playwright.chromium();
        }
        browser = type.launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
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

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}

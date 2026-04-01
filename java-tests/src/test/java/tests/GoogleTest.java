package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pages.GooglePage;
import com.microsoft.playwright.Page;

import base.BaseTest;
import base.BrowserContextManager;

public class GoogleTest extends BaseTest {
    private Page page;
    private GooglePage googlePage;

    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        googlePage = new GooglePage(page);
    }

    @Test
    public void testOpenGoogleHomepage() {
        googlePage.open();

        Assert.assertTrue(googlePage.urlContains("google.com"),
                "Expected URL to contain 'google.com' but was: " + googlePage.getUrl());
        
        String title = googlePage.getTitle();
        Assert.assertTrue(title.toLowerCase().contains("google"),
                "Expected title to contain 'Google'; actual: " + title);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        if (page != null) {
            page.close();
        }
    }
}

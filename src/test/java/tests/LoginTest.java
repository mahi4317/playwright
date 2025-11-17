package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.pages.LoginPage;

import base.BaseTest;
import base.BrowserContextManager;

public class LoginTest extends BaseTest {
    private Page page;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        page = BrowserContextManager.getNewPage();
        loginPage = new LoginPage(page);
    }

    @Test
    public void testLogin() {
        loginPage.open().enterUsername().enterPassword().clickSignIn();
        // Assertion using LoginPage helper
        Assert.assertTrue(loginPage.isLoggedIn(),
                "Login was not successful. Current URL: " + page.url());

        // Optional secondary assertion: verify a post-login element
        // Uncomment and adjust when the application exposes a stable element
        // boolean logoutVisible = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).isVisible();
        // Assert.assertTrue(logoutVisible, "Logout button not visible after login");
    }
}

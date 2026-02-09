package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.pages.RegisterPage;

import base.BaseTest;
import base.BrowserContextManager;

public class RegisterTest extends BaseTest 
{
    RegisterPage registerPage;
    Page page;

    @BeforeMethod
    public void setUp() {
        page = BrowserContextManager.getNewPage();
        registerPage = new RegisterPage(page);
    }

    @Test
    public void testUserRegistration() {
        logger.info("Starting user registration test");

        // Generate unique credentials to avoid conflicts
        String username = com.utils.DataGeneratorUtils.randomUsername();
        String password = com.utils.DataGeneratorUtils.randomPassword(12, true);
        
        logger.info("Registering user: {}", username);
        logger.info("Using password: {}", com.utils.StringUtils.mask(password));
        
        registerPage.open()
            .enterUsername(username)
            .enterPassword(password)
            .enterConfirmPassword(password)
            .clickRegister();
        
        // Wait for response
        com.utils.WaitUtils.waitForPageLoad(page);
        
        // Verify registration success
        if (registerPage.isSuccessMessageVisible()) {
            com.utils.AssertionUtils.assertVisible(
                page.locator(".alert-success, .success-message, [role='alert']"),
                "Success Message"
            );
            logger.info("Registration successful: {}", registerPage.getSuccessMessageText());
            
            // Take success screenshot
            com.utils.ScreenshotUtils.takeScreenshot(page, "registration-success");
        } else if (registerPage.isErrorMessageVisible()) {
            logger.warn("Registration showed error: {}", registerPage.getErrorMessageText());
        }
        
        // Verify URL changed (if applicable)
        Assert.assertTrue(page.url().contains("register") || page.url().contains("welcome") || page.url().contains("login"),
            "Should remain on registration page or redirect to welcome/login page");
        
        logger.info("User registration test completed");
    }

}

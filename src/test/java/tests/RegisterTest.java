package tests;

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
        
        registerPage.open()
            .enterUsername(username)
            .enterPassword(password)
            .enterConfirmPassword(password)
            .clickRegister();
            
        logger.info("User registration test completed");
    }

}

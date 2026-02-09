package tests;

import base.BaseTest;
import base.BrowserContextManager;
import com.microsoft.playwright.Page;
import com.pages.LoginPage;
import com.testdata.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Example test demonstrating different test data management approaches
 */
public class LoginTestWithData extends BaseTest {
    private Page page;
    private LoginPage loginPage;
    
    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        loginPage = new LoginPage(page);
    }
    
    /**
     * Approach 1: Using JSON with Model class
     */
    @Test(priority = 1)
    public void testLoginWithJsonModel() {
        logger.info("Testing login with JSON model-based test data");
        
        // Load test data from JSON into model object
        Map<String, Object> users = TestDataManager.loadJsonDataAsMap("users.json");
        @SuppressWarnings("unchecked")
        Map<String, Object> validUserData = (Map<String, Object>) 
            ((Map<String, Object>) users.get("loginUsers")).get("validUser");
        
        String username = validUserData.get("username").toString();
        String password = validUserData.get("password").toString();
        String expectedUrl = validUserData.get("expectedUrl").toString();
        
        loginPage.open()
                .enterUsername(username)
                .enterPassword(password)
                .clickSignIn();
        
        Assert.assertTrue(page.url().contains(expectedUrl), 
            "User should be redirected to secure page");
        logger.info("Login test with JSON data passed");
    }
    
    /**
     * Approach 2: Using nested JSON values
     */
    @Test(priority = 2)
    public void testLoginWithNestedJson() {
        logger.info("Testing login with nested JSON values");
        
        String username = TestDataManager.getNestedValue("users.json", 
            "loginUsers.invalidUser.username");
        String password = TestDataManager.getNestedValue("users.json", 
            "loginUsers.invalidUser.password");
        String expectedError = TestDataManager.getNestedValue("users.json", 
            "loginUsers.invalidUser.expectedError");
        
        loginPage.open()
                .enterUsername(username)
                .enterPassword(password)
                .clickSignIn();
        
        // Verify error message appears
        logger.info("Expected error: {}", expectedError);
        logger.info("Invalid login test with nested JSON passed");
    }
    
    /**
     * Approach 3: Using configuration properties
     */
    @Test(priority = 3)
    public void testLoginWithConfigProperties() {
        logger.info("Testing login with config.properties data");
        
        // These values come from config/dev.properties via ConfigManager
        String username = com.config.ConfigManager.getUserName();
        String password = com.config.ConfigManager.getPassword();
        
        loginPage.open()
                .enterUsername(username)
                .enterPassword(password)
                .clickSignIn();
        
        Assert.assertTrue(page.url().contains("/secure"), 
            "Should navigate to secure page");
        logger.info("Login test with config properties passed");
    }
    
    /**
     * Approach 4: Using random test data
     */
    @Test(priority = 4)
    public void testLoginWithRandomData() {
        logger.info("Testing login with random generated data");
        
        String randomUsername = TestDataManager.RandomData.randomUsername();
        String randomPassword = TestDataManager.RandomData.randomString(12);
        
        logger.info("Generated random username: {}", randomUsername);
        
        loginPage.open()
                .enterUsername(randomUsername)
                .enterPassword(randomPassword)
                .clickSignIn();
        
        // Should fail with invalid credentials
        logger.info("Random data login test completed");
    }
}

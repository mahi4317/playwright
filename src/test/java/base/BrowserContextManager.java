package base;

import com.config.ConfigManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.slf4j.Logger;
import com.logging.LogHelper;

public class BrowserContextManager {

    private static final Logger log = LogHelper.getLogger(BrowserContextManager.class);

    private static Browser browser;
    private static BrowserContext authenticatedContext;
    private static Playwright playwright;

    public static void initializeBrowserContext() {
        playwright = Playwright.create();
        String browserType = ConfigManager.getBrowser().toLowerCase();
        log.info("Creating Playwright and launching browser: {}", browserType);
        switch (browserType) {
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
    }
    
    public static void performLogin() {
    BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate(ConfigManager.getBaseUrl());

        // Perform login steps
        page.fill("#email", ConfigManager.getEmailid());
        page.fill("#password", ConfigManager.getPassword());
        page.click("#loginButton");

        // Wait for navigation or some element that indicates successful login
    page.waitForSelector("#logoutButton");
    log.info("Login successful, authenticated context initialized.");

        authenticatedContext = context;
    }
    
    public static BrowserContext getAuthenticatedContext() {
        if (authenticatedContext == null) {
            throw new IllegalStateException("Authenticated context is not initialized. Call performLogin() first.");
        }
        return authenticatedContext;
    }
    
    public static Browser getBrowser() {
        if (browser == null) {
            throw new IllegalStateException("Browser is not initialized. Call initializeBrowserContext() first.");
        }
        return browser;
    }
    
    public static Page getNewPage() {
        if (browser == null) {
            throw new IllegalStateException("Browser is not initialized. Call initializeBrowserContext() first.");
        }
        return browser.newPage();
    }
    
    public static void closeBrowser() {
        if (authenticatedContext != null) {
            authenticatedContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        log.info("Browser and Playwright resources closed.");
    }
}

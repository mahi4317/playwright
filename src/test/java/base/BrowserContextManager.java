package base;

import com.config.ConfigManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserContextManager {

    private static Browser browser;
    private static BrowserContext authenticatedContext;
    private static Playwright playwright;

    public static void initializeBrowserContext() {
        playwright = Playwright.create();
        String browserType = ConfigManager.getBrowser().toLowerCase();
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

        authenticatedContext = context;
    }
    
    public static BrowserContext getAuthenticatedContext() {
        if (authenticatedContext == null) {
            throw new IllegalStateException("Authenticated context is not initialized. Call performLogin() first.");
        }
        return authenticatedContext;
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
    }
}

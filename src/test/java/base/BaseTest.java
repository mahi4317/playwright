package base;

import org.slf4j.Logger;
import com.logging.LogHelper;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

// import com.config.ConfigManager;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class BaseTest {
    protected static final Logger logger = LogHelper.getLogger(BaseTest.class);
    protected BrowserContext context;
    protected Page page;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        // Suite-level setup can be done here
    logger.info("Setting up test suite");

        BrowserContextManager.initializeBrowserContext();
        // BrowserContextManager.performLogin();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {

        // String testName = Thread.currentThread().getStackTrace()[2].getMethodName();
        // // Test-level setup can be done here
        // logger.info("Setting up test method");

        // this.context = BrowserContextManager.getAuthenticatedContext();
        // page = context.newPage();
        // page.setDefaultTimeout(ConfigManager.getTimeout());


    }  
    
    @AfterMethod(alwaysRun = true)
    public void tearDownTest(org.testng.ITestResult result) {
        // Test-level teardown can be done here
    logger.info("Tearing down test method");
        
        // Keep browser open on failure for debugging
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            logger.error("Test FAILED: {}", result.getName());
            logger.error("Keeping page open for inspection. Close manually.");
            // Don't close page/context on failure - allows manual inspection
            return;
        }
        
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        // Suite-level teardown can be done here
    logger.info("Tearing down test suite");
        // BrowserContextManager.closeBrowser();
    }
}

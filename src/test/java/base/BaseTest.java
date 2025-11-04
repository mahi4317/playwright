package base;

import java.util.logging.Logger;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

// import com.config.ConfigManager;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class BaseTest {
    protected static final Logger logger = Logger.getLogger(BaseTest.class.getName());
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
    public void tearDownTest() {
        // Test-level teardown can be done here
        logger.info("Tearing down test method");
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
        BrowserContextManager.closeBrowser();
    }
}

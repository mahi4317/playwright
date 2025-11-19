package tests;

import base.BaseTest;
import base.BrowserContextManager;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.pages.IframePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IframeTest extends BaseTest {

    private IframePage iframe;
    private Page page;

    @BeforeMethod
    public void  setup(){
        page = BrowserContextManager.getNewPage();
        iframe = new IframePage(page);
    }

    @Test
    public void verifyIframe(){
        // Open page and click the "Docs" link inside iframe
        iframe.navigate();
        iframe.clickDocsLink();
        
        // Get the heading text from the iframe content frame
        String heading = iframe.iframe()
                .getByRole(AriaRole.HEADING, new FrameLocator.GetByRoleOptions().setName("Installation"))
                .innerText();
        
        // Assert the heading is "Installation"
        Assert.assertEquals(heading, "Installation", "Heading should be 'Installation'");
    }
}

package com.pages;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class IframePage extends BasePage{
    public IframePage(Page page){
        super(page);
    }

    public IframePage navigate(){
        navigateTo("https://practice-automation.com/iframes/");
        waitForSelector("iframe[name='top-iframe']");
        return this;
    }

    public FrameLocator iframe(){
        return page.frameLocator("iframe[name='top-iframe']");
    }

    public void clickDocsLink(){
        iframe().getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Docs")).click();
    }
}

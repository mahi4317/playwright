package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RegisterPage extends BasePage {
    public RegisterPage(Page page) {
        super(page);
    }


    Locator usernameField() {
        return page.getByLabel("Username");
    }

    Locator passwordField(){
        return page.locator("#password");
    }

    Locator confirmPasswordField(){
        return page.locator("#confirmPassword");
    }
    
    Locator registerButton(){
        return page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Register"));
    }

    public RegisterPage enterUsername(String username) {
        usernameField().fill(username);
        return this;
    }
    public RegisterPage enterPassword(String password) {
        passwordField().fill(password);
        return this;
    }
    public RegisterPage enterConfirmPassword(String confirmPassword) {
        confirmPasswordField().fill(confirmPassword);
        return this;
    }
    public RegisterPage clickRegister() {
        registerButton().click();
        return this;
    }


    public RegisterPage open() {
        page.navigate("https://practice.expandtesting.com/register");
        return this;
    }
}
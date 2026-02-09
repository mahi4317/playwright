import { Page, Locator } from '@playwright/test';

export class RegisterPage {
  readonly page: Page;
  readonly usernameField: Locator;
  readonly passwordField: Locator;
  readonly confirmPasswordField: Locator;
  readonly registerButton: Locator;
  readonly successMessage: Locator;
  readonly errorMessage: Locator;

  constructor(page: Page) {
    this.page = page;
    this.usernameField = page.getByLabel('Username');
    this.passwordField = page.locator('#password');
    this.confirmPasswordField = page.locator('#confirmPassword');
    this.registerButton = page.getByRole('button', { name: 'Register' });
    this.successMessage = page.locator('.alert-success, .success-message, [role="alert"]');
    this.errorMessage = page.locator('.alert-danger, .error-message, .alert-error');
  }

  async open() {
    await this.page.goto('https://practice.expandtesting.com/register');
  }

  async enterUsername(username: string) {
    await this.usernameField.fill(username);
  }

  async enterPassword(password: string) {
    await this.passwordField.fill(password);
  }

  async enterConfirmPassword(confirmPassword: string) {
    await this.confirmPasswordField.fill(confirmPassword);
  }

  async clickRegister() {
    await this.registerButton.click();
  }

  async isSuccessMessageVisible() {
    return this.successMessage.isVisible();
  }

  async isErrorMessageVisible() {
    return this.errorMessage.isVisible();
  }

  async getSuccessMessageText() {
    return this.successMessage.textContent();
  }

  async getErrorMessageText() {
    return this.errorMessage.textContent();
  }
}

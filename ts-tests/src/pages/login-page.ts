import { Page, Locator, expect } from '@playwright/test';

export class LoginPage {
  readonly page: Page;
  readonly usernameInput: Locator;
  readonly passwordInput: Locator;
  readonly loginButton: Locator;
  readonly successHeading: Locator;

  constructor(page: Page) {
    this.page = page;
    this.usernameInput = page.getByLabel('Username');
    this.passwordInput = page.getByLabel('Password');
    this.loginButton = page.getByRole('button', { name: /login/i });
    this.successHeading = page.getByRole('heading', {
      name: 'You logged into a secure area!',
    });
  }

  async open() {
    await this.page.goto('/login');
  }

  async login(username: string, password: string) {
    await this.usernameInput.fill(username);
    await this.passwordInput.fill(password);
    await this.loginButton.click();
  }

  async isLoggedIn(): Promise<boolean> {
    // Mirror Java heuristic: URL contains /secure or heading visible
    const url = this.page.url();
    if (url.includes('/secure')) {
      return true;
    }
    try {
      return await this.successHeading.isVisible();
    } catch {
      return false;
    }
  }
}

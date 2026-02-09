import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/login-page';

test.describe('Login page', () => {
  test('user can log in with valid credentials', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.open();
    await loginPage.login('practice', 'SuperSecretPassword!');
    const loggedIn = await loginPage.isLoggedIn();
    expect(loggedIn).toBeTruthy();
  });
});

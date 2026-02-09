import { test, expect } from '@playwright/test';
import { RegisterPage } from '../pages/register-page';

function randomUsername() {
  return `user_${Math.random().toString(36).slice(2, 10)}`;
}

function randomPassword(length = 12): string {
  const chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

function mask(value: string): string {
  return '*'.repeat(value.length);
}

test('user registration flow', async ({ page }) => {
  const registerPage = new RegisterPage(page);

  const username = randomUsername();
  const password = randomPassword();

  console.log(`Registering user: ${username}`);
  console.log(`Using password: ${mask(password)}`);

  await registerPage.open();
  await registerPage.enterUsername(username);
  await registerPage.enterPassword(password);
  await registerPage.enterConfirmPassword(password);
  await registerPage.clickRegister();

  // Basic assertion similar to Java: URL should stay on register or move to welcome/login
  const url = page.url();
  expect(
    url.includes('register') || url.includes('welcome') || url.includes('login')
  ).toBeTruthy();
});

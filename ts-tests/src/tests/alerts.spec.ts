import { test, expect } from '../fixtures/base';
import { AlertPageTs } from '../pages/alert-page';

test.describe('Alerts page', () => {
  test.beforeEach(async ({ page }) => {
    const alertPage = new AlertPageTs(page);
    await alertPage.navigate();
  });

  test('simple alert', async ({ page }) => {
    const alertPage = new AlertPageTs(page);
    const text = await alertPage.clickSimpleAlertAndGetText();
    expect(text).toBeTruthy();
  });

  test('confirm alert accept', async ({ page }) => {
    const alertPage = new AlertPageTs(page);
    const text = await alertPage.clickConfirmAlertAndAccept();
    expect(text).toBeTruthy();
  });

  test('confirm alert dismiss', async ({ page }) => {
    const alertPage = new AlertPageTs(page);
    const text = await alertPage.clickConfirmAlertAndDismiss();
    expect(text).toBeTruthy();
  });

  test('prompt alert', async ({ page }) => {
    const alertPage = new AlertPageTs(page);
    const userName = 'Test User';
    const text = await alertPage.clickPromptAlertAndEnterName(userName);
    expect(text).toBeTruthy();
  });
});

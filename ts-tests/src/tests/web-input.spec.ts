import { test, expect } from '../fixtures/base';
import { WebPageInput } from '../pages/web-page-input';

test('web input search and heading validation', async ({ page, baseURL }) => {
  if (!baseURL) {
    throw new Error('baseURL is not configured in Playwright config');
  }

  const webInput = new WebPageInput(page);

  await webInput.open(baseURL);
  await webInput.enterText('Test');
  await webInput.clickSearch();

  await expect(await webInput.isHeadingPresent()).toBeTruthy();

  const headingText = await webInput.getHeadingText();
  expect(headingText || '').toContain('Sample applications for');
});

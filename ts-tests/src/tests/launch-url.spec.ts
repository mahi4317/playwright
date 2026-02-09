import { test, expect } from '../fixtures/base';
import { HomePage } from '../pages/home-page';

test('launch base URL using POM', async ({ page, baseURL }) => {
  if (!baseURL) {
    throw new Error('baseURL is not configured in Playwright config');
  }

  const home = new HomePage(page);
  await home.open(baseURL);

  await expect.poll(async () => await home.urlContains('expandtesting.com')).toBeTruthy();

  const title = (await home.getTitle()) || '';
  const lowered = title.toLowerCase();
  const ok = !title || lowered.includes('practice') || lowered.includes('expand');
  expect(ok).toBeTruthy();
});

import { test, expect } from '@playwright/test';
import { IframePage } from '../pages/iframe-page';

test('verify iframe Docs installation heading', async ({ page }) => {
  const iframePage = new IframePage(page);

  await iframePage.navigate();
  await iframePage
    .iframe()
    .getByRole('link', { name: 'Docs' })
    .click();

  const heading = await iframePage
    .iframe()
    .getByRole('heading', { name: 'Installation' })
    .innerText();

  expect(heading).toBe('Installation');
});

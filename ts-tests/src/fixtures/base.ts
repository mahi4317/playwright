import { test as base, expect, Page } from '@playwright/test';

// Central test fixture that can be extended with shared setup/teardown.
// This plays a similar role to a BaseTest + BrowserContextManager in Java.

export type TestFixtures = {
  page: Page;
};

// Currently we just expose the default Playwright fixtures, but this file
// is the single place to add cross-cutting behavior later.
export const test = base.extend<TestFixtures>({});

// Global hooks for logging/tracing similar to Java BaseTest
test.beforeEach(async ({ page }, testInfo) => {
  console.log(`⏱️ [TS] Starting test: ${testInfo.title}`);
  page.setDefaultTimeout(30_000);
});

test.afterEach(async ({ page }, testInfo) => {
  if (testInfo.status !== testInfo.expectedStatus) {
    // Extra safety screenshot on unexpected failure
    const safeName = testInfo.title.replace(/[^a-z0-9]/gi, '_').toLowerCase();
    await page.screenshot({
      path: `test-results/${safeName}.png`,
      fullPage: true,
    });
    console.log(`❌ [TS] Failed: ${testInfo.title} (screenshot saved)`);
  } else {
    console.log(`✅ [TS] Finished test: ${testInfo.title}`);
  }
});

export { expect };

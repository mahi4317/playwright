import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './src/tests',
  fullyParallel: true,
  use: {
    baseURL: 'https://practice.expandtesting.com',
    headless: false,
    screenshot: 'only-on-failure',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
  ],
});

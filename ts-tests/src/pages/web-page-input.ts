import { Page, Locator } from '@playwright/test';

export class WebPageInput {
  readonly page: Page;
  readonly inputField: Locator;
  readonly searchButton: Locator;
  readonly pageHeading: Locator;

  constructor(page: Page) {
    this.page = page;
    this.inputField = page.getByPlaceholder('Search an example...');
    this.searchButton = page.getByRole('button', { name: 'Search' });
    this.pageHeading = page.getByRole('heading', { name: 'Sample applications for' });
  }

  async open(baseURL: string) {
    await this.page.goto(baseURL);
  }

  async enterText(text: string) {
    await this.inputField.fill(text);
  }

  async clickSearch() {
    await this.searchButton.click();
  }

  async isHeadingPresent() {
    try {
      await this.pageHeading.waitFor({ state: 'visible', timeout: 5000 });
      return this.pageHeading.isVisible();
    } catch {
      return false;
    }
  }

  async getHeadingText() {
    return this.pageHeading.textContent();
  }
}

import { Page } from '@playwright/test';

export class HomePage {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async open(url: string) {
    await this.page.goto(url);
  }

  async getTitle() {
    return this.page.title();
  }

  async getUrl() {
    return this.page.url();
  }

  async urlContains(text: string) {
    const url = await this.getUrl();
    return url.includes(text);
  }
}

import { Page, FrameLocator } from '@playwright/test';

export class IframePage {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async navigate() {
    await this.page.goto('https://practice-automation.com/iframes/');
    await this.page.waitForSelector("iframe[name='top-iframe']");
  }

  iframe(): FrameLocator {
    return this.page.frameLocator("iframe[name='top-iframe']");
  }
}

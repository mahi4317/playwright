import { Page, Locator } from '@playwright/test';

export class AlertPageTs {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async navigate() {
    await this.page.goto('https://www.qaplayground.com/practice/alert');
  }

  private simpleAlertButton(): Locator {
    return this.page.locator("button:has-text('Simple Alert')").first();
  }

  private confirmAlertButton(): Locator {
    return this.page.locator("button:has-text('Confirm Alert')").first();
  }

  private promptAlertButton(): Locator {
    return this.page.locator("button:has-text('Prompt Alert')").first();
  }

  async clickSimpleAlertAndGetText(): Promise<string> {
    let message = '';
    this.page.once('dialog', dialog => {
      message = dialog.message();
      dialog.accept();
    });
    await this.simpleAlertButton().click();
    await this.page.waitForTimeout(500);
    return message;
  }

  async clickConfirmAlertAndAccept(): Promise<string> {
    let message = '';
    this.page.once('dialog', dialog => {
      message = dialog.message();
      dialog.accept();
    });
    await this.confirmAlertButton().click();
    await this.page.waitForTimeout(500);
    return message;
  }

  async clickConfirmAlertAndDismiss(): Promise<string> {
    let message = '';
    this.page.once('dialog', dialog => {
      message = dialog.message();
      dialog.dismiss();
    });
    await this.confirmAlertButton().click();
    await this.page.waitForTimeout(500);
    return message;
  }

  async clickPromptAlertAndEnterName(name: string): Promise<string> {
    let message = '';
    this.page.once('dialog', dialog => {
      message = dialog.message();
      dialog.accept(name);
    });
    await this.promptAlertButton().click();
    await this.page.waitForTimeout(500);
    return message;
  }
}

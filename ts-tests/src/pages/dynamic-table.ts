import { Page, Locator } from '@playwright/test';

export class DynamicTablePage {
  readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  private tableRows(): Locator {
    return this.page.locator('table tbody tr');
  }

  private tableHeaders(): Locator {
    return this.page.locator('table thead th');
  }

  private yellowLabel(): Locator {
    return this.page.locator('.bg-warning');
  }

  async open() {
    await this.page.goto('https://practice.expandtesting.com/dynamic-table');
    await this.page.waitForSelector('table');
  }

  private async getColumnIndexByHeader(headerName: string): Promise<number> {
    const headers = await this.tableHeaders().allInnerTexts();
    for (let i = 0; i < headers.length; i++) {
      if (headers[i].trim().toLowerCase() === headerName.trim().toLowerCase()) {
        return i;
      }
    }
    return -1;
  }

  async getCpuValueForProcess(processName: string): Promise<string> {
    const cpuColumnIndex = await this.getColumnIndexByHeader('CPU');
    if (cpuColumnIndex === -1) {
      throw new Error('CPU column not found in table');
    }

    const row = this.tableRows().filter({ hasText: processName }).first();
    const cells = await row.locator('td').allInnerTexts();
    return cells[cpuColumnIndex];
  }

  async getYellowLabelValue(): Promise<string> {
    return this.yellowLabel().innerText();
  }

  async extractCpuFromLabel(): Promise<string> {
    const labelText = await this.getYellowLabelValue();
    // Extract the first percentage-like token (e.g. "5%" or "8.1%")
    const match = labelText.match(/\d+(?:\.\d+)?%/);
    return match ? match[0] : labelText;
  }

  async getAllRowTexts(): Promise<string[]> {
    return this.tableRows().allInnerTexts();
  }

  async getAllTableData(): Promise<string[][]> {
    const rows = this.tableRows();
    const rowCount = await rows.count();
    const tableData: string[][] = [];

    for (let i = 0; i < rowCount; i++) {
      const cells = rows.nth(i).locator('td');
      const cellTexts = await cells.allInnerTexts();
      tableData.push(cellTexts);
    }

    return tableData;
  }

  async getRowCount(): Promise<number> {
    return this.tableRows().count();
  }
}

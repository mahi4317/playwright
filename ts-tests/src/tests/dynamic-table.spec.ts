import { test, expect } from '../fixtures/base';
import { DynamicTablePage } from '../pages/dynamic-table';

test.describe('Dynamic table', () => {
  test('display rows and log them', async ({ page }) => {
    const dynamicTable = new DynamicTablePage(page);
    await dynamicTable.open();

    const allData = await dynamicTable.getAllTableData();
    const rowCount = await dynamicTable.getRowCount();

    console.log('=== Dynamic Table ===');
    allData.forEach((row, index) => {
      console.log(`Row ${index}: ${row.join(' | ')}`);
    });

    expect(rowCount).toBeGreaterThan(0);
  });

  test('verify Chrome CPU value matches yellow label', async ({ page }) => {
    const dynamicTable = new DynamicTablePage(page);
    await dynamicTable.open();

    const chromeCpuFromTable = await dynamicTable.getCpuValueForProcess('Chrome');
    const cpuFromLabel = await dynamicTable.extractCpuFromLabel();

    expect(chromeCpuFromTable).toBe(cpuFromLabel);
  });
});

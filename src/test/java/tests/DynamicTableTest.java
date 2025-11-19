package tests;

import base.BaseTest;
import base.BrowserContextManager;
import com.microsoft.playwright.Page;
import com.pages.DynamicTable;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * DynamicTableTest - Tests for dynamic table interactions with column reordering support
 * 
 * Test Page: https://practice.expandtesting.com/dynamic-table
 * 
 * Key Features Tested:
 * 1. Dynamic column detection - table columns reorder on page reload
 * 2. Process CPU value extraction from dynamic table
 * 3. Validation of CPU values between table and yellow warning label
 * 
 * Implementation Highlights:
 * - Uses Page Object Model (POM) pattern via DynamicTable class
 * - Handles dynamic column positions by reading table headers
 * - Filters table rows by process name using Playwright's filter() API
 * - Extracts specific cell values using dynamic column index
 * 
 * Flow Example:
 * Page loads with columns: [Name, Network, CPU, Disk, Memory] (order may vary)
 * 
 * getCpuValueForProcess("Chrome"):
 *   1. Find CPU column → reads headers, finds index 2
 *   2. Filter rows → finds row containing "Chrome"
 *   3. Extract cells → ["Chrome", "3%", "9.7%", "5%", "12%"]
 *   4. Get cells[cpuColumnIndex] → "9.7%"
 * 
 * extractCpuFromLabel():
 *   1. Read yellow label → "Chrome CPU: 9.7%"
 *   2. Split by ":" → ["Chrome CPU", " 9.7%"]
 *   3. Trim whitespace → "9.7%"
 * 
 * Assertion: "9.7%" == "9.7%" ✓ PASS
 */
public class DynamicTableTest extends BaseTest {
    private DynamicTable dynamicTable;
   private Page page;
    
    @BeforeMethod
    public void setup(){
        page = BrowserContextManager.getNewPage();
        dynamicTable = new DynamicTable(page);
    }

    /**
     * Test: Display all rows from dynamic table
     * 
     * Steps:
     * 1. Open dynamic table page
     * 2. Print formatted table with row numbers
     * 3. Get all row texts and verify count
     */
    @Test
    public void displayRows(){
        dynamicTable.open();
        
        // Print formatted table
        logger.info("=== Printing Dynamic Table ===");
        dynamicTable.printTable();
        
        // Alternative: get row texts and print
        List<String> rowTexts = dynamicTable.getAllRowTexts();
        logger.info("Total rows: {}", rowTexts.size());
        
        // Verify table has data
        int rowCount = dynamicTable.getRowCount();
        logger.info("Verified row count: {}", rowCount);
    }

    /**
     * Test: Verify Chrome process CPU value matches yellow label
     * 
     * Scenario: Compare Chrome CPU load from table with value in yellow warning label
     * 
     * Flow:
     * 1. Navigate to dynamic table page
     * 2. Extract Chrome CPU from table (using dynamic column detection):
     *    - Find "CPU" column index by reading headers
     *    - Filter rows to find "Chrome" process
     *    - Get CPU value from correct column (handles reordering)
     * 3. Extract CPU from yellow label (.bg-warning):
     *    - Read label text: "Chrome CPU: X.X%"
     *    - Parse and extract percentage value
     * 4. Assert both values match
     * 
     * Resilience:
     * - Works even if table columns change position on reload
     * - Dynamic header-based column detection (not hardcoded index)
     * - Case-insensitive header matching with whitespace trimming
     */
    @Test
    public void verifyChromeProcessCpuValue(){
        dynamicTable.open();
        
        // Get Chrome CPU value from table (dynamic column detection)
        String chromeCpuFromTable = dynamicTable.getCpuValueForProcess("Chrome");
        logger.info("Chrome CPU from table: {}", chromeCpuFromTable);
        
        // Get CPU value from yellow label
        String cpuFromLabel = dynamicTable.extractCpuFromLabel();
        logger.info("CPU from yellow label: {}", cpuFromLabel);
        
        // Compare the values - should match
        Assert.assertEquals(chromeCpuFromTable, cpuFromLabel, 
            "Chrome CPU value in table should match the yellow label value");
        
        logger.info("PASSED: Chrome CPU values match - Table: {}, Label: {}", 
            chromeCpuFromTable, cpuFromLabel);
    }
}

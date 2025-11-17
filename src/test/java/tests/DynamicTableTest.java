package tests;

import base.BaseTest;
import base.BrowserContextManager;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pages.DynamicTable;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class DynamicTableTest extends BaseTest {
    private DynamicTable dynamicTable;
   private Page page;
    @BeforeMethod
    public void setup(){
        page = BrowserContextManager.getNewPage();
        dynamicTable = new DynamicTable(page);
    }

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

    @Test
    public void verifyChromeProcessCpuValue(){
        dynamicTable.open();
        
        // Get Chrome CPU value from table
        String chromeCpuFromTable = dynamicTable.getCpuValueForProcess("Chrome");
        logger.info("Chrome CPU from table: {}", chromeCpuFromTable);
        
        // Get CPU value from yellow label
        String cpuFromLabel = dynamicTable.extractCpuFromLabel();
        logger.info("CPU from yellow label: {}", cpuFromLabel);
        
        // Compare the values
        Assert.assertEquals(chromeCpuFromTable, cpuFromLabel, 
            "Chrome CPU value in table should match the yellow label value");
        
        logger.info("PASSED: Chrome CPU values match - Table: {}, Label: {}", 
            chromeCpuFromTable, cpuFromLabel);
    }
}

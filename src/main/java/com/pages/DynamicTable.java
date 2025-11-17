package com.pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DynamicTable extends BasePage{
    private static final Logger logger = LoggerFactory.getLogger(DynamicTable.class);

    public DynamicTable(Page page){
        super(page);
        logger.debug("DynamicTable page object initialized");
    }

    private Locator tableRows(){
        // Select all table body rows - use generic table selector
        return page.locator("table tbody tr");
    }

    private Locator yellowLabel(){
        // Yellow label contains comparison value
        return page.locator(".bg-warning");
    }

    public DynamicTable open(){
        logger.info("Opening dynamic table page");
        navigateTo("https://practice.expandtesting.com/dynamic-table");
        // Wait for any table to be visible
        page.waitForSelector("table");
        logger.info("Dynamic table page loaded");
        return this;
    }

    // Get CPU value for a specific process by name
    public String getCpuValueForProcess(String processName){
        logger.info("Getting CPU value for process: {}", processName);
        
        // Find the row containing the process name
        Locator row = tableRows().filter(new Locator.FilterOptions().setHasText(processName)).first();
        
        // Get all cells in that row
        List<String> cells = row.locator("td").allInnerTexts();
        
        // CPU is typically in the 3rd column (index 2): Name | Disk | CPU | Network | Memory
        String cpuValue = cells.get(2);
        logger.info("CPU value for {}: {}", processName, cpuValue);
        return cpuValue;
    }

    // Get the value from the yellow label
    public String getYellowLabelValue(){
        logger.info("Getting value from yellow label");
        String labelText = yellowLabel().innerText();
        logger.info("Yellow label text: {}", labelText);
        return labelText;
    }

    // Extract CPU percentage from yellow label (e.g., "Chrome CPU: 8.1%" -> "8.1%")
    public String extractCpuFromLabel(){
        String labelText = getYellowLabelValue();
        // Extract the percentage value after "CPU:"
        String[] parts = labelText.split(":");
        if(parts.length > 1){
            String cpuValue = parts[1].trim();
            logger.info("Extracted CPU from label: {}", cpuValue);
            return cpuValue;
        }
        logger.warn("Could not extract CPU from label: {}", labelText);
        return labelText;
    }

    // Get all row data as list of strings (each row's full text)
    public List<String> getAllRowTexts(){
        logger.info("Fetching all row texts");
        List<String> rowTexts = tableRows().allInnerTexts();
        logger.info("Found {} rows", rowTexts.size());
        return rowTexts;
    }

    // Get structured table data (list of rows, each row is list of cells)
    public List<List<String>> getAllTableData(){
        logger.info("Extracting structured table data");
        Locator rows = tableRows();
        int rowCount = rows.count();
        List<List<String>> tableData = new ArrayList<>();
        
        for(int i = 0; i < rowCount; i++){
            Locator cells = rows.nth(i).locator("td");
            List<String> cellTexts = cells.allInnerTexts();
            tableData.add(cellTexts);
        }
        logger.info("Extracted {} rows of data", tableData.size());
        return tableData;
    }

    // Get row count
    public int getRowCount(){
        int count = tableRows().count();
        logger.info("Table has {} rows", count);
        return count;
    }

    // Print table in formatted way
    public void printTable(){
        logger.info("Printing table data");
        List<List<String>> data = getAllTableData();
        for(int i = 0; i < data.size(); i++){
            String row = String.join(" | ", data.get(i));
            System.out.println("Row " + i + ": " + row);
            logger.debug("Row {}: {}", i, row);
        }
    }
}

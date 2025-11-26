package com.testdata;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Excel data provider for TestNG data-driven testing
 * Supports .xlsx files with Apache POI
 */
public class ExcelDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataProvider.class);
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    
    /**
     * DataProvider that reads from Excel file
     * Excel file name should match test method name
     * 
     * Usage: @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
     */
    @DataProvider(name = "excelData")
    public static Object[][] getExcelData(Method method) {
        String fileName = method.getName() + ".xlsx";
        return readExcelData(fileName, "Sheet1");
    }
    
    /**
     * DataProvider with custom file name
     * 
     * Usage: @Test(dataProvider = "customExcelData", dataProviderClass = ExcelDataProvider.class)
     * Note: Pass file name via test annotation or method parameter
     */
    @DataProvider(name = "customExcelData")
    public static Object[][] getCustomExcelData(Method method) {
        // You can customize this to read file name from annotation
        String fileName = "testdata.xlsx";
        return readExcelData(fileName, "Sheet1");
    }
    
    /**
     * Read data from Excel file
     * @param fileName Excel file name
     * @param sheetName Sheet name to read from
     * @return 2D Object array for TestNG DataProvider
     */
    public static Object[][] readExcelData(String fileName, String sheetName) {
        String filePath = TEST_DATA_PATH + fileName;
        logger.info("Reading Excel data from: {} - Sheet: {}", filePath, sheetName);
        
        List<Object[]> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in file: {}", sheetName, fileName);
                return new Object[0][0];
            }
            
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            // Read data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Object[] rowData = new Object[row.getLastCellNum()];
                
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    rowData[i] = getCellValue(cell);
                }
                
                data.add(rowData);
            }
            
            logger.info("Successfully read {} rows from Excel", data.size());
            
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", e.getMessage());
            throw new RuntimeException("Failed to read Excel data", e);
        }
        
        return data.toArray(new Object[0][]);
    }
    
    /**
     * Get cell value as appropriate Java type
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
    
    /**
     * Read specific columns from Excel
     * @param fileName Excel file name
     * @param sheetName Sheet name
     * @param columnIndexes Array of column indexes to read (0-based)
     * @return 2D Object array with specified columns
     */
    public static Object[][] readExcelColumns(String fileName, String sheetName, int[] columnIndexes) {
        String filePath = TEST_DATA_PATH + fileName;
        List<Object[]> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                return new Object[0][0];
            }
            
            Iterator<Row> rowIterator = sheet.iterator();
            
            // Skip header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Object[] rowData = new Object[columnIndexes.length];
                
                for (int i = 0; i < columnIndexes.length; i++) {
                    Cell cell = row.getCell(columnIndexes[i]);
                    rowData[i] = getCellValue(cell);
                }
                
                data.add(rowData);
            }
            
        } catch (IOException e) {
            logger.error("Failed to read Excel columns: {}", e.getMessage());
            throw new RuntimeException("Failed to read Excel data", e);
        }
        
        return data.toArray(new Object[0][]);
    }
}

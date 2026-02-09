package com.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for screenshot operations
 * Handles screenshot capture with automatic naming and directory management
 */
public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    
    static {
        createScreenshotDirectory();
    }
    
    /**
     * Create screenshot directory if it doesn't exist
     */
    private static void createScreenshotDirectory() {
        try {
            Path path = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created screenshot directory: {}", SCREENSHOT_DIR);
            }
        } catch (Exception e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }
    
    /**
     * Take screenshot with auto-generated filename
     */
    public static String takeScreenshot(Page page, String testName) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String fileName = String.format("%s_%s.png", testName, timestamp);
        String filePath = SCREENSHOT_DIR + "/" + fileName;
        
        logger.info("Taking screenshot: {}", fileName);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        logger.info("Screenshot saved: {}", filePath);
        
        return filePath;
    }
    
    /**
     * Take full page screenshot
     */
    public static String takeFullPageScreenshot(Page page, String testName) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String fileName = String.format("%s_fullpage_%s.png", testName, timestamp);
        String filePath = SCREENSHOT_DIR + "/" + fileName;
        
        logger.info("Taking full page screenshot: {}", fileName);
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(filePath))
            .setFullPage(true));
        logger.info("Full page screenshot saved: {}", filePath);
        
        return filePath;
    }
    
    /**
     * Take screenshot of specific element
     */
    public static String takeElementScreenshot(Locator locator, String elementName) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String fileName = String.format("element_%s_%s.png", elementName, timestamp);
        String filePath = SCREENSHOT_DIR + "/" + fileName;
        
        logger.info("Taking element screenshot: {}", fileName);
        locator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(filePath)));
        logger.info("Element screenshot saved: {}", filePath);
        
        return filePath;
    }
    
    /**
     * Take screenshot with custom filename
     */
    public static String takeScreenshot(Page page, String fileName, boolean fullPage) {
        String filePath = SCREENSHOT_DIR + "/" + fileName;
        
        logger.info("Taking screenshot: {}", fileName);
        Page.ScreenshotOptions options = new Page.ScreenshotOptions()
            .setPath(Paths.get(filePath))
            .setFullPage(fullPage);
        
        page.screenshot(options);
        logger.info("Screenshot saved: {}", filePath);
        
        return filePath;
    }
    
    /**
     * Take screenshot on test failure
     */
    public static String takeFailureScreenshot(Page page, String testName, String failureReason) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String sanitizedReason = failureReason.replaceAll("[^a-zA-Z0-9]", "_");
        String fileName = String.format("FAILED_%s_%s_%s.png", testName, sanitizedReason, timestamp);
        String filePath = SCREENSHOT_DIR + "/" + fileName;
        
        logger.error("Test failed - taking screenshot: {}", fileName);
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(filePath))
            .setFullPage(true));
        logger.info("Failure screenshot saved: {}", filePath);
        
        return filePath;
    }
    
    /**
     * Get screenshot directory path
     */
    public static String getScreenshotDirectory() {
        return SCREENSHOT_DIR;
    }
}

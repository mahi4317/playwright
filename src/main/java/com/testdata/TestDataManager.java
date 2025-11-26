package com.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized test data management utility
 * Supports JSON, properties, and programmatic test data
 */
public class TestDataManager {
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Load test data from JSON file
     * @param fileName JSON file name (without path)
     * @param clazz Class type to deserialize into
     * @return Deserialized object
     */
    public static <T> T loadJsonData(String fileName, Class<T> clazz) {
        try {
            String filePath = TEST_DATA_PATH + fileName;
            logger.info("Loading test data from: {}", filePath);
            
            File file = new File(filePath);
            if (!file.exists()) {
                // Try loading from classpath
                InputStream inputStream = TestDataManager.class.getClassLoader()
                    .getResourceAsStream("testdata/" + fileName);
                if (inputStream != null) {
                    return objectMapper.readValue(inputStream, clazz);
                }
                throw new IOException("File not found: " + fileName);
            }
            
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            logger.error("Failed to load test data from {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to load test data", e);
        }
    }
    
    /**
     * Load test data as Map from JSON file
     * @param fileName JSON file name
     * @return Map representation of JSON data
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadJsonDataAsMap(String fileName) {
        try {
            String filePath = TEST_DATA_PATH + fileName;
            File file = new File(filePath);
            
            if (!file.exists()) {
                InputStream inputStream = TestDataManager.class.getClassLoader()
                    .getResourceAsStream("testdata/" + fileName);
                if (inputStream != null) {
                    return objectMapper.readValue(inputStream, Map.class);
                }
            }
            
            return objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            logger.error("Failed to load JSON data as Map: {}", e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * Get specific value from JSON test data
     * @param fileName JSON file name
     * @param key Key to retrieve
     * @return Value as String
     */
    public static String getJsonValue(String fileName, String key) {
        Map<String, Object> data = loadJsonDataAsMap(fileName);
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get nested value from JSON using dot notation
     * Example: getNestedValue("users.json", "admin.username")
     */
    public static String getNestedValue(String fileName, String path) {
        Map<String, Object> data = loadJsonDataAsMap(fileName);
        String[] keys = path.split("\\.");
        
        Object current = data;
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            } else {
                return null;
            }
        }
        
        return current != null ? current.toString() : null;
    }
    
    /**
     * Load test data from text file
     * @param fileName File name
     * @return File content as String
     */
    public static String loadTextData(String fileName) {
        try {
            String filePath = TEST_DATA_PATH + fileName;
            logger.info("Loading text data from: {}", filePath);
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            logger.error("Failed to load text data: {}", e.getMessage());
            throw new RuntimeException("Failed to load text data", e);
        }
    }
    
    /**
     * Save test data to JSON file (useful for test output/results)
     * @param fileName File name
     * @param data Object to serialize
     */
    public static void saveJsonData(String fileName, Object data) {
        try {
            String filePath = TEST_DATA_PATH + fileName;
            logger.info("Saving test data to: {}", filePath);
            
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            logger.info("Test data saved successfully");
        } catch (IOException e) {
            logger.error("Failed to save test data: {}", e.getMessage());
            throw new RuntimeException("Failed to save test data", e);
        }
    }
    
    /**
     * Generate random test data
     */
    public static class RandomData {
        private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        public static String randomString(int length) {
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = (int) (Math.random() * ALPHANUMERIC.length());
                sb.append(ALPHANUMERIC.charAt(index));
            }
            return sb.toString();
        }
        
        public static String randomEmail() {
            return "test_" + randomString(8) + "@example.com";
        }
        
        public static String randomUsername() {
            return "user_" + randomString(6);
        }
        
        public static int randomNumber(int min, int max) {
            return min + (int) (Math.random() * (max - min + 1));
        }
    }
}

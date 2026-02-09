package com.utils;

/**
 * Utility class for common String operations in tests
 * Provides string manipulation, validation, and formatting methods
 */
public class StringUtils {
    
    /**
     * Check if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null or empty
     */
    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }
    
    /**
     * Get default value if string is null or empty
     */
    public static String getOrDefault(String str, String defaultValue) {
        return isNullOrEmpty(str) ? defaultValue : str;
    }
    
    /**
     * Truncate string to max length
     */
    public static String truncate(String str, int maxLength) {
        if (isNullOrEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * Remove all whitespace from string
     */
    public static String removeWhitespace(String str) {
        return isNullOrEmpty(str) ? str : str.replaceAll("\\s+", "");
    }
    
    /**
     * Normalize whitespace (multiple spaces to single space)
     */
    public static String normalizeWhitespace(String str) {
        return isNullOrEmpty(str) ? str : str.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Convert string to camelCase
     */
    public static String toCamelCase(String str) {
        if (isNullOrEmpty(str)) return str;
        
        String[] words = str.split("[\\s_-]+");
        StringBuilder result = new StringBuilder(words[0].toLowerCase());
        
        for (int i = 1; i < words.length; i++) {
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
    
    /**
     * Convert string to snake_case
     */
    public static String toSnakeCase(String str) {
        if (isNullOrEmpty(str)) return str;
        
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                  .replaceAll("[\\s-]+", "_")
                  .toLowerCase();
    }
    
    /**
     * Convert string to kebab-case
     */
    public static String toKebabCase(String str) {
        if (isNullOrEmpty(str)) return str;
        
        return str.replaceAll("([a-z])([A-Z])", "$1-$2")
                  .replaceAll("[\\s_]+", "-")
                  .toLowerCase();
    }
    
    /**
     * Capitalize first letter
     */
    public static String capitalize(String str) {
        if (isNullOrEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Capitalize each word
     */
    public static String capitalizeWords(String str) {
        if (isNullOrEmpty(str)) return str;
        
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
    
    /**
     * Reverse string
     */
    public static String reverse(String str) {
        if (isNullOrEmpty(str)) return str;
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * Count occurrences of substring
     */
    public static int countOccurrences(String str, String substring) {
        if (isNullOrEmpty(str) || isNullOrEmpty(substring)) return 0;
        
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }
    
    /**
     * Check if string contains only digits
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) return false;
        return str.matches("\\d+");
    }
    
    /**
     * Check if string contains only alphabetic characters
     */
    public static boolean isAlphabetic(String str) {
        if (isNullOrEmpty(str)) return false;
        return str.matches("[a-zA-Z]+");
    }
    
    /**
     * Check if string is alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        if (isNullOrEmpty(str)) return false;
        return str.matches("[a-zA-Z0-9]+");
    }
    
    /**
     * Check if string is valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Check if string is valid URL format
     */
    public static boolean isValidUrl(String url) {
        if (isNullOrEmpty(url)) return false;
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return url.matches(urlRegex);
    }
    
    /**
     * Remove special characters, keep only alphanumeric
     */
    public static String removeSpecialCharacters(String str) {
        if (isNullOrEmpty(str)) return str;
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
    
    /**
     * Mask string (for sensitive data in logs)
     */
    public static String mask(String str) {
        return mask(str, 4, '*');
    }
    
    public static String mask(String str, int visibleChars, char maskChar) {
        if (isNullOrEmpty(str) || str.length() <= visibleChars) {
            return str;
        }
        
        String visible = str.substring(0, visibleChars);
        String masked = String.valueOf(maskChar).repeat(str.length() - visibleChars);
        return visible + masked;
    }
    
    /**
     * Extract numbers from string
     */
    public static String extractNumbers(String str) {
        if (isNullOrEmpty(str)) return "";
        return str.replaceAll("[^0-9]", "");
    }
    
    /**
     * Extract letters from string
     */
    public static String extractLetters(String str) {
        if (isNullOrEmpty(str)) return "";
        return str.replaceAll("[^a-zA-Z]", "");
    }
    
    /**
     * Repeat string n times
     */
    public static String repeat(String str, int times) {
        if (isNullOrEmpty(str) || times <= 0) return "";
        return str.repeat(times);
    }
    
    /**
     * Pad string to left with character
     */
    public static String padLeft(String str, int length, char padChar) {
        if (isNullOrEmpty(str)) str = "";
        if (str.length() >= length) return str;
        
        return String.valueOf(padChar).repeat(length - str.length()) + str;
    }
    
    /**
     * Pad string to right with character
     */
    public static String padRight(String str, int length, char padChar) {
        if (isNullOrEmpty(str)) str = "";
        if (str.length() >= length) return str;
        
        return str + String.valueOf(padChar).repeat(length - str.length());
    }
    
    /**
     * Compare strings ignoring case
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equalsIgnoreCase(str2);
    }
    
    /**
     * Generate slug from string (URL-friendly)
     */
    public static String toSlug(String str) {
        if (isNullOrEmpty(str)) return str;
        
        return str.toLowerCase()
                  .trim()
                  .replaceAll("[^a-z0-9\\s-]", "")
                  .replaceAll("\\s+", "-")
                  .replaceAll("-+", "-")
                  .replaceAll("^-|-$", "");
    }
}

package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating test data
 * Provides methods for creating random strings, numbers, emails, etc.
 */
public class DataGeneratorUtils {
    private static final Random random = new Random();
    
    // Character sets
    private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String ALPHANUMERIC = ALPHA_UPPER + ALPHA_LOWER + NUMERIC;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    
    /**
     * Generate random alphanumeric string
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate random alphabetic string
     */
    public static String randomAlphabetic(int length) {
        String alphabet = ALPHA_UPPER + ALPHA_LOWER;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate random numeric string
     */
    public static String randomNumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMERIC.charAt(random.nextInt(NUMERIC.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate random email address
     */
    public static String randomEmail() {
        return randomEmail("test");
    }
    
    public static String randomEmail(String prefix) {
        return String.format("%s_%s@example.com", prefix, randomString(8).toLowerCase());
    }
    
    /**
     * Generate random email with specific domain
     */
    public static String randomEmail(String prefix, String domain) {
        return String.format("%s_%s@%s", prefix, randomString(8).toLowerCase(), domain);
    }
    
    /**
     * Generate random username
     */
    public static String randomUsername() {
        return "user_" + randomString(8).toLowerCase();
    }
    
    /**
     * Generate random password with specific criteria
     */
    public static String randomPassword(int length, boolean includeSpecial) {
        String chars = ALPHANUMERIC + (includeSpecial ? SPECIAL_CHARS : "");
        StringBuilder password = new StringBuilder(length);
        
        // Ensure at least one uppercase, lowercase, and number
        password.append(ALPHA_UPPER.charAt(random.nextInt(ALPHA_UPPER.length())));
        password.append(ALPHA_LOWER.charAt(random.nextInt(ALPHA_LOWER.length())));
        password.append(NUMERIC.charAt(random.nextInt(NUMERIC.length())));
        
        if (includeSpecial) {
            password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        }
        
        // Fill remaining characters
        for (int i = password.length(); i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        // Shuffle the password
        return shuffleString(password.toString());
    }
    
    /**
     * Generate random integer between min and max (inclusive)
     */
    public static int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Generate random long between min and max
     */
    public static long randomLong(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min));
    }
    
    /**
     * Generate random double between min and max
     */
    public static double randomDouble(double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }
    
    /**
     * Generate random phone number
     */
    public static String randomPhoneNumber() {
        return String.format("+1-%s-%s-%s", 
            randomNumeric(3), 
            randomNumeric(3), 
            randomNumeric(4));
    }
    
    /**
     * Generate random US phone number
     */
    public static String randomUSPhoneNumber() {
        return String.format("(%s) %s-%s", 
            randomNumeric(3), 
            randomNumeric(3), 
            randomNumeric(4));
    }
    
    /**
     * Generate UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate current timestamp string
     */
    public static String currentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }
    
    /**
     * Generate timestamp with custom format
     */
    public static String currentTimestamp(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Generate random boolean
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }
    
    /**
     * Pick random element from array
     */
    public static <T> T randomElement(T[] array) {
        return array[random.nextInt(array.length)];
    }
    
    /**
     * Generate random first name
     */
    public static String randomFirstName() {
        String[] names = {"John", "Jane", "Michael", "Emily", "David", "Sarah", 
                         "Robert", "Lisa", "James", "Mary", "William", "Patricia"};
        return randomElement(names);
    }
    
    /**
     * Generate random last name
     */
    public static String randomLastName() {
        String[] names = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia",
                         "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez"};
        return randomElement(names);
    }
    
    /**
     * Generate random full name
     */
    public static String randomFullName() {
        return randomFirstName() + " " + randomLastName();
    }
    
    /**
     * Generate random company name
     */
    public static String randomCompanyName() {
        String[] adjectives = {"Global", "Advanced", "Dynamic", "Innovative", "Premier"};
        String[] nouns = {"Solutions", "Technologies", "Systems", "Enterprises", "Industries"};
        return randomElement(adjectives) + " " + randomElement(nouns);
    }
    
    /**
     * Generate random URL
     */
    public static String randomUrl() {
        return "https://" + randomString(8).toLowerCase() + ".com";
    }
    
    /**
     * Generate random color hex code
     */
    public static String randomHexColor() {
        return String.format("#%06x", random.nextInt(0xffffff + 1));
    }
    
    /**
     * Shuffle string characters
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}

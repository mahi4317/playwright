# Utils Package Documentation

## Overview
The `com.utils` package provides reusable utility classes to enhance test automation capabilities. These utilities abstract common operations, reduce code duplication, and improve test maintainability.

---

## 📦 Available Utilities

### 1. WaitUtils
**Purpose**: Centralized explicit wait operations for Playwright elements and pages.

**Key Methods**:
```java
// Element state waits
WaitUtils.waitForVisible(locator, timeout);
WaitUtils.waitForHidden(locator, timeout);
WaitUtils.waitForAttached(locator);
WaitUtils.waitForDetached(locator);

// Page state waits
WaitUtils.waitForPageLoad(page);
WaitUtils.waitForNetworkIdle(page);
WaitUtils.waitForUrlContains(page, "checkout", 5000);

// Text waits
WaitUtils.waitForText(locator, "Success", 10000);

// Element count waits
WaitUtils.waitForCount(locator, 5, 8000);

// Custom condition waits
WaitUtils.waitForCondition(() -> page.title().contains("Dashboard"), 15000);

// Simple sleep (use sparingly)
WaitUtils.sleep(2000);
```

**Usage Example**:
```java
public class CheckoutPage extends BasePage {
    public void completeCheckout() {
        clickElement(checkoutButton());
        WaitUtils.waitForUrlContains(page, "/order-confirmation", 10000);
        WaitUtils.waitForVisible(confirmationMessage(), 5000);
    }
}
```

---

### 2. ScreenshotUtils
**Purpose**: Screenshot capture with automatic naming, directory management, and failure tracking.

**Key Methods**:
```java
// Auto-generated filename with timestamp
ScreenshotUtils.takeScreenshot(page, "loginTest");
// → target/screenshots/loginTest_20241215_143522.png

// Full page screenshot
ScreenshotUtils.takeFullPageScreenshot(page, "homePage");

// Element-specific screenshot
ScreenshotUtils.takeElementScreenshot(locator, "addToCartButton");

// Custom filename
ScreenshotUtils.takeScreenshot(page, "custom-name.png", true); // fullPage=true

// Failure screenshot (with reason)
ScreenshotUtils.takeFailureScreenshot(page, "loginTest", "Invalid credentials");
// → target/screenshots/FAILED_loginTest_Invalid_credentials_20241215_143600.png
```

**Usage Example**:
```java
@AfterMethod
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        String path = ScreenshotUtils.takeFailureScreenshot(
            page, result.getName(), result.getThrowable().getMessage()
        );
        logger.error("Test failed - screenshot saved: {}", path);
    }
}
```

**Features**:
- Auto-creates `target/screenshots/` directory
- Timestamp-based naming to avoid overwriting
- Full page & element-specific captures
- Special naming for failure screenshots

---

### 3. DataGeneratorUtils
**Purpose**: Generate random test data for data-driven tests, avoiding hardcoded values.

**Key Methods**:
```java
// String generation
DataGeneratorUtils.randomString(10);              // → "aB3xK9mP2q"
DataGeneratorUtils.randomAlphabetic(8);           // → "AbCdEfGh"
DataGeneratorUtils.randomNumeric(6);              // → "847239"

// Email generation
DataGeneratorUtils.randomEmail();                 // → "test_aB3xK9mP@example.com"
DataGeneratorUtils.randomEmail("user", "qa.com"); // → "user_xyz12345@qa.com"

// Username & password
DataGeneratorUtils.randomUsername();              // → "user_xyz12345"
DataGeneratorUtils.randomPassword(12, true);      // → "A8b!Xz2@Kp9m" (with special chars)

// Numbers
DataGeneratorUtils.randomInt(1, 100);             // → 47
DataGeneratorUtils.randomDouble(0.0, 1.0);        // → 0.735289

// Phone numbers
DataGeneratorUtils.randomPhoneNumber();           // → "+1-847-239-5612"
DataGeneratorUtils.randomUSPhoneNumber();         // → "(847) 239-5612"

// UUIDs & timestamps
DataGeneratorUtils.randomUUID();                  // → "e4b7c8d1-2f3a-4b5c-6d7e-8f9a0b1c2d3e"
DataGeneratorUtils.currentTimestamp();            // → "20241215_143522"
DataGeneratorUtils.currentTimestamp("yyyy-MM-dd"); // → "2024-12-15"

// Names & companies
DataGeneratorUtils.randomFirstName();             // → "Emily"
DataGeneratorUtils.randomLastName();              // → "Johnson"
DataGeneratorUtils.randomFullName();              // → "Michael Garcia"
DataGeneratorUtils.randomCompanyName();           // → "Global Technologies"

// URLs & colors
DataGeneratorUtils.randomUrl();                   // → "https://xyz12345.com"
DataGeneratorUtils.randomHexColor();              // → "#3a7f9c"

// Utilities
DataGeneratorUtils.randomBoolean();               // → true/false
DataGeneratorUtils.randomElement(new String[]{"A", "B", "C"}); // → "B"
```

**Usage Example**:
```java
@Test
public void testUserRegistration() {
    String email = DataGeneratorUtils.randomEmail("user");
    String password = DataGeneratorUtils.randomPassword(12, true);
    String fullName = DataGeneratorUtils.randomFullName();
    
    registrationPage.open()
        .enterEmail(email)
        .enterPassword(password)
        .enterFullName(fullName)
        .clickRegister();
    
    AssertionUtils.assertUrlContains(page, "/dashboard");
}
```

---

### 4. StringUtils
**Purpose**: Common string operations for test data manipulation, validation, and formatting.

**Key Methods**:
```java
// Null/empty checks
StringUtils.isNullOrEmpty(str);                   // → true/false
StringUtils.isNotNullOrEmpty(str);                // → true/false
StringUtils.getOrDefault(str, "default");         // → str or "default"

// String manipulation
StringUtils.truncate("Long text here", 10);       // → "Long text ..."
StringUtils.removeWhitespace("a b c");            // → "abc"
StringUtils.normalizeWhitespace("a    b");        // → "a b"

// Case conversions
StringUtils.toCamelCase("user_name");             // → "userName"
StringUtils.toSnakeCase("userName");              // → "user_name"
StringUtils.toKebabCase("userName");              // → "user-name"
StringUtils.capitalize("hello");                  // → "Hello"
StringUtils.capitalizeWords("hello world");       // → "Hello World"

// Validation
StringUtils.isNumeric("12345");                   // → true
StringUtils.isAlphabetic("abc");                  // → true
StringUtils.isAlphanumeric("abc123");             // → true
StringUtils.isValidEmail("user@example.com");     // → true
StringUtils.isValidUrl("https://example.com");    // → true

// Extraction
StringUtils.extractNumbers("abc123xyz");          // → "123"
StringUtils.extractLetters("123abc456");          // → "abc"

// Masking (for sensitive data)
StringUtils.mask("password1234");                 // → "pass********"
StringUtils.mask("1234567890", 4, '*');           // → "1234******"

// Padding
StringUtils.padLeft("5", 3, '0');                 // → "005"
StringUtils.padRight("5", 3, '0');                // → "500"

// Utility
StringUtils.reverse("hello");                     // → "olleh"
StringUtils.countOccurrences("hello", "l");       // → 2
StringUtils.repeat("ha", 3);                      // → "hahaha"
StringUtils.removeSpecialCharacters("a@b#c");     // → "abc"

// URL slug generation
StringUtils.toSlug("Hello World!");               // → "hello-world"
```

**Usage Example**:
```java
@Test
public void testSearch() {
    String userInput = "  Product Name  ";
    String normalized = StringUtils.normalizeWhitespace(userInput);
    
    searchPage.search(normalized);
    
    String resultCount = searchPage.getResultCountText();
    String numberOnly = StringUtils.extractNumbers(resultCount); // "25 results" → "25"
    
    Assert.assertTrue(Integer.parseInt(numberOnly) > 0);
}
```

---

### 5. AssertionUtils
**Purpose**: Enhanced assertions with better logging and descriptive failure messages.

**Key Methods**:
```java
// Visibility assertions
AssertionUtils.assertVisible(locator, "Login Button");
AssertionUtils.assertNotVisible(locator, "Error Message");

// State assertions
AssertionUtils.assertEnabled(locator, "Submit Button");
AssertionUtils.assertDisabled(locator, "Submit Button");

// Text assertions
AssertionUtils.assertTextEquals(locator, "Welcome", "Header");
AssertionUtils.assertContainsText(locator, "Success", "Message");

// Attribute assertions
AssertionUtils.assertHasAttribute(locator, "disabled", "Submit Button");
AssertionUtils.assertAttributeEquals(locator, "type", "text", "Username Input");
AssertionUtils.assertHasClass(locator, "active", "Nav Link");

// Count assertions
AssertionUtils.assertCount(locator, 5, "Product Cards");

// Checkbox/Radio assertions
AssertionUtils.assertChecked(locator, "Terms Checkbox");
AssertionUtils.assertNotChecked(locator, "Newsletter Checkbox");

// URL & Title assertions
AssertionUtils.assertUrlEquals(page, "https://example.com/dashboard");
AssertionUtils.assertUrlContains(page, "/dashboard");
AssertionUtils.assertTitleEquals(page, "Dashboard");
AssertionUtils.assertTitleContains(page, "Dashboard");

// Input value assertions
AssertionUtils.assertInputValue(locator, "john@example.com", "Email Field");
AssertionUtils.assertEditable(locator, "Comment Box");

// Soft assertions (log but don't fail test)
AssertionUtils.softAssertTrue(condition, "Optional feature check");
AssertionUtils.softAssertVisible(locator, "Promotional Banner");
```

**Usage Example**:
```java
@Test
public void testLoginSuccess() {
    loginPage.open()
        .enterUsername("practice")
        .enterPassword("SuperSecretPassword!")
        .clickSignIn();
    
    // Enhanced assertions with logging
    AssertionUtils.assertUrlContains(page, "/secure");
    AssertionUtils.assertVisible(successMessage(), "Success Message");
    AssertionUtils.assertContainsText(successMessage(), "logged in", "Success Message");
    AssertionUtils.assertEnabled(logoutButton(), "Logout Button");
}
```

**Benefits**:
- Automatic logging of assertion attempts
- Descriptive failure messages with context
- Clear pass/fail status in logs
- Soft assertion support for non-critical checks

---

## 🎯 Integration with Framework

### Using in Page Objects
```java
public class LoginPage extends BasePage {
    private Locator usernameField() {
        return page.getByLabel("Username");
    }
    
    private Locator successMessage() {
        return page.locator(".alert-success");
    }
    
    public LoginPage enterUsername(String username) {
        WaitUtils.waitForVisible(usernameField(), 5000);  // Utils usage
        usernameField().fill(username);
        return this;
    }
    
    public void verifyLoginSuccess() {
        WaitUtils.waitForUrlContains(page, "/secure", 10000);
        AssertionUtils.assertVisible(successMessage(), "Success Message");
        
        String screenshotPath = ScreenshotUtils.takeScreenshot(page, "login-success");
        logger.info("Screenshot saved: {}", screenshotPath);
    }
}
```

### Using in Tests
```java
public class RegistrationTest extends BaseTest {
    @Test
    public void testNewUserRegistration() {
        // Generate test data
        String email = DataGeneratorUtils.randomEmail("newuser");
        String password = DataGeneratorUtils.randomPassword(12, true);
        String firstName = DataGeneratorUtils.randomFirstName();
        String lastName = DataGeneratorUtils.randomLastName();
        
        logger.info("Generated credentials: {} / {}", email, StringUtils.mask(password));
        
        // Perform registration
        registrationPage.open()
            .enterEmail(email)
            .enterPassword(password)
            .enterFirstName(firstName)
            .enterLastName(lastName)
            .clickRegister();
        
        // Verify with enhanced assertions
        AssertionUtils.assertUrlContains(page, "/welcome");
        AssertionUtils.assertVisible(welcomeMessage(), "Welcome Message");
        
        // Take success screenshot
        ScreenshotUtils.takeScreenshot(page, "registration-success");
    }
}
```

### Using in BaseTest
```java
public class BaseTest {
    @AfterMethod
    public void tearDownTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture failure screenshot
            ScreenshotUtils.takeFailureScreenshot(
                page, 
                result.getName(), 
                result.getThrowable().getMessage()
            );
            
            // Keep browser open for debugging
            logger.error("Test FAILED: {}", result.getName());
            return;
        }
        
        // Close page on success
        if (page != null) {
            page.close();
        }
    }
}
```

---

## 📋 Best Practices

### 1. **Prefer Utils Over Duplicated Code**
```java
// ❌ BAD - Duplicated wait logic
page.waitForSelector("#element", new Page.WaitForSelectorOptions().setTimeout(5000));

// ✅ GOOD - Centralized utility
WaitUtils.waitForVisible(page.locator("#element"), 5000);
```

### 2. **Use Descriptive Element Names in Assertions**
```java
// ❌ BAD
AssertionUtils.assertVisible(locator, "element");

// ✅ GOOD
AssertionUtils.assertVisible(loginButton(), "Login Button");
```

### 3. **Generate Random Data for Unique Tests**
```java
// ❌ BAD - Same email causes conflicts in repeated runs
String email = "testuser@example.com";

// ✅ GOOD - Unique email each run
String email = DataGeneratorUtils.randomEmail("testuser");
```

### 4. **Take Screenshots at Key Points**
```java
// After critical actions
loginPage.clickSignIn();
ScreenshotUtils.takeScreenshot(page, "after-login");

// On verification failures (automatic in @AfterMethod)
```

### 5. **Log Masked Sensitive Data**
```java
String password = DataGeneratorUtils.randomPassword(12, true);
logger.info("Using password: {}", StringUtils.mask(password)); // → "Pass********"
```

---

## 🔄 Updating Documentation
When adding new utilities:
1. Create the utility class in `src/main/java/com/utils/`
2. Add documentation to this file
3. Update `FRAMEWORK_ARCHITECTURE.md` with utils layer details
4. Add usage examples in relevant page objects or tests

---

## 📊 Summary Table

| Utility | Primary Use Case | Common Methods |
|---------|------------------|----------------|
| **WaitUtils** | Explicit waits & synchronization | `waitForVisible()`, `waitForText()`, `waitForCondition()` |
| **ScreenshotUtils** | Visual debugging & failure tracking | `takeScreenshot()`, `takeFailureScreenshot()` |
| **DataGeneratorUtils** | Random test data generation | `randomEmail()`, `randomPassword()`, `randomFullName()` |
| **StringUtils** | String manipulation & validation | `normalize()`, `mask()`, `isValidEmail()`, `toSlug()` |
| **AssertionUtils** | Enhanced assertions with logging | `assertVisible()`, `assertTextEquals()`, `assertUrlContains()` |

---

**Last Updated**: December 2024  
**Framework Version**: 1.0  
**Playwright Version**: 1.48.0

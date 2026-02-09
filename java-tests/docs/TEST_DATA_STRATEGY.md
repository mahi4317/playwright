# Test Data Strategy Guide

## Overview
The framework provides **two complementary approaches** for test data management:

1. **`com.testdata` package** - Pre-defined, structured test data (JSON, Excel, Properties)
2. **`com.utils.DataGeneratorUtils`** - Dynamic, random test data generation

Both approaches serve different purposes and can be used **together** in the same test.

---

## 🎯 When to Use Each Approach

### Use `com.testdata` (Structured Data)

**Scenarios:**
- ✅ **Data-driven testing** - Running same test with multiple datasets
- ✅ **Known edge cases** - Boundary values, special characters, specific formats
- ✅ **Regression testing** - Consistent data across test runs
- ✅ **Cross-environment testing** - Different credentials per environment
- ✅ **Complex scenarios** - Multi-step workflows requiring specific data relationships

**Examples:**
```java
// Login with predefined credentials from JSON
LoginUser user = TestDataManager.getLoginUser("admin");
loginPage.enterUsername(user.getUsername())
         .enterPassword(user.getPassword());

// Test with multiple datasets from Excel
@Test(dataProvider = "loginData", dataProviderClass = ExcelDataProvider.class)
public void testLoginVariations(String username, String password, String expected) {
    // Test runs multiple times with different data
}

// Environment-specific config
String apiUrl = ConfigManager.getProperty("api.url");
String dbHost = ConfigManager.getProperty("db.host");
```

---

### Use `DataGeneratorUtils` (Random Data)

**Scenarios:**
- ✅ **Unique data required** - User registration, creating records
- ✅ **Avoiding data conflicts** - No duplicate emails, usernames
- ✅ **Performance testing** - Generate thousands of unique records
- ✅ **Exploratory testing** - Unpredictable input patterns
- ✅ **Privacy compliance** - No real user data in tests

**Examples:**
```java
// Generate unique user for registration
String email = DataGeneratorUtils.randomEmail("testuser");
String password = DataGeneratorUtils.randomPassword(12, true);
String fullName = DataGeneratorUtils.randomFullName();

// Each test run creates unique data - no conflicts
registrationPage.enterEmail(email)  // → testuser_aB3xK9mP@example.com
                .enterPassword(password)
                .enterFullName(fullName);
```

---

## 🔄 Combined Usage Patterns

### Pattern 1: Base Data + Random Variations

Use **structured data** for test flow, **random data** for uniqueness:

```java
@Test
public void testNewProductCreation() {
    // Get structured product template from JSON
    Map<String, Object> productTemplate = TestDataManager.loadJsonDataAsMap(
        "src/test/resources/testdata/products.json", 
        "templates.basicProduct"
    );
    
    // Add random unique identifiers
    String productName = productTemplate.get("name") + "_" + DataGeneratorUtils.randomString(6);
    String sku = "SKU-" + DataGeneratorUtils.randomNumeric(8);
    Double price = DataGeneratorUtils.randomDouble(10.0, 100.0);
    
    // Create product with structured template + random values
    productPage.createProduct()
        .setName(productName)
        .setSku(sku)
        .setCategory((String) productTemplate.get("category"))
        .setPrice(price)
        .save();
    
    AssertionUtils.assertVisible(successMessage(), "Success Message");
}
```

**Breakdown:**
- **JSON template** provides: Category, description, base name structure
- **Random generators** provide: Unique SKU, unique name suffix, price variation

---

### Pattern 2: Environment Config + Random User

Use **properties** for environment settings, **random data** for user creation:

```java
@Test
public void testUserRegistrationFlow() {
    // Get environment-specific base URL from properties
    String baseUrl = ConfigManager.getBaseUrl();
    
    // Generate unique user credentials
    String firstName = DataGeneratorUtils.randomFirstName();
    String lastName = DataGeneratorUtils.randomLastName();
    String email = DataGeneratorUtils.randomEmail(firstName.toLowerCase());
    String password = DataGeneratorUtils.randomPassword(12, true);
    String phone = DataGeneratorUtils.randomUSPhoneNumber();
    
    logger.info("Registering user: {} {} ({})", firstName, lastName, email);
    
    // Navigate using config, register with random data
    page.navigate(baseUrl + "/register");
    
    registrationPage.enterFirstName(firstName)
                    .enterLastName(lastName)
                    .enterEmail(email)
                    .enterPassword(password)
                    .enterPhone(phone)
                    .acceptTerms()
                    .submit();
    
    AssertionUtils.assertUrlContains(page, "/welcome");
}
```

**Breakdown:**
- **ConfigManager** provides: Base URL, timeouts, environment-specific settings
- **DataGeneratorUtils** provides: Unique user data that won't conflict with existing users

---

### Pattern 3: Excel Datasets + Random Variations

Use **Excel DataProvider** for test variations, **random data** for unique fields:

```java
@Test(dataProvider = "checkoutScenarios", dataProviderClass = ExcelDataProvider.class)
public void testCheckoutWithVariations(String paymentMethod, String shippingSpeed, String promoCode) {
    // Excel provides: Payment methods, shipping options, promo codes
    
    // Generate random customer data
    String customerEmail = DataGeneratorUtils.randomEmail("customer");
    String cardNumber = "4532" + DataGeneratorUtils.randomNumeric(12); // Test card
    String cvv = DataGeneratorUtils.randomNumeric(3);
    
    // Add random products
    int quantity = DataGeneratorUtils.randomInt(1, 5);
    
    // Execute checkout with Excel scenario + random customer
    checkoutPage.addProductsToCart(quantity)
                .proceedToCheckout()
                .enterEmail(customerEmail)
                .selectPaymentMethod(paymentMethod)     // From Excel
                .enterCardDetails(cardNumber, cvv)      // Random
                .selectShipping(shippingSpeed)          // From Excel
                .applyPromoCode(promoCode)              // From Excel
                .placeOrder();
    
    AssertionUtils.assertVisible(orderConfirmation(), "Order Confirmation");
}
```

**Excel File (`testdata.xlsx`):**
```
| paymentMethod | shippingSpeed | promoCode   |
|---------------|---------------|-------------|
| CreditCard    | Standard      | SAVE10      |
| PayPal        | Express       | FREESHIP    |
| DebitCard     | Overnight     | NONE        |
```

**Breakdown:**
- **Excel** provides: Business scenarios (payment/shipping combinations)
- **Random generators** provide: Unique customer data per test run

---

### Pattern 4: JSON Model + Random Field Values

Use **JSON data models**, but override specific fields with random data:

```java
@Test
public void testUserProfileUpdate() {
    // Load predefined user structure from JSON
    LoginUser baseUser = TestDataManager.getLoginUser("standardUser");
    
    // Login with predefined credentials
    loginPage.open()
             .enterUsername(baseUser.getUsername())
             .enterPassword(baseUser.getPassword())
             .clickSignIn();
    
    // Generate random profile updates
    String newBio = "Bio_" + DataGeneratorUtils.randomString(20);
    String newPhone = DataGeneratorUtils.randomUSPhoneNumber();
    String newCompany = DataGeneratorUtils.randomCompanyName();
    
    // Update profile with random data
    profilePage.open()
               .updateBio(newBio)
               .updatePhone(newPhone)
               .updateCompany(newCompany)
               .save();
    
    AssertionUtils.assertContainsText(successMessage(), "Profile updated", "Success Message");
    
    // Verify random data was saved
    AssertionUtils.assertTextEquals(bioField(), newBio, "Bio Field");
}
```

**Breakdown:**
- **JSON LoginUser** provides: Valid login credentials (consistent)
- **Random generators** provide: Profile update values (unique per run)

---

## 📊 Decision Matrix

| Scenario | Use testdata Package | Use DataGeneratorUtils | Both |
|----------|---------------------|------------------------|------|
| **User registration** | ❌ | ✅ (unique emails) | |
| **Login with known credentials** | ✅ (JSON/Properties) | ❌ | |
| **Data-driven testing (DDT)** | ✅ (Excel/JSON) | ❌ | |
| **Creating new records** | | | ✅ (template + random) |
| **Edge case testing** | ✅ (predefined edge cases) | ❌ | |
| **Performance testing** | ❌ | ✅ (mass generation) | |
| **Environment switching** | ✅ (Properties) | ❌ | |
| **API testing with payloads** | | | ✅ (base JSON + random IDs) |
| **Cross-browser testing** | ✅ (same data, different browsers) | ❌ | |
| **Exploratory testing** | ❌ | ✅ (random inputs) | |

---

## 🛠️ Real-World Test Examples

### Example 1: E-commerce Order Flow

```java
@Test
public void testCompleteOrderFlow() {
    // 1. LOGIN - Use predefined credentials
    LoginUser customer = TestDataManager.getLoginUser("customer");
    loginPage.open()
             .loginAs(customer.getUsername(), customer.getPassword());
    
    // 2. PRODUCT SEARCH - Use random search term
    String searchTerm = DataGeneratorUtils.randomElement(new String[]{
        "laptop", "phone", "tablet", "headphones"
    });
    
    homePage.search(searchTerm);
    WaitUtils.waitForVisible(productList(), 5000);
    
    // 3. ADD TO CART - Random quantity
    int quantity = DataGeneratorUtils.randomInt(1, 3);
    productPage.selectFirstProduct()
               .setQuantity(quantity)
               .addToCart();
    
    // 4. CHECKOUT - Random shipping address
    String address = DataGeneratorUtils.randomString(10) + " Street";
    String city = DataGeneratorUtils.randomElement(new String[]{"NYC", "LA", "Chicago"});
    String zipCode = DataGeneratorUtils.randomNumeric(5);
    
    checkoutPage.proceedToCheckout()
                .enterShippingAddress(address, city, zipCode)
                .selectPaymentMethod("CreditCard")  // From config
                .placeOrder();
    
    AssertionUtils.assertUrlContains(page, "/order-confirmation");
    
    // Screenshot for evidence
    ScreenshotUtils.takeScreenshot(page, "order-confirmation");
}
```

**Data Sources:**
- ✅ **JSON** - Customer credentials
- ✅ **Random** - Search term, quantity, shipping address
- ✅ **Config** - Payment method options

---

### Example 2: Data-Driven Login Testing

```java
@Test(dataProvider = "loginData", dataProviderClass = ExcelDataProvider.class)
public void testLoginScenarios(String username, String password, String expectedResult) {
    logger.info("Testing login: {} / {} - Expecting: {}", username, 
                StringUtils.mask(password), expectedResult);
    
    loginPage.open()
             .enterUsername(username)     // From Excel
             .enterPassword(password)     // From Excel
             .clickSignIn();
    
    if (expectedResult.equals("SUCCESS")) {
        AssertionUtils.assertUrlContains(page, "/dashboard");
        AssertionUtils.assertVisible(logoutButton(), "Logout Button");
    } else {
        AssertionUtils.assertVisible(errorMessage(), "Error Message");
        AssertionUtils.assertContainsText(errorMessage(), expectedResult, "Error Message");
    }
}
```

**Excel Data (`testdata.xlsx` - LoginData sheet):**
```
| username        | password           | expectedResult              |
|-----------------|--------------------|-----------------------------|
| validuser       | ValidPass123!      | SUCCESS                     |
| invaliduser     | WrongPass123       | Invalid credentials         |
| admin@test.com  | Admin123!          | SUCCESS                     |
| user@test.com   | short              | Password too short          |
| test            |                    | Password is required        |
```

**Data Source:**
- ✅ **Excel** - All test variations with expected outcomes

---

### Example 3: Hybrid Approach - User Creation with Templates

```java
@Test
public void testBulkUserCreation() {
    // Load user template from JSON (role, permissions, settings)
    Map<String, Object> userTemplate = TestDataManager.loadJsonDataAsMap(
        "src/test/resources/testdata/users.json",
        "templates.standardUser"
    );
    
    int usersToCreate = 5;
    List<String> createdEmails = new ArrayList<>();
    
    for (int i = 0; i < usersToCreate; i++) {
        // Generate unique credentials
        String firstName = DataGeneratorUtils.randomFirstName();
        String lastName = DataGeneratorUtils.randomLastName();
        String email = DataGeneratorUtils.randomEmail(
            firstName.toLowerCase() + "." + lastName.toLowerCase()
        );
        String password = DataGeneratorUtils.randomPassword(12, true);
        
        logger.info("Creating user {}/{}: {}", i+1, usersToCreate, email);
        
        // Create user with template settings + random credentials
        adminPage.navigateToUserManagement()
                 .clickCreateUser()
                 .enterFirstName(firstName)
                 .enterLastName(lastName)
                 .enterEmail(email)
                 .enterPassword(password)
                 .selectRole((String) userTemplate.get("role"))           // From JSON
                 .setPermissions((List) userTemplate.get("permissions"))  // From JSON
                 .save();
        
        createdEmails.add(email);
        WaitUtils.waitForVisible(successMessage(), 3000);
    }
    
    // Verify all users created
    AssertionUtils.assertCount(userRows(), usersToCreate, "Created User Rows");
    
    logger.info("Successfully created {} users: {}", usersToCreate, createdEmails);
}
```

**JSON Template (`users.json`):**
```json
{
  "templates": {
    "standardUser": {
      "role": "User",
      "permissions": ["read", "write"],
      "emailNotifications": true,
      "twoFactorAuth": false
    }
  }
}
```

**Data Sources:**
- ✅ **JSON** - User role, permissions, settings (consistent)
- ✅ **Random** - Names, emails, passwords (unique)

---

## 🎨 Best Practices

### 1. **Use Structured Data for Repeatability**
```java
// ✅ GOOD - Same data every run (regression testing)
LoginUser admin = TestDataManager.getLoginUser("admin");
loginPage.loginAs(admin.getUsername(), admin.getPassword());
```

### 2. **Use Random Data for Uniqueness**
```java
// ✅ GOOD - Different email each run (no conflicts)
String email = DataGeneratorUtils.randomEmail("newuser");
registrationPage.register(email, "Password123!");
```

### 3. **Combine for Complex Scenarios**
```java
// ✅ BEST - Structured base + random variations
Map<String, Object> orderTemplate = TestDataManager.loadJsonDataAsMap(...);
String customerId = "CUST-" + DataGeneratorUtils.randomNumeric(8);
Double orderAmount = DataGeneratorUtils.randomDouble(10.0, 1000.0);

createOrder(orderTemplate, customerId, orderAmount);
```

### 4. **Log Data Decisions**
```java
// ✅ GOOD - Clear logging of data sources
logger.info("Using predefined credentials from JSON: {}", user.getUsername());
logger.info("Generated random email: {}", email);
logger.info("Using config base URL: {}", ConfigManager.getBaseUrl());
```

### 5. **Mask Sensitive Random Data**
```java
// ✅ GOOD - Mask passwords in logs
String password = DataGeneratorUtils.randomPassword(12, true);
logger.info("Generated password: {}", StringUtils.mask(password));
```

---

## 📁 Package Responsibilities

### `com.testdata` Package
```
testdata/
├── TestDataManager.java           # JSON/Properties loader
├── ExcelDataProvider.java         # TestNG DataProvider
└── models/
    ├── LoginUser.java              # Data models
    └── WebInputData.java
```

**Responsibilities:**
- Load structured data from files (JSON, Excel, Properties)
- Provide data models for type safety
- Support data-driven testing with TestNG DataProviders
- Manage environment-specific configurations

---

### `com.utils.DataGeneratorUtils` Class
```
utils/
└── DataGeneratorUtils.java         # Random data generation
```

**Responsibilities:**
- Generate random strings, numbers, emails, UUIDs
- Create unique test data on-the-fly
- Avoid hardcoded values in tests
- Support exploratory and performance testing

---

## 🔍 Quick Reference

### Load Structured Data
```java
// JSON model
LoginUser user = TestDataManager.getLoginUser("admin");

// Nested JSON map
Map<String, Object> data = TestDataManager.loadJsonDataAsMap(path, "user.profile");

// Properties file
String apiUrl = ConfigManager.getProperty("api.url");

// Excel DataProvider
@Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
```

### Generate Random Data
```java
// Basic generation
String email = DataGeneratorUtils.randomEmail();
String password = DataGeneratorUtils.randomPassword(12, true);
String name = DataGeneratorUtils.randomFullName();

// Numeric
int quantity = DataGeneratorUtils.randomInt(1, 10);
String phone = DataGeneratorUtils.randomPhoneNumber();

// Unique identifiers
String uuid = DataGeneratorUtils.randomUUID();
String timestamp = DataGeneratorUtils.currentTimestamp();
```

---

## Summary

| Aspect | testdata Package | DataGeneratorUtils |
|--------|------------------|-------------------|
| **Purpose** | Predefined, structured test data | Dynamic, random data generation |
| **Use Case** | Regression, DDT, known scenarios | Unique records, exploratory testing |
| **Data Format** | JSON, Excel, Properties | Programmatic generation |
| **Consistency** | Same data across runs | Different data each run |
| **Best For** | Login credentials, edge cases, config | Registration, new records, stress tests |
| **Example** | Admin user from JSON | Random email for new user |

**Golden Rule:**  
Use **structured data** when you need **consistency**.  
Use **random data** when you need **uniqueness**.  
Use **both** for **complex scenarios** requiring templates with variations.

---

**Last Updated**: November 2024  
**Framework Version**: 1.0  
**Related Docs**: [TEST_DATA_MANAGEMENT.md](TEST_DATA_MANAGEMENT.md), [UTILS_PACKAGE.md](UTILS_PACKAGE.md)

# Playwright Java Framework - Architecture & Execution Flow

## 📐 Framework Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    PLAYWRIGHT JAVA FRAMEWORK                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   TestNG     │  │   Logging    │  │  Reporting   │          │
│  │  Orchestrator│  │   (SLF4J)    │  │   (HTML)     │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│                                                                   │
├─────────────────────────────────────────────────────────────────┤
│                        TEST LAYER                                │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  tests/                                                   │   │
│  │  ├── LoginTest.java           (Business Logic)          │   │
│  │  ├── WebInputTest.java        (Test Scenarios)          │   │
│  │  ├── LoginTestWithData.java   (Data-Driven Tests)       │   │
│  │  └── ...                                                 │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              ↓                                    │
├─────────────────────────────────────────────────────────────────┤
│                       PAGE OBJECT LAYER                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  pages/                                                   │   │
│  │  ├── BasePage.java            (Common Actions)          │   │
│  │  ├── LoginPage.java           (Login Actions)           │   │
│  │  ├── WebPageInput.java        (Input Actions)           │   │
│  │  └── ...                                                 │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              ↓                                    │
├─────────────────────────────────────────────────────────────────┤
│                    INFRASTRUCTURE LAYER                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  base/                                                    │   │
│  │  ├── BaseTest.java            (Test Lifecycle)          │   │
│  │  ├── BrowserContextManager    (Browser Management)      │   │
│  │  └── LoggingListener          (Event Logging)           │   │
│  │                                                           │   │
│  │  config/                                                  │   │
│  │  └── ConfigManager.java       (Configuration)           │   │
│  │                                                           │   │
│  │  testdata/                                                │   │
│  │  ├── TestDataManager          (Data Loading)            │   │
│  │  └── ExcelDataProvider        (Excel Support)           │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              ↓                                    │
├─────────────────────────────────────────────────────────────────┤
│                      PLAYWRIGHT API                              │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Browser → Context → Page → Locators → Actions          │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              ↓                                    │
├─────────────────────────────────────────────────────────────────┤
│              BROWSERS (Chromium, Firefox, WebKit)                │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Complete Execution Flow

### 1. Test Suite Initialization (@BeforeSuite)

```
START TEST SUITE
    │
    ├─► BaseTest.setupSuite()
    │   ├─► LoggingListener logs "Suite Started"
    │   ├─► BrowserContextManager.initializeBrowserContext()
    │   │   ├─► ConfigManager.getBrowser() → "chromium"
    │   │   ├─► Playwright.create()
    │   │   ├─► Browser.launch(headless=false)
    │   │   └─► Returns Browser instance
    │   └─► Browser is now ready (singleton)
    │
    └─► Suite setup complete
```

**Key Classes Involved:**
- `BaseTest.java` - Defines suite lifecycle
- `BrowserContextManager.java` - Creates browser instance
- `ConfigManager.java` - Loads config from `dev.properties`
- `LoggingListener.java` - Logs suite events

---

### 2. Individual Test Execution (@BeforeMethod → @Test → @AfterMethod)

```
FOR EACH TEST METHOD:
    │
    ├─► @BeforeMethod setupTest()
    │   ├─► BrowserContextManager.getNewPage()
    │   │   ├─► Creates new BrowserContext
    │   │   ├─► Creates new Page in context
    │   │   └─► Returns fresh Page instance
    │   │
    │   └─► Initialize Page Object (e.g., loginPage = new LoginPage(page))
    │
    ├─► @Test testMethod()
    │   │
    │   ├─► Load Test Data (if needed)
    │   │   ├─► TestDataManager.loadJsonDataAsMap("users.json")
    │   │   ├─► OR ConfigManager.getUserName()
    │   │   ├─► OR TestDataManager.RandomData.randomEmail()
    │   │   └─► OR ExcelDataProvider.readExcelData()
    │   │
    │   ├─► Execute Page Object Actions
    │   │   ├─► loginPage.open()
    │   │   │   └─► BasePage.navigateTo(url)
    │   │   │       └─► page.navigate(url)
    │   │   │
    │   │   ├─► loginPage.enterUsername(username)
    │   │   │   ├─► usernameField() finds locator
    │   │   │   └─► locator.fill(username)
    │   │   │
    │   │   ├─► loginPage.enterPassword(password)
    │   │   └─► loginPage.clickSignIn()
    │   │
    │   ├─► Perform Assertions
    │   │   └─► Assert.assertTrue(page.url().contains("/secure"))
    │   │
    │   └─► Log Results
    │       └─► logger.info("Test passed")
    │
    └─► @AfterMethod tearDownTest(ITestResult result)
        │
        ├─► Check Test Result Status
        │   │
        │   ├─► IF PASSED:
        │   │   ├─► Close page: page.close()
        │   │   ├─► Close context: context.close()
        │   │   └─► Log: "Test passed"
        │   │
        │   └─► IF FAILED:
        │       ├─► Log error with stack trace
        │       ├─► Keep browser OPEN for debugging
        │       └─► Log: "Keeping page open for inspection"
        │
        └─► Method cleanup complete
```

**Key Classes Involved:**
- Test classes (e.g., `LoginTest.java`)
- Page Objects (e.g., `LoginPage.java`)
- `TestDataManager.java` - Loads test data
- `BaseTest.java` - Manages test lifecycle

---

### 3. Test Suite Teardown (@AfterSuite)

```
END TEST SUITE
    │
    ├─► BaseTest.tearDownSuite()
    │   ├─► LoggingListener logs "Suite Finished"
    │   ├─► Close Browser (if still open)
    │   │   └─► BrowserContextManager.closeBrowser()
    │   └─► Generate Reports
    │       ├─► TestNG HTML Report
    │       ├─► Surefire XML Reports
    │       └─► Execution Logs
    │
    └─► Suite teardown complete
```

---

## 📂 Framework Components Deep Dive

### Layer 1: Test Layer (`src/test/java/tests/`)

**Purpose:** Contains test scenarios and business logic

**Components:**
- **BaseTest.java** - Parent class for all tests
  - Provides logger instance
  - Manages suite lifecycle (@BeforeSuite, @AfterSuite)
  - Manages test lifecycle (@BeforeMethod, @AfterMethod)
  
- **Test Classes** (e.g., LoginTest.java)
  - Extend BaseTest
  - Focus on WHAT to test (not HOW)
  - Use Page Objects for interactions
  - Perform assertions

**Example Flow:**
```java
public class LoginTest extends BaseTest {
    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();  // Get fresh page
        loginPage = new LoginPage(page);             // Initialize page object
    }
    
    @Test
    public void testValidLogin() {
        loginPage.open()                             // Navigate
                .enterUsername("practice")           // Interact
                .enterPassword("password")
                .clickSignIn();
        Assert.assertTrue(loginPage.isLoggedIn());   // Verify
    }
}
```

---

### Layer 2: Page Object Layer (`src/main/java/com/pages/`)

**Purpose:** Encapsulates UI interactions and locators

**Components:**
- **BasePage.java** - Common utilities for all pages
  - `navigateTo(url)` - Navigation
  - `waitForSelector()` - Explicit waits
  - `clickElement()`, `fillInput()` - Basic actions
  - `takeScreenshot()` - Screenshot capture

- **Page Classes** (e.g., LoginPage.java)
  - Extend BasePage
  - Define locators (lazy evaluation)
  - Provide action methods
  - Use Fluent API (return `this`)

**Locator Strategy Priority:**
```java
// 1. Accessible labels (most resilient)
page.getByLabel("Username")

// 2. Placeholders
page.getByPlaceholder("Search...")

// 3. ARIA roles + names
page.getByRole(AriaRole.BUTTON, options.setName("Login"))

// 4. CSS selectors (last resort)
page.locator("#username")
```

**Example Page Object:**
```java
public class LoginPage extends BasePage {
    // Lazy locators (evaluated when called)
    private Locator usernameField() {
        return page.getByLabel("Username");
    }
    
    // Fluent action methods
    public LoginPage enterUsername(String username) {
        usernameField().fill(username);
        return this;  // Enable chaining
    }
}
```

---

### Layer 3: Infrastructure Layer

#### A. Browser Management (`base/BrowserContextManager.java`)

**Purpose:** Singleton pattern for browser lifecycle

```
Browser Lifecycle:
    │
    ├─► Suite starts
    │   └─► initializeBrowserContext()
    │       ├─► Create Playwright instance (once)
    │       ├─► Launch Browser (once)
    │       └─► Store as singleton
    │
    ├─► Each test
    │   └─► getNewPage()
    │       ├─► Create new BrowserContext
    │       ├─► Create new Page
    │       └─► Return isolated page
    │
    └─► Suite ends
        └─► closeBrowser()
            ├─► Close all contexts
            └─► Close browser
```

**Why Singleton?**
- Browser launch is slow (~2-3 seconds)
- Reusing browser saves time
- Each test gets fresh context/page (isolation)

---

#### B. Configuration Management (`config/ConfigManager.java`)

**Purpose:** Centralized configuration loading

```
Configuration Flow:
    │
    ├─► Static initialization
    │   └─► loadConfiguration()
    │       ├─► Read env parameter (-Denv=qa)
    │       ├─► Default to "dev"
    │       ├─► Load config/dev.properties
    │       └─► Store in Properties object
    │
    └─► Runtime access
        ├─► getBaseUrl() → "https://practice.expandtesting.com/"
        ├─► getBrowser() → "chromium"
        ├─► getTimeout() → 30000
        └─► getUserName() → "practice"
```

**Environment Switching:**
```bash
mvn test                    # Uses dev.properties
mvn test -Denv=qa          # Uses qa.properties
mvn test -Denv=prod        # Uses prod.properties
```

---

#### C. Test Data Management (`testdata/`)

**Purpose:** Separate test data from test logic

**Components:**

1. **TestDataManager.java**
   ```
   Data Loading Flow:
       │
       ├─► JSON Files
       │   ├─► loadJsonData("users.json", LoginUser.class)
       │   ├─► loadJsonDataAsMap("users.json")
       │   └─► getNestedValue("users.json", "loginUsers.validUser.username")
       │
       ├─► Random Generation
       │   ├─► RandomData.randomEmail()
       │   ├─► RandomData.randomUsername()
       │   └─► RandomData.randomString(12)
       │
       └─► File Operations
           ├─► loadTextData("template.txt")
           └─► saveJsonData("results.json", data)
   ```

2. **ExcelDataProvider.java**
   ```
   Excel Data Flow:
       │
       ├─► @Test(dataProvider = "excelData")
       │
       ├─► Read Excel file
       │   ├─► Open workbook (Apache POI)
       │   ├─► Select sheet
       │   ├─► Skip header row
       │   └─► Read data rows
       │
       └─► Return Object[][] for TestNG
   ```

---

#### D. Logging System (`logging/LogHelper.java` + `logback.xml`)

**Purpose:** Comprehensive test execution logging

```
Logging Flow:
    │
    ├─► Application Code
    │   └─► logger.info("Test started")
    │
    ├─► SLF4J API (abstraction)
    │   └─► Routes to implementation
    │
    └─► Logback Implementation
        ├─► Console Appender
        │   └─► Prints to terminal (real-time)
        │
        └─► Rolling File Appender
            ├─► Write to target/logs/test-execution.log
            ├─► Rotate daily
            └─► Keep 7 days history
```

**Log Levels:**
- `TRACE` - Very detailed debugging
- `DEBUG` - Detailed information (framework internals)
- `INFO` - General information (test progress)
- `WARN` - Warning messages
- `ERROR` - Error messages + stack traces

---

#### E. TestNG Listener (`base/LoggingListener.java`)

**Purpose:** Automatic test lifecycle event logging

```
Listener Event Flow:
    │
    ├─► onStart(ISuite)
    │   └─► Log: "Starting suite: {name}"
    │
    ├─► onTestStart(ITestResult)
    │   └─► Log: "Starting test: {name}"
    │
    ├─► onTestSuccess(ITestResult)
    │   └─► Log: "Test PASSED: {name} - Duration: {time}ms"
    │
    ├─► onTestFailure(ITestResult)
    │   ├─► Log: "Test FAILED: {name}"
    │   └─► Log: Stack trace
    │
    ├─► onTestSkipped(ITestResult)
    │   └─► Log: "Test SKIPPED: {name}"
    │
    └─► onFinish(ISuite)
        └─► Log: "Suite finished: {name}"
```

**Registered in testng.xml:**
```xml
<listeners>
    <listener class-name="base.LoggingListener"/>
</listeners>
```

---

## 🔄 Data Flow Example: Complete Login Test

```
┌─────────────────────────────────────────────────────────────┐
│ 1. TEST INITIALIZATION                                      │
├─────────────────────────────────────────────────────────────┤
│ TestNG reads testng.xml                                     │
│ └─► Loads LoginTest class                                   │
│     └─► Extends BaseTest                                    │
│         └─► @BeforeSuite: Initialize browser                │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. CONFIGURATION LOADING                                    │
├─────────────────────────────────────────────────────────────┤
│ ConfigManager static block executes                         │
│ ├─► Read -Denv parameter (default: dev)                    │
│ ├─► Load config/dev.properties                             │
│ └─► Store configuration                                     │
│     ├─► base.url = https://practice.expandtesting.com/     │
│     ├─► browser = chromium                                  │
│     ├─► username = practice                                 │
│     └─► password = SuperSecretPassword!                     │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 3. BROWSER INITIALIZATION                                   │
├─────────────────────────────────────────────────────────────┤
│ BrowserContextManager.initializeBrowserContext()            │
│ ├─► Playwright.create()                                    │
│ ├─► playwright.chromium()                                  │
│ └─► browser.launch(headless=false)                         │
│     └─► Browser window opens                               │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. TEST METHOD PREPARATION                                  │
├─────────────────────────────────────────────────────────────┤
│ @BeforeMethod setupTest()                                   │
│ ├─► page = BrowserContextManager.getNewPage()              │
│ │   ├─► context = browser.newContext()                     │
│ │   └─► page = context.newPage()                           │
│ │                                                            │
│ └─► loginPage = new LoginPage(page)                        │
│     └─► BasePage constructor stores page reference          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. TEST DATA LOADING                                        │
├─────────────────────────────────────────────────────────────┤
│ TestDataManager.loadJsonDataAsMap("users.json")            │
│ ├─► Read file from testdata/users.json                     │
│ ├─► Parse JSON with Jackson                                │
│ └─► Return Map<String, Object>                             │
│     └─► Extract username, password, expectedUrl            │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. PAGE OBJECT ACTIONS                                      │
├─────────────────────────────────────────────────────────────┤
│ loginPage.open()                                            │
│ └─► BasePage.navigateTo(url)                               │
│     ├─► logger.info("Navigating to {}", url)               │
│     ├─► page.navigate(url)                                 │
│     └─► Wait for page load                                 │
│                                                              │
│ loginPage.enterUsername("practice")                         │
│ ├─► usernameField() → page.getByLabel("Username")          │
│ ├─► locator.fill("practice")                               │
│ └─► logger.info("Typing username")                         │
│                                                              │
│ loginPage.enterPassword("SuperSecretPassword!")             │
│ ├─► passwordField() → page.getByLabel("Password")          │
│ ├─► locator.fill("SuperSecretPassword!")                   │
│ └─► logger.info("Typing password")                         │
│                                                              │
│ loginPage.clickSignIn()                                     │
│ ├─► signInButton() → page.getByRole(BUTTON, "Login")       │
│ ├─► locator.click()                                        │
│ └─► logger.info("Clicking Sign in")                        │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. ASSERTIONS                                                │
├─────────────────────────────────────────────────────────────┤
│ Assert.assertTrue(page.url().contains("/secure"))           │
│ ├─► Get current URL                                        │
│ ├─► Check if contains "/secure"                            │
│ └─► Pass or Fail                                            │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. TEST CLEANUP                                              │
├─────────────────────────────────────────────────────────────┤
│ @AfterMethod tearDownTest(ITestResult result)               │
│ ├─► Check result.getStatus()                               │
│ │                                                            │
│ ├─► IF SUCCESS:                                            │
│ │   ├─► logger.info("Test passed")                         │
│ │   ├─► page.close()                                       │
│ │   └─► context.close()                                    │
│ │                                                            │
│ └─► IF FAILURE:                                            │
│     ├─► logger.error("Test failed: {}", exception)         │
│     └─► Keep browser OPEN for debugging                    │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 9. REPORTING & LOGGING                                       │
├─────────────────────────────────────────────────────────────┤
│ TestNG generates reports                                    │
│ ├─► target/surefire-reports/index.html                     │
│ ├─► target/surefire-reports/testng-results.xml             │
│ └─► target/surefire-reports/*.txt                          │
│                                                              │
│ Logback writes logs                                         │
│ ├─► Console output (real-time)                             │
│ └─► target/logs/test-execution.log                         │
│                                                              │
│ LoggingListener logs events                                 │
│ ├─► Test started                                            │
│ ├─► Test passed/failed                                      │
│ └─► Test duration                                           │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ 10. SUITE TEARDOWN                                           │
├─────────────────────────────────────────────────────────────┤
│ @AfterSuite tearDownSuite()                                  │
│ └─► BrowserContextManager.closeBrowser()                    │
│     ├─► Close all contexts                                  │
│     ├─► browser.close()                                     │
│     ├─► playwright.close()                                  │
│     └─► logger.info("Browser closed")                       │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Design Patterns Used

### 1. Page Object Model (POM)
```
Separation of Concerns:
    Tests       → Define WHAT to test (business logic)
    Page Objects → Define HOW to interact (UI logic)
    Locators    → WHERE elements are (encapsulated)
```

### 2. Singleton Pattern
```java
// BrowserContextManager ensures one browser instance
private static Browser browser = null;

public static void initializeBrowserContext() {
    if (browser == null) {
        browser = playwright.chromium().launch();
    }
}
```

### 3. Factory Pattern
```java
// ConfigManager creates configuration based on environment
String env = System.getProperty("env", "dev");
String configFile = "config/" + env + ".properties";
```

### 4. Fluent Interface Pattern
```java
// Method chaining for readable test code
loginPage.open()
         .enterUsername("user")
         .enterPassword("pass")
         .clickSignIn();
```

### 5. Builder Pattern (Test Data)
```java
// Building complex test data objects
LoginUser user = UserBuilder.aUser()
    .withUsername("test")
    .withRandomEmail()
    .build();
```

### 6. Strategy Pattern (Locator Strategy)
```java
// Multiple locator strategies with priority
1. getByLabel()      // Highest priority
2. getByPlaceholder()
3. getByRole()
4. locator()         // Lowest priority
```

---

## 📊 Framework Benefits

### ✅ Maintainability
- Centralized locators in Page Objects
- Single point of change for UI updates
- Clear separation of concerns

### ✅ Scalability
- Easy to add new pages and tests
- Reusable components (BasePage, BaseTest)
- Modular architecture

### ✅ Reliability
- Resilient locator strategies
- Automatic waits with Playwright
- Comprehensive logging for debugging

### ✅ Flexibility
- Multiple data sources (JSON, Excel, Properties, Random)
- Environment-specific configuration
- Multi-browser support

### ✅ Debugging
- Browser stays open on failure
- Detailed execution logs
- Stack trace logging
- Screenshot capability

---

## 🔍 Execution Command Flow

### Maven Test Execution
```bash
mvn clean test
    ↓
Maven Surefire Plugin
    ↓
Reads testng.xml
    ↓
TestNG Executor
    ├─► @BeforeSuite (all test classes)
    ├─► FOR EACH TEST CLASS:
    │   ├─► @BeforeMethod
    │   ├─► @Test methods
    │   └─► @AfterMethod
    └─► @AfterSuite
    ↓
Generate Reports
    ├─► Surefire reports
    ├─► TestNG HTML report
    └─► Execution logs
```

### With Parameters
```bash
mvn test -Denv=qa -Dtest=LoginTest
    ↓
System properties set:
    env=qa
    test=LoginTest
    ↓
ConfigManager loads qa.properties
    ↓
Surefire runs only LoginTest
```

---

## 📁 File Structure Summary

```
playwright/
├── pom.xml                           # Dependencies & build config
├── Jenkinsfile                       # CI/CD pipeline
├── docs/
│   ├── TEST_DATA_MANAGEMENT.md       # Test data guide
│   └── FRAMEWORK_ARCHITECTURE.md     # This document
│
├── src/main/java/com/
│   ├── config/
│   │   └── ConfigManager.java        # Configuration loader
│   ├── logging/
│   │   └── LogHelper.java            # Logger utility
│   ├── pages/
│   │   ├── BasePage.java             # Common page actions
│   │   ├── LoginPage.java            # Login page object
│   │   └── ...
│   └── testdata/
│       ├── TestDataManager.java      # Data manager
│       ├── ExcelDataProvider.java    # Excel support
│       └── models/
│           ├── LoginUser.java        # Data models
│           └── ...
│
├── src/test/java/
│   ├── base/
│   │   ├── BaseTest.java             # Test base class
│   │   ├── BrowserContextManager.java # Browser manager
│   │   └── LoggingListener.java      # TestNG listener
│   └── tests/
│       ├── LoginTest.java            # Test classes
│       ├── LoginTestWithData.java    # Data-driven tests
│       └── ...
│
├── src/test/resources/
│   ├── config/
│   │   ├── dev.properties            # Dev environment
│   │   ├── qa.properties             # QA environment
│   │   └── prod.properties           # Prod environment
│   ├── testdata/
│   │   ├── users.json                # Test data files
│   │   ├── webinputs.json
│   │   └── testdata.xlsx
│   ├── logback.xml                   # Logging config
│   └── testng.xml                    # TestNG suite config
│
└── target/
    ├── logs/
    │   └── test-execution.log        # Execution logs
    └── surefire-reports/             # Test reports
        ├── index.html
        └── testng-results.xml
```

---

## 🚀 Quick Reference

### Run Tests
```bash
# All tests
mvn clean test

# Specific test
mvn test -Dtest=LoginTest

# With environment
mvn test -Denv=qa

# Specific method
mvn test -Dtest=LoginTest#testValidLogin
```

### Debug Test Failures
1. Check console output for errors
2. Review `target/logs/test-execution.log`
3. Open `target/surefire-reports/index.html`
4. Browser stays open on failure - inspect manually

### Add New Test
1. Create test class extending `BaseTest`
2. Add `@BeforeMethod` to get page
3. Initialize page objects
4. Write `@Test` methods
5. Add class to `testng.xml`

### Add New Page Object
1. Create class extending `BasePage`
2. Define lazy locator methods
3. Add action methods (return `this`)
4. Use resilient locator strategies

---

**Framework Version:** 1.0  
**Last Updated:** November 2025  
**Maintained By:** Automation Team

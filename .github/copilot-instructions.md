# Copilot Instructions for Playwright Java Automation Framework

## Architecture Overview

This is a **Playwright Java + TestNG** automation framework using **Page Object Model (POM)** with:
- **Java 16** as the minimum version
- **Maven** for build management
- **TestNG** for test orchestration with XML suites
- **SLF4J/Logback** for comprehensive logging

### Key Components
- `src/main/java/com/pages/` - Page Object classes (extend `BasePage`)
- `src/test/java/base/` - TestNG base classes and browser management
- `src/test/java/tests/` - Test classes (extend `BaseTest`)
- `src/test/resources/config/` - Environment-specific properties (`dev.properties`)
- `src/test/resources/testng.xml` - TestNG suite configuration

## Critical Workflows

### Running Tests
```bash
# Install Playwright browsers (first-time setup)
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# Run all tests via TestNG suite
mvn clean test

# Run specific test class
mvn test -Dtest=WebInputTest

# Switch environment (default: dev)
mvn test -Denv=qa
```

### Browser Management Pattern
- **DO NOT** create `Playwright` or `Browser` instances in test classes
- Use `BrowserContextManager.getNewPage()` in `@BeforeMethod` to get fresh pages
- Suite-level `BrowserContextManager.initializeBrowserContext()` runs in `BaseTest.@BeforeSuite`
- Browser stays **open on test failure** for debugging (see `BaseTest.tearDownTest()`)

## Page Object Conventions

### Locator Strategy (Priority Order)
1. **Prefer accessible selectors** (most resilient):
   ```java
   page.getByLabel("Username")
   page.getByPlaceholder("Search...")
   page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"))
   ```

2. **Lazy locator pattern** - Return `Locator` objects from private methods:
   ```java
   private Locator usernameField() {
       return page.getByLabel("Username");
   }
   
   public LoginPage enterUsername(String username) {
       usernameField().fill(username);
       return this;
   }
   ```

3. **Fluent API** - All action methods return `this` for chaining:
   ```java
   webInput.open()
           .enterText("Test")
           .clickSearch();
   ```

### BasePage Utilities
All page objects extend `BasePage` which provides:
- `navigateTo(url)` - Navigate with logging
- `clickElement(selector)`, `fillInput(selector, value)` - Basic actions
- `waitForSelector(selector)` - Explicit waits
- `takeScreenshot(path)` - Capture screenshots
- `scrollByRoll(selector, deltaY)` - Custom scroll via `evalOnSelector`

**Example**: See `LoginPage` for fluent pattern, `IframePage` for iframe handling with `FrameLocator`

## Configuration System

### ConfigManager Pattern
- Static properties loaded from `src/test/resources/config/{env}.properties`
- Access via static methods: `ConfigManager.getBaseUrl()`, `ConfigManager.getBrowser()`
- Supports `chromium`, `firefox`, `webkit` browsers
- Default timeout: 30000ms (configurable via `timeout` property)

### Environment Switching
```properties
# dev.properties
base.url=https://practice.expandtesting.com/
username=practice
password=SuperSecretPassword!
browser=chromium
timeout=30000
```
Override: `mvn test -Denv=qa` loads `qa.properties`

## Logging Architecture

### Logger Instantiation
```java
// In test classes (extends BaseTest)
protected static final Logger logger = LogHelper.getLogger(BaseTest.class);

// In page classes
private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
```

### TestNG Listener Integration
- `LoggingListener` auto-registered in `testng.xml` via `<listeners>` tag
- Logs test lifecycle: suite start/finish, test start/pass/fail/skip
- Logs include execution duration and full stack traces on failure

### Log Output
- **Console**: `HH:mm:ss.SSS` format with thread and logger name
- **Rolling file**: `target/logs/test-execution.log` (7-day rotation)
- Framework packages (`com.pages`, `base`) set to **DEBUG** level (see `logback.xml`)

## TestNG Suite Structure

### testng.xml Pattern
```xml
<suite name="Playwright Test Suite">
    <listeners>
        <listener class-name="base.LoggingListener"/>
    </listeners>
    <test name="Launch URL Test">
        <classes>
            <class name="tests.LaunchUrlTest"/>
            <class name="tests.WebInputTest"/>
        </classes>
    </test>
</suite>
```
- Maven Surefire references this file in `pom.xml`: `<suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>`

## Common Patterns

### Adding a New Test
1. Create test class in `src/test/java/tests/` extending `BaseTest`
2. Use `@BeforeMethod` to get page: `page = BrowserContextManager.getNewPage()`
3. Instantiate page object: `myPage = new MyPage(page)`
4. Add test class to `testng.xml`

### Adding a New Page Object
1. Create class in `src/main/java/com/pages/` extending `BasePage`
2. Accept `Page` in constructor and pass to super
3. Use lazy locator pattern (private methods returning `Locator`)
4. Return `this` from action methods for fluent chaining
5. Add verification methods (`isElementVisible()`, etc.)

### Debugging Failed Tests
- Browser **stays open** on failure (see `BaseTest.tearDownTest()` check for `FAILURE` status)
- Check `target/logs/test-execution.log` for detailed logs
- TestNG reports: `target/surefire-reports/index.html`

## Dependencies & Versions
- Playwright: 1.48.0
- TestNG: 7.10.2
- SLF4J: 2.0.16, Logback: 1.5.6
- Maven Compiler: Java 16 (source/target/release)
- Maven Surefire: 3.5.1

## Project-Specific Notes
- Uses `BrowserType.LaunchOptions().setHeadless(false)` - **headed mode** by default
- `package.json` includes `@playwright/test` (likely for TypeScript tests in `seed.spec.ts` - separate from Java tests)
- Authentication context manager exists but is commented out in `BaseTest` - not currently used

---

# Framework Deep Dive Explanation

## 🎯 Framework Purpose
This is a production-ready test automation framework for web applications using **Playwright** (Microsoft's modern browser automation library) with **Java**. It follows industry best practices with the **Page Object Model (POM)** design pattern.

## 🏗️ Architecture & Design Patterns

### 1. Page Object Model (POM)
The framework separates **test logic** from **UI interactions**:

```
Tests (what to test)  →  Page Objects (how to interact)  →  Playwright (browser control)
```

**Example Flow:**
```java
// In test class (tests/WebInputTest.java)
webInput.open()           // Navigation logic in page object
        .enterText("Test") // Interaction logic in page object
        .clickSearch();    // Action logic in page object

// Each method is defined in WebPageInput.java page object class
```

### 2. Three-Layer Architecture

```
┌─────────────────────────────────────┐
│   Test Layer (src/test/java/tests) │  ← Business logic & assertions
├─────────────────────────────────────┤
│   Page Layer (src/main/java/pages) │  ← UI interactions & locators
├─────────────────────────────────────┤
│   Base Layer (src/test/java/base)  │  ← Infrastructure (browser, config)
└─────────────────────────────────────┘
```

## 🔑 Key Components Explained

### Browser Management (BrowserContextManager)
- **Singleton pattern** for browser instances
- Creates ONE browser per test suite (not per test)
- Provides fresh `Page` objects to each test via `getNewPage()`
- Supports Chromium, Firefox, and WebKit

```java
// Suite setup (once)
BrowserContextManager.initializeBrowserContext(); // Creates browser

// Per test
page = BrowserContextManager.getNewPage(); // Gets new page in same browser
```

**Why?** Starting a browser is slow (~2-3 seconds). Reusing it across tests speeds up execution.

### Configuration Management (ConfigManager)
- **Environment-specific settings** loaded from `.properties` files
- Static class pattern - no instantiation needed
- Supports multiple environments via `-Denv=qa` flag

```java
ConfigManager.getBaseUrl()    // → https://practice.expandtesting.com/
ConfigManager.getBrowser()    // → chromium
ConfigManager.getTimeout()    // → 30000
```

**Files:**
- `dev.properties` - Development environment
- `qa.properties` - QA environment (if exists)
- `prod.properties` - Production (if exists)

### Logging System (SLF4J + Logback)
**Two-pronged approach:**

1. **Console Output** - Immediate feedback during test runs
   ```
   14:23:45.123 INFO  [main] LoginPage - Typing username
   ```

2. **File Output** - Detailed logs for post-execution analysis
   - Location: `target/logs/test-execution.log`
   - 7-day rolling history
   - Includes full stack traces

**TestNG Listener Integration:**
```java
// LoggingListener automatically logs:
- Suite start/finish
- Test start/pass/fail/skip
- Execution duration
- Failure stack traces
```

## 🎨 Page Object Patterns

### 1. Lazy Locator Pattern
Locators are defined as **private methods** that return `Locator` objects:

```java
// ❌ BAD - Eager locator (evaluated immediately)
private Locator usernameField = page.locator("#username");

// ✅ GOOD - Lazy locator (evaluated when called)
private Locator usernameField() {
    return page.getByLabel("Username");
}
```

**Why lazy?** The DOM might not be ready when the page object is instantiated. Lazy locators are evaluated when actually used.

### 2. Fluent API (Method Chaining)
All action methods return `this` to enable chaining:

```java
public LoginPage enterUsername(String username) {
    usernameField().fill(username);
    return this;  // ← Returns current instance
}

// Usage:
loginPage.open()
         .enterUsername("practice")
         .enterPassword("secret")
         .clickSignIn();  // All in one fluent chain
```

### 3. Accessible Selector Priority
The framework prioritizes **resilient** selectors:

```java
// Priority 1: Semantic labels (best)
page.getByLabel("Username")

// Priority 2: Placeholders
page.getByPlaceholder("Search...")

// Priority 3: ARIA roles + accessible names
page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"))

// Priority 4: CSS selectors (last resort)
page.locator("#username")
```

**Why?** Labels and roles rarely change, even if developers refactor CSS/IDs.

## 🧪 Test Execution Flow

### Complete Test Lifecycle:

```
1. @BeforeSuite (once per suite)
   ├─ BrowserContextManager.initializeBrowserContext()
   └─ Launches browser (chromium/firefox/webkit)

2. @BeforeMethod (before each test)
   ├─ Get fresh page: page = BrowserContextManager.getNewPage()
   └─ Instantiate page object: loginPage = new LoginPage(page)

3. @Test (actual test)
   └─ Execute test logic with assertions

4. @AfterMethod (after each test)
   ├─ If test PASSED → Close page and context
   └─ If test FAILED → Keep browser OPEN for debugging ⚠️

5. @AfterSuite (once per suite)
   └─ Close browser (commented out - relies on JVM shutdown)
```

### Debugging on Failure
```java
// In BaseTest.tearDownTest()
if (result.getStatus() == org.testng.ITestResult.FAILURE) {
    logger.error("Test FAILED: {}", result.getName());
    logger.error("Keeping page open for inspection. Close manually.");
    return;  // ← Browser stays open!
}
```

**Why?** You can manually inspect the page state, check console errors, or take screenshots.

## 📋 TestNG Suite Configuration

### testng.xml Structure:
```xml
<suite name="Playwright Test Suite">
    <listeners>
        <listener class-name="base.LoggingListener"/>  ← Auto-logging
    </listeners>
    <test name="Launch URL Test">
        <classes>
            <class name="tests.LaunchUrlTest"/>        ← Test execution order
            <class name="tests.WebInputTest"/>
        </classes>
    </test>
</suite>
```

**Maven Surefire Integration:**
- `mvn test` → Runs `testng.xml` automatically
- `mvn test -Dtest=WebInputTest` → Runs specific test
- `mvn test -Denv=qa` → Switches to QA environment

## 🛠️ Common Development Workflows

### Adding a New Test:
```java
// 1. Create test class
public class CheckoutTest extends BaseTest {
    private Page page;
    private CheckoutPage checkoutPage;
    
    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        checkoutPage = new CheckoutPage(page);
    }
    
    @Test
    public void testCheckout() {
        checkoutPage.open()
                    .addToCart()
                    .proceedToCheckout();
        Assert.assertTrue(checkoutPage.isOrderConfirmed());
    }
}

// 2. Add to testng.xml
<class name="tests.CheckoutTest"/>
```

### Adding a New Page Object:
```java
public class CheckoutPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);
    
    public CheckoutPage(Page page) {
        super(page);  // Must call super
    }
    
    // Lazy locators
    private Locator addToCartButton() {
        return page.getByRole(AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Add to Cart"));
    }
    
    // Fluent methods
    public CheckoutPage addToCart() {
        logger.info("Adding item to cart");
        addToCartButton().click();
        return this;  // For chaining
    }
}
```

## 🔍 Special Features

### 1. Iframe Handling
```java
// IframePage.java demonstrates FrameLocator usage
public FrameLocator iframe() {
    return page.frameLocator("iframe[name='top-iframe']");
}

public void clickDocsLink() {
    iframe().getByRole(AriaRole.LINK, 
        new FrameLocator.GetByRoleOptions().setName("Docs")).click();
}
```

### 2. BasePage Utilities
All page objects inherit helpful methods:
- `navigateTo(url)` - Navigation with logging
- `waitForSelector(selector)` - Explicit waits
- `takeScreenshot(path)` - Screenshot capture
- `scrollByRoll(selector, deltaY)` - Custom scrolling via JavaScript

### 3. Multi-Environment Support
```bash
# Development
mvn test

# QA environment
mvn test -Denv=qa

# Production (read-only tests)
mvn test -Denv=prod
```

## 📊 Reports & Artifacts

After `mvn test`, check:
- **TestNG HTML Report**: `target/surefire-reports/index.html`
- **Execution Logs**: `target/logs/test-execution.log`
- **XML Results**: `target/surefire-reports/testng-results.xml`

## ⚙️ Technology Stack Summary

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Language** | Java | 16 | Core programming language |
| **Automation** | Playwright | 1.48.0 | Browser control & interactions |
| **Test Framework** | TestNG | 7.10.2 | Test orchestration & lifecycle |
| **Build Tool** | Maven | 3.6+ | Dependency & build management |
| **Logging API** | SLF4J | 2.0.16 | Logging abstraction |
| **Logging Impl** | Logback | 1.5.6 | Actual log output |

## 💡 Framework Philosophy

1. **Resilient Selectors** - Use accessible attributes over fragile CSS/XPath
2. **Lazy Evaluation** - Don't find elements until you need them
3. **Fluent Interfaces** - Make tests read like natural language
4. **Fail-Fast Debugging** - Keep browser open on failure
5. **Centralized Config** - One place to change environments
6. **Comprehensive Logging** - Every action is logged with context

This framework is designed for **maintainability** (easy to update), **scalability** (easy to add tests), and **debuggability** (easy to troubleshoot failures).

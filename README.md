# Playwright Java Automation Framework

A production-ready test automation framework built with **Playwright**, **Java 16**, **TestNG**, and **SLF4J/Logback** logging. Follows Page Object Model (POM) design pattern for maintainable and scalable test automation.

## Features

✅ **Page Object Model (POM)** - Clean separation of test logic and UI interactions  
✅ **Centralized Configuration** - Environment-specific properties (dev, qa, prod)  
✅ **Advanced Logging** - SLF4J + Logback with console and rolling file appenders  
✅ **TestNG Listeners** - Automatic lifecycle logging and failure diagnostics  
✅ **Multi-Browser Support** - Chromium, Firefox, WebKit  
✅ **Flexible Teardown** - Keep browser open on test failure for debugging  
✅ **Maven Integration** - Easy build and test execution  

## Prerequisites

- **Java 16** or higher
- **Maven 3.6+**
- **Git** (for version control)

## Project Structure

```
playwright-java-automation/
├── src/
│   ├── main/java/com/
│   │   ├── config/
│   │   │   └── ConfigManager.java          # Environment configuration loader
│   │   ├── logging/
│   │   │   └── LogHelper.java              # SLF4J logger utility
│   │   └── pages/
│   │       ├── BasePage.java               # Base page with common actions
│   │       ├── WebPageInput.java           # Example page object (search flow)
│   │       └── LoginPage.java              # Example page object (login flow with fluent API)
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   ├── BaseTest.java           # TestNG base class with lifecycle hooks
│       │   │   ├── BrowserContextManager.java  # Browser/context management
│       │   │   └── LoggingListener.java    # TestNG listener for events
│       │   └── tests/
│       │       └── WebInputTest.java       # Example test
│       └── resources/
│           ├── config/
│           │   └── dev.properties          # Environment-specific config
│           ├── logback.xml                 # Logback configuration
│           └── testng.xml                  # TestNG suite definition
├── target/
│   ├── logs/
│   │   └── test-execution.log              # Rolling log files (7-day rotation)
│   └── surefire-reports/                   # TestNG HTML/XML reports
├── pom.xml
└── README.md
```

## Setup

1. **Clone the repository** (if applicable):
   ```bash
   git clone <repository-url>
   cd myid-groups-playwright-java-autoamtion
   ```

2. **Install Playwright browsers**:
   ```bash
   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
   ```

3. **Verify Java version**:
   ```bash
   java -version
   ```
   Should show Java 16 or higher.

## Configuration

Edit `src/test/resources/config/dev.properties` to configure:

```properties
base.url=https://practice.expandtesting.com/
browser=chromium
timeout=30000
```

**Supported browsers:**
- `chromium` (default)
- `firefox`
- `webkit`

## Running Tests

### Run all tests
```bash
mvn clean test
```

### Run a specific test class
```bash
mvn test -Dtest=WebInputTest
```

### Run a specific test method
```bash
mvn test -Dtest=WebInputTest#testWebInput
```

### Run with different environment
```bash
mvn test -Denv=qa    # Uses src/test/resources/config/qa.properties
mvn test -Denv=prod  # Uses src/test/resources/config/prod.properties
```

### Debug mode with verbose logging
```bash
mvn test -X
```

## Logging

The framework uses **SLF4J + Logback** for structured logging.

### Log Outputs
- **Console**: Real-time output during test execution
- **File**: `target/logs/test-execution.log` (daily rotation, 7-day retention)

### View Logs
```powershell
# View latest log file
Get-Content -Tail 50 target/logs/test-execution.log

# Follow logs in real-time (PowerShell)
Get-Content -Wait target/logs/test-execution.log
```

### Adjust Log Levels
Edit `src/test/resources/logback.xml`:

```xml
<!-- Default level -->
<root level="INFO">
  <appender-ref ref="CONSOLE"/>
  <appender-ref ref="TEST_FILE"/>
</root>

<!-- Detailed logging for specific packages -->
<logger name="com.pages" level="DEBUG"/>
<logger name="base" level="DEBUG"/>
```

**Available levels**: TRACE, DEBUG, INFO, WARN, ERROR

### TestNG Lifecycle Logging
`LoggingListener` automatically logs:
- ✅ Suite start/finish
- ✅ Test start/pass/fail/skip
- ✅ Test duration
- ✅ Failure stack traces

## Test Reports

After test execution, reports are generated in:

- **Surefire Reports**: `target/surefire-reports/`
  - `index.html` - HTML report
  - `TEST-*.xml` - XML reports
  - `*.txt` - Text summaries

Open the HTML report:
```bash
start target/surefire-reports/index.html
```

## Writing Tests

### 1. Create a Page Object (Improved Pattern)

Below is an updated `LoginPage` using **lazy getters**, **resilient locator strategy hierarchy**, **fluent API**, and **SLF4J logging**.

Locator Strategy Hierarchy (most stable first):
1. `getByTestId("id")` (after configuring `setTestIdAttribute("data-testid")` in `BrowserContextManager`)
2. `getByLabel("Username")` (accessible label)
3. `getByPlaceholder("Email")` (if label absent)
4. `getByRole(AriaRole.BUTTON, options.setName("Sign in"))` (semantic role + accessible name)
5. Scoped CSS within a form/component root (e.g., `form#login >> input[name='user']`)

```java
package com.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage(Page page) {
        super(page);
        log.debug("LoginPage initialized");
    }

    // Prefer label; fall back to role+name
    private Locator usernameField() { return page.getByLabel("Username"); }
    private Locator passwordField() { return page.getByLabel("Password"); }
    private Locator signInButton() { return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")); }

    public LoginPage open() {
        navigateTo("https://example.test/login");
        return this;
    }

    public LoginPage enterUsername(String user) {
        log.info("Entering username");
        usernameField().fill(user);
        return this;
    }

    public LoginPage enterPassword(String pass) {
        log.info("Entering password");
        passwordField().fill(pass);
        return this;
    }

    public LoginPage clickSignIn() {
        log.info("Clicking Sign in");
        signInButton().click();
        return this;
    }

    public LoginPage loginAs(String user, String pass) {
        return enterUsername(user).enterPassword(pass).clickSignIn();
    }

    public boolean isLoaded() {
        return usernameField().isVisible();
    }
}
```

### 2. Create a Test Class

```java
package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.microsoft.playwright.Page;
import com.pages.LoginPage;
import base.BaseTest;
import base.BrowserContextManager;

public class LoginTest extends BaseTest {
    private Page page; // Retrieved from BrowserContextManager
    private LoginPage login;

    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        login = new LoginPage(page);
        Assert.assertTrue(login.isLoaded(), "Login page did not load properly");
    }

    @Test
    public void testValidLogin() {
        login.open()
             .loginAs("user@example.com", "SecurePass123");

        // Example post-login assertion
        Assert.assertTrue(page.url().contains("/dashboard"), "User not redirected to dashboard");
        logger.info("Login successful");
    }
}
```

### 3. Add Test to TestNG Suite

Edit `src/test/resources/testng.xml`:

```xml
<suite name="Playwright Test Suite">
    <listeners>
        <listener class-name="base.LoggingListener"/>
    </listeners>
    <test name="Login Tests">
        <classes>
            <class name="tests.LoginTest"/>
        </classes>
    </test>
</suite>
```

## Troubleshooting

### Issue: Playwright browsers not installed
**Solution:**
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Test fails but browser closes immediately
The framework keeps the browser open on failure by default. Check `BaseTest.tearDownTest()` - it only closes on success.

To manually close after inspection: Press Ctrl+C in terminal or close browser window.

### Issue: SLF4J multiple bindings warning
Ensure only `logback-classic` is in dependencies. Run:
```bash
mvn clean
mvn dependency:tree | findstr slf4j
```

### Issue: Logs not appearing in file
Check that `target/logs/` directory exists. Logback creates it automatically, but verify permissions.

### Issue: Browser doesn't launch in headless mode
By default, tests run in headed mode (`.setHeadless(false)`). To enable headless:

Edit `BrowserContextManager.java`:
```java
browser = playwright.chromium().launch(
    new BrowserType.LaunchOptions().setHeadless(true)
);
```

### Issue: Page object locators fail
Use Playwright Inspector to debug selectors:
```powershell
$env:PWDEBUG="1"
mvn test -Dtest=YourTest
```

Or use codegen to generate locators:
```powershell
npx playwright codegen --target=java https://your-app-url.com
```

## Dependencies

- **Playwright**: 1.48.0 - Browser automation library
- **TestNG**: 7.10.2 - Test framework
- **SLF4J API**: 2.0.16 - Logging facade
- **Logback Classic**: 1.5.6 - Logging implementation
- **Java**: 16+
- **Maven**: 3.6+

## Framework Design Patterns

### Page Object Model (POM)
- Each page/component is a separate class
- Locators and actions encapsulated within page objects
- Tests remain clean and focused on business logic

### Singleton Pattern
- `BrowserContextManager` manages single browser instance per suite
- Reduces overhead of launching browsers for every test

### Factory Pattern
- `ConfigManager` loads environment-specific properties
- Switch environments via `-Denv=qa` parameter

### Listener Pattern
- `LoggingListener` implements `ITestListener`
- Automatically logs test lifecycle events
- Extensible for screenshots, videos, custom reporting

## Best Practices

✅ **Use fluent API** - Chain page object methods: `loginPage.open().enterEmail().submit()`  
✅ **Keep tests simple** - One assertion per test (when possible)  
✅ **Use meaningful names** - `testValidLoginRedirectsToDashboard` not `test1`  
✅ **Centralize waits** - Use `BasePage` helpers, avoid `Thread.sleep()`  
✅ **Prefer stable locators** - Use `data-test` attributes over CSS tied to styling  
✅ **Log actions** - Use `logger.info()` for key test steps  
✅ **Handle cleanup** - Close pages/contexts in `@AfterMethod`  

## CI/CD Integration

### GitHub Actions Example

Create `.github/workflows/playwright-tests.yml`:

```yaml
name: Playwright Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 16
        uses: actions/setup-java@v4
        with:
          java-version: '16'
          distribution: 'temurin'
          cache: 'maven'
      
      - name: Install Playwright browsers
        run: mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
      
      - name: Run tests
        run: mvn clean test -Denv=qa
      
      - name: Upload test reports
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: test-reports
          path: |
            target/surefire-reports/
            target/logs/
          retention-days: 7
      
      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v2
        if: always()
        with:
          files: target/surefire-reports/TEST-*.xml
```

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 16'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Install Playwright') {
            steps {
                sh 'mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"'
            }
        }
        
        stage('Run Tests') {
            steps {
                sh 'mvn clean test -Denv=${ENV}'
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: 'target/logs/**/*.log', allowEmptyArchive: true
            publishHTML([
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: 'TestNG Report'
            ])
        }
    }
}
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Create Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or issues, please contact the development team or open an issue on GitHub.

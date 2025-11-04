# Playwright Java Automation

Playwright-based test automation framework using Java and TestNG.

## Prerequisites

- **Java 16** or higher
- **Maven 3.6+**
- **Git** (for version control)

## Project Structure

```
playwright-java-automation/
├── src/
│   ├── main/java/
│   │   └── com/
│   │       ├── config/
│   │       │   └── ConfigManager.java      # Configuration management
│   │       └── pages/
│   │           ├── BasePage.java           # Base page object
│   │           └── HomePage.java           # Home page object
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   ├── BaseTest.java           # Base test setup
│       │   │   └── BrowserContextManager.java
│       │   └── tests/
│       │       └── LaunchUrlTest.java      # Launch URL test
│       └── resources/
│           ├── config/
│           │   └── dev.properties          # Environment config
│           └── testng.xml                  # TestNG suite
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
mvn test -Dtest=LaunchUrlTest
```

### Run with TestNG XML suite
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Clean and run tests
```bash
mvn clean test
```

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

### Example Test using Page Object Model

```java
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.config.ConfigManager;
import com.pages.HomePage;
import base.BaseTest;

public class MyTest extends BaseTest {
    
    @Test
    public void testExample() {
        String baseUrl = ConfigManager.getBaseUrl();
        HomePage homePage = new HomePage(page).open(baseUrl);
        
        Assert.assertTrue(homePage.urlContains("expandtesting.com"));
    }
}
```

## Troubleshooting

### Issue: Playwright browsers not installed
**Solution:**
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: SLF4J warning about StaticLoggerBinder
This is harmless. The project uses `slf4j-simple` which provides basic logging.

### Issue: Tests fail with authentication error
Check that `BrowserContextManager.performLogin()` is uncommented in `BaseTest.setupSuite()` if your tests require authentication.

### Issue: Browser doesn't launch
Set headless mode in the test:
```java
browser = type.launch(new BrowserType.LaunchOptions().setHeadless(true));
```

## Dependencies

- **Playwright**: 1.48.0
- **TestNG**: 7.10.2
- **SLF4J**: 2.0.16
- **Java**: 16

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Run Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 16
        uses: actions/setup-java@v3
        with:
          java-version: '16'
          distribution: 'temurin'
      - name: Install Playwright
        run: mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
      - name: Run tests
        run: mvn clean test
      - name: Upload test reports
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-reports
          path: target/surefire-reports/
```

## Contact

For questions or issues, please contact the development team.

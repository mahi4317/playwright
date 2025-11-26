# Test Data Management Guide

## 📚 Overview

This framework provides multiple approaches for handling test data:

1. **JSON Files** - Structured data with model mapping
2. **Excel Files** - Data-driven testing with Apache POI
3. **Properties Files** - Environment-specific configuration
4. **Random Data Generation** - Dynamic test data creation
5. **Programmatic Data** - Direct code-based test data

---

## 🗂️ Project Structure

```
src/
├── main/java/com/
│   └── testdata/
│       ├── TestDataManager.java        # Core utility for data management
│       ├── ExcelDataProvider.java      # TestNG data provider for Excel
│       └── models/
│           ├── LoginUser.java          # Login data model
│           └── WebInputData.java       # Web input data model
└── test/resources/testdata/
    ├── users.json                      # User test data
    ├── webinputs.json                  # Web input test data
    └── testdata.xlsx                   # Excel test data (optional)
```

---

## 1️⃣ JSON Test Data

### Creating JSON Files

**`src/test/resources/testdata/users.json`**
```json
{
  "loginUsers": {
    "validUser": {
      "username": "practice",
      "password": "SuperSecretPassword!",
      "expectedUrl": "/secure"
    },
    "invalidUser": {
      "username": "invalidUser",
      "password": "wrongPassword",
      "expectedError": "Your username is invalid!"
    }
  }
}
```

### Usage Examples

#### Approach A: Load as Map
```java
import com.testdata.TestDataManager;
import java.util.Map;

@Test
public void testWithJsonMap() {
    Map<String, Object> data = TestDataManager.loadJsonDataAsMap("users.json");
    String username = data.get("username").toString();
    String password = data.get("password").toString();
}
```

#### Approach B: Load with Model Class
```java
import com.testdata.TestDataManager;
import com.testdata.models.LoginUser;

@Test
public void testWithModel() {
    LoginUser user = TestDataManager.loadJsonData("validUser.json", LoginUser.class);
    loginPage.login(user.getUsername(), user.getPassword());
}
```

#### Approach C: Nested Values
```java
@Test
public void testWithNestedValue() {
    String username = TestDataManager.getNestedValue("users.json", 
        "loginUsers.validUser.username");
    String password = TestDataManager.getNestedValue("users.json", 
        "loginUsers.validUser.password");
}
```

---

## 2️⃣ Excel Test Data

### Creating Excel Files

Create `src/test/resources/testdata/logindata.xlsx`:

| username | password | expectedResult |
|----------|----------|---------------|
| practice | SuperSecretPassword! | success |
| invalid | wrongpass | failure |
| admin | admin123 | success |

### Usage with DataProvider

```java
import com.testdata.ExcelDataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    
    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testLoginWithExcel(String username, String password, String expectedResult) {
        logger.info("Testing with: {} / {}", username, expectedResult);
        
        loginPage.open()
                .login(username, password);
        
        if ("success".equals(expectedResult)) {
            Assert.assertTrue(page.url().contains("/secure"));
        } else {
            Assert.assertTrue(page.url().contains("/login"));
        }
    }
}
```

### Reading Specific Columns

```java
// Read only columns 0 and 2 (username and expectedResult)
Object[][] data = ExcelDataProvider.readExcelColumns(
    "logindata.xlsx", 
    "Sheet1", 
    new int[]{0, 2}
);
```

---

## 3️⃣ Properties File Data

### Using ConfigManager

Already configured in `src/test/resources/config/dev.properties`:

```properties
base.url=https://practice.expandtesting.com/
username=practice
password=SuperSecretPassword!
browser=chromium
timeout=30000
```

### Usage

```java
import com.config.ConfigManager;

@Test
public void testWithConfigData() {
    String baseUrl = ConfigManager.getBaseUrl();
    String username = ConfigManager.get("username");
    String password = ConfigManager.get("password");
    
    loginPage.navigateTo(baseUrl + "/login");
    loginPage.login(username, password);
}
```

### Add Getters to ConfigManager

Add these methods to `ConfigManager.java`:

```java
public static String getUsername() {
    return properties.getProperty("username");
}

public static String getPassword() {
    return properties.getProperty("password");
}
```

---

## 4️⃣ Random Test Data

### Using Random Data Generator

```java
import com.testdata.TestDataManager.RandomData;

@Test
public void testWithRandomData() {
    // Generate random username
    String username = RandomData.randomUsername();  // Returns: user_abc123
    
    // Generate random email
    String email = RandomData.randomEmail();  // Returns: test_xyz789@example.com
    
    // Generate random string
    String password = RandomData.randomString(12);  // Returns: aB3xYz9Qw1Po
    
    // Generate random number
    int age = RandomData.randomNumber(18, 65);  // Returns: random between 18-65
    
    logger.info("Generated data - User: {}, Email: {}", username, email);
}
```

---

## 5️⃣ Data-Driven Testing with TestNG

### Multiple Data Providers

```java
public class DataDrivenTest extends BaseTest {
    
    // Method 1: Inline DataProvider
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
            {"user1", "pass1", true},
            {"user2", "pass2", false},
            {"user3", "pass3", true}
        };
    }
    
    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean shouldPass) {
        loginPage.login(username, password);
        
        if (shouldPass) {
            Assert.assertTrue(page.url().contains("/secure"));
        }
    }
    
    // Method 2: JSON-based DataProvider
    @DataProvider(name = "jsonLoginData")
    public Object[][] getJsonData() {
        Map<String, Object> data = TestDataManager.loadJsonDataAsMap("users.json");
        // Convert JSON to Object[][]
        return new Object[][] {
            {data.get("username"), data.get("password")}
        };
    }
    
    // Method 3: Excel DataProvider
    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testWithExcel(String username, String password) {
        loginPage.login(username, password);
    }
}
```

---

## 6️⃣ Best Practices

### ✅ DO

1. **Centralize test data** - Keep all test data in `src/test/resources/testdata/`
2. **Use models** - Create POJO classes for complex data structures
3. **Separate environments** - Use different data files for dev/qa/prod
4. **Version control** - Commit test data files to Git
5. **Document data** - Add descriptions to JSON fields
6. **Parameterize tests** - Use DataProviders for data-driven testing

### ❌ DON'T

1. **Hard-code** sensitive data (passwords, API keys)
2. **Mix test data** with test logic
3. **Use production data** in automated tests
4. **Commit sensitive data** to version control
5. **Duplicate data** across multiple files

---

## 7️⃣ Advanced Patterns

### Pattern 1: Test Data Builder

```java
public class UserBuilder {
    private String username;
    private String password;
    private String email;
    
    public static UserBuilder aUser() {
        return new UserBuilder();
    }
    
    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public UserBuilder withRandomEmail() {
        this.email = RandomData.randomEmail();
        return this;
    }
    
    public LoginUser build() {
        return new LoginUser(username, password);
    }
}

// Usage
@Test
public void testWithBuilder() {
    LoginUser user = UserBuilder.aUser()
        .withUsername("testuser")
        .withRandomEmail()
        .build();
}
```

### Pattern 2: Environment-Specific Data

```java
// Load data based on environment
String env = System.getProperty("env", "dev");
String dataFile = "users_" + env + ".json";  // users_dev.json or users_qa.json
Map<String, Object> data = TestDataManager.loadJsonDataAsMap(dataFile);
```

### Pattern 3: Data Cleanup

```java
@AfterMethod
public void saveTestResults() {
    TestResult result = new TestResult();
    result.setTestName(method.getName());
    result.setStatus(testResult.getStatus());
    
    // Save test execution data
    TestDataManager.saveJsonData("results_" + timestamp + ".json", result);
}
```

---

## 8️⃣ Example: Complete Test Class

```java
package tests;

import base.BaseTest;
import base.BrowserContextManager;
import com.microsoft.playwright.Page;
import com.pages.WebPageInput;
import com.testdata.TestDataManager;
import com.testdata.models.WebInputData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WebInputDataDrivenTest extends BaseTest {
    private Page page;
    private WebPageInput webInput;
    
    @BeforeMethod
    public void setupTest() {
        page = BrowserContextManager.getNewPage();
        webInput = new WebPageInput(page);
    }
    
    @DataProvider(name = "webInputData")
    public Object[][] getWebInputData() {
        // Load from JSON file
        Map<String, Object> data = TestDataManager.loadJsonDataAsMap("webinputs.json");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> inputs = (List<Map<String, Object>>) data.get("webInputs");
        
        Object[][] testData = new Object[inputs.size()][3];
        for (int i = 0; i < inputs.size(); i++) {
            Map<String, Object> input = inputs.get(i);
            testData[i][0] = input.get("testName");
            testData[i][1] = input.get("inputText");
            testData[i][2] = input.get("expectedValue");
        }
        
        return testData;
    }
    
    @Test(dataProvider = "webInputData")
    public void testWebInputWithData(String testName, String inputText, String expectedValue) {
        logger.info("Running test: {}", testName);
        
        webInput.open()
                .enterText(inputText)
                .clickSearch();
        
        String actualValue = webInput.getDisplayedText();
        Assert.assertEquals(actualValue, expectedValue, 
            "Input value should match for: " + testName);
        
        logger.info("Test passed: {}", testName);
    }
}
```

---

## 9️⃣ Running Tests

```bash
# Run with default dev data
mvn test

# Run with QA environment data
mvn test -Denv=qa

# Run specific test with data
mvn test -Dtest=WebInputDataDrivenTest

# Run with custom properties
mvn test -Dusername=customuser -Dpassword=custompass
```

---

## 🔟 Troubleshooting

### Issue: JSON file not found
**Solution:** Check file path is relative to project root or use classpath loading

### Issue: Excel file format error
**Solution:** Ensure file is .xlsx format (not .xls)

### Issue: Data not loading
**Solution:** Check file encoding is UTF-8 and JSON is valid

### Issue: Random data conflicts
**Solution:** Add timestamps or UUIDs to make data unique

---

## 📖 Additional Resources

- **Jackson Documentation:** https://github.com/FasterXML/jackson
- **Apache POI Documentation:** https://poi.apache.org/
- **TestNG DataProvider:** https://testng.org/doc/documentation-main.html#parameters-dataproviders

---

**Framework Version:** 1.0  
**Last Updated:** November 2025

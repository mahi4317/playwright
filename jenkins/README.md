# Jenkins CI/CD Setup for Playwright Java Framework

## 📋 Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Jenkins Setup](#jenkins-setup)
- [Pipeline Configuration](#pipeline-configuration)
- [Running the Pipeline](#running-the-pipeline)
- [Advanced Configuration](#advanced-configuration)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

This Jenkins pipeline provides complete CI/CD automation for the Playwright Java test framework with the following features:

- ✅ Multi-environment support (dev, qa, prod)
- ✅ Multi-browser testing (Chromium, Firefox, WebKit)
- ✅ Headless and headed mode execution
- ✅ Parallel test execution
- ✅ HTML test reports with history
- ✅ Email notifications
- ✅ Artifact archiving
- ✅ Code quality checks

## 📦 Prerequisites

### 1. Jenkins Server Requirements
- Jenkins 2.387+ (LTS recommended)
- Java 17+ installed
- Maven 3.9+
- Node.js 18+ (for Playwright browser installation)

### 2. Required Jenkins Plugins

Install these plugins from **Manage Jenkins → Plugin Manager**:

```
Core Plugins:
├── Pipeline
├── Pipeline: Stage View
├── Git
├── Maven Integration
├── JUnit
├── HTML Publisher
├── Email Extension (Email-ext)
├── Workspace Cleanup
└── Build Timeout

Optional but Recommended:
├── Blue Ocean (modern UI)
├── Slack Notification
├── Discord Notifier
├── TestNG Results
└── Checkstyle
```

### 3. System Tools Configuration

Configure tools in **Manage Jenkins → Global Tool Configuration**:

#### Maven Configuration
```
Name: Maven-3.9.0
Install automatically: ☑️
Version: 3.9.0 or later
```

#### JDK Configuration
```
Name: JDK-17
Install automatically: ☑️
Version: Java 17 (Temurin recommended)
```

## 🚀 Jenkins Setup

### Step 1: Create New Pipeline Job

1. Go to Jenkins Dashboard
2. Click **New Item**
3. Enter job name: `Playwright-Tests`
4. Select **Pipeline**
5. Click **OK**

### Step 2: Configure Pipeline

#### General Configuration
```
Description: Automated Playwright tests for web application
☑️ Discard old builds
   - Strategy: Log Rotation
   - Days to keep builds: 30
   - Max # of builds to keep: 50
```

#### Build Triggers
Choose one or more:

```
☑️ Poll SCM
   Schedule: H/15 * * * * (Every 15 minutes)

☑️ GitHub hook trigger for GITScm polling

☑️ Build periodically
   Schedule: H 2 * * * (Daily at 2 AM)
```

#### Pipeline Definition
```
Definition: Pipeline script from SCM
SCM: Git
Repository URL: https://github.com/mahi4317/playwright.git
Branch: */main
Script Path: Jenkinsfile
```

### Step 3: Configure Email Notifications

**Manage Jenkins → Configure System → Extended E-mail Notification**

```
SMTP Server: smtp.gmail.com
SMTP Port: 587
Credentials: Add → Username with password
   - Username: your-email@gmail.com
   - Password: app-specific password
Default Recipients: team@example.com
Use SSL: ☑️
```

### Step 4: Configure Node.js (for Playwright)

**Manage Jenkins → Global Tool Configuration → NodeJS**

```
Name: NodeJS-18
Install automatically: ☑️
Version: 18.x or later
```

Then add to Jenkinsfile or create a Jenkins agent with Node.js pre-installed.

## 🎮 Pipeline Configuration

### Environment Variables

The pipeline uses these environment variables (configured in `config/dev.properties`, `qa.properties`, etc.):

```properties
# Base configuration
base.url=https://practice.expandtesting.com/
browser=chromium
timeout=30000

# Credentials
username=practice
password=SuperSecretPassword!
```

### Pipeline Parameters

When you run the pipeline, you can configure:

| Parameter | Type | Options | Description |
|-----------|------|---------|-------------|
| ENVIRONMENT | Choice | dev, qa, prod | Test environment |
| BROWSER | Choice | chromium, firefox, webkit, all | Browser selection |
| HEADLESS | Boolean | true/false | Headless mode toggle |
| TEST_CLASS | String | (optional) | Specific test class to run |

## 🏃 Running the Pipeline

### Method 1: Manual Build with Parameters

1. Go to your Jenkins job
2. Click **Build with Parameters**
3. Select options:
   - Environment: `dev`
   - Browser: `chromium`
   - Headless: `true`
   - Test Class: (leave empty for all tests)
4. Click **Build**

### Method 2: Automated Builds

Configure triggers in job configuration:
- **SCM Polling**: Automatically builds on code changes
- **Scheduled**: Runs at specific times (e.g., nightly)
- **Webhook**: Triggered by GitHub push events

### Method 3: Run Specific Test

To run a specific test class:
- Test Class: `WebInputTest`
- Or: `WebInputTest,LoginTest` (multiple tests)

## 📊 Pipeline Stages Explained

```
┌─────────────────────────────────────────┐
│ 1. Checkout                             │  Clean workspace & checkout code
├─────────────────────────────────────────┤
│ 2. Environment Info                     │  Display build configuration
├─────────────────────────────────────────┤
│ 3. Install Dependencies                 │  mvn clean install -DskipTests
├─────────────────────────────────────────┤
│ 4. Install Playwright Browsers          │  Install browser binaries
├─────────────────────────────────────────┤
│ 5. Compile Tests                        │  mvn test-compile
├─────────────────────────────────────────┤
│ 6. Run Tests                            │  Execute TestNG suite
├─────────────────────────────────────────┤
│ 7. Generate Reports                     │  Publish HTML reports
├─────────────────────────────────────────┤
│ 8. Archive Artifacts                    │  Save logs, screenshots, reports
├─────────────────────────────────────────┤
│ 9. Publish Test Results                 │  JUnit/TestNG results
├─────────────────────────────────────────┤
│ 10. Code Quality Analysis (dev only)    │  Checkstyle, static analysis
└─────────────────────────────────────────┘
```

## 🐳 Docker Support (Optional)

### Using Jenkins with Docker Agent

Create a custom Docker image for Playwright tests:

```dockerfile
# See jenkins/Dockerfile
FROM jenkins/agent:latest-jdk17

USER root

# Install Node.js and dependencies
RUN apt-get update && apt-get install -y \
    nodejs \
    npm \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Install Playwright system dependencies
RUN npx playwright install-deps

USER jenkins
```

Build and use:
```bash
docker build -t playwright-jenkins-agent ./jenkins
```

Update Jenkinsfile:
```groovy
agent {
    docker {
        image 'playwright-jenkins-agent:latest'
    }
}
```

## 🔧 Advanced Configuration

### Parallel Test Execution

Modify `testng.xml` to enable parallel execution:

```xml
<suite name="Playwright Test Suite" parallel="tests" thread-count="3">
    <!-- Your test configuration -->
</suite>
```

### Integration with Slack

Add to Jenkinsfile post section:

```groovy
post {
    success {
        slackSend(
            color: 'good',
            message: "✅ Tests Passed: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n" +
                     "Environment: ${params.ENVIRONMENT}\n" +
                     "Browser: ${params.BROWSER}\n" +
                     "Report: ${env.BUILD_URL}TestNG_20HTML_20Report/"
        )
    }
}
```

### Scheduled Test Runs

Add to Jenkins job configuration:

```
Daily regression: H 2 * * *
Hourly smoke tests: H * * * *
Weekend full suite: H 0 * * 0
```

### Test Retry on Failure

Add to `testng.xml`:

```xml
<suite name="Playwright Test Suite">
    <listeners>
        <listener class-name="org.testng.reporters.FailedReporter"/>
    </listeners>
</suite>
```

Then add retry stage in Jenkinsfile:

```groovy
stage('Retry Failed Tests') {
    when {
        expression { currentBuild.result == 'UNSTABLE' }
    }
    steps {
        sh 'mvn test -Dtestng.groups=retry'
    }
}
```

## 🛠️ Troubleshooting

### Issue 1: Playwright Browsers Not Installing

**Error:** `Browser not found`

**Solution:**
```groovy
// Add to Jenkinsfile
environment {
    PLAYWRIGHT_BROWSERS_PATH = "${WORKSPACE}/.playwright"
    PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD = "false"
}
```

### Issue 2: Permission Denied Errors

**Error:** `Permission denied` when installing browsers

**Solution:**
```bash
# On Jenkins agent
sudo chown -R jenkins:jenkins /home/jenkins/.cache/ms-playwright
```

### Issue 3: Tests Fail in Headless Mode

**Error:** Tests pass locally but fail in Jenkins

**Solution:**
- Check display settings (for non-headless): `DISPLAY=:99`
- Increase timeouts in `config/*.properties`
- Enable verbose logging in `logback.xml`

### Issue 4: Out of Memory Errors

**Error:** `java.lang.OutOfMemoryError`

**Solution:**
```groovy
environment {
    MAVEN_OPTS = '-Xmx2048m -XX:MaxPermSize=1024m'
}
```

### Issue 5: Email Notifications Not Sending

**Solution:**
1. Check SMTP credentials in **Manage Jenkins → Configure System**
2. Use Gmail app-specific password (not regular password)
3. Test with: **Manage Jenkins → Configure System → Test configuration**

## 📈 Monitoring & Reports

### Available Reports

1. **TestNG HTML Report**
   - Path: `${BUILD_URL}/TestNG_HTML_Report/`
   - Contains: Test results, execution time, stack traces

2. **JUnit Trend**
   - Path: `${BUILD_URL}/testReport/`
   - Contains: Test trends, pass/fail graphs

3. **Build Logs**
   - Path: `${BUILD_URL}/console`
   - Contains: Complete execution logs

4. **Archived Artifacts**
   - Path: `${BUILD_URL}/artifact/`
   - Contains: Logs, screenshots, reports

### Build Metrics

Track these metrics in Jenkins:
- Build success rate
- Average build duration
- Test pass rate
- Failed test trends

## 🔐 Security Best Practices

1. **Credentials Management**
   ```groovy
   // Use Jenkins credentials store
   environment {
       LOGIN_CREDENTIALS = credentials('playwright-credentials')
   }
   ```

2. **Restrict Pipeline Access**
   - Configure project-based authorization
   - Use role-based access control

3. **Scan Dependencies**
   ```groovy
   stage('Security Scan') {
       sh 'mvn org.owasp:dependency-check-maven:check'
   }
   ```

## 📚 Additional Resources

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Playwright Java Documentation](https://playwright.dev/java/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

## 🆘 Support

For issues or questions:
1. Check Jenkins console logs: `${BUILD_URL}/console`
2. Review test execution logs: `target/logs/test-execution.log`
3. Check TestNG reports: `target/surefire-reports/index.html`

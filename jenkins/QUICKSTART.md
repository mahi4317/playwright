# Quick Start Guide - Jenkins CI/CD for Playwright

## 🚀 Getting Started in 5 Minutes

### Option 1: Docker Setup (Recommended)

```bash
# Navigate to jenkins directory
cd jenkins

# Copy environment file
cp .env.example .env

# Edit .env with your settings (optional for quick start)
# nano .env

# Run setup script
chmod +x setup.sh
./setup.sh

# Access Jenkins
open http://localhost:8080
```

### Option 2: Existing Jenkins Server

1. **Upload Jenkinsfile** to your repository root
2. **Create Jenkins Pipeline Job:**
   - New Item → Pipeline
   - Pipeline from SCM
   - Repository: `https://github.com/mahi4317/playwright.git`
   - Script Path: `Jenkinsfile`
3. **Configure Tools** (Manage Jenkins → Global Tool Configuration):
   - Maven: `Maven-3.9.0`
   - JDK: `JDK-17`
4. **Install Plugins:**
   - Pipeline
   - HTML Publisher
   - Email Extension
   - TestNG Results
5. **Run Build with Parameters**

## 📋 What You Get

```
✅ Automated test execution on every commit
✅ Multi-browser testing (Chromium, Firefox, WebKit)
✅ Environment-specific testing (dev, qa, prod)
✅ HTML test reports with history
✅ Email notifications on build status
✅ Artifact archiving (logs, screenshots, reports)
✅ Test result trending and analytics
```

## 🎯 First Build

1. Open Jenkins: `http://localhost:8080`
2. Navigate to `Playwright-Tests` job
3. Click **Build with Parameters**
4. Select:
   - Environment: `dev`
   - Browser: `chromium`
   - Headless: `true`
5. Click **Build**
6. View results in **TestNG HTML Report**

## 📊 Pipeline Stages

```
Checkout → Environment Info → Install Dependencies → 
Install Playwright Browsers → Compile Tests → Run Tests → 
Generate Reports → Archive Artifacts → Publish Results → 
Code Quality Analysis
```

Each stage logs detailed information for troubleshooting.

## 🔔 Notifications

Configure email notifications in `.env`:

```env
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@gmail.com
SMTP_PASSWORD=your-app-password
EMAIL_RECIPIENTS=team@example.com
```

**Gmail Users:** Use [App Passwords](https://myaccount.google.com/apppasswords)

## 🐳 Docker Commands

```bash
# View logs
docker-compose logs -f jenkins

# Stop Jenkins
docker-compose down

# Restart
docker-compose restart

# Complete cleanup (removes volumes)
docker-compose down -v

# Rebuild agent image
docker-compose build jenkins-agent-playwright
```

## 📝 Custom Test Execution

### Run Specific Test Class
Build Parameters → Test Class: `LoginTest`

### Run Multiple Browsers
Build Parameters → Browser: `all`

### Switch Environment
Build Parameters → Environment: `qa`

### Debug Mode (Headed)
Build Parameters → Headless: `false`

## 🔍 Troubleshooting

### Jenkins Won't Start
```bash
# Check logs
docker-compose logs jenkins

# Verify port 8080 is free
lsof -i :8080

# Try different port
# Edit docker-compose.yml: "9090:8080"
```

### Tests Fail in Jenkins but Pass Locally
```bash
# Check browser installation
docker exec -it jenkins-agent-playwright npx playwright install

# Increase timeout in config/dev.properties
timeout=60000

# Enable headed mode for debugging
Build Parameters → Headless: false
```

### Email Notifications Not Sending
1. Check SMTP credentials in Jenkins
2. Verify Gmail App Password (not regular password)
3. Test: Manage Jenkins → Configure System → Email → Test

### Agent Not Connecting
```bash
# Get agent secret
Jenkins UI → Manage Nodes → playwright-agent → Secret

# Update .env
JENKINS_AGENT_SECRET=<your-secret>

# Restart containers
docker-compose restart
```

## 📚 Next Steps

- [Complete Documentation](README.md)
- [Customize Jenkinsfile](../Jenkinsfile)
- [Add Slack Notifications](README.md#integration-with-slack)
- [Configure Parallel Execution](README.md#parallel-test-execution)
- [Set up Scheduled Runs](README.md#scheduled-test-runs)

## 💡 Tips

- **Monitor builds**: Enable email notifications for failures
- **Daily runs**: Schedule nightly regression at 2 AM
- **Keep history**: Retain 50 builds for trend analysis
- **Parallel execution**: Edit `testng.xml` for faster runs
- **Screenshots**: Automatically archived on test failure

## 🆘 Need Help?

Check:
1. Jenkins console logs: `${BUILD_URL}/console`
2. Test execution logs: `target/logs/test-execution.log`
3. TestNG reports: `${BUILD_URL}/TestNG_HTML_Report/`
4. [Full Documentation](README.md)

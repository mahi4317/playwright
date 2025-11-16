package base;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingListener implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("SUITE START: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("SUITE FINISH: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("TEST START: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST PASS: {} ({} ms)", result.getMethod().getMethodName(), duration(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("TEST FAIL: {}", result.getMethod().getMethodName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST SKIP: {}", result.getMethod().getMethodName());
    }

    private long duration(ITestResult r) {
        return r.getEndMillis() - r.getStartMillis();
    }
}

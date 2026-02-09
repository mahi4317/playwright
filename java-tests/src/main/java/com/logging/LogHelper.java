package com.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central logging helper to obtain loggers and provide convenience methods.
 */
public final class LogHelper {
    private LogHelper() {}

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void step(Logger logger, String message) {
        logger.info("STEP: {}", message);
    }

    public static void warn(Logger logger, String message) {
        logger.warn(message);
    }

    public static void error(Logger logger, String message, Throwable t) {
        logger.error(message, t);
    }
}

package me.alpha432.oyvey.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

    private static final Logger LOGGER = LogManager.getLogger("uop.cc");

    public static void info(String msg) {
        if (LOGGER != null) {
            LOGGER.info(formatMessage(msg));
        }
    }

    public static void warn(String msg) {
        if (LOGGER != null) {
            LOGGER.warn(formatMessage(msg));
        }
    }

    public static void error(String msg) {
        if (LOGGER != null) {
            LOGGER.error(formatMessage(msg));
        }
    }

    public static void error(Exception e) {
        if (LOGGER != null) {
            LOGGER.error(formatMessage(e.getMessage()), e);
        }
    }

    public static void error(String msg, Exception e) {
        if (LOGGER != null) {
            LOGGER.error(formatMessage(msg), e);
        }
    }

    public static void debug(String msg) {
        if (LOGGER != null) {
            LOGGER.debug(formatMessage(msg));
        }
    }

    private static String formatMessage(String msg) {
        return String.format("%s: %s", "uop.cc", msg);
    }
}